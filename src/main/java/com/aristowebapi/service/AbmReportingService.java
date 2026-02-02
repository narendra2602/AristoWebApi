package com.aristowebapi.service;

import com.aristowebapi.request.AbmReportingRequest;
import com.aristowebapi.response.AbmReportingResponse;
import com.aristowebapi.response.ApiResponse;

public interface AbmReportingService {

	ApiResponse<AbmReportingResponse> getAbmReporting(int loginId);
}
