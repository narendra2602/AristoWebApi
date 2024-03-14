package com.aristowebapi.service;

import com.aristowebapi.request.MktRepo7Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo7Response;

public interface MktRepo7Service { 
	ApiResponse<MktRepo7Response> getMktRepo7(MktRepo7Request request);

}
