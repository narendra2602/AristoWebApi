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
import com.aristowebapi.request.NearExpiryRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.NearExpiryResponse;
import com.aristowebapi.service.NearExpiryService;

@RestController
@CrossOrigin
@RequestMapping("${mrc_base_path}")
public class NearExpiryController {
	
	Logger logger = LoggerFactory.getLogger(NearExpiryController.class);

	@Autowired
	private NearExpiryService nearExpiryService;

	@GetMapping("${mrc_nearexpiry_path}")
	public ResponseEntity<ApiResponse<NearExpiryResponse>> getExpiryReport(@RequestBody NearExpiryRequest request)
	{
		logger.info(AristoWebLogMsgConstant.DAILY_REPORT_CONTROLLER,"getDailyRepo1");

		return new ResponseEntity<ApiResponse<NearExpiryResponse>>(nearExpiryService.getExpiryReport(request), HttpStatus.OK);
	
	}

	
}
