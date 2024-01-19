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
import com.aristowebapi.request.BranchMisRepo5Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchMisRepo5Response;
import com.aristowebapi.service.BranchMisRepo5Service;

@RestController
@CrossOrigin
@RequestMapping("${mrc_base_path}")
public class BranchMisRepo5Controller {
	
	
	Logger logger = LoggerFactory.getLogger(BranchMisRepo5Controller.class);

	@Autowired
	private BranchMisRepo5Service branchMisRepo5Service;
	
	@GetMapping("${mrc_branch_mis_repo5_path}")
	public ResponseEntity<ApiResponse<BranchMisRepo5Response>> getBranchMisRepo5(@RequestBody BranchMisRepo5Request request)
	{
		logger.info(AristoWebLogMsgConstant.BRANCH_MIS_REPORT_5_CONTROLLER,"getBranchMisRepo5");

		return new ResponseEntity<ApiResponse<BranchMisRepo5Response>>(branchMisRepo5Service.getBranchMisRepo5(request), HttpStatus.OK);
	
	}


}
