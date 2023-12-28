package com.aristowebapi.service;

import com.aristowebapi.request.MktRepo4Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo4Response;

public interface MktRepo4Service {
	
	ApiResponse<MktRepo4Response> getMktRepo4(MktRepo4Request request);

}
