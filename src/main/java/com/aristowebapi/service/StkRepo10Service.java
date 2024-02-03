package com.aristowebapi.service;

import com.aristowebapi.request.StkRepo10Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.StkRepo10Response;

public interface StkRepo10Service {

	ApiResponse<StkRepo10Response> getStkRepo10(StkRepo10Request request);
}
