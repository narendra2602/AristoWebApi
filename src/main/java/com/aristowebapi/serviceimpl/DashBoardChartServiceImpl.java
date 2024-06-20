package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.DashBoardDao;
import com.aristowebapi.dto.DashBoardAchGthTrend;
import com.aristowebapi.dto.DashBoardCurrentMonthChart;
import com.aristowebapi.dto.DashBoardData;
import com.aristowebapi.dto.DashBoardDataAchGTh;
import com.aristowebapi.dto.DashBoardDataDouble;
import com.aristowebapi.dto.DashBoardSalesChart;
import com.aristowebapi.dto.MktRepo9;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.dto.StockiestMaster;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DashBoardAchGthResponse;
import com.aristowebapi.response.DashBoardChartResponse;
import com.aristowebapi.response.DashBoardDataResponse;
import com.aristowebapi.response.DashBoardDataResponseDouble;
import com.aristowebapi.response.DashBoardPanelDataResponse;
import com.aristowebapi.response.DataSetResponse;
import com.aristowebapi.response.MktRepo9Response;
import com.aristowebapi.response.StockiestResponse;
import com.aristowebapi.service.DashBoardService;
import com.aristowebapi.utility.AppCalculationUtils;

@Service
public class DashBoardChartServiceImpl implements DashBoardService {

	private final String monthArray[] = new String[] {"","OCT","NOV","DEC","JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP"};
	
	@Autowired
	private AristoWebMessageConstant aristoWebMessageConstant;
	
	@Autowired
	private DashBoardDao dashBoardDao; 
	
	@Override
	public DashBoardChartResponse getDashboardMainChart(int myear,int div_code, int depo_code,int login_id,int utype) {
		// TODO Auto-generated method stub
		List<DashBoardSalesChart> chartList= dashBoardDao.getDashboardMainChart(myear,div_code,depo_code,login_id,utype);
		
		DashBoardChartResponse dashBoardChartResponse=null;
		DashBoardSalesChart dashBoardChart=null;
		List<Double> dataValue=null;
		List<DataSetResponse> dataSetResponseList=null;
		Set<String> chartLabels = null;
		int size = chartList.size();
		int srno=0;
		boolean first=true;
		for(int i=0; i<size;i++)
		{
			dashBoardChart = chartList.get(i);
			if(first){
				dashBoardChartResponse = new DashBoardChartResponse();
				dataValue = new ArrayList<Double>();
				dataSetResponseList = new ArrayList<>();
				chartLabels = new LinkedHashSet<>();
				srno=dashBoardChart.getSrno();
				first=false;
			}
			
			if(srno!=dashBoardChart.getSrno())
			{

				DataSetResponse dataSetResponse = new DataSetResponse();
				dataSetResponse.setValues(dataValue);
				dataSetResponse.setDatasetLabel(srno==2?"Net Sale":srno==1?"Budget":"Last Year");
				dataSetResponse.setDataSetColor(srno==2?aristoWebMessageConstant.sale:srno==1?aristoWebMessageConstant.target:aristoWebMessageConstant.lys);
				dataSetResponseList.add(dataSetResponse);
				
				
				dataValue = new ArrayList<Double>();
				srno=dashBoardChart.getSrno();

			}
			
			chartLabels.add(dashBoardChart.getMnth_abbr());
			dataValue.add(dashBoardChart.getVal());
			
		}
		
		if(!first)
		{

			DataSetResponse dataSetResponse = new DataSetResponse();
			dataSetResponse.setValues(dataValue);
			dataSetResponse.setDatasetLabel(srno==1?"Sales":srno==2?"Target":"Last Year");
			dataSetResponse.setDataSetColor(srno==1?aristoWebMessageConstant.sale:srno==2?aristoWebMessageConstant.target:aristoWebMessageConstant.lys);
			dataSetResponseList.add(dataSetResponse);

			dashBoardChartResponse.setChartType("bar");
			dashBoardChartResponse.setChartTitle("Target Vs Sales");
			dashBoardChartResponse.setChartLabels(chartLabels);
			dashBoardChartResponse.setDataSet(dataSetResponseList);
		}

		return dashBoardChartResponse;

	}

