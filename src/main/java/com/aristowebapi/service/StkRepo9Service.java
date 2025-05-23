package com.aristowebapi.service;

import com.aristowebapi.request.StkRepo9Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.StkRepo5Response;
import com.aristowebapi.response.StkRepo9Response;

public interface StkRepo9Service {
	
	ApiResponse<StkRepo9Response> getStkRepo9(StkRepo9Request request);
	ApiResponse<StkRepo5Response> getStkRepo5(StkRepo9Request request);

}
