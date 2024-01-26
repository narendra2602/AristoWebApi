package com.aristowebapi.service;

import com.aristowebapi.request.StkRepo3Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.StkRepo3Response;

public interface StkRepo3Service {
	
	ApiResponse<StkRepo3Response> getStkRepo3(StkRepo3Request request);

}
