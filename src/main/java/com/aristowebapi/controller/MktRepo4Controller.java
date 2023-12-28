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

import com.aristowebapi.request.MktRepo4Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo4Response;
import com.aristowebapi.service.MktRepo4Service;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class MktRepo4Controller {
	
	Logger logger = LoggerFactory.getLogger(MktRepo4Controller.class);
	@Autowired
	private MktRepo4Service mktRepo4Service;
	@GetMapping("/mktrepo4")
	public ResponseEntity<ApiResponse<MktRepo4Response>> getMktRepo4(@RequestBody MktRepo4Request request)
	{
	    logger.info("MktRep4 Controller div_code {}", request.getDivCode() );
		return new ResponseEntity<ApiResponse<MktRepo4Response>>(mktRepo4Service.getMktRepo4(request), HttpStatus.OK);
	
	}

}
