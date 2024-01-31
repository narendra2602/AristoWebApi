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
import com.aristowebapi.request.StkRepo3Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.StkRepo3Response;
import com.aristowebapi.service.StkRepo3Service;

@RestController
@CrossOrigin
@RequestMapping("${mrc_base_path}")
public class StkRepo3Controller {
	
	Logger logger = LoggerFactory.getLogger(StkRepo3Controller.class);

	@Autowired
	private StkRepo3Service stkRepo3Service;
	
	@GetMapping("${mrc_stkRep3_path}")
	public ResponseEntity<ApiResponse<StkRepo3Response>> getStkRepo3(@RequestBody StkRepo3Request request)
	{
		logger.info(AristoWebLogMsgConstant.STK_REPO3_CONTROLLER,"getStkRepo3");

		return new ResponseEntity<ApiResponse<StkRepo3Response>>(stkRepo3Service.getStkRepo3(request), HttpStatus.OK);
	
	}




}
