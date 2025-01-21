package com.aristowebapi.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.request.SampleSm01Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.SampleSm01Response;
import com.aristowebapi.response.SampleSm02Response;
import com.aristowebapi.service.SampleSm01Service;
import com.aristowebapi.utility.AppRequestParameterUtils;

@RestController
@CrossOrigin
@RequestMapping("${mrc_base_path}")
public class SampleReportController {
	Logger logger = LoggerFactory.getLogger(SampleReportController.class);

	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;
	
	
	@Autowired
	private SampleSm01Service sampleSm01Service;
	
	
	@PostMapping("${mrc_samplesm01_path}")
	public ResponseEntity<ApiResponse<SampleSm01Response>> getSampleSm01(@RequestBody SampleSm01Request request,HttpServletRequest req)
	{

		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		request.setUtype(requestValues[1]);

		logger.info(AristoWebLogMsgConstant.SAMPLE_REPORT_CONTROLLER_01,"getSampleSm01", request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getUtype(),request.getLoginId());
		
		return new ResponseEntity<ApiResponse<SampleSm01Response>>(sampleSm01Service.getSampleSm01(request), HttpStatus.OK);
		
	
	}
	@PostMapping("${mrc_samplesm02_path}")
	public ResponseEntity<ApiResponse<SampleSm02Response>> getSampleSm02(@RequestBody SampleSm01Request request,HttpServletRequest req)
	{

		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		request.setUtype(requestValues[1]);

		logger.info(AristoWebLogMsgConstant.SAMPLE_REPORT_CONTROLLER_01,"getSampleSm02", request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getUtype(),request.getLoginId());
		
		return new ResponseEntity<ApiResponse<SampleSm02Response>>(sampleSm01Service.getSampleSm02(request), HttpStatus.OK);
		
	
	}

	
	private int[] getRequestData(HttpServletRequest req)
	{
		String authHeader = req.getHeader("Authorization");
		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
		return requestValues;
	}

}
