package com.aristowebapi.service;

import java.util.List;

import com.aristowebapi.request.LoginRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchResponse;
import com.aristowebapi.response.DataUploadMessageResponse;
import com.aristowebapi.response.DivResponse;
import com.aristowebapi.response.LoginResponse;
import com.aristowebapi.response.ReportTabResponse;

public interface LoginService {
	 
	LoginResponse authenticateUser(LoginRequest request);
	
	DataUploadMessageResponse getMessage();
	
	
	List<ReportTabResponse> getReportMenuList(int loginId);


	
}
