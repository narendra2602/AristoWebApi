package com.aristowebapi.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.response.AbmReportingResponse;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DashBoardDataResponse;
import com.aristowebapi.response.PsrResponse;
import com.aristowebapi.service.AbmReportingService;
import com.aristowebapi.utility.AppRequestParameterUtils;

@RestController
@CrossOrigin 
@RequestMapping("${mrc_base_path}")
public class AbmReportingController {
	Logger logger = LoggerFactory.getLogger(AbmReportingController.class);
	
	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;

	
	@Autowired
	private AbmReportingService abmReportingService;
	
	@GetMapping("${mrc_abm_reporting_path}")
	public ResponseEntity<ApiResponse<AbmReportingResponse>> getAbmReporting(HttpServletRequest req)
	{
		
		System.out.println("contoller per aaya kya ");
		int requestValues[]=getRequestData(req);
		int LoginId=(requestValues[0]);
		logger.info(AristoWebLogMsgConstant.Abm_Reporting_CONTROLLER,"getAbmReporting", LoginId);
		
		return new ResponseEntity<ApiResponse<AbmReportingResponse>>(abmReportingService.getAbmReporting(LoginId), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardPsrCombo_path}")
	public ResponseEntity<ApiResponse<PsrResponse>> getPsrList(HttpServletRequest request)
	{

        int loginId=getLoginIdFromToken(request)[0];

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getPsrList");
        
		return new ResponseEntity<ApiResponse<PsrResponse>>(abmReportingService.getPsrList(loginId), HttpStatus.OK);

	
	}
	@GetMapping("${mrc_dashboardBrandCombo_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getAristoBrandList(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,HttpServletRequest request)
	{
		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getAristoBrandList");
        
		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(abmReportingService.getAristoBrandList(myear,divCode), HttpStatus.OK);

	
	}

	
	private int[] getRequestData(HttpServletRequest req)
	{
		String authHeader = req.getHeader("Authorization");
		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
		return requestValues;
	}
	
    private int[] getLoginIdFromToken(HttpServletRequest request)
    {
		String authHeader = request.getHeader("Authorization");
		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
        return requestValues;
    }

}
