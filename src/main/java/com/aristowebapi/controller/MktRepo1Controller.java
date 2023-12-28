package com.aristowebapi.controller;

import java.util.List;

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
import com.aristowebapi.request.GrossSaleRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.GrossSaleResponse;
import com.aristowebapi.service.MktRepo1Service;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class MktRepo1Controller {
	
	Logger logger = LoggerFactory.getLogger(MktRepo1Controller.class);
	
	@Autowired
	private MktRepo1Service grossSaleService;
	
	
	@GetMapping("/grosssale")
	public ResponseEntity<ApiResponse<GrossSaleResponse>> getMktRepo1(@RequestBody GrossSaleRequest request)
	{
		logger.info(AristoWebLogMsgConstant.MKT_REPORT_CONTROLLER_01,"getMktRepo1", request.getMyear(),request.getDivCode());

		return new ResponseEntity<ApiResponse<GrossSaleResponse>>(grossSaleService.getMktRepo1(request), HttpStatus.OK);
	
	}

}
