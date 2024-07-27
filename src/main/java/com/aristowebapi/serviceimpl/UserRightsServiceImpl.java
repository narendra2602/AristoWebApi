package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.UserRightsBranchDao;
import com.aristowebapi.dao.UserRightsDao;
import com.aristowebapi.dao.UserRightsReportDao;
import com.aristowebapi.dto.UserDepo;
import com.aristowebapi.dto.UserDivision;
import com.aristowebapi.dto.UserReports;
import com.aristowebapi.dto.UserRights;
import com.aristowebapi.dto.UserRightsReport;
import com.aristowebapi.request.UserReportRightsRequest;
import com.aristowebapi.request.UserRightsRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.UserReportRightsMenuResponse;
import com.aristowebapi.response.UserReportRightsTabResponse;
import com.aristowebapi.response.UserRightsResponse;
import com.aristowebapi.service.UserRightsService;

@Service
public class UserRightsServiceImpl implements UserRightsService{

	Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Autowired
	private UserRightsDao userRightsDao;
	
	
	@Autowired
	private UserRightsBranchDao userRightsBranchDao;
	
	@Autowired
	private UserRightsReportDao userRightsReportDao;
	
	@Autowired
	private AristoWebMessageConstant aristoWebMessageConstant;
	
	@Override
	public ApiResponse<UserRightsResponse>  getUserDivisionList(int loginId) {
		
		List<UserRights> reportList=null;
		
		reportList = userRightsDao.getDivisionList(loginId);
		
		UserRightsResponse response=null;
		List<UserRightsResponse> rightsList = new ArrayList();
		int size=reportList.size();
		boolean first=true;
		for(int i=0;i<size;i++)
		{
			
			UserRights data = reportList.get(i);
			
			response=new UserRightsResponse();
			response.setVal(data.getVal());
			response.setName(data.getName());
			response.setUserStatus(data.getuser_status());
			response.setId(data.getId());
			rightsList.add(response);
		
		}
		ApiResponse<UserRightsResponse> apiResponse = new ApiResponse<>("Division List", size,rightsList);
		return apiResponse;


	}

	@Override
	public int saveUserDivisionList(List<UserRightsRequest> divisionList) {
		// TODO Auto-generated method stub
		List<UserDivision> userDivisionList = new ArrayList<>();
		divisionList.forEach(div -> {
				UserDivision userDiv = new UserDivision();
				userDiv.setUser_id(div.getUserId());
				userDiv.setDiv_code(div.getVal());
				userDiv.setUser_status(div.getUserStatus());
				userDiv.setId(div.getId());
				userDivisionList.add(userDiv);
		});
		  List<UserDivision> udiv = userRightsDao.saveAll(userDivisionList);
		  return udiv.size();
	}

	@Override
	public ApiResponse<UserRightsResponse> getUserDepoList(int loginId) {
		List<UserRights> reportList=null;
		
		reportList = userRightsBranchDao.getDepoList(loginId);
		
		UserRightsResponse response=null;
		List<UserRightsResponse> rightsList = new ArrayList();
		int size=reportList.size();
		for(int i=0;i<size;i++)
		{
			
			UserRights data = reportList.get(i);
			response=new UserRightsResponse();
			response.setVal(data.getVal());
			response.setName(data.getName());
			response.setUserStatus(data.getuser_status());
			response.setId(data.getId());
			rightsList.add(response);
		
		}
		ApiResponse<UserRightsResponse> apiResponse = new ApiResponse<>("Branch List", size,rightsList);
		return apiResponse;


	}

	@Override
	public int saveUserDepoList(List<UserRightsRequest> depoList) {
		// TODO Auto-generated method stub
		List<UserDepo> userDepoList = new ArrayList<>();
		depoList.forEach(depo -> {
				UserDepo userDepo = new UserDepo();
				userDepo.setUser_id(depo.getUserId());
				userDepo.setDepo_code(depo.getVal());
				userDepo.setStatus(depo.getUserStatus());
				userDepo.setId(depo.getId());
				userDepoList.add(userDepo);
		});
		  List<UserDepo> udepo = userRightsBranchDao.saveAll(userDepoList);
		  return udepo.size();

		 
	}

	@Override
	public ApiResponse<UserReportRightsTabResponse> getUserReportList(int loginId) {
		List<UserReportRightsMenuResponse> reportResponseList=new ArrayList<>();
		List<UserReportRightsTabResponse> reportTabResponseList=new ArrayList<>();
		
		List<UserRightsReport> reportList = userRightsReportDao.getReportList(loginId);
		
		int size=reportList.size();
		UserRightsReport userRightsReport=null;
		UserReportRightsTabResponse reportTabResponse=null;
		UserReportRightsMenuResponse reportMenuResponse=null;
		boolean first=true;
		int tabId=0;
		String tabName=null;

		for(int i=0;i<size;i++)
		{
			userRightsReport = reportList.get(i);
			if(first)
			{
				tabId=userRightsReport.getTab_id();
				tabName=userRightsReport.getTab_name();
				first=false;
			}
			if(tabId!=userRightsReport.getTab_id())
			{
				reportTabResponse = new UserReportRightsTabResponse();
				reportTabResponse.setTabId(tabId);
				reportTabResponse.setTabName(tabName);
				reportTabResponse.setUserId(loginId);
				reportTabResponse.setMenuList(reportResponseList);

				reportTabResponseList.add(reportTabResponse);
				tabId=userRightsReport.getTab_id();
				tabName=userRightsReport.getTab_name();
				reportResponseList=new ArrayList<>();

			}
			
			  reportMenuResponse = new UserReportRightsMenuResponse();
			  if(tabId == 12 || tabId == 13 || tabId == 14 )
				  reportMenuResponse.setRepoId(userRightsReport.getRepo_id().substring(1));
			  else
				  reportMenuResponse.setRepoId(userRightsReport.getRepo_id());
			  reportMenuResponse.setRepoName(userRightsReport.getRepo_name());
			  reportMenuResponse.setId(userRightsReport.getId());
			  reportMenuResponse.setUserStatus(userRightsReport.getuser_status());
			  reportResponseList.add(reportMenuResponse);
			  
			
		}
		reportTabResponse = new UserReportRightsTabResponse();
		reportTabResponse.setTabId(tabId);
		reportTabResponse.setTabName(tabName);
		reportTabResponse.setUserId(loginId);
		reportTabResponse.setMenuList(reportResponseList);
		reportTabResponseList.add(reportTabResponse);

		ApiResponse<UserReportRightsTabResponse> apiResponse = new ApiResponse<>("Report List", size,reportTabResponseList);
		return apiResponse;
	}

	@Override
	public int saveUserReportList(List<UserReportRightsRequest> reportList) {
		List<UserReports> userReportList = new ArrayList<>();
		reportList.forEach(report -> {
			   UserReports userReport = new UserReports();
			   userReport.setUser_id(report.getUserId());
			   userReport.setTab_id(report.getTabId());
			   userReport.setRepo_id(report.getRepoId());
			   userReport.setStatus(report.getUserStatus());
			   userReport.setId(report.getId());
			   userReportList.add(userReport);
		});
		  List<UserReports> ureport = userRightsReportDao.saveAll(userReportList);
		  return ureport.size();
	}

}
