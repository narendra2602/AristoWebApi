package com.aristowebapi.service;

import com.aristowebapi.request.MktRepo11Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo11Response;

public interface MktRepo11Service { 
	
	ApiResponse<MktRepo11Response> getMktRepo11(MktRepo11Request request);

}
