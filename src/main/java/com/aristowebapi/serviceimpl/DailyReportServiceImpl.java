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
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.DailyReportDao;
import com.aristowebapi.dto.DailyEntryStatus;
import com.aristowebapi.dto.DailyReport;
import com.aristowebapi.exception.ApiException;
import com.aristowebapi.request.DailyReportRequest;
import com.aristowebapi.request.DailyUpdationRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DailyReportResponse;
import com.aristowebapi.response.DailyStatusResponse;
import com.aristowebapi.service.DailyReportService;
import com.aristowebapi.utility.AppCalculationUtils;



@Service
public class DailyReportServiceImpl  implements DailyReportService{

	Logger logger = LoggerFactory.getLogger(DailyReportServiceImpl.class);
	
	@Autowired
	private DailyReportDao dailyReportDao;
	@Autowired
	private AristoWebMessageConstant  aristoWebMessageConstant;
	public String lupdate="";
	
	private String getTitle(DailyReportRequest request,DailyReport data)
	{
		StringBuilder title=new StringBuilder();
		title.append(request.getDivCode()>0?aristoWebMessageConstant.divisionMap.get(String.valueOf(data.getDiv_code())):"ALL DIVISION");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		title.append(request.getRepType()==1?" HO Report ":" Branch Report ");
		title.append(" BILLING MONTH ");
		title.append(getDayMonthYear(sdf.format(request.getEntryDate())));
		title.append(" AS ON ");
		title.append(sdf1.format(request.getEntryDate()));
		title.append(" (In Lakhs)");
		
		return title.toString();

	}

