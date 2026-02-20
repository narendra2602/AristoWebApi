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
import com.aristowebapi.dto.DashBoardData;
import com.aristowebapi.dto.PsrDto;
import com.aristowebapi.response.AbmReportingResponse;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DashBoardDataResponse;
import com.aristowebapi.response.PsrResponse;
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

	@Override
	public ApiResponse<PsrResponse> getPsrList(int loginId) {
		List<PsrDto> dataList= abmReportingDao.getPsrList( loginId);
		List<PsrResponse> saleList = getResponseData(dataList);
		String title="Psr List ";

		
		int size=dataList.size();
		System.out.println("size is "+size+" id "+loginId);
		
		ApiResponse<PsrResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;

	}
	
	private List<PsrResponse> getResponseData(List<PsrDto> dataList)
	{
		List<PsrResponse> saleList = new ArrayList<PsrResponse>();
		int size = dataList.size();
		
		PsrResponse response=null;
		
		
		for(int i=0; i<size;i++)
		{

			PsrDto data = dataList.get(i);
			response= new PsrResponse();
			response.setName(data.getPsr_name());
			response.setValue(data.getVal());
			response.setHq(data.getTer_name());
			saleList.add(response);
		}
		return saleList;

	}

	@Override
	public ApiResponse<DashBoardDataResponse> getAristoBrandList(int mktYear, int divCode) {
		List<DashBoardData> dataList= abmReportingDao.getAristoBrandList(mktYear,divCode);
		
		List<DashBoardDataResponse> saleList = new ArrayList<DashBoardDataResponse>();
		int size=dataList.size();
		
		DashBoardDataResponse response=null;
		
		
		for(int i=0; i<size;i++)
		{

			DashBoardData data = dataList.get(i);
			response= new DashBoardDataResponse();
			response.setName(data.getName());
			response.setValue(data.getVal());

			saleList.add(response);
		}

		
		
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>("Aristo Brand List", size,saleList);
		return apiResponse;
	}


}
