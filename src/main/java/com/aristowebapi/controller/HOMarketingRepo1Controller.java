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
import com.aristowebapi.request.HOMarketingRepo1Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.HOMarketingRepo1Response;
import com.aristowebapi.service.HOMarketingRepo1Service;

@RestController
@CrossOrigin
@RequestMapping("${mrc_base_path}")
public class HOMarketingRepo1Controller {
	
	
	
	Logger logger = LoggerFactory.getLogger(BranchMisRepo5Controller.class);

	@Autowired
	private HOMarketingRepo1Service hOMarketingRepo1Service;
	
	@GetMapping("${mrc_homarketingrepo1_path}")
	public ResponseEntity<ApiResponse<HOMarketingRepo1Response>> getHoMarketingRepo1(@RequestBody HOMarketingRepo1Request request)
	{
		logger.info(AristoWebLogMsgConstant.HO_MARKEING_REPO1_CONTROLLER,"getHoMarketingRepo1");

		return new ResponseEntity<ApiResponse<HOMarketingRepo1Response>>(hOMarketingRepo1Service.getHoMarketingRepo1(request), HttpStatus.OK);
	
	}



}