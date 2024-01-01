package com.aristowebapi.controller;

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
import com.aristowebapi.response.DataUploadMessageResponse;
import com.aristowebapi.response.LoginResponse;
import com.aristowebapi.service.LoginService;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class LoginController {
	
	Logger logger = LoggerFactory.getLogger(LoginController.class);

	
	@Autowired
	private LoginService loginService;

	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest request)
	{
		logger.info(AristoWebLogMsgConstant.LOGIN_CONTROLLER,"authenticateUser");

		return new ResponseEntity<LoginResponse>(loginService.authenticateUser(request), HttpStatus.OK);
	
	}

	@GetMapping("/loginmessage/{depoCode}")
	public ResponseEntity<DataUploadMessageResponse> getDataUploadMessage(@PathVariable("depoCode") int depoCode)
	{

		logger.info(AristoWebLogMsgConstant.LOGIN_CONTROLLER,"getMessage");

		return new ResponseEntity<DataUploadMessageResponse>(loginService.getMessage(depoCode), HttpStatus.OK);
	
	}


}
