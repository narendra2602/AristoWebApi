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
import com.aristowebapi.request.MktRepo8Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo8Response;
import com.aristowebapi.service.MktRepo8Service;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class MktRepo8Controller {
	
	Logger logger = LoggerFactory.getLogger(MktRepo8Controller.class);
	
	@Autowired
	private MktRepo8Service mktRepo8Service;
	
	
	@GetMapping("/mktrepo8")
	public ResponseEntity<ApiResponse<MktRepo8Response>> getMktRepo8(@RequestBody MktRepo8Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_08,"getMktRepo8", request.getMyear(),request.getDivCode());

		return new ResponseEntity<ApiResponse<MktRepo8Response>>(mktRepo8Service.getMktRepo8(request), HttpStatus.OK);
	
	}



}
