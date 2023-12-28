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
import com.aristowebapi.request.MktRepo5Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo5Response;
import com.aristowebapi.service.MktRepo5Service;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class MktRepo5Controller {
	
	Logger logger = LoggerFactory.getLogger(MktRepo2Controller.class);
	
	@Autowired
	private MktRepo5Service mktRepo5Service;
	
	@GetMapping("/mktrepo5")
	public ResponseEntity<ApiResponse<MktRepo5Response>> getMktRepo5(@RequestBody MktRepo5Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_05,"getMktRepo5", request.getMyear(),request.getDivCode());
		return new ResponseEntity<ApiResponse<MktRepo5Response>>(mktRepo5Service.getMktRepo5(request), HttpStatus.OK);
	
	}


}
