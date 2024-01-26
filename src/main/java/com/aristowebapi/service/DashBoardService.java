package com.aristowebapi.service;

import java.util.List;

import com.aristowebapi.response.DashBoardChartResponse;
import com.aristowebapi.response.DashBoardDataResponse;

public interface DashBoardService {
	
	DashBoardChartResponse getDashboardMainChart(int div_code,int depo_code);
	
	DashBoardChartResponse getDashboardCurrentMonth(int div_code,int depo_code);


	
	List<DashBoardDataResponse> getDashboardTop5(int div_code,int depo_code);
	List<DashBoardDataResponse> getDashboardThept(int div_code,int depo_code);
	List<DashBoardDataResponse> getDashboardPendingPI(int div_code,int depo_code);

	List<DashBoardDataResponse> getDashboardPanelData(int div_code,int depo_code);

	
}
