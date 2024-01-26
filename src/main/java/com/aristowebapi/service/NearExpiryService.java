package com.aristowebapi.service;

import com.aristowebapi.request.NearExpiryRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.NearExpiryResponse;

public interface NearExpiryService {
	
	ApiResponse<NearExpiryResponse> getExpiryReport(NearExpiryRequest request);

}
