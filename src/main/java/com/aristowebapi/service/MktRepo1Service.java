package com.aristowebapi.service;

import com.aristowebapi.request.MktRepo1Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo1AchResponse;
import com.aristowebapi.response.MktRepo1Response;
import com.aristowebapi.response.MtbStockiestResponse;

public interface MktRepo1Service {
	 
	ApiResponse<MktRepo1Response> getMktRepo1(MktRepo1Request request);
	ApiResponse<MktRepo1AchResponse> getMktRepo1Ach(MktRepo1Request request);
	ApiResponse<MktRepo1Response> getMktRepo1Pmr(MktRepo1Request request);
	ApiResponse<MtbStockiestResponse> getMtbStockiestReport(MktRepo1Request request);
	
	
}