	private String getTitle(DailyUpdationRequest request,DailyEntryStatus data)
	{
		StringBuilder title=new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		title.append(" UPDATION STATUS FOR THE MONTH ");
		title.append(getDayMonthYear(sdf.format(request.getEntryDate())));
		title.append(" AS ON ");
		title.append(sdf1.format(request.getEntryDate()));
		
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
		Map<Integer,String> divMap = new HashMap<Integer,String>();
		Map<Integer,String> corpMap = new HashMap<Integer,String>();
		try {

		if(request.getRepType()==1)	
			DailyReportList=dailyReportDao.getDailyBillDetail(request.getDivCode(),request.getLoginId(),new java.sql.Date(request.getEntryDate().getTime()),request.getMonth());
		else if(request.getRepType()==2)	
			DailyReportList=dailyReportDao.getDailyBillDetailBranch(request.getDivCode(),request.getLoginId(),new java.sql.Date(request.getEntryDate().getTime()),request.getMonth());
		size=DailyReportList.size();
		logger.info("size of the data is {}",size);

		DailyReportResponse response=null;
		List<DailyReportResponse> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		boolean status=true;	
		boolean status1=false;	
		boolean groupStatus=false;	
	
		for (int i=0;i<size;i++)
		{
			DailyReport data = DailyReportList.get(i);
			
			
			if(first)
			{
				title = getTitle(request, data);
				first=false;
			}
			
			if(request.getRepType()==1)	
			{		

				
					System.out.println("value of tp "+data.getTp()+" status is value "+data.getStatus()+" "+data.getBr_name());
				if(data.getTp()<50)
				{
					if(branchMap.containsKey(data.getDepo_code()))
					{

						if(!branchMap.get(data.getDepo_code()).contains("Open"))
						{
							branchMap.put(data.getDepo_code(), data.getStatus());
							status=false;
						}

						if(!corpMap.get(data.getDepo_code()).contains("Open"))
						{
							corpMap.put(data.getDepo_code(), data.getStatus());
						}

					}
					else
					{
						branchMap.put(data.getDepo_code(), data.getStatus());
						corpMap.put(data.getDepo_code(), data.getStatus());
					}

				}

				if(data.getTp()==50 || data.getTp()==98)
				{
					branchMap.put(data.getDepo_code(), corpMap.get(data.getDepo_code()));
					//					branchMap.put(data.getDepo_code(), groupStatus?"Open":"Close");
					//					groupStatus=false;

				}

				response=new DailyReportResponse();
				response.setBr(data.getDepo_code());
				if(data.getBr_name().equalsIgnoreCase("ALL INDIA") && data.getTp()<990)
					response.setBr(0);
				else if(data.getBr_name().contains("TOTAL") && data.getTp()<990)
					response.setBr(0);
				else if(data.getBr_name().contains("CORP") && data.getTp()==980)
					response.setBr(0);
				else if(data.getBr_name().equalsIgnoreCase("ALL INDIA") && data.getTp()==990)
					response.setBr(0);
				else if(data.getBr_name().equalsIgnoreCase("NEPAL"))
					response.setBr(0);

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
//				response.setStatus(data.getStatus());
/*				if(data.getTp()<50)
					response.setStatus(data.getStatus());
				else if(data.getTp()==980 || data.getTp()==50 )
					response.setStatus(branchMap.get(data.getDepo_code()));
				else 
					response.setStatus(status?"Open":"Close");
*/
				
				if(data.getTp()<302 || data.getTp()==970 || data.getTp()==990 || data.getTp()==980)
					response.setStatus(data.getStatus());
				else if(data.getTp()==995 || data.getTp()==996 || data.getTp()==997 || data.getTp()==998 || data.getTp()==999)
					response.setStatus(data.getStatus());
				else if(data.getTp()==970)
					response.setStatus(branchMap.get(data.getDepo_code()));
				else if(data.getTp()==980)
					response.setStatus(status1?"Open":"Close");
//					response.setStatus(divMap.get(data.getDiv_code()));
				else 
					response.setStatus(status1?"Open":"Close");

				
				response.setEntryDate(data.getEnt_Date());
				response.setTime(data.getEnt_time());
				if(data.getBr_name().equalsIgnoreCase("ALL INDIA") && data.getTp()<990)
					response.setColor(1);
				else if(data.getBr_name().contains("TOTAL") && data.getTp()<990)
					response.setColor(1);
				else if(data.getBr_name().contains("CORP") && data.getTp()==980)
					response.setColor(1);
				else if(data.getBr_name().equalsIgnoreCase("ALL INDIA") && data.getTp()==990)
					response.setColor(2);
				else if(data.getDiv_name().contains("CORP"))
					response.setColor(0);
				if(data.getDepo_code()==99)
					response.setColor(1);
				if(data.getDepo_code()==0 || data.getBr_name().equalsIgnoreCase("ALL INDIA"))
					response.setColor(2);
				if(data.getDepo_code()==0 || data.getBr_name().equalsIgnoreCase("NEPAL"))
					response.setColor(2);

				saleList.add(response);
			} // eof rep_type 1 
			else if(request.getRepType()==2)	
			{		

				if(data.getTp()<50 )
				{
					if(divMap.containsKey(data.getDiv_code()))
					{

						if(!divMap.get(data.getDiv_code()).contains("Open"))
						{
							divMap.put(data.getDiv_code(), data.getStatus());

						}

						if(!corpMap.get(data.getDiv_code()).contains("Open"))
						{
							corpMap.put(data.getDiv_code(), data.getStatus());
						}

					}
					else
					{
						divMap.put(data.getDiv_code(), data.getStatus());
						corpMap.put(data.getDiv_code(), data.getStatus());
					}

				}

				if(data.getTp()<50 )
				{
					if(branchMap.containsKey(data.getDepo_code()))
					{

						if(!branchMap.get(data.getDepo_code()).contains("Open"))
						{
							branchMap.put(data.getDepo_code(), data.getStatus());
							status1=true;
						}


					}
					else
					{
						branchMap.put(data.getDepo_code(), data.getStatus());
					}

				}

				if(data.getTp()==970)
				{
					branchMap.put(data.getDepo_code(), status1?"Open":"Close");

				}
				if(data.getTp()==980)
				{
					divMap.put(data.getDiv_code(), corpMap.get(data.getDiv_code()));

				}

				response=new DailyReportResponse();
				response.setBr(data.getDepo_code());
				if(data.getBr_name().equalsIgnoreCase("ALL INDIA") && data.getTp()<990)
					response.setBr(0);
				else if(data.getBr_name().contains("TOTAL") && data.getTp()<990)
					response.setBr(0);
				else if(data.getBr_name().contains("CORP") && data.getTp()==980)
					response.setBr(0);
				else if(data.getBr_name().equalsIgnoreCase("ALL INDIA") && data.getTp()==990)
					response.setBr(0);
				else if(data.getBr_name().equalsIgnoreCase("NEPAL"))
					response.setBr(0);

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
//				if(data.getTp()<50)
				if(data.getTp()<302 || data.getTp()==970 || data.getTp()==990 | data.getTp()==980)
					response.setStatus(data.getStatus());
				else if(data.getTp()==995 || data.getTp()==996 || data.getTp()==997 || data.getTp()==998 || data.getTp()==999)
					response.setStatus(data.getStatus());
				else if(data.getTp()==970)
					response.setStatus(branchMap.get(data.getDepo_code()));
				else if(data.getTp()==980)
					response.setStatus(divMap.get(data.getDiv_code()));
				else 
					response.setStatus(status1?"Open":"Close");
				response.setEntryDate(data.getEnt_Date());
				response.setTime(data.getEnt_time());
				if(data.getBr_name().equalsIgnoreCase("ALL INDIA") && data.getTp()<990)
					response.setColor(1);
				else if(data.getBr_name().contains("TOTAL") && data.getTp()<990)
					response.setColor(1);
				else if(data.getBr_name().contains("CORP") && data.getTp()==980)
					response.setColor(1);
				else if(data.getBr_name().equalsIgnoreCase("ALL INDIA") && data.getTp()==990)
					response.setColor(2);
				if(data.getDepo_code()==99)
					response.setColor(1);
				if(data.getDepo_code()==0 || data.getBr_name().equalsIgnoreCase("ALL INDIA"))
					response.setColor(2);

				saleList.add(response);
			} // eof rep_type 2 

		} //end of for loop

		
		ApiResponse<DailyReportResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.DAILY_REPORT_SERVICE,"getDailyReport");
			throw new ApiException(e.getMessage());
			
		}
		

	}


	@Override
	public ApiResponse<DailyStatusResponse> getDailyEntryStatus(DailyUpdationRequest request) {
		logger.info(AristoWebLogMsgConstant.DAILY_REPORT_SERVICE,"getDailyEntryStatus");
		List<DailyEntryStatus> DailyReportList=null;
		int size = 0;
		Map<Integer,String> branchMap = new HashMap<Integer,String>();
		try {

		DailyReportList=dailyReportDao.getDailyUpdationStatus(request.getLoginId(),new java.sql.Date(request.getEntryDate().getTime()),request.getMonth());
		size=DailyReportList.size();
		logger.info("size of the data is {}",size);

		DailyStatusResponse response=null;
		List<DailyStatusResponse> saleList = new ArrayList();

		boolean first=true;
		String title=null;
		
		for (int i=0;i<size;i++)
		{
			DailyEntryStatus data = DailyReportList.get(i);
			
			
			if(first)
			{
				title = getTitle(request, data);
				first=false;
			}
			
			

			response=new DailyStatusResponse();
			response.setCode(data.getDepo_code());
			response.setBranchName(data.getBr_name());
			response.setBillingDate(data.getBilling_date());
			response.setEntryDate(data.getEntry_date());
			response.setTime(data.getEntry_time());
			response.setRemark(data.getStatus());
			saleList.add(response);

		} //end of for loop

		
		ApiResponse<DailyStatusResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
		return apiResponse;
		
		} catch (Exception e) {
			logger.error(AristoWebLogMsgConstant.DAILY_REPORT_SERVICE,"getDailyReport");
			throw new ApiException(e.getMessage());
		}
		

	}

}
