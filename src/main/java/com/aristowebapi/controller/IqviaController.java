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
import com.aristowebapi.request.DailyReportRequest;
import com.aristowebapi.request.IqviaRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DailyReportResponse;
import com.aristowebapi.response.IqviaResponse;
import com.aristowebapi.service.IqviaService;
import com.aristowebapi.utility.AppRequestParameterUtils;

@RestController
@CrossOrigin
@RequestMapping("${mrc_base_path}")
public class IqviaController {
	Logger logger = LoggerFactory.getLogger(IqviaController.class);
	
	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;

	@Autowired
	private IqviaService iqviaService;
	
	
	@GetMapping("${mrc_iqvia_path}")
	public ResponseEntity<ApiResponse<IqviaResponse>> getIqvia(@RequestBody IqviaRequest request,HttpServletRequest req)
	{
		
		logger.info(AristoWebLogMsgConstant.IQVIA_CONTROLLER,"getIqvia", request.getDivCode(),request.getDepoCode());
		
		return new ResponseEntity<ApiResponse<IqviaResponse>>(iqviaService.getIqvia(request), HttpStatus.OK);
	
	}

	
}
