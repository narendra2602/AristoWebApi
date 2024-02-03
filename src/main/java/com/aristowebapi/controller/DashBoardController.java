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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.constant.AristoWebLogMsgConstant;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.BranchResponse;
import com.aristowebapi.response.DashBoardChartResponse;
import com.aristowebapi.response.DashBoardDataResponse;
import com.aristowebapi.response.DashBoardPanelDataResponse;
import com.aristowebapi.response.DivResponse;
import com.aristowebapi.service.DashBoardService;
import com.aristowebapi.utility.AppRequestParameterUtils;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class DashBoardController {

	Logger logger = LoggerFactory.getLogger(DashBoardController.class);
	
	@Autowired
	private DashBoardService dashBoardService;
	
	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;
	
	@GetMapping("${mrc_dashboardchart_path}")
	public ResponseEntity<DashBoardChartResponse> getDashboardMainChart(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,HttpServletRequest request)
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardMainChart");
		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		return new ResponseEntity<DashBoardChartResponse>(dashBoardService.getDashboardMainChart(myear,divCode,depoCode,loginId,uType), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardcurrentchart_path}")
	public ResponseEntity<DashBoardChartResponse> getDashboardCurrentMonth(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,@PathVariable("loginid") int loginid,@PathVariable("utype") int utype)
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardMainChart");

		return new ResponseEntity<DashBoardChartResponse>(dashBoardService.getDashboardCurrentMonth(myear,divCode,depoCode,cmon,loginid,utype), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardtop5_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getDashboardTop5(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,HttpServletRequest request)

	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardTop5");
		
		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getDashboardTop5(myear,divCode,depoCode,cmon,loginId,uType), HttpStatus.OK);
	
	}
	@GetMapping("${mrc_dashboardthept_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getDashboardThept(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,HttpServletRequest request)
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardThept");

		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getDashboardThept(myear,divCode,depoCode,cmon,loginId,uType), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardpendingpi_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getDashboardPendingPI(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,HttpServletRequest request)
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardPendingPI");

		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getDashboardPendingPI(myear,divCode,depoCode,cmon,loginId,uType), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardpaneldata_path}")
	public ResponseEntity<ApiResponse<DashBoardPanelDataResponse>> getDashboardPanelData(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,HttpServletRequest request)
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardPanelData");
		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		
		return new ResponseEntity<ApiResponse<DashBoardPanelDataResponse>>(dashBoardService.getDashboardPanelData(myear,divCode,depoCode,cmon,loginId,uType), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardgroupwiseach_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getDashboardGroupwiseAch(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,HttpServletRequest request)
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardGroupwiseAch");

		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getDashboardGroupwiseAch(myear,divCode,depoCode,cmon,loginId,uType), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardbranchwiseach_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getDashboardBranchAch(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,HttpServletRequest request)
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardBranchAch");

		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		
		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getDashboardBranchAch(myear,divCode,depoCode,cmon,loginId,uType), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardHqwiseach_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getDashboardHqwiseAch(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,HttpServletRequest request)
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardHqwiseAch");

		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getDashboardHqwiseAch(myear,divCode,depoCode,cmon,loginId,uType), HttpStatus.OK);
	
	}
	
	@GetMapping("${mrc_dashboardMonthCombo_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getDashboardMonthCombo(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,HttpServletRequest request)
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardMonthCombo");

        int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		
		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getDashboardMonthCombo(myear,divCode,depoCode,loginId,uType), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardYearCombo_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getDashboardYearCombo()
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardYearCombo");

		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getDashboardYearCombo(), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardDivCombo_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getDivisionList(HttpServletRequest request)
	{
		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"authenticateUser");

        int loginId=getLoginIdFromToken(request)[0];

		
//		return new ResponseEntity<LoginResponse>(loginService.authenticateUser(request), HttpStatus.OK);
		//loginService.getDivisionList(163);
		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getDivisionList(loginId), HttpStatus.OK);

	
	}

	@GetMapping("${mrc_dashboardDepoCombo_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getBranchList(HttpServletRequest request)
	{
		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"authenticateUser");

        int loginId=getLoginIdFromToken(request)[0];

        
		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getBranchList(loginId), HttpStatus.OK);

	
	}

    private int[] getLoginIdFromToken(HttpServletRequest request)
    {
		String authHeader = request.getHeader("Authorization");
		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
        return requestValues;
    }

	
}
