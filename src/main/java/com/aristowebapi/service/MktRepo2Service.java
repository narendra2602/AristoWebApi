package com.aristowebapi.service;

import com.aristowebapi.request.MktRepo2Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo2Response;
 
public interface MktRepo2Service {
	ApiResponse<MktRepo2Response> getMktRepo2(MktRepo2Request request);
}
