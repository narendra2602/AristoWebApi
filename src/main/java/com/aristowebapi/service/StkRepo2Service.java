package com.aristowebapi.service;

import com.aristowebapi.request.StkRepo2Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.StkRepo2Response;

public interface StkRepo2Service { 
	
	ApiResponse<StkRepo2Response> getStkRepo2(StkRepo2Request request);
	ApiResponse<StkRepo2Response> getStkRepo2UV(StkRepo2Request request);

}
