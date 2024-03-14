package com.aristowebapi.service;

import com.aristowebapi.request.MktRepo8Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo8Response;

public interface MktRepo8Service { 
	
	ApiResponse<MktRepo8Response> getMktRepo8(MktRepo8Request request);

}
