package com.aristowebapi.service;

import com.aristowebapi.request.MktRepo5Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo5Response;

public interface MktRepo5Service { 
	ApiResponse<MktRepo5Response> getMktRepo5(MktRepo5Request request);

}
