package com.aristowebapi.service;

import com.aristowebapi.request.MktRepo3Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo3Response;

public interface MktRepo3Service {
	
	ApiResponse<MktRepo3Response> getMktRepo3(MktRepo3Request request);

}
