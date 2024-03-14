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
import com.aristowebapi.request.BranchMktRepo5Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchMktRepo5Response;
import com.aristowebapi.service.BranchMktService;
import com.aristowebapi.utility.AppRequestParameterUtils;

@RestController
@CrossOrigin
@RequestMapping("${mrc_base_path}")
public class BranchMktController {
	
	
	Logger logger = LoggerFactory.getLogger(BranchMktController.class);

	
	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;

	
	@Autowired
	private BranchMktService branchMktService;

	@GetMapping("${mrc_branch_mkt_repo5_path}")
	
	public ResponseEntity<ApiResponse<BranchMktRepo5Response>> getBranchMktRepo5(@RequestBody BranchMktRepo5Request request,HttpServletRequest req)
	{
		

		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		request.setUtype(requestValues[1]);
		logger.info(AristoWebLogMsgConstant.BRANCH_MKT_REPORT_5_CONTROLLER,"getBranchMktRepo5", request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getUtype(),request.getLoginId());
		
		return new ResponseEntity<ApiResponse<BranchMktRepo5Response>>(branchMktService.getBranchMktRepo5(request), HttpStatus.OK);
	
	}

	
	
	
	private int[] getRequestData(HttpServletRequest req)
	{
		String authHeader = req.getHeader("Authorization");
		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
		return requestValues;
	}

}
