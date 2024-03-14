package com.aristowebapi.service;

import com.aristowebapi.request.MktRepo6Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo6Response;

public interface MktRepo6Service {
	 
	ApiResponse<MktRepo6Response> getMktRepo6(MktRepo6Request request);

}
