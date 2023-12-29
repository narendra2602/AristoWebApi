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
import com.aristowebapi.request.MktRepo6Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo6Response;
import com.aristowebapi.service.MktRepo6Service;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class MktRepo6Controller {
	
	Logger logger = LoggerFactory.getLogger(MktRepo1Controller.class);
	
	@Autowired
	private MktRepo6Service mktRepo6Service;
	
	
	@GetMapping("/mktrepo6")
	public ResponseEntity<ApiResponse<MktRepo6Response>> getMktRepo6(@RequestBody MktRepo6Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_06,"getMktRepo6", request.getMyear(),request.getDivCode());

		return new ResponseEntity<ApiResponse<MktRepo6Response>>(mktRepo6Service.getMktRepo6(request), HttpStatus.OK);
	
	}


}
