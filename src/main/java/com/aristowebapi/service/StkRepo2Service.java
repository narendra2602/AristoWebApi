package com.aristowebapi.service;

import com.aristowebapi.request.StkRepo2Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.StkRepo2UnitResponse;
import com.aristowebapi.response.StkRepo2UVResponse;
import com.aristowebapi.response.StkRepo2ValueResponse;

public interface StkRepo2Service {
	
	ApiResponse<StkRepo2UnitResponse> getStkRepo2Unit(StkRepo2Request request);
	
	ApiResponse<StkRepo2ValueResponse> getStkRepo2Value(StkRepo2Request request);
	
	ApiResponse<StkRepo2UVResponse> getStkRepo2UV(StkRepo2Request request);

}
