package com.aristowebapi.service;

import com.aristowebapi.request.BranchMktRepo5Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchMktRepo5Response;

public interface BranchMktService {  
	ApiResponse<BranchMktRepo5Response> getBranchMktRepo5(BranchMktRepo5Request request);
}
