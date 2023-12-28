package com.aristowebapi.service;

import java.util.List;

import com.aristowebapi.request.GrossSaleRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.GrossSaleResponse;

public interface MktRepo1Service {
	
	ApiResponse<GrossSaleResponse> getMktRepo1(GrossSaleRequest request);
//	List<GrossSaleResponse> getNwRepo1(GrossSaleRequest request);

}
