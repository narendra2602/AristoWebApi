package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.AbmReportingDao;
import com.aristowebapi.dto.AbmReportingDto;
import com.aristowebapi.response.AbmReportingResponse;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.service.AbmReportingService;

@Service
public class AbmReportingServiceImpl  implements AbmReportingService{

	Logger logger = LoggerFactory.getLogger(AbmReportingServiceImpl.class);
	
	
	
	@Autowired
	private AbmReportingDao abmReportingDao;

	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;
	
	@Override
	public ApiResponse<AbmReportingResponse> getAbmReporting(int loginId ) {
		logger.info(AristoWebLogMsgConstant.Abm_Reporting_SERVICE,"getAbmReporting");
		
		List<AbmReportingDto> AbmReportingList=null;
		int size = 0;

		AbmReportingList=abmReportingDao.getLine1Reporting(loginId);
		
		size = AbmReportingList.size();
		logger.info("size of the data is {}",size);
		
		System.out.println("size is "+size+" id is "+loginId);
		AbmReportingResponse response=null;
		
		List<AbmReportingResponse> dataList = new ArrayList();
		boolean first=true;
		ArrayList<String> decimalKeys = new ArrayList<>();
		for (int i=0;i<size;i++)
		{
			AbmReportingDto data = AbmReportingList.get(i);
			
			
			
			
			if(first)
			{
				response=new AbmReportingResponse();
			response.setHq(data.getHq());
			response.setLine1_empname(data.getLine1_empname());
			response.setLine1_desg(data.getLine1_desg());
			response.setLine2_empname(data.getLine2_empname());
			response.setLine2_desg(data.getLine2_desg());
			response.setLine3_empname(data.getLine3_empname());
			response.setLine3_desg(data.getLine3_desg());

			dataList.add(response);

			first=false;
			}
		} //end of for loop

		ApiResponse<AbmReportingResponse> apiResponse = new ApiResponse<>("", size,decimalKeys,dataList);
		return apiResponse;

	}

	
}
