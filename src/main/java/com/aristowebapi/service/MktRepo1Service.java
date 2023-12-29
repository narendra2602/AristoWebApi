package com.aristowebapi.service;

import java.util.List;

import com.aristowebapi.request.MktRepo1Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo1Response;

public interface MktRepo1Service {
	
	ApiResponse<MktRepo1Response> getMktRepo1(MktRepo1Request request);
//	List<GrossSaleResponse> getNwRepo1(GrossSaleRequest request);

}
