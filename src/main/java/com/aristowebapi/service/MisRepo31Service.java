package com.aristowebapi.service;

import com.aristowebapi.request.MisRepo31Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MisRepo31Response;

public interface MisRepo31Service {
	ApiResponse<MisRepo31Response> getMisRepo31(MisRepo31Request request);

}
