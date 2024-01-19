package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.dao.BranchMisRepo5Dao;
import com.aristowebapi.dto.BranchMisRepo5;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.BranchMisRepo5Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchMisRepo5Response;
import com.aristowebapi.service.BranchMisRepo5Service;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class BranchMisRepo5ServiceImpl implements BranchMisRepo5Service{

	Logger logger = LoggerFactory.getLogger(MktRepo2ServiceImpl.class);
	@Autowired
	private BranchMisRepo5Dao branchMisRepo5Dao;

	private String getTitle(BranchMisRepo5Request request,BranchMisRepo5 data)
	{
		StringBuilder title=new StringBuilder();
		title.append(" H.Q. WISE/GROSS/CREDIT/NET SALE FROM ");
		title.append(data.getSmname());
		title.append(" To ");
		title.append(data.getEmname());
		return title.toString();

	}

	
	
	@Override
	public ApiResponse<BranchMisRepo5Response> getBranchMisRepo5(BranchMisRepo5Request request) {
		
		logger.info(AristoWebLogMsgConstant.BRANCH_MIS_REPORT_5_SERVICE,"getBranchMisRepo5");
		List<BranchMisRepo5> BranchMisRepo5List=null;
		int size = 0;
		try {
			BranchMisRepo5List=branchMisRepo5Dao.getBranchMisRepo5(request.getMyear(),request.getDepoCode(),request.getUtype(),request.getLoginId()
					,request.getSmon(),request.getEmon());
			size = BranchMisRepo5List.size();
			logger.info("size of the data is {}",size);
/*			if(size==0)
				throw new DataNotFoundException(AristoWebLogMsgConstant.MKT_REPORT_SERVICE_022);
*/				
		
		BranchMisRepo5Response response=null;
		List<BranchMisRepo5Response> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		for (int i=0;i<size;i++)
		{
			BranchMisRepo5 data = BranchMisRepo5List.get(i);
			
			if(first)
			{
				title = getTitle(request, data);
				first=false;
			}
			
			
			response=new BranchMisRepo5Response();
			response.setName(data.getName());
			response.setBudget(data.getBudget());
			response.setGross(data.getGross());
			response.setCredit(data.getCredit());
			response.setNet(data.getNet());
			response.setAch(data.getAch());
//			response.setAch(AppCalculationUtils.calculateAch(data.getNet(), data.getBudget()));
			response.setSurdef(data.getSurdef());
			response.setColor(data.getColor());
	    	saleList.add(response);

		} //end of for loop

		
		ApiResponse<BranchMisRepo5Response> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.BRANCH_MIS_REPORT_5_SERVICE_021,"getMktRepo2");
			throw new ApiException(e.getMessage());
		}
		

	}



}
