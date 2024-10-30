package com.aristowebapi.service;

import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DashBoardAchGthResponse;
import com.aristowebapi.response.DashBoardChartResponse;
import com.aristowebapi.response.DashBoardDataResponse;
import com.aristowebapi.response.DashBoardDataResponseDouble;
import com.aristowebapi.response.DashBoardPanelDataResponse;
import com.aristowebapi.response.StockiestResponse;
import com.aristowebapi.serviceimpl.DashBoardDataResponseAchAndGth;

public interface DashBoardService {
	 
	DashBoardChartResponse getDashboardMainChart(int myear,int div_code, int depo_code,int login_id,int utype);
	
	DashBoardChartResponse getDashboardCurrentMonth(int myear,int div_code, int depo_code,int cmon,int login_id,int utype);


	
	ApiResponse<DashBoardDataResponseDouble> getDashboardTop5(int myear,int div_code, int depo_code,int cmon,int login_id,int utype);
	ApiResponse<DashBoardDataResponseDouble> getDashboardThept(int myear,int div_code, int depo_code,int cmon,int login_id,int utype);
	ApiResponse<DashBoardDataResponseDouble> getDashboardPendingPI(int myear,int div_code, int depo_code,int cmon,int login_id,int utype);

	ApiResponse<DashBoardPanelDataResponse> getDashboardPanelData(int myear,int div_code, int depo_code,int cmon,int login_id,int utype);

	ApiResponse<DashBoardAchGthResponse> getDashboardAchGthData(int myear,int div_code, int depo_code,int utype,int login_id);

	ApiResponse<DashBoardDataResponseAchAndGth> getDashboardGroupwiseAch(int myear,int div_code, int depo_code,int cmon,int login_id,int usertype);
	ApiResponse<DashBoardDataResponseAchAndGth> getDashboardBranchAch(int myear,int div_code, int depo_code,int cmon,int login_id,int usertype);
	ApiResponse<DashBoardDataResponseAchAndGth> getDashboardHqwiseAch(int myear,int div_code, int depo_code,int cmon,int login_id,int usertype);
	ApiResponse<DashBoardDataResponseDouble> getDashboardTop5Products(int myear,int div_code, int depo_code,int cmon,int login_id);
	ApiResponse<DashBoardDataResponse> getDashboardMonthCombo(int myear,int div_code, int depo_code,int login_id,int usertype);
	ApiResponse<DashBoardDataResponse> getDashboardYearCombo();
	
	ApiResponse<DashBoardDataResponse> getDivisionList(int loginId);

	ApiResponse<DashBoardDataResponse> getBranchList(int loginId);
	ApiResponse<DashBoardDataResponse> getHoBranchList(int loginId,int divCode);
	ApiResponse<DashBoardDataResponse> getHqList(int myear,int div_code, int depo_code,int utype,int login_id);
	ApiResponse<DashBoardDataResponse> getRegionList(int myear,int div_code, int depo_code,int utype,int login_id);
	ApiResponse<DashBoardDataResponse> getAreaList(int myear,int div_code, int depo_code,int utype,int login_id);

	
	
	ApiResponse<DashBoardDataResponse> getProductList(int div_code,int utype,int login_id,int myear,int gp_code);
	
	ApiResponse<DashBoardDataResponse> getGroupList(int div_code,int utype,int login_id,int myear);
	
	ApiResponse<DashBoardDataResponse> getIqviaGroupList(int div_code);

	ApiResponse<StockiestResponse> getStockiestList(int myear,int div_code, int depo_code,int utype,int login_id);
	
}
