package com.aristowebapi.service;

import com.aristowebapi.response.AbmReportingResponse;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DashBoardDataResponse;
import com.aristowebapi.response.PsrResponse;

public interface AbmReportingService {

	ApiResponse<AbmReportingResponse> getAbmReporting(int loginId);
	ApiResponse<PsrResponse> getPsrList(int loginId);
	ApiResponse<DashBoardDataResponse> getAristoBrandList(int mktYear,int divCode);
}
