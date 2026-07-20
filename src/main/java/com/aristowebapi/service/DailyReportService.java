package com.aristowebapi.service;

import com.aristowebapi.request.DailyReportRequest;
import com.aristowebapi.request.DailyUpdationRequest;
import com.aristowebapi.request.DayWiseBillingRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DailyReportResponse;
import com.aristowebapi.response.DailyStatusResponse;
import com.aristowebapi.response.DayWiseResponse;

public interface DailyReportService {
	
	ApiResponse<DailyReportResponse> getDailyReport(DailyReportRequest request);
	
	ApiResponse<DailyStatusResponse> getDailyEntryStatus(DailyUpdationRequest request);
	
	ApiResponse<DayWiseResponse> getDayWiseReport(DayWiseBillingRequest request);

}
