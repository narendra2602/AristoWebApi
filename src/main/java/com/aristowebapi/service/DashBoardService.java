package com.aristowebapi.service;

import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchResponse;
import com.aristowebapi.response.DashBoardChartResponse;
import com.aristowebapi.response.DashBoardDataResponse;
import com.aristowebapi.response.DashBoardPanelDataResponse;
import com.aristowebapi.response.DivResponse;

public interface DashBoardService {
	
	DashBoardChartResponse getDashboardMainChart(int myear,int div_code, int depo_code,int login_id,int utype);
	
	DashBoardChartResponse getDashboardCurrentMonth(int myear,int div_code, int depo_code,int cmon,int login_id,int utype);


	
	ApiResponse<DashBoardDataResponse> getDashboardTop5(int myear,int div_code, int depo_code,int cmon,int login_id,int utype);
	ApiResponse<DashBoardDataResponse> getDashboardThept(int myear,int div_code, int depo_code,int cmon,int login_id,int utype);
	ApiResponse<DashBoardDataResponse> getDashboardPendingPI(int myear,int div_code, int depo_code,int cmon,int login_id,int utype);

	ApiResponse<DashBoardPanelDataResponse> getDashboardPanelData(int myear,int div_code, int depo_code,int cmon,int login_id,int utype);
	
	ApiResponse<DashBoardDataResponse> getDashboardGroupwiseAch(int myear,int div_code, int depo_code,int cmon,int login_id,int usertype);
	ApiResponse<DashBoardDataResponse> getDashboardBranchAch(int myear,int div_code, int depo_code,int cmon,int login_id,int usertype);
	ApiResponse<DashBoardDataResponse> getDashboardHqwiseAch(int myear,int div_code, int depo_code,int cmon,int login_id,int usertype);
	ApiResponse<DashBoardDataResponse> getDashboardTop5Products(int myear,int div_code, int depo_code,int cmon,int login_id);
	ApiResponse<DashBoardDataResponse> getDashboardMonthCombo(int myear,int div_code, int depo_code,int login_id,int usertype);
	ApiResponse<DashBoardDataResponse> getDashboardYearCombo();
	
	ApiResponse<DashBoardDataResponse> getDivisionList(int loginId);

	ApiResponse<DashBoardDataResponse> getBranchList(int loginId);

	
}
