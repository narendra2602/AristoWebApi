package com.aristowebapi.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.request.LoginRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchResponse;
import com.aristowebapi.response.DataUploadMessageResponse;
import com.aristowebapi.response.DivResponse;
import com.aristowebapi.response.LoginResponse;
import com.aristowebapi.response.ReportTabResponse;
import com.aristowebapi.service.LoginService;
import com.aristowebapi.utility.AppRequestParameterUtils;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class LoginController {
	
	Logger logger = LoggerFactory.getLogger(LoginController.class);

	
	@Autowired
	private LoginService loginService;

	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest request)
	{
		logger.info(AristoWebLogMsgConstant.LOGIN_CONTROLLER,"authenticateUser");

		return new ResponseEntity<LoginResponse>(loginService.authenticateUser(request), HttpStatus.OK);
	
	}

	@PostMapping("/logindiv")
	public ResponseEntity<ApiResponse<DivResponse>> getDivisionList(HttpServletRequest request)
	{
		logger.info(AristoWebLogMsgConstant.LOGIN_CONTROLLER,"authenticateUser");

		String authHeader = request.getHeader("Authorization");
		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
        int loginId=requestValues[0]; 

         System.out.println("login id is "+loginId);

		
//		return new ResponseEntity<LoginResponse>(loginService.authenticateUser(request), HttpStatus.OK);
		//loginService.getDivisionList(163);
		return new ResponseEntity<ApiResponse<DivResponse>>(loginService.getDivisionList(163), HttpStatus.OK);

	
	}


	@PostMapping("/logindepo")
	public ResponseEntity<ApiResponse<BranchResponse>> getBranchList(HttpServletRequest request)
	{
		logger.info(AristoWebLogMsgConstant.LOGIN_CONTROLLER,"authenticateUser");
		return new ResponseEntity<ApiResponse<BranchResponse>>(loginService.getBranchList(163), HttpStatus.OK);

	
	}

	
	@PostMapping("/loginmenu")
	public ResponseEntity<ApiResponse<ReportTabResponse>> getMenuList(HttpServletRequest request)
	{
		logger.info(AristoWebLogMsgConstant.LOGIN_CONTROLLER,"authenticateUser");

		String authHeader = request.getHeader("Authorization");
        String fname=appRequestParameterUtils.getRequestBodyFirstName(authHeader);
		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
        int loginId=requestValues[0]; 

         System.out.println("login id is "+loginId);

        ApiResponse<ReportTabResponse> apiResponse = new ApiResponse<>(fname,0,"",loginService.getReportMenuList(163));


		return new ResponseEntity<ApiResponse<ReportTabResponse>>(apiResponse, HttpStatus.OK);

	
	}

	
	@GetMapping("/loginmessage/{depoCode}")
	public ResponseEntity<DataUploadMessageResponse> getDataUploadMessage(@PathVariable("depoCode") int depoCode)
	{

		logger.info(AristoWebLogMsgConstant.LOGIN_CONTROLLER,"getMessage");

		return new ResponseEntity<DataUploadMessageResponse>(loginService.getMessage(depoCode), HttpStatus.OK);
	
	}


}
