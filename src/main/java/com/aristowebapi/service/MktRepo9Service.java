package com.aristowebapi.service;

import com.aristowebapi.request.MktRepo9Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo9Response;

public interface MktRepo9Service { 

	ApiResponse<MktRepo9Response> getMktRepo9(MktRepo9Request request);
}
