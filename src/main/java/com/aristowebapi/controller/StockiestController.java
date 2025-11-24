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
import com.aristowebapi.request.StkRepo10Request;
import com.aristowebapi.request.StkRepo2Request;
import com.aristowebapi.request.StkRepo3Request;
import com.aristowebapi.request.StkRepo9Request;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.StkRepo10Response;
import com.aristowebapi.response.StkRepo2Response;
import com.aristowebapi.response.StkRepo3Response;
import com.aristowebapi.response.StkRepo5Response;
import com.aristowebapi.response.StkRepo5ResponseNew;
import com.aristowebapi.response.StkRepo9Response;
import com.aristowebapi.service.StkRepo10Service;
import com.aristowebapi.service.StkRepo2Service;
import com.aristowebapi.service.StkRepo3Service;
import com.aristowebapi.service.StkRepo9Service;
import com.aristowebapi.serviceimpl.StkRepo5ServiceImpl;
import com.aristowebapi.utility.AppRequestParameterUtils;

@RestController
@CrossOrigin
@RequestMapping("${mrc_base_path}")
public class StockiestController {

	
	Logger logger = LoggerFactory.getLogger(StockiestController.class);

	
	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;

	
	@Autowired
	private StkRepo2Service stkRepo2Service;
	


	@Autowired
	private StkRepo3Service stkRepo3Service;
	
	
	@Autowired
	private StkRepo5ServiceImpl stkRepo5ServiceImpl;

	
	@Autowired
	private StkRepo9Service stkRepo9Service;

	
	@Autowired
	private StkRepo10Service stkRepo10Service;

	
	@GetMapping("${mrc_stk_rep2_path}")
	public ResponseEntity<ApiResponse<StkRepo2Response>> getStkRepo2(@RequestBody StkRepo2Request request,HttpServletRequest req)
	{
		

		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		request.setUtype(requestValues[1]);

		logger.info(AristoWebLogMsgConstant.STK_REPO2_CONTROLLER,"getStkRepo2", request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getUtype(),request.getLoginId());
		return new ResponseEntity<ApiResponse<StkRepo2Response>>(stkRepo2Service.getStkRepo2(request), HttpStatus.OK);
	
	}


	@GetMapping("${mrc_stk_rep2_uv_path}")
	public ResponseEntity<ApiResponse<StkRepo2Response>> getStkRepo2UV(@RequestBody StkRepo2Request request,HttpServletRequest req)
	{
		
		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		request.setUtype(requestValues[1]);

		logger.info(AristoWebLogMsgConstant.STK_REPO2_CONTROLLER,"getStkRepo2Value", request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getUtype(),request.getLoginId());

		return new ResponseEntity<ApiResponse<StkRepo2Response>>(stkRepo2Service.getStkRepo2UV(request), HttpStatus.OK);
	
	}
	
	@GetMapping("${mrc_stk_rep3_path}")
	public ResponseEntity<ApiResponse<StkRepo3Response>> getStkRepo3(@RequestBody StkRepo3Request request,HttpServletRequest req)
	{
		
		
		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		request.setUtype(requestValues[1]);

		logger.info(AristoWebLogMsgConstant.STK_REPO3_CONTROLLER,"getStkRepo3", request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getUtype(),request.getLoginId());

		return new ResponseEntity<ApiResponse<StkRepo3Response>>(stkRepo3Service.getStkRepo3(request), HttpStatus.OK);
	
	}


	@GetMapping("${mrc_stk_rep5_path}")
	public ResponseEntity<ApiResponse<StkRepo5Response>> getStkRepo5(@RequestBody StkRepo9Request request ,HttpServletRequest req)
	{
		
		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		request.setUtype(requestValues[1]);

		logger.info(AristoWebLogMsgConstant.STK_REPO5_CONTROLLER,"getStkRepo5",request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getUtype(),request.getLoginId());

		return new ResponseEntity<ApiResponse<StkRepo5Response>>(stkRepo9Service.getStkRepo5(request), HttpStatus.OK);

	
	}

	// this is new jdbc templet service
/*	@GetMapping("${mrc_stk_rep5_path}")
	public ResponseEntity<ApiResponse<StkRepo5ResponseNew>> getStkRepo5(@RequestBody StkRepo9Request request ,HttpServletRequest req)
	{
		
		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		request.setUtype(requestValues[1]);

		logger.info(AristoWebLogMsgConstant.STK_REPO5_CONTROLLER,"getStkRepo5",request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getUtype(),request.getLoginId());
//		return new ResponseEntity<ApiResponse<StkRepo5ResponseNew>>(stkRepo5ServiceImpl.getStkRepo5(request), HttpStatus.OK);
	
	}
*/
	
	@GetMapping("${mrc_stk_rep9_path}")
	public ResponseEntity<ApiResponse<StkRepo9Response>> getStkRepo9(@RequestBody StkRepo9Request request ,HttpServletRequest req)
	{
		
		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		request.setUtype(requestValues[1]);

		logger.info(AristoWebLogMsgConstant.STK_REPO9_CONTROLLER,"getStkRepo9",request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getUtype(),request.getLoginId());

		return new ResponseEntity<ApiResponse<StkRepo9Response>>(stkRepo9Service.getStkRepo9(request), HttpStatus.OK);
	
	}


	
	@GetMapping("${mrc_stk_rep10_path}")
	public ResponseEntity<ApiResponse<StkRepo10Response>> getStkRepo10(@RequestBody StkRepo10Request request ,HttpServletRequest req)
	{
		
		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		request.setUtype(requestValues[1]);
		logger.info(AristoWebLogMsgConstant.STK_REPO9_CONTROLLER,"getStkRepo10", request.getMyear(),request.getDivCode(),request.getDepoCode(),request.getUtype(),request.getLoginId());


		return new ResponseEntity<ApiResponse<StkRepo10Response>>(stkRepo10Service.getStkRepo10(request), HttpStatus.OK);
	
	}


private int[] getRequestData(HttpServletRequest req)
{
	String authHeader = req.getHeader("Authorization");
	int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
	return requestValues;
}

}