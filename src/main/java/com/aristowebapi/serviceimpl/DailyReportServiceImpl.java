package com.aristowebapi.serviceimpl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.dao.DailyReportDao;
import com.aristowebapi.dto.DailyReport;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.DailyReportRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DailyReportResponse;
import com.aristowebapi.service.DailyReportService;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class DailyReportServiceImpl  implements DailyReportService{

	Logger logger = LoggerFactory.getLogger(DailyReportServiceImpl.class);
	
	@Autowired
	private DailyReportDao dailyReportDao;

	
	private String getTitle(DailyReportRequest request,DailyReport data)
	{
		StringBuilder title=new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		title.append(" BILLING MONTH ");
		title.append(getDayMonthYear(sdf.format(request.getEntryDate())));
		title.append(" AS ON ");
		title.append(data.getEnt_Date());
		title.append(" (In Lakhs)");
		
		return title.toString();

	}

	
	// Function to get day, month, and  year from date
    private String  getDayMonthYear(String date)
    {
 
        // Get an instance of LocalTime
        // from date
        LocalDate currentDate = LocalDate.parse(date);
 
 
        // Get month from date
        Month month = currentDate.getMonth();
 
        // Get year from date
        int year = currentDate.getYear();
 
        return (new StringBuilder(month.toString()).append("-").append(year)).toString();
    }
	
	
	
	@Override
	public ApiResponse<DailyReportResponse> getDailyReport(DailyReportRequest request) {

		logger.info(AristoWebLogMsgConstant.DAILY_REPORT_SERVICE,"getDailyReport");
		List<DailyReport> DailyReportList=null;
		int size = 0;
		Map<Integer,String> branchMap = new HashMap<Integer,String>();
		try {

			
		DailyReportList=dailyReportDao.getDailyBillDetail(request.getDivCode(),request.getDepoCode(),request.getLoginId(),new java.sql.Date(request.getEntryDate().getTime()),request.getMonth());
		size=DailyReportList.size();
		logger.info("size of the data is {}",size);

		DailyReportResponse response=null;
		List<DailyReportResponse> saleList = new ArrayList();

		boolean first=true;
		String title=null;

		for (int i=0;i<size;i++)
		{
			DailyReport data = DailyReportList.get(i);
			
			if(first)
			{
				title = getTitle(request, data);
				first=false;
			}
			
			
			if(data.getTp()<50)
			{
				if(branchMap.containsKey(data.getDepo_code()))
				{
					if(!branchMap.get(data.getDepo_code()).contains("Open"))
						branchMap.put(data.getDepo_code(), data.getStatus());
				}
				else
					branchMap.put(data.getDepo_code(), data.getStatus());

			}
			
			response=new DailyReportResponse();
			response.setBr(data.getDepo_code());
			response.setBranch(data.getBr_name());
			response.setDivision(data.getDiv_name());
			response.setBudget(data.getBudget());
			response.setSales(data.getSales_trade());
			response.setAchPer(AppCalculationUtils.calculateAch(data.getSales_trade(),data.getBudget()));
			response.setSurDef(Math.round((data.getSales_trade()-data.getBudget())*100.00)/100.00);
			response.setCn100(data.getCn100());
			response.setTodaySales(data.getSales_today());
			response.setLmSale(data.getLast_month());
			response.setLySale(data.getLast_year());
			response.setCollCumm(data.getCollection_cumm());
			response.setRemiitToday(data.getRemit());
			response.setRemitCumm(data.getRemit_cumm());
			response.setOsAsOn(data.getOutstand());
			if(data.getTp()<50)
				response.setStatus(data.getStatus());
			else
				response.setStatus(branchMap.get(data.getDepo_code()));
			response.setEntryDate(data.getEnt_Date());
			response.setTime(data.getEnt_time());
			if(data.getBr_name().equalsIgnoreCase("ALL INDIA") && data.getTp()<99)
				response.setColor(1);
			if(data.getBr_name().equalsIgnoreCase("ALL INDIA") && data.getTp()==99)
				response.setColor(2);
	    	saleList.add(response);

		} //end of for loop

/*		if(!first)
		{
			response=new DailyReportResponse();
			response.setColor(1);

			saleList.add(response);


			response=new DailyReportResponse();
			response.setColor(2);

			saleList.add(response);
		}
*/
		
		ApiResponse<DailyReportResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.DAILY_REPORT_SERVICE,"getDailyReport");
			throw new ApiException(e.getMessage());
		}
		

	}

}