	@Override
	public DashBoardChartResponse getDashboardCurrentMonth(int myear,int div_code, int depo_code,int cmon,int login_id,int utype) {
		
		DashBoardCurrentMonthChart chartData= dashBoardDao.getDashboardCurrentMonthChart(myear,div_code,depo_code,cmon,login_id,utype,2);
		DashBoardChartResponse dashBoardChartResponse=new DashBoardChartResponse();
		List<Double> dataValue=null;
		List<DataSetResponse> dataSetResponseList= new ArrayList<>();
		Set<String> chartLabels = null;

		dashBoardChartResponse.setChartTitle("Current Month Chart");
		dashBoardChartResponse.setChartType("horizontalBar");
		chartLabels = new LinkedHashSet<>();
		chartLabels.add("SurDef");
		chartLabels.add("Ach");
		chartLabels.add("Sale");
		chartLabels.add("Target");
		dashBoardChartResponse.setChartLabels(chartLabels);
		
		dataValue = new ArrayList<>();
		dataValue.add(AppCalculationUtils.valueDivideByLacs(chartData.getSurdef()));
		dataValue.add(chartData.getAch());
		dataValue.add(AppCalculationUtils.valueDivideByLacs(chartData.getSaleval()));
		dataValue.add(AppCalculationUtils.valueDivideByLacs(chartData.getTargetval()));

		DataSetResponse dataSetResponse = new DataSetResponse();
		dataSetResponse.setValues(dataValue);
		dataSetResponse.setDatasetLabel("Current Month");
		dataSetResponse.setDataSetColor("Red");
		dataSetResponseList.add(dataSetResponse);

		dashBoardChartResponse.setDataSet(dataSetResponseList);
		
		
		return dashBoardChartResponse;

	}

	@Override
	public ApiResponse<DashBoardDataResponseDouble> getDashboardTop5(int myear,int div_code, int depo_code,int cmon,int login_id,int utype) {
		List<DashBoardDataDouble> dataList= dashBoardDao.getDashboardTop5Stockiest(myear,div_code,depo_code,cmon,login_id,utype);
		
		List<DashBoardDataResponseDouble> saleList = getResponseDataDouble(dataList);
		String title=getMonth("Top 5 Stockiest Sale for ",myear,cmon);
		
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponseDouble> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;

	}

	@Override
	public ApiResponse<DashBoardDataResponseDouble> getDashboardThept(int myear,int div_code, int depo_code,int cmon,int login_id,int utype) {
		List<DashBoardDataDouble> dataList= dashBoardDao.getDashboardTheraputicSales(myear,div_code,depo_code,cmon,login_id,utype);
		List<DashBoardDataResponseDouble> saleList = getResponseDataDouble(dataList);
		
		String title=getMonth("Therapeutic Sales for ",myear,cmon);
		
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponseDouble> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;

	}

	@Override
	public ApiResponse<DashBoardDataResponseDouble> getDashboardPendingPI(int myear,int div_code, int depo_code,int cmon,int login_id,int utype) {
		List<DashBoardDataDouble> dataList= dashBoardDao.getDashboardPendingPI(myear,div_code,depo_code,cmon,login_id,utype);
		List<DashBoardDataResponseDouble> saleList = getResponseDataDouble(dataList);
		
		String title=getMonth(aristoWebMessageConstant.pendingPi,myear,cmon);
		
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponseDouble> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;

	}

