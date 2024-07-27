package com.aristowebapi.controller;
import javax.servlet.http.HttpServletRequest;

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
import com.aristowebapi.request.PendingRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.PendingResponse;
import com.aristowebapi.service.PendingService;
import com.aristowebapi.utility.AppRequestParameterUtils;

@RestController
@CrossOrigin
@RequestMapping("${mrc_base_path}")
public class PendingController {

	Logger logger = LoggerFactory.getLogger(PendingController.class);

	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;

	@Autowired
	private PendingService pendingService;
	
	
	@GetMapping("${mrc_pending_path}")
	public ResponseEntity<ApiResponse<PendingResponse>> getPendingReport(@RequestBody PendingRequest request,HttpServletRequest req)
	{
		
		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		request.setUtype(requestValues[1]);
		logger.info(AristoWebLogMsgConstant.PENDING_REPORT_CONTROLLER,"getPendingReport", request.getDivCode(),request.getLoginId());
		
		return new ResponseEntity<ApiResponse<PendingResponse>>(pendingService.getPendingReport(request), HttpStatus.OK);
	
	}

	private int[] getRequestData(HttpServletRequest req)
	{
		String authHeader = req.getHeader("Authorization");
		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
		return requestValues;
	}

	
}
