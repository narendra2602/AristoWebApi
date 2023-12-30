package com.aristowebapi.service;

import com.aristowebapi.request.ViewRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo9Response;

public interface MktRepo10Service {
	
	ApiResponse<MktRepo9Response> getMktRepo10(ViewRequest request);

}
