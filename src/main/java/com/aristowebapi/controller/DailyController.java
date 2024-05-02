package com.aristowebapi.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.dto.DailyEntry;
import com.aristowebapi.request.DailyEntryListRequest;
import com.aristowebapi.request.DailyReportRequest;
import com.aristowebapi.request.DailyUpdationRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DailyEntryListResponse;
import com.aristowebapi.response.DailyReportResponse;
import com.aristowebapi.response.DailyStatusResponse;
import com.aristowebapi.service.DailyEntryService;
import com.aristowebapi.service.DailyReportService;
import com.aristowebapi.utility.AppRequestParameterUtils;

@RestController
@CrossOrigin
@RequestMapping("${mrc_base_path}")
public class DailyController {
	
	Logger logger = LoggerFactory.getLogger(DailyController.class);

	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;

	@Autowired
	private DailyEntryService dailyentryService;
	
	@Autowired
	private DailyReportService dailyReportService;
	
	
	@PostMapping("${mrc_daily_entry_path}")
	public ResponseEntity<DailyEntry> saveDailyEntry(@RequestBody DailyEntry dailyentry)
	{
		System.out.println(dailyentry.toString());
		
		return new ResponseEntity<DailyEntry>(dailyentryService.saveDailyEntry(dailyentry),HttpStatus.CREATED);
		
	}

	@GetMapping("${mrc_daily_entry_list_path}")
	public ResponseEntity<ApiResponse<DailyEntryListResponse>> getDailyEntryList(@RequestBody DailyEntryListRequest request,HttpServletRequest req)
	{
		
		
		return new ResponseEntity<ApiResponse<DailyEntryListResponse>>(dailyentryService.getDailyEntryList(request), HttpStatus.OK);
	
	}

	
	@GetMapping("${mrc_daily_path}")
	public ResponseEntity<ApiResponse<DailyReportResponse>> getDailyReport(@RequestBody DailyReportRequest request,HttpServletRequest req)
	{
		
		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
//		request.setUtype(requestValues[1]);
		logger.info(AristoWebLogMsgConstant.DAILY_REPORT_CONTROLLER,"getDailyRepo1", request.getDivCode(),request.getLoginId());
		
		return new ResponseEntity<ApiResponse<DailyReportResponse>>(dailyReportService.getDailyReport(request), HttpStatus.OK);
	
	}


	@GetMapping("${mrc_daily_path_status}")
	public ResponseEntity<ApiResponse<DailyStatusResponse>> getDailyEntryStatus(@RequestBody DailyUpdationRequest request,HttpServletRequest req)
	{
		
		int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		logger.info(AristoWebLogMsgConstant.DAILY_REPORT_CONTROLLER,"getDailyEntryStatus");
		
		return new ResponseEntity<ApiResponse<DailyStatusResponse>>(dailyReportService.getDailyEntryStatus(request), HttpStatus.OK);
	
	}
	private int[] getRequestData(HttpServletRequest req)
	{
		String authHeader = req.getHeader("Authorization");
		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
		return requestValues;
	}

}
