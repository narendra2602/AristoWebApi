package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.dao.MktRepo11Dao;
import com.aristowebapi.dto.MktRepo11;
import com.aristowebapi.dto.MktRepo5;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.MktRepo11Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo11Response;
import com.aristowebapi.response.MktRepo5Response;
import com.aristowebapi.service.MktRepo11Service;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class MktRepo11ServiceImpl implements MktRepo11Service{

	
	Logger logger = LoggerFactory.getLogger(MktRepo11ServiceImpl.class);

	@Autowired
	private MktRepo11Dao mktRepo11Dao;
	
	private String getTitle(MktRepo11Request request,MktRepo11 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(request.getDepoCode()==0?"ALL INDIA":mktRepo11Dao.getBranch(request.getDepoCode())+" Branch: ");
		title.append(" MONTH WISE DETAIL ");
		return title.toString();

	}

	@Override
	public ApiResponse<MktRepo11Response> getMktRepo11(MktRepo11Request request) {
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_11,"getMktRepo11");
		List<MktRepo11> MktRepo11List=null;
		int size = 0;

		try {
			MktRepo11List=mktRepo11Dao.getWebReportSalesReviewSpl(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType());
			size = MktRepo11List.size();
			logger.info("size of the data is {}",size);
		
		MktRepo11Response response=null;
		List<MktRepo11Response> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		int cummFs=0;
		long cummTarget=0;
		long cummSale=0;
		long cummLys=0;
		
		for (int i=0;i<size;i++)
		{
			MktRepo11 data = MktRepo11List.get(i);
			
			
			if(first)
			{
				title = getTitle(request, data);
				first=false;
			}
			
			response=new MktRepo11Response();

			
			cummFs+=data.getFs();
			cummTarget+=data.getTgt_val();
			cummSale+=data.getSales_val();
			cummLys+=data.getLys_val();
			
			response.setMonth(data.getMnth_abbr());
			response.setFs(data.getFs());
	    	response.setMonthTgt(AppCalculationUtils.valueDivideByLacs(data.getTgt_val()));
	    	response.setMonthSale(AppCalculationUtils.valueDivideByLacs(data.getSales_val()));
	    	response.setMonthLys(AppCalculationUtils.valueDivideByLacs(data.getLys_val()));
	    	response.setMonthAch(data.getAch());
	    	response.setMonthGth(data.getGth());
	    	response.setMonthPmr(AppCalculationUtils.calculatePmr(AppCalculationUtils.valueDivideByLacs(data.getSales_val()),data.getFs()));
	    	response.setMonthSd(AppCalculationUtils.valueDivideByLacs(data.getSurdef()));

			response.setCummFs(cummFs);
	    	response.setCummTgt(AppCalculationUtils.valueDivideByLacs(cummTarget));
	    	response.setCummSale(AppCalculationUtils.valueDivideByLacs(cummSale));

	    	response.setCummLys(AppCalculationUtils.valueDivideByLacs(cummLys));
	    	response.setCummAch(AppCalculationUtils.calculateAch(cummSale, cummTarget));
	    	response.setCummGth(AppCalculationUtils.calculateGth(cummSale, cummLys));
	    	response.setCummPmr(AppCalculationUtils.calculatePmr(AppCalculationUtils.valueDivideByLacs(cummSale), cummFs));
	    	response.setCummSd(AppCalculationUtils.valueDivideByLacs((cummSale-cummTarget)));
	    	response.setCummIncrSale(AppCalculationUtils.valueDivideByLacs((cummSale-cummLys)));




	    	saleList.add(response);


		} //end of for loop

		if(!first)
		{

			response=new MktRepo11Response();
			response.setMonth("GRAND TOTAL");
			response.setColor(2);

			saleList.add(response);
		}

		
		ApiResponse<MktRepo11Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_111,"getMktRepo11");
			throw new ApiException(e.getMessage());
		}
		

	}

	

}





