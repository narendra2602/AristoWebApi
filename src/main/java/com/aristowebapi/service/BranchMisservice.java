package com.aristowebapi.service;

import com.aristowebapi.request.BranchMisRepo5Request;
import com.aristowebapi.request.BranchMisRepo6Request;
import com.aristowebapi.request.BranchMisRepo8Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchMisRepo5Response;
import com.aristowebapi.response.BranchMisRepo6Response;
import com.aristowebapi.response.BranchMisRepo8AchResponse;
import com.aristowebapi.response.BranchMisRepo8Response;

public interface BranchMisservice {
	
	ApiResponse<BranchMisRepo5Response> getBranchMisRepo5(BranchMisRepo5Request request);
	ApiResponse<BranchMisRepo6Response> getBranchMisRepo6(BranchMisRepo6Request request);
	ApiResponse<BranchMisRepo8Response> getBranchMisRepo8(BranchMisRepo8Request request);
	ApiResponse<BranchMisRepo8AchResponse> getBranchMisRepo8Ach(BranchMisRepo8Request request);

}
