package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.StkRepo9Dao;
import com.aristowebapi.dto.StkRepo9;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.StkRepo9Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.StkRepo2Response;
import com.aristowebapi.response.StkRepo9Response;
import com.aristowebapi.service.StkRepo9Service;
import com.aristowebapi.utility.AppCalculationUtils;
@Service

public class StkRepo9ServiceImpl implements StkRepo9Service {

	Logger logger = LoggerFactory.getLogger(StkRepo9ServiceImpl.class);
	
	@Autowired
	private StkRepo9Dao stkRepo9Dao;
	
	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;

	private String lupdate="";

	private String getTitle(StkRepo9Request request,StkRepo9 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(request.getDivCode())));
		title.append("PRODUCT - > ");
		title.append(data.getPname());
		title.append(" STOCKIEST WISE SALES DETAIL FROM ");
		title.append(data.getSmname());
		title.append(" TO ");
		title.append(data.getEmname());
		return title.toString();

	}

	
	@Override
	public ApiResponse<StkRepo9Response> getStkRepo9(StkRepo9Request request) {
		
		logger.info(AristoWebLogMsgConstant.STK_REPO9_SERVICE,"getStkRepo9");
		
		List<StkRepo9> StkRepo9List=null;
		int size = 0;

		try {
			StkRepo9List=stkRepo9Dao.getStockiestRepo9(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getCode(),request.getLoginId());
			size = StkRepo9List.size();
			
			logger.info("size of the data is {}",size);
		
			String pname="";
			if(size==0)
				pname=stkRepo9Dao.getPname(request.getDivCode(), request.getCode());
			
			StkRepo9Response response=null;
		List<StkRepo9Response> saleList = new ArrayList();

		boolean first=true;
		String title=null;
    	int gsaleqty=0;
    	double gsaleval=0.00;
    	int  gsalableqty=0;
    	double gsalableval=0.00;
    	int  gexpbrkqty=0;
    	double gexbrkval=0.00;
    	int gnetqty=0;
    	double gnetval=0.00;


		for (int i=0;i<size;i++)
		{
			StkRepo9 data = StkRepo9List.get(i);
			
			
			if(first)
			{
				title = getTitle(request, data);
				first=false;
			}
			
			response=new StkRepo9Response();

			response.setName(data.getName());
	    	response.setSaleQty(data.getSaleqty());
	    	response.setSaleVal(data.getSalesval());

	    	response.setSalableQty(data.getSalableqty());
	    	response.setSalableVal(data.getSalableval());

	    	response.setExpQty(data.getExpqty());
	    	response.setExpVal(data.getExpval());

	    	
	    	response.setBrksppdltQty(data.getBrkspqty());
	    	response.setBrksppdltVal(data.getBrkspval());

	    	
	    	response.setNetQty(data.getNetqty());
	    	response.setNetVal(data.getNetval());
	    	
	    	response.setColor(data.getTp());

	    	saleList.add(response);


		} //end of for loop

		if(!first)
		{

			ApiResponse<StkRepo9Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"",size,lupdate,saleList);
			return apiResponse;
		}
		else 
			return new ApiResponse<StkRepo9Response>(pname!=null?pname.toString():"",size,lupdate,saleList);		
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.STK_REPO2_SERVICE_01,"getStkRepo9");
			throw new ApiException(e.getMessage());
		}
		

	}

	
}