	private List<DashBoardDataResponse> getResponseData(List<DashBoardData> dataList)
	{
		List<DashBoardDataResponse> saleList = new ArrayList<DashBoardDataResponse>();
		int size = dataList.size();
		
		DashBoardDataResponse response=null;
		
		
		for(int i=0; i<size;i++)
		{

			DashBoardData data = dataList.get(i);
			response= new DashBoardDataResponse();
			response.setName(data.getName());
			response.setValue(data.getVal());
			saleList.add(response);
		}
		return saleList;

	}

	
	private List<StockiestResponse> getResponseStk(List<StockiestMaster> dataList)
	{
		List<StockiestResponse> saleList = new ArrayList<StockiestResponse>();
		int size = dataList.size();
		
		StockiestResponse response=null;
		
		
		for(int i=0; i<size;i++)
		{

			StockiestMaster data = dataList.get(i);
			response= new StockiestResponse();
			response.setName(data.getName());
			response.setCode(data.getCode());
			saleList.add(response);
		}
		return saleList;

	}

	
	private List<DashBoardDataResponseDouble> getResponseDataDouble(List<DashBoardDataDouble> dataList)
	{
		List<DashBoardDataResponseDouble> saleList = new ArrayList<DashBoardDataResponseDouble>();
		int size = dataList.size();
		
		DashBoardDataResponseDouble response=null;
		
		
		for(int i=0; i<size;i++)
		{

			DashBoardDataDouble data = dataList.get(i);
			response= new DashBoardDataResponseDouble();
			response.setName(data.getName());
			response.setValue(data.getVal());
			saleList.add(response);
		}
		return saleList;

	}

	private List<DashBoardDataResponseAchAndGth> getResponseDataDoubleNew(List<DashBoardDataAchGTh> dataList)
	{
		List<DashBoardDataResponseAchAndGth> saleList = new ArrayList<DashBoardDataResponseAchAndGth>();
		int size = dataList.size();
		
		DashBoardDataResponseAchAndGth response=null;
		
		
		for(int i=0; i<size;i++)
		{

			DashBoardDataAchGTh data = dataList.get(i);
			response= new DashBoardDataResponseAchAndGth();
			response.setName(data.getName());
			response.setAchPer(data.getVal());
			response.setGthPer(data.getGth());
			saleList.add(response);
		}
		return saleList;

	}

	
	@Override
	public ApiResponse<DashBoardPanelDataResponse> getDashboardPanelData(int myear,int div_code, int depo_code,int cmon,int login_id,int utype) {
		// TODO Auto-generated method stub
		DashBoardCurrentMonthChart panelDataCumm= dashBoardDao.getDashboardCurrentMonthChart(myear,div_code,depo_code,cmon,login_id,utype,1);
		DashBoardCurrentMonthChart panelDataMonthly= dashBoardDao.getDashboardCurrentMonthChart(myear,div_code,depo_code,cmon,login_id,utype,2);
		
		List<DashBoardPanelDataResponse> saleList = new ArrayList<DashBoardPanelDataResponse>();
		DashBoardPanelDataResponse response= new DashBoardPanelDataResponse();
		response.setName("Sale");
		response.setMonthly(String.valueOf(panelDataMonthly.getSaleval()));
		response.setCumm(String.valueOf(panelDataCumm.getSaleval()));
		saleList.add(response);
		
		response= new DashBoardPanelDataResponse();
		response.setName("Target");
		response.setMonthly(String.valueOf(panelDataMonthly.getTargetval()));
		response.setCumm(String.valueOf(panelDataCumm.getTargetval()));

		saleList.add(response);
		
		response= new DashBoardPanelDataResponse();
		response.setName("Last Year");
		response.setMonthly(String.valueOf(panelDataMonthly.getLysval()));
		response.setCumm(String.valueOf(panelDataCumm.getLysval()));
		saleList.add(response);
		
		response= new DashBoardPanelDataResponse();
		response.setName("Ach");
		response.setMonthly(String.valueOf(panelDataMonthly.getAch()));
		response.setCumm(String.valueOf(panelDataCumm.getAch()));
		saleList.add(response);
		
		response= new DashBoardPanelDataResponse();
		response.setName("SurDef");
		response.setMonthly(String.valueOf(panelDataMonthly.getSurdef()));
		response.setCumm(String.valueOf(panelDataCumm.getSurdef()));

		saleList.add(response);
		

		response= new DashBoardPanelDataResponse();
		response.setName("Gth");
		response.setMonthly(String.valueOf(panelDataMonthly.getGth()));
		response.setCumm(String.valueOf(panelDataCumm.getGth()));

		saleList.add(response);
		String title="Panel Data";
		
		int size=6;
		
		ApiResponse<DashBoardPanelDataResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;
	}

