package com.aristowebapi.service;

import com.aristowebapi.request.HOMarketingRepo1Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.HOMarketingRepo1Response;

public interface HOMarketingRepo1Service {
	
	ApiResponse<HOMarketingRepo1Response> getHoMarketingRepo1(HOMarketingRepo1Request request);

}
