package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.StkRepo5Repository;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.StkRepo9Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.StkRepo5ResponseNew;

@Service
public class StkRepo5ServiceImpl {

	
	Logger logger = LoggerFactory.getLogger(StkRepo9ServiceImpl.class);
	
	
	@Autowired
	public StkRepo5Repository stkRepo5Repository; 
	
	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;

	private String lupdate="";


	private String getTitle5(StkRepo9Request request,HashMap<String,Object> data)
	{
		StringBuilder title=new StringBuilder();
		title.append(aristoWebMessageConstant.divisionMap.get(String.valueOf(request.getDivCode())));
		title.append("PRODUCT - > ");
		title.append(data.get("pname"));
		title.append(" INVOICE WISE SALES DETAIL FROM ");
		title.append((String) data.get("smname"));
		title.append(" TO ");
		title.append(data.get("emname"));
		return title.toString();

	}
	 
	
	public ApiResponse<StkRepo5ResponseNew> getStkRepo5(StkRepo9Request request) {
		logger.info(AristoWebLogMsgConstant.STK_REPO5_SERVICE,"getStkRepo5");
		
		//List<StkRepo5> StkRepo5List=null;
		int size = 0;

		try {
/*			StkRepo5List=stkRepo9Dao.getStockiestRepo5(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getCode(),request.getLoginId());
*/				
			
			List<HashMap<String, Object>> StkRepo5List =  stkRepo5Repository.getStockiestRepo5(request.getMyear(),request.getDivCode(),request.getDepoCode()
					,request.getSmon(),request.getEmon(),request.getCode(),request.getLoginId());
			size = StkRepo5List.size();
		
			logger.info("size of the data is {}",size);
		
			String pname="";
			//if(size==0)
				//pname=stkRepo9Dao.getPname(request.getDivCode(), request.getCode());
			
			StkRepo5ResponseNew response=null;
		List<StkRepo5ResponseNew> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		HashMap<String,Object> data = new HashMap<>();
		HashMap<String,Boolean> checkData = new HashMap<>();
		for (int i=0;i<size;i++)
		{
			data = StkRepo5List.get(i);
			
			
			if(first)
			{
				title = getTitle5(request, data);
				first=false;
			}
			
			response=new StkRepo5ResponseNew();
			
			response.setDataMap(data);
			
				response.getDataMap().remove("smname");
				response.getDataMap().remove("emname");
				response.getDataMap().remove("pname");


	    	saleList.add(response);


		} //end of for loop

		if(!first)
		{

			ApiResponse<StkRepo5ResponseNew> apiResponse = new ApiResponse<>(title!=null?title.toString():"",size,lupdate,saleList);
			return apiResponse;
		}
		else 
			return new ApiResponse<StkRepo5ResponseNew>(pname!=null?pname.toString():"",size,saleList);		
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.STK_REPO2_SERVICE_01,"getStkRepo5");
			throw new ApiException(e.getMessage());
		}

	}	


	
	
}
