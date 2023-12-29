package com.aristowebapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.request.LoginRequest;
import com.aristowebapi.response.LoginResponse;
import com.aristowebapi.service.LoginService;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class LoginController {
	
	@Autowired
	private LoginService loginService;

	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest request)
	{
//		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_01,"getMktRepo1", request.getMyear(),request.getDivCode());

		return new ResponseEntity<LoginResponse>(loginService.authenticateUser(request), HttpStatus.OK);
	
	}


}
