package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.MktRepo11Dao;
import com.aristowebapi.dto.MktRepo11;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.MktRepo11Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo11Response;
import com.aristowebapi.service.MktRepo11Service;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class MktRepo11ServiceImpl implements MktRepo11Service{

	
	Logger logger = LoggerFactory.getLogger(MktRepo11ServiceImpl.class);

	@Autowired
	private MktRepo11Dao mktRepo11Dao;
	
	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;


	
	public String lupdate="";
	
	private String getTitle(MktRepo11Request request,MktRepo11 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(request.getDepoCode()==0?"ALL INDIA":mktRepo11Dao.getBranch(request.getDepoCode())+" Branch: ");
		title.append(" MONTH WISE DETAIL ");
		title.append(" For The Marketing Year - ");
		title.append(request.getMyear());
		title.append("-");
		title.append(request.getMyear()+1);

		return title.toString();

	}

	@Override
	public ApiResponse<MktRepo11Response> getMktRepo11(MktRepo11Request request) {
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_11,"getMktRepo11");
		List<MktRepo11> MktRepo11List=null;
		int size = 0;

		try {
			MktRepo11List=mktRepo11Dao.getWebReportSalesReviewSpl(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getHqCode());
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
		double monthTarget=0;
		double monthSale=0;
		double monthLys=0;

		
		for (int i=0;i<size;i++)
		{
			MktRepo11 data = MktRepo11List.get(i);
			
			
			if(first)
			{
				title = getTitle(request, data);
				first=false;
			}
			
			response=new MktRepo11Response();


			monthTarget+=data.getTgt_val();
			monthSale+=data.getSales_val();
			monthLys+=data.getLys_val();
			

			cummFs+=data.getFs();
			cummTarget+=data.getTgt_val();
			cummSale+=data.getSales_val();
			cummLys+=data.getLys_val();
			
			response.setMonth(data.getMnth_abbr());
			response.setFs(data.getFs());
	    	response.setMthBudget(AppCalculationUtils.valueDivideByLacInDouble(data.getTgt_val()));
	    	response.setMthSale(AppCalculationUtils.valueDivideByLacInDouble(data.getSales_val()));
	    	response.setMthLys(AppCalculationUtils.valueDivideByLacInDouble(data.getLys_val()));
	    	response.setMthAchPer(data.getAch());
	    	response.setMthGthPer(data.getGth());
	    	response.setMthPmr(AppCalculationUtils.calculatePmr(AppCalculationUtils.valueDivideByLacInDouble(data.getSales_val()),data.getFs()));
	    	response.setMthSurSlashdef(AppCalculationUtils.valueDivideByLacInDouble(data.getSurdef()));

			response.setCumFs(cummFs);
	    	response.setCumBudget(AppCalculationUtils.valueDivideByLacInDouble(cummTarget));
	    	response.setCumSale(AppCalculationUtils.valueDivideByLacInDouble(cummSale));

	    	response.setCumLys(AppCalculationUtils.valueDivideByLacInDouble(cummLys));
	    	response.setCumAchPer(AppCalculationUtils.calculateAch(cummSale, cummTarget));
	    	response.setCumGthPer(AppCalculationUtils.calculateGth(cummSale, cummLys));
	    	response.setCumPmr(AppCalculationUtils.calculatePmr(AppCalculationUtils.valueDivideByLacInDouble(cummSale), cummFs));
	    	response.setCumSurSlashdef(AppCalculationUtils.valueDivideByLacInDouble((cummSale-cummTarget)));
	    	response.setCumIncrSale(AppCalculationUtils.valueDivideByLacInDouble((cummSale-cummLys)));




	    	saleList.add(response);


		} //end of for loop

		if(!first)
		{

			response=new MktRepo11Response();
			response.setMonth("GRAND TOTAL");
			
			response.setFs(cummFs);
	    	response.setMthBudget(AppCalculationUtils.valueDivideByLacInDouble(monthTarget));
	    	response.setMthSale(AppCalculationUtils.valueDivideByLacInDouble(monthSale));
	    	response.setMthLys(AppCalculationUtils.valueDivideByLacInDouble(monthLys));
	    	response.setMthAchPer(AppCalculationUtils.calculateAch(monthSale, monthTarget));
	    	response.setMthGthPer(AppCalculationUtils.calculateGth(monthSale, monthLys));
	    	response.setMthPmr(AppCalculationUtils.calculatePmr(AppCalculationUtils.valueDivideByLacInDouble(monthSale), cummFs));
	    	response.setMthSurSlashdef(AppCalculationUtils.valueDivideByLacInDouble((monthSale-monthTarget)));
	    	
	    	
			response.setCumFs(cummFs);
	    	response.setCumBudget(AppCalculationUtils.valueDivideByLacInDouble(cummTarget));
	    	response.setCumSale(AppCalculationUtils.valueDivideByLacInDouble(cummSale));
	    	response.setCumLys(AppCalculationUtils.valueDivideByLacInDouble(cummLys));
	    	response.setCumAchPer(AppCalculationUtils.calculateAch(cummSale, cummTarget));
	    	response.setCumGthPer(AppCalculationUtils.calculateGth(cummSale, cummLys));
	    	response.setCumPmr(AppCalculationUtils.calculatePmr(AppCalculationUtils.valueDivideByLacInDouble(cummSale), cummFs));
	    	response.setCumSurSlashdef(AppCalculationUtils.valueDivideByLacInDouble((cummSale-cummTarget)));
	    	response.setCumIncrSale(AppCalculationUtils.valueDivideByLacInDouble((cummSale-cummLys)));
			response.setColor(2);

			saleList.add(response);
		}

		
		ApiResponse<MktRepo11Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_111,"getMktRepo11");
			throw new ApiException(e.getMessage());
		}
		

	}

	

}





