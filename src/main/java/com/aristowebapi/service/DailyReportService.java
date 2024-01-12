package com.aristowebapi.service;

import com.aristowebapi.request.DailyReportRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DailyReportResponse;

public interface DailyReportService {
	
	ApiResponse<DailyReportResponse> getDailyReport(DailyReportRequest request);

}
