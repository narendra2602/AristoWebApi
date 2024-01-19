package com.aristowebapi.service;

import com.aristowebapi.request.BranchMisRepo5Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchMisRepo5Response;

public interface BranchMisRepo5Service {
	
	ApiResponse<BranchMisRepo5Response> getBranchMisRepo5(BranchMisRepo5Request request);

}