	@Override
	public ApiResponse<DashBoardDataResponseAchAndGth> getDashboardGroupwiseAch(int myear,int div_code, int depo_code,int cmon,int login_id,int utype) {
		List<DashBoardDataAchGTh> dataList= dashBoardDao.getDashboardGroupwiseAch(myear, div_code, depo_code, cmon, login_id, utype);
		List<DashBoardDataResponseAchAndGth> saleList = getResponseDataDoubleNew(dataList);
		String title=getMonth("Groupwise Achievement & Growth % for ",myear,cmon);
		
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponseAchAndGth> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;
	}

	@Override
	public ApiResponse<DashBoardDataResponseAchAndGth> getDashboardBranchAch(int myear,int div_code, int depo_code,int cmon,int login_id,int utype) {
		List<DashBoardDataAchGTh> dataList= dashBoardDao.getDashboardBranchwiseAch(myear, div_code, depo_code, cmon, login_id, utype);
		List<DashBoardDataResponseAchAndGth> saleList = getResponseDataDoubleNew(dataList);
		String title=getMonth("Branchwise Achievement & Growth %  for ",myear,cmon);
		
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponseAchAndGth> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;
	}

	@Override
	public ApiResponse<DashBoardDataResponseAchAndGth> getDashboardHqwiseAch(int myear,int div_code, int depo_code,int cmon,int login_id,int utype) {
		List<DashBoardDataAchGTh> dataList= dashBoardDao.getDashboardHqwiseAch(myear, div_code, depo_code, cmon, login_id, utype);
		List<DashBoardDataResponseAchAndGth> saleList = getResponseDataDoubleNew(dataList);
		String title=getMonth("HQwise Achievement & Growth % for ",myear,cmon);

		
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponseAchAndGth> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;
	}

	
	private String getMonth(String title,int myear,int mon)
	{
		StringBuilder sb=new StringBuilder(title);
		sb.append(monthArray[mon]).append("-").append(mon>3?(myear+1):myear);
		return sb.toString();
	}

	@Override
	public ApiResponse<DashBoardDataResponseDouble> getDashboardTop5Products(int myear,int div_code, int depo_code, int cmon,int login_id) {
		List<DashBoardDataDouble> dataList= dashBoardDao.getDashboardTop5Products(myear,div_code, depo_code,cmon, login_id);
		List<DashBoardDataResponseDouble> saleList = getResponseDataDouble(dataList);
		String title=getMonth("Top 5 Products Sale for ",myear,cmon);

		
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponseDouble> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;
	}

	@Override
	public ApiResponse<DashBoardDataResponse> getDashboardMonthCombo(int myear, int div_code, int depo_code,int login_id, int usertype) 
	{
		int monthIndex= dashBoardDao.getDashboardMonthIndex(myear,div_code, depo_code, login_id,usertype);
		
		List<DashBoardDataResponse> monthList = new ArrayList<>();
		for(int i=1;i<monthArray.length;i++)
		{
			DashBoardDataResponse response=new DashBoardDataResponse();
			response.setName(monthArray[i]);
			response.setValue(i);
			monthList.add(response);
		}
			
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>("Month",12,monthList,monthIndex);
		return apiResponse;

	}

	@Override
	public ApiResponse<DashBoardDataResponse> getDashboardYearCombo() {
		List<DashBoardData> dataList= dashBoardDao.getDashboardYearCombo();
		List<DashBoardDataResponse> yearList = getResponseData(dataList);
		

		
		int size=dataList.size();
		System.out.println("size is "+size);
		
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>("Year",size,yearList);
		return apiResponse;
		
	}

	@Override
	public ApiResponse<DashBoardDataResponse> getDivisionList(int loginId) {
		
		List<DashBoardData> dataList=dashBoardDao.getDivList(loginId);
		List<DashBoardDataResponse> divlist = getResponseData(dataList);
		int size=dataList.size();
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>("Year",size,divlist);
		return apiResponse;

	}


