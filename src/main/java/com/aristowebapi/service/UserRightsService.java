package com.aristowebapi.service;

import java.util.List;

import com.aristowebapi.request.UserReportRightsRequest;
import com.aristowebapi.request.UserRightsRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.UserReportRightsTabResponse;
import com.aristowebapi.response.UserRightsResponse;

public interface UserRightsService {
	

	ApiResponse<UserRightsResponse> getUserDivisionList(int loginId);
	int saveUserDivisionList(List<UserRightsRequest> divisionList);
	
	ApiResponse<UserRightsResponse> getUserDepoList(int loginId);
	int saveUserDepoList(List<UserRightsRequest> depoList);
	
	ApiResponse<UserReportRightsTabResponse> getUserReportList(int loginId);
	int saveUserReportList(List<UserReportRightsRequest> reportList);
}
