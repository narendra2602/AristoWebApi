package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.dao.DailyEntryDao;
import com.aristowebapi.dto.DailyEntry;
import com.aristowebapi.dto.DailyEntryList;
import com.aristowebapi.dto.MktRepo5;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.DailyEntryListRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DailyEntryListResponse;
import com.aristowebapi.response.MktRepo5Response;
import com.aristowebapi.service.DailyEntryService;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class DailyEntryServiceImpl implements DailyEntryService{

	@Autowired
	private DailyEntryDao dailyeEntryDao;
	
	@Override
	public DailyEntry saveDailyEntry(DailyEntry dailyentry) {
		
		return dailyeEntryDao.save(dailyentry);
		
	}

	@Override
	public ApiResponse<DailyEntryListResponse> getDailyEntryList(DailyEntryListRequest request) {
		
		List<DailyEntryList> DailyEntryList=null;
		int size = 0;
		DailyEntryList=dailyeEntryDao.getLastEntry(request.getMyear(),request.getDivCode(),request.getDepoCode()
						,request.getMonth());
			size = DailyEntryList.size();
		
		DailyEntryListResponse response=null;
		List<DailyEntryListResponse> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		double tval=0;
		double  sval=0;
		double cval=0;
		double pval=0;
		double lval=0;
		int fs=0;
		response=new DailyEntryListResponse();
		for (int i=0;i<size;i++)
		{
			DailyEntryList data = DailyEntryList.get(i);
			
			response.setBudgetPer(data.getBudget_per());
			response.setBudget(data.getBudget());
	    	response.setCn100(data.getCn100());
	    	response.setNetSalesToday(data.getSales_today());
	    	response.setNetSalesUptoDate(data.getSales_trade());
	    	response.setLastMonthSales(data.getLast_month());
	    	response.setLastYearSales(data.getLast_year());
	    	response.setCollectionToday(data.getCollection());
	    	response.setCollectionCumm(data.getCollection_cumm());
	    	response.setRemittanceToday(data.getRemit());
	    	response.setRemittanceCumm(data.getRemit_cumm());
	    	response.setOutstandingAsOnDate(data.getOutstand());
	    	response.setMonthlyBillingStatus(data.getStatus());


	    	saleList.add(response);

		} //end of for loop

		
		ApiResponse<DailyEntryListResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"",size, saleList);
		return apiResponse;
		
			
	}


}