	@Override
	public ApiResponse<DashBoardDataResponse> getBranchList(int loginId) {
		
		List<DashBoardData> branchlist=dashBoardDao.getBranchList(loginId);
		List<DashBoardDataResponse> branchResponseList=new ArrayList<>();
		
		int size=branchlist.size();

		if(size>1 )
		{
			DashBoardData  branchdto=new DashBoardData()
			{
				
				@Override
				public String getName() {
					return "All India";
				}

				@Override
				public long getVal() {
					return 0;
				}
			};
			branchlist.add(0, branchdto);
		}

		branchlist.forEach(data->{
			
			

			DashBoardDataResponse branchres=new DashBoardDataResponse();
			branchres.setValue(data.getVal());
			branchres.setName(data.getName());
			branchResponseList.add(branchres);
		});
	
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>("Branch",size,branchResponseList);
		return apiResponse;

	}

	@Override
	public ApiResponse<DashBoardDataResponse> getHqList(int myear, int div_code, int depo_code, int utype,int login_id) {
		List<DashBoardData> dataList= dashBoardDao.getHqList(myear,div_code, depo_code,utype, login_id);
		List<DashBoardDataResponse> saleList = getResponseData(dataList);
		String title="Hq List ";

		
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;

		
	}

	@Override
	public ApiResponse<DashBoardDataResponse> getProductList(int div_code, int utype, int login_id, int myear,int gp_code) {
		List<DashBoardData> dataList= dashBoardDao.getProductList(div_code,utype, login_id,myear,gp_code);
		List<DashBoardDataResponse> saleList = getResponseData(dataList);
		String title="Product List ";

		
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;

		
	}

	@Override
	public ApiResponse<DashBoardDataResponse> getGroupList(int div_code, int utype, int login_id,int myear) {
		List<DashBoardData> dataList= dashBoardDao.getGroupList(div_code,utype, login_id, myear);
		List<DashBoardDataResponse> saleList = getResponseData(dataList);
		String title="Group List ";

		
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;
	}

	@Override
	public ApiResponse<StockiestResponse> getStockiestList(int myear, int div_code, int depo_code, int utype,int login_id) {
		List<StockiestMaster> dataList= dashBoardDao.getStockiestList(myear,div_code,depo_code,utype, login_id);
		List<StockiestResponse> saleList = getResponseStk(dataList);
		String title="Stockiest List ";

		
		int size=dataList.size();
		
		ApiResponse<StockiestResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;
	}

	@Override
	public ApiResponse<DashBoardDataResponse> getRegionList(int myear, int div_code, int depo_code, int utype,int login_id) {
		List<DashBoardData> dataList= dashBoardDao.getRegionList(myear,div_code, depo_code,utype, login_id);
		List<DashBoardDataResponse> saleList = getResponseData(dataList);
		String title="Region List ";

		
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;

	}

	@Override
	public ApiResponse<DashBoardDataResponse> getAreaList(int myear, int div_code, int depo_code, int utype,int login_id) {
		List<DashBoardData> dataList= dashBoardDao.getAreaList(myear,div_code, depo_code,utype, login_id);
		List<DashBoardDataResponse> saleList = getResponseData(dataList);
		String title="Area List ";

		
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,saleList);
		return apiResponse;


		
	}

	@Override
	public ApiResponse<DashBoardAchGthResponse> getDashboardAchGthData(int myear, int div_code, int depo_code,
			int utype, int login_id) {
		
		String title="Trend";
		
		List<DashBoardAchGthTrend> DataList=null;

		
			
		DataList=dashBoardDao.getDashboardAchGthReport(myear,div_code,depo_code,utype,login_id);
				
			
		DashBoardAchGthResponse response=null;
		
		List<DashBoardAchGthResponse> saleList = new ArrayList();

		Map<String, Double> ach=null;
		Map<String, Double> gth=null;

		
		boolean first=true;
		int size = DataList.size();
		
		
		//create ReportTitleResponse class object and set title with Report heading
		int i=0;
		for (i=0;i<size;i++)
		{
			DashBoardAchGthTrend data = DataList.get(i);
			if(first)
			{
				response=new DashBoardAchGthResponse();
				ach=new LinkedHashMap();
				gth=new LinkedHashMap();
				first=false;
			}

			ach.put(data.getMnth_abbr(),data.getAch());
			gth.put(data.getMnth_abbr(),data.getGth());
			
		}		

		
		
		response.setAchPer(ach);
		response.setGthPer(gth);
		saleList.add(response);
		
		return new ApiResponse<DashBoardAchGthResponse>(title.toString(),size,saleList);
	}


}
