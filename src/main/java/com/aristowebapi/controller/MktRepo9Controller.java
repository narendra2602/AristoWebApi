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
import com.aristowebapi.request.MktRepo9Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo9Response;
import com.aristowebapi.service.MktRepo9Service;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class MktRepo9Controller {
	
	Logger logger = LoggerFactory.getLogger(MktRepo9Controller.class);
	
	@Autowired
	private MktRepo9Service mktRepo9Service;
	
	
	@GetMapping("/mktrepo9")
	public ResponseEntity<ApiResponse<MktRepo9Response>> getMktRepo9(@RequestBody MktRepo9Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_09,"getMktRepo9", request.getMyear(),request.getDivCode());

		return new ResponseEntity<ApiResponse<MktRepo9Response>>(mktRepo9Service.getMktRepo9(request), HttpStatus.OK);
	
	}




}
