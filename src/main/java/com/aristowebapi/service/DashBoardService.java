package com.aristowebapi.service;

import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DashBoardChartResponse;
import com.aristowebapi.response.DashBoardDataResponse;

public interface DashBoardService {
	
	DashBoardChartResponse getDashboardMainChart(int div_code,int depo_code);
	
	DashBoardChartResponse getDashboardCurrentMonth(int div_code,int depo_code);


	
	ApiResponse<DashBoardDataResponse> getDashboardTop5(int div_code,int depo_code);
	ApiResponse<DashBoardDataResponse> getDashboardThept(int div_code,int depo_code);
	ApiResponse<DashBoardDataResponse> getDashboardPendingPI(int div_code,int depo_code);

	ApiResponse<DashBoardDataResponse> getDashboardPanelData(int div_code,int depo_code);
	
	ApiResponse<DashBoardDataResponse> getDashboardGroupwiseAch(int myear,int div_code, int depo_code,int cmon,int login_id,int usertype);
	ApiResponse<DashBoardDataResponse> getDashboardBranchAch(int myear,int div_code, int depo_code,int cmon,int login_id,int usertype);
	ApiResponse<DashBoardDataResponse> getDashboardHqwiseAch(int myear,int div_code, int depo_code,int cmon,int login_id,int usertype);

	
}
