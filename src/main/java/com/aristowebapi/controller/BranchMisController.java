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
import com.aristowebapi.request.BranchMisRepo5Request;
import com.aristowebapi.request.BranchMisRepo6Request;
import com.aristowebapi.request.BranchMisRepo8Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchMisRepo5Response;
import com.aristowebapi.response.BranchMisRepo6Response;
import com.aristowebapi.response.BranchMisRepo8Response;
import com.aristowebapi.service.BranchMisservice;
import com.aristowebapi.utility.AppRequestParameterUtils;

@RestController
@CrossOrigin 
@RequestMapping("${mrc_base_path}")
public class BranchMisController {
	
	
	Logger logger = LoggerFactory.getLogger(BranchMisController.class);

	
	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;

	
	@Autowired
	private BranchMisservice branchMisService;
	
	@GetMapping("${mrc_branch_mis_repo5_path}")
	public ResponseEntity<ApiResponse<BranchMisRepo5Response>> getBranchMisRepo5(@RequestBody BranchMisRepo5Request request,HttpServletRequest req)
	{
		
		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		request.setUtype(requestValues[1]);
		logger.info(AristoWebLogMsgConstant.BRANCH_MIS_REPORT_5_CONTROLLER,"getBranchMisRepo5", request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getUtype(),request.getLoginId());
		
		return new ResponseEntity<ApiResponse<BranchMisRepo5Response>>(branchMisService.getBranchMisRepo5(request), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_branch_mis_repo6_path}")
	public ResponseEntity<ApiResponse<BranchMisRepo6Response>> getBranchMisRepo6(@RequestBody BranchMisRepo6Request request,HttpServletRequest req)
	{
		
		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		request.setUtype(requestValues[1]);
		logger.info(AristoWebLogMsgConstant.BRANCH_MIS_REPORT_6_CONTROLLER,"getBranchMisRepo6", request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getUtype(),request.getLoginId());
		
		return new ResponseEntity<ApiResponse<BranchMisRepo6Response>>(branchMisService.getBranchMisRepo6(request), HttpStatus.OK);
	
	}
	
	@GetMapping("${mrc_branch_mis_repo8_path}")
	public ResponseEntity<ApiResponse<BranchMisRepo8Response>> getBranchMisRepo8(@RequestBody BranchMisRepo8Request request,HttpServletRequest req)
	{
		

		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		request.setUtype(requestValues[1]);
		logger.info(AristoWebLogMsgConstant.BRANCH_MIS_REPORT_8_CONTROLLER,"getBranchMisRepo8", request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getUtype(),request.getLoginId());
		
		return new ResponseEntity<ApiResponse<BranchMisRepo8Response>>(branchMisService.getBranchMisRepo8(request), HttpStatus.OK);
	
	}
	private int[] getRequestData(HttpServletRequest req)
	{
		String authHeader = req.getHeader("Authorization");
		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
		return requestValues;
	}

}
