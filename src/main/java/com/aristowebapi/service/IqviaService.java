package com.aristowebapi.service;

import com.aristowebapi.request.IqviaRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.IqviaResponse;

public interface IqviaService {

	
	ApiResponse<IqviaResponse> getIqvia(IqviaRequest request);
	
	
}
