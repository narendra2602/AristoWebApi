package com.aristowebapi.service;

import com.aristowebapi.request.SampleSm01Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.SampleSm01Response;
import com.aristowebapi.response.SampleSm02Response;
import com.aristowebapi.response.SampleSm03Response;

public interface SampleSm01Service {

	
	ApiResponse<SampleSm01Response> getSampleSm01(SampleSm01Request request);
	ApiResponse<SampleSm02Response> getSampleSm02(SampleSm01Request request);
	ApiResponse<SampleSm03Response> getSampleSm03(SampleSm01Request request);
}
