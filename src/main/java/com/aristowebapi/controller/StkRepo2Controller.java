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
import com.aristowebapi.request.StkRepo2Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.StkRepo2Response;
import com.aristowebapi.service.StkRepo2Service;

@RestController
@CrossOrigin
@RequestMapping("${mrc_base_path}")
public class StkRepo2Controller {
	

	Logger logger = LoggerFactory.getLogger(StkRepo2Controller.class);

	@Autowired
	private StkRepo2Service stkRepo2Service;
	
	@GetMapping("${mrc_stk_rep2_path}")
	public ResponseEntity<ApiResponse<StkRepo2Response>> getStkRepo2(@RequestBody StkRepo2Request request)
	{
		logger.info(AristoWebLogMsgConstant.STK_REPO2_CONTROLLER,"getStkRepo2");

		return new ResponseEntity<ApiResponse<StkRepo2Response>>(stkRepo2Service.getStkRepo2(request), HttpStatus.OK);
	
	}


	@GetMapping("${mrc_stk_rep2_uv_path}")
	public ResponseEntity<ApiResponse<StkRepo2Response>> getStkRepo2UV(@RequestBody StkRepo2Request request)
	{
		logger.info(AristoWebLogMsgConstant.STK_REPO2_CONTROLLER,"getStkRepo2Value");

		return new ResponseEntity<ApiResponse<StkRepo2Response>>(stkRepo2Service.getStkRepo2UV(request), HttpStatus.OK);
	
	}

}
