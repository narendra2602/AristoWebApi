package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.dao.MktRepo7Dao;
import com.aristowebapi.dto.MktRepo5;
import com.aristowebapi.dto.MktRepo7;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.MktRepo7Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo5Response;
import com.aristowebapi.response.MktRepo7Response;
import com.aristowebapi.service.MktRepo7Service;

@Service
public class MktRepo7ServiceImpl implements  MktRepo7Service{

	Logger logger = LoggerFactory.getLogger(MktRepo5ServiceImpl.class);
	
	@Autowired
	private MktRepo7Dao mktRepo7Dao;

	private String getTitle(MktRepo7Request request,MktRepo7 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(request.getDepoCode()==0?"All India ":mktRepo7Dao.getBranch(request.getDepoCode())+" Branch: ");
		title.append("EXPIRY/BREAKAGE/SALABLE From  ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}

	
	@Override
	public ApiResponse<MktRepo7Response> getMktRepo7(MktRepo7Request request) {
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_07,"getMktRepo7");
		
		List<MktRepo7> MktRepo7List=null;
		int size = 0;

		try {
			MktRepo7List=mktRepo7Dao.getWebReportHqwiseSalableExpiryBreakage(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType());
			size = MktRepo7List.size();
			
			logger.info("size of the data is {}",size);
		
		MktRepo7Response response=null;
		List<MktRepo7Response> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		long  sval=0;
		long  eval=0;
		long  bval=0;
		long  tval=0;


		for (int i=0;i<size;i++)
		{
			MktRepo7 data = MktRepo7List.get(i);
			
			if(data.getDepo_code()==0)
				continue;
			
			if(first)
			{
				title = getTitle(request, data);
				first=false;
			}
			
			response=new MktRepo7Response();

			response.setName(data.getDepo_name());
	    	response.setSaleable(data.getSalable());
	    	response.setExpiry(data.getExpiry());
	    	response.setBreakage(data.getBreakage());
	    	response.setTotal(data.getSalable()+data.getExpiry()+data.getBreakage());

	    	saleList.add(response);

	    	sval+=data.getSalable();
	    	eval+=data.getExpiry();
	    	bval+=data.getBreakage();
	    	tval+=data.getSalable()+data.getExpiry()+data.getBreakage();

		} //end of for loop

		if(!first)
		{

			response=new MktRepo7Response();
			response.setName("GRAND TOTAL");
	    	response.setSaleable(sval);
	    	response.setExpiry(eval);
	    	response.setBreakage(bval);
	    	response.setTotal(tval);
			response.setColor(2);

			saleList.add(response);
		}

		
		ApiResponse<MktRepo7Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"", saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_071,"getMktRepo7");
			throw new ApiException(e.getMessage());
		}
		

	}

	
}

