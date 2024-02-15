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
import com.aristowebapi.request.DailyUpdationRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DailyReportResponse;
import com.aristowebapi.response.DailyStatusResponse;
import com.aristowebapi.service.DailyReportService;
import com.aristowebapi.utility.AppRequestParameterUtils;

@RestController
@CrossOrigin
@RequestMapping("${mrc_base_path}")
public class DailyReportController {
	
	Logger logger = LoggerFactory.getLogger(DailyReportController.class);

	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;


	
	@Autowired
	private DailyReportService dailyReportService;
	
	@GetMapping("${mrc_daily_path}")
	public ResponseEntity<ApiResponse<DailyReportResponse>> getDailyReport(@RequestBody DailyReportRequest request,HttpServletRequest req)
	{
		logger.info(AristoWebLogMsgConstant.DAILY_REPORT_CONTROLLER,"getDailyRepo1");
		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
//		request.setUtype(requestValues[1]);

		
		return new ResponseEntity<ApiResponse<DailyReportResponse>>(dailyReportService.getDailyReport(request), HttpStatus.OK);
	
	}


	@GetMapping("${mrc_daily_path_status}")
	public ResponseEntity<ApiResponse<DailyStatusResponse>> getDailyEntryStatus(@RequestBody DailyUpdationRequest request,HttpServletRequest req)
	{
		logger.info(AristoWebLogMsgConstant.DAILY_REPORT_CONTROLLER,"getDailyEntryStatus");
		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);

		return new ResponseEntity<ApiResponse<DailyStatusResponse>>(dailyReportService.getDailyEntryStatus(request), HttpStatus.OK);
	
	}
	private int[] getRequestData(HttpServletRequest req)
	{
		String authHeader = req.getHeader("Authorization");
		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
		return requestValues;
	}

}
