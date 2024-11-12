package com.aristowebapi.service;

import com.aristowebapi.request.MktRepo2Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo2Response;
import com.aristowebapi.response.MktRepo2SelectiveItemResponse;
 
public interface MktRepo2Service {
	ApiResponse<MktRepo2Response> getMktRepo2(MktRepo2Request request);
	ApiResponse<MktRepo2SelectiveItemResponse> getMktRepo2SelectiveItem(MktRepo2Request request);
	
}
