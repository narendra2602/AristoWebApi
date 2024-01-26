package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.constant.AristoWebMessageConstant;
import com.aristowebapi.dao.LoginDao;
import com.aristowebapi.dto.BranchMasterDto;
import com.aristowebapi.dto.DivDto;
import com.aristowebapi.dto.LoginDto;
import com.aristowebapi.dto.ReportMenuDto;
import com.aristowebapi.request.LoginRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchResponse;
import com.aristowebapi.response.DataUploadMessageResponse;
import com.aristowebapi.response.DivResponse;
import com.aristowebapi.response.LoginResponse;
import com.aristowebapi.response.ReportMenuResponse;
import com.aristowebapi.response.ReportTabResponse;
import com.aristowebapi.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

	Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Autowired
	private LoginDao loginDao;
	
	public String lupdate="";
	
	@Autowired
	private AristoWebMessageConstant aristoWebMessageConstant;
	
	@Override
	public LoginResponse authenticateUser(LoginRequest request) {
		
		logger.info(AristoWebLogMsgConstant.LOGIN_SERVICE,"authenticateUser");
		
		LoginDto ldto=loginDao.authenticateUser(request.getUsername(), request.getPassword(), "Yes");
		
		List<DivDto> divlist=loginDao.getDivList(ldto.getId());
		List<BranchMasterDto> branchlist=loginDao.getBranchList(ldto.getId());
		List<ReportMenuDto> reportList = loginDao.getMenuList(ldto.getId());
		
		List<DivResponse> divResponseList=new ArrayList<>();
		List<BranchResponse> branchResponseList=new ArrayList<>();
		List<ReportMenuResponse> reportResponseList=new ArrayList<>();
		List<ReportTabResponse> reportTabResponseList=new ArrayList<>();

		
		LoginResponse response=new LoginResponse();
		response.setLoginId(ldto.getId());
		response.setFname(ldto.getF_name());
		response.setUtype(ldto.getOpt());
		response.setLastLoginDate(ldto.getLast_ldate());
		response.setLastLoginTime(ldto.getLast_ltime());
		

		
		divlist.forEach(data->{
			DivResponse divres=new DivResponse();
			divres.setDivCode(data.getDiv_code());
			divres.setDivName(data.getDiv_name());
			divResponseList.add(divres);
		});
		
		response.setDivisions(divResponseList);

		
		branchlist.forEach(data->{
			BranchResponse brnres=new BranchResponse();
			brnres.setDepoCode(data.getDepo_code());
			brnres.setDepoName(data.getDepo_name());
			branchResponseList.add(brnres);
		});
		
		response.setBranches(branchResponseList);

		int size=reportList.size();
		ReportMenuDto reportMenuDto=null;
		ReportTabResponse reportTabResponse=null;
		ReportMenuResponse reportMenuResponse=null;
		boolean first=true;
		int tabId=0;
		String tabName=null;
		for(int i=0;i<size;i++)
		{
			reportMenuDto = reportList.get(i);
			if(first)
			{
				tabId=reportMenuDto.getTab_id();
				tabName=reportMenuDto.getTab_name();
				first=false;
			}
			if(tabId!=reportMenuDto.getTab_id())
			{
				reportTabResponse = new ReportTabResponse();
				reportTabResponse.setTabName(tabName);
				reportTabResponse.setMenuList(reportResponseList);

				reportTabResponseList.add(reportTabResponse);
				tabId=reportMenuDto.getTab_id();
				tabName=reportMenuDto.getTab_name();
				reportResponseList=new ArrayList<>();

			}
			
			  reportMenuResponse = new ReportMenuResponse();
			  reportMenuResponse.setRepoId(reportMenuDto.getRepo_id());
			  reportMenuResponse.setRepoName(reportMenuDto.getRepo_name());
			  reportResponseList.add(reportMenuResponse);
			  
			
		}
		reportTabResponse = new ReportTabResponse();
		reportTabResponse.setTabName(tabName);
		reportTabResponse.setMenuList(reportResponseList);
		reportTabResponseList.add(reportTabResponse);

		response.setTabResponse(reportTabResponseList);
		
		
		return response ;
	}

	@Override
	public DataUploadMessageResponse getMessage(int depoCode) {
		
		logger.info(AristoWebLogMsgConstant.LOGIN_SERVICE,"getMessage");
		
		String message=aristoWebMessageConstant.message+" "+loginDao.getMessage(depoCode);
		DataUploadMessageResponse response = new DataUploadMessageResponse();
		response.setMessage(message);
		return response;
		
	}

	@Override
	public ApiResponse<DivResponse> getDivisionList(int loginId) {
		
		List<DivDto> divlist=loginDao.getDivList(loginId);
		List<DivResponse> divResponseList=new ArrayList<>();
		
		int size=divlist.size();
		divlist.forEach(data->{
			DivResponse divres=new DivResponse();
			divres.setDivCode(data.getDiv_code());
			divres.setDivName(data.getDiv_name());
			divResponseList.add(divres);
		});
	
		ApiResponse<DivResponse> apiResponse = new ApiResponse<>("Division", size,lupdate,divResponseList);
		return apiResponse;

	}


	@Override
	public ApiResponse<BranchResponse> getBranchList(int loginId) {
		
		List<BranchMasterDto> branchlist=loginDao.getBranchList(loginId);
		List<BranchResponse> branchResponseList=new ArrayList<>();
		
		int size=branchlist.size();
		branchlist.forEach(data->{
			BranchResponse branchres=new BranchResponse();
			branchres.setDepoCode(data.getDepo_code());
			branchres.setDepoName(data.getDepo_name());
			branchResponseList.add(branchres);
		});
	
		ApiResponse<BranchResponse> apiResponse = new ApiResponse<>("Branch",size,lupdate, branchResponseList);
		return apiResponse;

	}

	@Override
	public List<ReportTabResponse> getReportMenuList(int loginId) {
		
		List<ReportMenuResponse> reportResponseList=new ArrayList<>();
		List<ReportTabResponse> reportTabResponseList=new ArrayList<>();
		
		List<ReportMenuDto> reportList = loginDao.getMenuList(loginId);
		
		int size=reportList.size();
		ReportMenuDto reportMenuDto=null;
		ReportTabResponse reportTabResponse=null;
		ReportMenuResponse reportMenuResponse=null;
		boolean first=true;
		int tabId=0;
		String tabName=null;
		for(int i=0;i<size;i++)
		{
			reportMenuDto = reportList.get(i);
			if(first)
			{
				tabId=reportMenuDto.getTab_id();
				tabName=reportMenuDto.getTab_name();
				first=false;
			}
			if(tabId!=reportMenuDto.getTab_id())
			{
				reportTabResponse = new ReportTabResponse();
				reportTabResponse.setTabName(tabName);
				reportTabResponse.setMenuList(reportResponseList);

				reportTabResponseList.add(reportTabResponse);
				tabId=reportMenuDto.getTab_id();
				tabName=reportMenuDto.getTab_name();
				reportResponseList=new ArrayList<>();

			}
			
			  reportMenuResponse = new ReportMenuResponse();
			  reportMenuResponse.setRepoId(reportMenuDto.getRepo_id());
			  reportMenuResponse.setRepoName(reportMenuDto.getRepo_name());
			  reportResponseList.add(reportMenuResponse);
			  
			
		}
		reportTabResponse = new ReportTabResponse();
		reportTabResponse.setTabName(tabName);
		reportTabResponse.setMenuList(reportResponseList);
		reportTabResponseList.add(reportTabResponse);

//		ApiResponse<ReportTabResponse> apiResponse = new ApiResponse<>(fname, reportTabResponseList);
		return reportTabResponseList;

	}

}
