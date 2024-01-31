package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.DashBoardDao;
import com.aristowebapi.dto.DashBoardCurrentMonthChart;
import com.aristowebapi.dto.DashBoardData;
import com.aristowebapi.dto.DashBoardSalesChart;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DashBoardChartResponse;
import com.aristowebapi.response.DashBoardDataResponse;
import com.aristowebapi.response.DataSetResponse;
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
	public DashBoardChartResponse getDashboardMainChart(int div_code, int depo_code) {
		// TODO Auto-generated method stub
		List<DashBoardSalesChart> chartList= dashBoardDao.getDashboardMainChart(div_code, depo_code);
		
		DashBoardChartResponse dashBoardChartResponse=null;
		DashBoardSalesChart dashBoardChart=null;
		List<Integer> dataValue=null;
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
				dataValue = new ArrayList<Integer>();
				dataSetResponseList = new ArrayList<>();
				chartLabels = new LinkedHashSet<>();
				srno=dashBoardChart.getSrno();
				first=false;
			}
			
			if(srno!=dashBoardChart.getSrno())
			{

				DataSetResponse dataSetResponse = new DataSetResponse();
				dataSetResponse.setValues(dataValue);
				dataSetResponse.setDatasetLabel(srno==1?"Sales":srno==2?"Target":"Last Year");
				dataSetResponse.setDataSetColor(srno==1?aristoWebMessageConstant.sale:srno==2?aristoWebMessageConstant.target:aristoWebMessageConstant.lys);
				dataSetResponseList.add(dataSetResponse);
				
				
				dataValue = new ArrayList<Integer>();
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
	public DashBoardChartResponse getDashboardCurrentMonth(int div_code, int depo_code) {
		
		DashBoardCurrentMonthChart chartData= dashBoardDao.getDashboardCurrentMonthChart(div_code, depo_code,2);
		DashBoardChartResponse dashBoardChartResponse=new DashBoardChartResponse();
		List<Integer> dataValue=null;
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
		dataValue.add((int) AppCalculationUtils.valueDivideByLacs(chartData.getSurdef()));
		dataValue.add((int) chartData.getAch());
		dataValue.add((int) AppCalculationUtils.valueDivideByLacs(chartData.getSaleval()));
		dataValue.add((int) AppCalculationUtils.valueDivideByLacs(chartData.getTargetval()));

		DataSetResponse dataSetResponse = new DataSetResponse();
		dataSetResponse.setValues(dataValue);
		dataSetResponse.setDatasetLabel("Current Month");
		dataSetResponse.setDataSetColor("Red");
		dataSetResponseList.add(dataSetResponse);

		dashBoardChartResponse.setDataSet(dataSetResponseList);
		
		
		return dashBoardChartResponse;

	}

	@Override
	public ApiResponse<DashBoardDataResponse> getDashboardTop5(int div_code, int depo_code) {
		List<DashBoardData> dataList= dashBoardDao.getDashboardTop5Stockiest(div_code, depo_code);
		
		List<DashBoardDataResponse> saleList = getResponseData(dataList);
		String title="Top 5 Stockiest";
		String lupdate="";
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
		return apiResponse;

	}

	@Override
	public ApiResponse<DashBoardDataResponse> getDashboardThept(int div_code, int depo_code) {
		List<DashBoardData> dataList= dashBoardDao.getDashboardTheraputicSales(div_code, depo_code);
		List<DashBoardDataResponse> saleList = getResponseData(dataList);
		String title="Theraputic Sales";
		String lupdate="";
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
		return apiResponse;

	}

	@Override
	public ApiResponse<DashBoardDataResponse> getDashboardPendingPI(int div_code, int depo_code) {
		List<DashBoardData> dataList= dashBoardDao.getDashboardPendingPI(div_code, depo_code);
		List<DashBoardDataResponse> saleList = getResponseData(dataList);
		String title="Pending PI";
		String lupdate="";
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
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

	@Override
	public ApiResponse<DashBoardDataResponse> getDashboardPanelData(int div_code, int depo_code) {
		// TODO Auto-generated method stub
		DashBoardCurrentMonthChart panelData= dashBoardDao.getDashboardCurrentMonthChart(div_code, depo_code,1);
		
		List<DashBoardDataResponse> saleList = new ArrayList<DashBoardDataResponse>();
		DashBoardDataResponse response= new DashBoardDataResponse();
		response.setName("Sale");
		response.setValue(panelData.getSaleval());
		saleList.add(response);
		
		response= new DashBoardDataResponse();
		response.setName("Target");
		response.setValue(panelData.getTargetval());
		saleList.add(response);
		
		response= new DashBoardDataResponse();
		response.setName("Last Year");
		response.setValue(panelData.getLysval());
		saleList.add(response);
		
		response= new DashBoardDataResponse();
		response.setName("Ach");
		response.setValue((int) panelData.getAch());
		saleList.add(response);
		
		response= new DashBoardDataResponse();
		response.setName("SurDef");
		response.setValue((int) panelData.getSurdef());
		saleList.add(response);
		

		response= new DashBoardDataResponse();
		response.setName("Gth");
		response.setValue((int) panelData.getGth());
		saleList.add(response);
		String title="Current Month Chart";
		String lupdate="";
		int size=1;
		
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
		return apiResponse;
	}

	@Override
	public ApiResponse<DashBoardDataResponse> getDashboardGroupwiseAch(int myear,int div_code, int depo_code,int cmon,int login_id,int utype) {
		List<DashBoardData> dataList= dashBoardDao.getDashboardGroupwiseAch(myear, div_code, depo_code, cmon, login_id, utype);
		List<DashBoardDataResponse> saleList = getResponseData(dataList);
		String title=getMonth("Groupwise Achievement % ",myear,cmon);
		String lupdate="";
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
		return apiResponse;
	}

	@Override
	public ApiResponse<DashBoardDataResponse> getDashboardBranchAch(int myear,int div_code, int depo_code,int cmon,int login_id,int utype) {
		List<DashBoardData> dataList= dashBoardDao.getDashboardBranchwiseAch(myear, div_code, depo_code, cmon, login_id, utype);
		List<DashBoardDataResponse> saleList = getResponseData(dataList);
		String title=getMonth("Branchwise Achievement % ",myear,cmon);
		String lupdate="";
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
		return apiResponse;
	}

	@Override
	public ApiResponse<DashBoardDataResponse> getDashboardHqwiseAch(int myear,int div_code, int depo_code,int cmon,int login_id,int utype) {
		List<DashBoardData> dataList= dashBoardDao.getDashboardHqwiseAch(myear, div_code, depo_code, cmon, login_id, utype);
		List<DashBoardDataResponse> saleList = getResponseData(dataList);
		String title=getMonth("Hqwise Achievement % ",myear,cmon);

		String lupdate="";
		int size=dataList.size();
		
		ApiResponse<DashBoardDataResponse> apiResponse = new ApiResponse<>(title!=null?title.toString():"", size,lupdate,saleList);
		return apiResponse;
	}

	
	private String getMonth(String title,int myear,int mon)
	{
		StringBuilder sb=new StringBuilder(title);
		sb.append(monthArray[mon]).append("-").append(mon>3?myear+1:myear);
		return sb.toString();
	}
}
