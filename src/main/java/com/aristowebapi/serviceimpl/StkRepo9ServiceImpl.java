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
import com.aristowebapi.dto.StkRepo5;
import com.aristowebapi.dto.StkRepo9;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.StkRepo9Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.StkRepo5Response;
import com.aristowebapi.response.StkRepo9Response;
import com.aristowebapi.service.StkRepo9Service;
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
		title.append(request.getRep_type_pgwise()==1?"PRODUCT - > ":"GROUP ->");
		title.append(data.getPname());
		title.append(" STOCKIEST WISE SALES DETAIL FROM ");
		title.append(data.getSmname());
		title.append(" TO ");
		title.append(data.getEmname());
		return title.toString();

	}

	private String getTitle5(StkRepo9Request request,StkRepo5 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(request.getDivCode())));
		title.append("PRODUCT - > ");
		title.append(data.getPname());
		title.append(" INVOICE WISE SALES DETAIL FROM ");
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
			if(request.getRep_type_pgwise()==1)
			StkRepo9List=stkRepo9Dao.getStockiestRepo9(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getCode(),request.getLoginId());
			else
				StkRepo9List=stkRepo9Dao.getStockiestRepo9Group(request.getMyear(),request.getDivCode(),request.getDepoCode()
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

			response.setBranch(data.getDepo_name());
			response.setName(data.getName());
	    	response.setSalesQty(data.getSaleqty());
	    	response.setSalesVal(data.getSalesval());

	    	response.setSaleableRetQty(data.getSalableqty());
	    	response.setSaleableRetVal(data.getSalableval());

	    	response.setExpQty(data.getExpqty());
	    	response.setExpVal(data.getExpval());

	    	
	    	response.setBrkSpoilQty(data.getBrkspqty());
	    	response.setBrkSpoilVal(data.getBrkspval());

	    	
	    	response.setNetSalesQty(data.getNetqty());
	    	response.setNetSalesVal(data.getNetval());
	    	
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


	@Override
	public ApiResponse<StkRepo5Response> getStkRepo5(StkRepo9Request request) {
		logger.info(AristoWebLogMsgConstant.STK_REPO5_SERVICE,"getStkRepo5");
		
		List<StkRepo5> StkRepo5List=null;
		int size = 0;

		try {
			StkRepo5List=stkRepo9Dao.getStockiestRepo5(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getCode(),request.getLoginId());
				
			size = StkRepo5List.size();
			
			logger.info("size of the data is {}",size);
		
			String pname="";
			if(size==0)
				pname=stkRepo9Dao.getPname(request.getDivCode(), request.getCode());
			
			StkRepo5Response response=null;
		List<StkRepo5Response> saleList = new ArrayList();

		boolean first=true;
		String title=null;
 
		for (int i=0;i<size;i++)
		{
			StkRepo5 data = StkRepo5List.get(i);
			
			
			if(first)
			{
				title = getTitle5(request, data);
				first=false;
			}
			
			response=new StkRepo5Response();

			response.setBranch(data.getDepo_name());
			response.setName(data.getName());
			response.setInvoiceNo(data.getSinv_no());
			response.setInvoiceDate(data.getSinv_dt());
			response.setBatchNo(data.getSbatch_no());
			response.setSupplyRate(data.getSrate_net());
	    	response.setSalesQty(data.getSaleqty());
	    	response.setSalesVal(data.getSalesval());

	    	
	    	response.setColor(data.getTp());

	    	saleList.add(response);


		} //end of for loop

		if(!first)
		{

			ApiResponse<StkRepo5Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"",size,lupdate,saleList);
			return apiResponse;
		}
		else 
			return new ApiResponse<StkRepo5Response>(pname!=null?pname.toString():"",size,saleList);		
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.STK_REPO2_SERVICE_01,"getStkRepo5");
			throw new ApiException(e.getMessage());
		}

	}	
}

