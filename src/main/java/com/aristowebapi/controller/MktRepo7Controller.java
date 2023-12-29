package com.aristowebapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.request.MktRepo7Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo7Response;
import com.aristowebapi.service.MktRepo7Service;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class MktRepo7Controller {
Logger logger = LoggerFactory.getLogger(MktRepo1Controller.class);
	
	@Autowired
	private MktRepo7Service mktRepo7Service;
	
	
	@GetMapping("/mktrepo7")
	public ResponseEntity<ApiResponse<MktRepo7Response>> getMktRepo7(@RequestBody MktRepo7Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_07,"getMktRepo7", request.getMyear(),request.getDivCode());

		return new ResponseEntity<ApiResponse<MktRepo7Response>>(mktRepo7Service.getMktRepo7(request), HttpStatus.OK);
	
	}



}
