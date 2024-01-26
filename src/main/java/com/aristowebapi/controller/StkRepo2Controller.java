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
import com.aristowebapi.response.StkRepo2UVResponse;
import com.aristowebapi.response.StkRepo2UnitResponse;
import com.aristowebapi.response.StkRepo2ValueResponse;
import com.aristowebapi.service.StkRepo2Service;

@RestController
@CrossOrigin
@RequestMapping("${mrc_base_path}")
public class StkRepo2Controller {
	

	Logger logger = LoggerFactory.getLogger(StkRepo2Controller.class);

	@Autowired
	private StkRepo2Service stkRepo2Service;
	
	@GetMapping("${mrc_stk_rep2_unit_path}")
	public ResponseEntity<ApiResponse<StkRepo2UnitResponse>> getStkRepo2Unit(@RequestBody StkRepo2Request request)
	{
		logger.info(AristoWebLogMsgConstant.STK_REPO2_CONTROLLER,"getStkRepo2Unit");

		return new ResponseEntity<ApiResponse<StkRepo2UnitResponse>>(stkRepo2Service.getStkRepo2Unit(request), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_stk_rep2_value_path}")
	public ResponseEntity<ApiResponse<StkRepo2ValueResponse>> getStkRepo2Value(@RequestBody StkRepo2Request request)
	{
		logger.info(AristoWebLogMsgConstant.STK_REPO2_CONTROLLER,"getStkRepo2Value");

		return new ResponseEntity<ApiResponse<StkRepo2ValueResponse>>(stkRepo2Service.getStkRepo2Value(request), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_stk_rep2_uv_path}")
	public ResponseEntity<ApiResponse<StkRepo2UVResponse>> getStkRepo2UV(@RequestBody StkRepo2Request request)
	{
		logger.info(AristoWebLogMsgConstant.STK_REPO2_CONTROLLER,"getStkRepo2Value");

		return new ResponseEntity<ApiResponse<StkRepo2UVResponse>>(stkRepo2Service.getStkRepo2UV(request), HttpStatus.OK);
	
	}

}
