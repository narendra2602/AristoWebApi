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
import com.aristowebapi.request.MktRepo11Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo11Response;
import com.aristowebapi.service.MktRepo11Service;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class MktRepo11Controller {
	
	Logger logger = LoggerFactory.getLogger(MktRepo10Controller.class);
	
	@Autowired
	private MktRepo11Service mktRepo11Service;
	
	
	@GetMapping("/mktrepo11")
	public ResponseEntity<ApiResponse<MktRepo11Response>> getMktRepo11(@RequestBody MktRepo11Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_11,"getMktRepo11", request.getMyear(),request.getDivCode());

		return new ResponseEntity<ApiResponse<MktRepo11Response>>(mktRepo11Service.getMktRepo11(request), HttpStatus.OK);
	
	}



}
