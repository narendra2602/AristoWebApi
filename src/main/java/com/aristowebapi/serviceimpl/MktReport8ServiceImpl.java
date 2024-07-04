package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.MktRepo8Dao;
import com.aristowebapi.dto.MktRepo8;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.MktRepo8Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo8Response;
import com.aristowebapi.service.MktRepo8Service;

@Service
public class MktReport8ServiceImpl implements MktRepo8Service{

	Logger logger = LoggerFactory.getLogger(MktReport8ServiceImpl.class);
	
	@Autowired
	private MktRepo8Dao mktRepo8Dao;
	
	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;

	
	public String lupdate="";
	
	private String getTitle(MktRepo8Request request,MktRepo8 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())));
		title.append(request.getDepoCode()==0?"All India ":mktRepo8Dao.getBranch(request.getDepoCode())+" Branch: ");
		title.append("EXPIRY RATIO From  ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}


	@Override
	public ApiResponse<MktRepo8Response> getMktRepo8(MktRepo8Request request) {
		
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_08,"getMktRepo8");
		
		List<MktRepo8> MktRepo8List=null;
		int size = 0;

		try {
			MktRepo8List=mktRepo8Dao.getWebReportHqwiseExpiryRatio(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType());
			size = MktRepo8List.size();
			
			logger.info("size of the data is {}",size);
		
		MktRepo8Response response=null;
		List<MktRepo8Response> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		long  sval=0;
		long  eval=0;
		long  bval=0;
		long  tval=0;


		for (int i=0;i<size;i++)
		{
			MktRepo8 data = MktRepo8List.get(i);
			
			if(data.getDepo_code()==0)
				continue;
			
			if(first)
			{
				title = getTitle(request, data);
				first=false;
			}
			
			response=new MktRepo8Response();

			response.setBranch(data.getBranch_name());
			response.setHqName(request.getDepoCode()>0?data.getDepo_name():"");
	    	response.setGross(data.getGross());
	    	response.setExpiry(data.getExpiry());
	    	response.setExpiryRaio(data.getExpratio());

	    	saleList.add(response);

	    	sval+=data.getGross();
	    	eval+=data.getExpiry();

		} //end of for loop

		if(!first)
		{

			response=new MktRepo8Response();
			response.setBranch("TOTAL");
			response.setHqName("");
	    	response.setGross(sval);
	    	response.setExpiry(eval);
	    	response.setExpiryRaio(Math.round(((eval*1.0/sval)*100)*100.0)/100.0);
			response.setColor(2);

			saleList.add(response);
		}

		
		ApiResponse<MktRepo8Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_081,"getMktRepo8");
			throw new ApiException(e.getMessage());
		}
		

	}

	
}



