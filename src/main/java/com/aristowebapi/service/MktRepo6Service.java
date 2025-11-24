package com.aristowebapi.service;

import com.aristowebapi.request.MktRepo6Request;
import com.aristowebapi.request.MktRepo6aRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo6Response;
import com.aristowebapi.response.MktRepo6aResponse;

public interface MktRepo6Service {
	 
	ApiResponse<MktRepo6Response> getMktRepo6(MktRepo6Request request);
	
	ApiResponse<MktRepo6aResponse> getMktRepo6a(MktRepo6aRequest request);

}
