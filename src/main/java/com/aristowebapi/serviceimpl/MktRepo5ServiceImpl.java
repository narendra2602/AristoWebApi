package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.dao.MktRepo5Dao;
import com.aristowebapi.dto.MktRepo5;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.MktRepo5Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo5Response;
import com.aristowebapi.service.MktRepo5Service;

@Service
public class MktRepo5ServiceImpl  implements MktRepo5Service  {
	
	Logger logger = LoggerFactory.getLogger(MktRepo5ServiceImpl.class);

	@Autowired
	private MktRepo5Dao mktRepo5Dao;
	
	private String getTitle(MktRepo5Request request,MktRepo5 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(request.getDepoCode()==0?"All India":mktRepo5Dao.getBranch(request.getDepoCode())+" Branch: ");
		title.append("GROSS/CREDIT/NET SALE From  ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}

	
	@Override
	public ApiResponse<MktRepo5Response> getMktRepo5(MktRepo5Request request) {

		logger.info(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_05,"getMktRepo5");
		List<MktRepo5> MktRepo5List=null;
		int size = 0;

		try {
			MktRepo5List=mktRepo5Dao.getWebReport24(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getUtype(),request.getLoginId(),request.getRepType());
			size = MktRepo5List.size();
			logger.info("size of the data is {}",size);
		
		MktRepo5Response response=null;
		List<MktRepo5Response> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		long tval=0;
		long  sval=0;
		long cval=0;
		long pval=0;
		for (int i=0;i<size;i++)
		{
			MktRepo5 data = MktRepo5List.get(i);
			
			if(data.getDepo_code()==0)
				continue;
			
			if(first)
			{
				title = getTitle(request, data);
				first=false;
			}
			
			response=new MktRepo5Response();

			response.setName(data.getDepo_name());
	    	response.setBudget(data.getTargetval());
	    	response.setGross(data.getSaleval());
	    	response.setCredit(data.getCrval());
	    	response.setNet(data.getSaleval()-data.getCrval());
	    	response.setAch(data.getTargetval()!=0?calculateAch(response.getNet(), data.getTargetval()):0);
	    	response.setSd(response.getNet()-data.getTargetval());
	    	response.setPisale(data.getPisale());


	    	saleList.add(response);

	    	tval+=data.getTargetval();
	    	sval+=data.getSaleval();
	    	cval+=data.getCrval();
	    	pval+=data.getPisale();

		} //end of for loop

		if(!first)
		{

			response=new MktRepo5Response();
			response.setName("GRAND TOTAL");
			response.setBudget(tval);
			response.setGross(sval);
			response.setCredit(cval);
			response.setNet(sval-cval);
			response.setAch(tval!=0?calculateAch((sval-cval), tval):0);
			response.setSd((sval-cval)-tval);
			response.setPisale(pval);
			response.setColor(2);

			saleList.add(response);
		}

		
		ApiResponse<MktRepo5Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"", saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_021,"getMktRepo2");
			throw new ApiException(e.getMessage());
		}
		

	}

	
	private double calculateAch(long sale,long target)
	{
		double ach1=0.00;  
		ach1=Math.round(((sale*1.0/target)*100)*100.0)/100.0;
		return (ach1);
	}

}



