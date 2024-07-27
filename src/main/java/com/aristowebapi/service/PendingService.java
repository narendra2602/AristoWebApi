package com.aristowebapi.service;

import com.aristowebapi.request.PendingRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.PendingResponse;

public interface PendingService {
	
	ApiResponse<PendingResponse> getPendingReport(PendingRequest request);

}
