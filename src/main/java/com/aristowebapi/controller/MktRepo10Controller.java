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
import com.aristowebapi.request.ViewRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.MktRepo9Response;
import com.aristowebapi.service.MktRepo10Service;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class MktRepo10Controller {
	
	Logger logger = LoggerFactory.getLogger(MktRepo10Controller.class);
	
	@Autowired
	private MktRepo10Service mktRepo10Service;
	
	
	@GetMapping("/mktrepo10")
	public ResponseEntity<ApiResponse<MktRepo9Response>> getMktRepo10(@RequestBody ViewRequest request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_10,"getMktRepo10", request.getMyear(),request.getDivCode());

		return new ResponseEntity<ApiResponse<MktRepo9Response>>(mktRepo10Service.getMktRepo10(request), HttpStatus.OK);
	
	}




}
