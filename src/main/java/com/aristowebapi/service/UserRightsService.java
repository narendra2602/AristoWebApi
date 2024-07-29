package com.aristowebapi.service;

import java.util.List;

import com.aristowebapi.request.UserReportRightsRequest;
import com.aristowebapi.request.UserRightsPmtRequest;
import com.aristowebapi.request.UserRightsRequest;
import com.aristowebapi.request.UserRightsTerRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DashBoardDataResponse;
import com.aristowebapi.response.UserReportRightsTabResponse;
import com.aristowebapi.response.UserRightsHqResponse;
import com.aristowebapi.response.UserRightsPmtResponse;
import com.aristowebapi.response.UserRightsResponse;

public interface UserRightsService {
	

	ApiResponse<UserRightsResponse> getUserDivisionList(int loginId);
	int saveUserDivisionList(List<UserRightsRequest> divisionList);
	
	ApiResponse<UserRightsResponse> getUserDepoList(int loginId);
	int saveUserDepoList(List<UserRightsRequest> depoList);
	
	ApiResponse<UserReportRightsTabResponse> getUserReportList(int loginId);
	int saveUserReportList(List<UserReportRightsRequest> reportList);
	
	ApiResponse<UserRightsPmtResponse> getUserPmtGroupList(int loginId,int divCode);
	int saveUserPmtGroupList(List<UserRightsPmtRequest> pmtList);
	
	ApiResponse<UserRightsHqResponse> getUserTerList(int loginId,int myear,int divCode,int depoCode);
	int saveUserTerList(List<UserRightsTerRequest> terList);
	
	ApiResponse<DashBoardDataResponse> getDivisionList(int loginId);

	ApiResponse<DashBoardDataResponse> getBranchList(int loginId);
}
