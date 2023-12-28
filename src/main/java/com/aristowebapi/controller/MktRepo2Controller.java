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
import com.aristowebapi.request.MktRepo2Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo2Response;
import com.aristowebapi.service.MktRepo2Service;
import com.aristowebapi.serviceimpl.MktRepo4ServiceImpl;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class MktRepo2Controller {

	Logger logger = LoggerFactory.getLogger(MktRepo2Controller.class);
	@Autowired
	private MktRepo2Service mktRepo2Service;
	@GetMapping("/mktrepo2")
	public ResponseEntity<ApiResponse<MktRepo2Response>> getMktRepo2(@RequestBody MktRepo2Request request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_02,"getMktRepo2", request.getMyear(),request.getDivCode());
		return new ResponseEntity<ApiResponse<MktRepo2Response>>(mktRepo2Service.getMktRepo2(request), HttpStatus.OK);
	
	}

}
