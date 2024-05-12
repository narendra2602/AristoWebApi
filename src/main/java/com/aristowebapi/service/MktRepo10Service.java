package com.aristowebapi.service;

import com.aristowebapi.request.MktRepo10Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo9Response;

public interface MktRepo10Service {
	
	ApiResponse<MktRepo9Response> getMktRepo10(MktRepo10Request request);

}
