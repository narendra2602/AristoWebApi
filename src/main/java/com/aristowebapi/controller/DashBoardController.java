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
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DashBoardChartResponse;
import com.aristowebapi.response.DashBoardDataResponse;
import com.aristowebapi.response.DashBoardDataResponseDouble;
import com.aristowebapi.response.DashBoardPanelDataResponse;
import com.aristowebapi.response.StockiestResponse;
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

		
		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

        logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardMainChart", myear,divCode,depoCode,uType,loginId);
		return new ResponseEntity<DashBoardChartResponse>(dashBoardService.getDashboardMainChart(myear,divCode,depoCode,loginId,uType), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardcurrentchart_path}")
	public ResponseEntity<DashBoardChartResponse> getDashboardCurrentMonth(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,@PathVariable("loginid") int loginid,@PathVariable("utype") int utype,HttpServletRequest request)
	{
		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		
		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardCurrentMonth", myear,divCode,depoCode,uType,loginId);

		return new ResponseEntity<DashBoardChartResponse>(dashBoardService.getDashboardCurrentMonth(myear,divCode,depoCode,cmon,loginid,utype), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardtop5Stockiest_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponseDouble>> getDashboardTop5(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,HttpServletRequest request)

	{

		
		
		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

        logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardTop5", myear,divCode,depoCode,uType,loginId);
        
		return new ResponseEntity<ApiResponse<DashBoardDataResponseDouble>>(dashBoardService.getDashboardTop5(myear,divCode,depoCode,cmon,loginId,uType), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardtop5Products_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponseDouble>> getDashboardTop5Products(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,HttpServletRequest request)

	{

		
		
		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];
        logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardTop5Products", myear,divCode,depoCode,uType,loginId);
        
		return new ResponseEntity<ApiResponse<DashBoardDataResponseDouble>>(dashBoardService.getDashboardTop5Products(myear,divCode,depoCode,cmon,loginId), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardthept_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponseDouble>> getDashboardThept(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,HttpServletRequest request)
	{


		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardThept", myear,divCode,depoCode,uType,loginId);

		return new ResponseEntity<ApiResponse<DashBoardDataResponseDouble>>(dashBoardService.getDashboardThept(myear,divCode,depoCode,cmon,loginId,uType), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardpendingpi_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponseDouble>> getDashboardPendingPI(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,HttpServletRequest request)
	{


		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardPendingPI", myear,divCode,depoCode,uType,loginId);

		return new ResponseEntity<ApiResponse<DashBoardDataResponseDouble>>(dashBoardService.getDashboardPendingPI(myear,divCode,depoCode,cmon,loginId,uType), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardpaneldata_path}")
	public ResponseEntity<ApiResponse<DashBoardPanelDataResponse>> getDashboardPanelData(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,HttpServletRequest request)
	{

		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardPanelData", myear,divCode,depoCode,uType,loginId);
		
		return new ResponseEntity<ApiResponse<DashBoardPanelDataResponse>>(dashBoardService.getDashboardPanelData(myear,divCode,depoCode,cmon,loginId,uType), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardgroupwiseach_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponseDouble>> getDashboardGroupwiseAch(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,HttpServletRequest request)
	{


		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardGroupwiseAch", myear,divCode,depoCode,uType,loginId);

        
		return new ResponseEntity<ApiResponse<DashBoardDataResponseDouble>>(dashBoardService.getDashboardGroupwiseAch(myear,divCode,depoCode,cmon,loginId,uType), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardbranchwiseach_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponseDouble>> getDashboardBranchAch(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,HttpServletRequest request)
	{


		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardBranchAch", myear,divCode,depoCode,uType,loginId);
		
		return new ResponseEntity<ApiResponse<DashBoardDataResponseDouble>>(dashBoardService.getDashboardBranchAch(myear,divCode,depoCode,cmon,loginId,uType), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardHqwiseach_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponseDouble>> getDashboardHqwiseAch(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,@PathVariable("cmon") int cmon,HttpServletRequest request)
	{


		int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

        logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardHqwiseAch", myear,divCode,depoCode,uType,loginId);

        
		return new ResponseEntity<ApiResponse<DashBoardDataResponseDouble>>(dashBoardService.getDashboardHqwiseAch(myear,divCode,depoCode,cmon,loginId,uType), HttpStatus.OK);
	
	}
	
	@GetMapping("${mrc_dashboardMonthCombo_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getDashboardMonthCombo(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,HttpServletRequest request)
	{


        int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardMonthCombo", myear,divCode,depoCode,uType,loginId);
		
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

        int loginId=getLoginIdFromToken(request)[0];
		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDivisionList");

		
//		return new ResponseEntity<LoginResponse>(loginService.authenticateUser(request), HttpStatus.OK);
		//loginService.getDivisionList(163);
		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getDivisionList(loginId), HttpStatus.OK);

	
	}

	@GetMapping("${mrc_dashboardDepoCombo_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getBranchList(HttpServletRequest request)
	{

        int loginId=getLoginIdFromToken(request)[0];

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getBranchList");
        
		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getBranchList(loginId), HttpStatus.OK);

	
	}

	@GetMapping("${mrc_dashboardHqCombo_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getHqList(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,HttpServletRequest request)
	{

        int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getHqList", myear,divCode,depoCode,uType,loginId);
        
		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getHqList(myear,divCode,depoCode,uType,loginId), HttpStatus.OK);

	
	}

	@GetMapping("${mrc_dashboardRegionCombo_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getRegionList(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,HttpServletRequest request)
	{

        int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getRegionList", myear,divCode,depoCode,uType,loginId);
        
		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getRegionList(myear,divCode,depoCode,uType,loginId), HttpStatus.OK);

	
	}

	@GetMapping("${mrc_dashboardAreaCombo_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getAreaList(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,HttpServletRequest request)
	{

        int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getAreaList", myear,divCode,depoCode,uType,loginId);
        
		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getAreaList(myear,divCode,depoCode,uType,loginId), HttpStatus.OK);

	
	}

	
	@GetMapping("${mrc_dashboardProductCombo_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getProductList(@PathVariable("divCode") int divCode,@PathVariable("myear") int myear,HttpServletRequest request)
	{

        int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getProductList");
        System.out.println("product list parameteres "+divCode+","+uType+","+loginId+","+myear);
		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getProductList(divCode,uType,loginId,myear), HttpStatus.OK);

	}

	@GetMapping("${mrc_dashboardGroupCombo_path}")
	public ResponseEntity<ApiResponse<DashBoardDataResponse>> getGroupList(@PathVariable("divCode") int divCode,@PathVariable("myear") int myear,HttpServletRequest request)
	{

        int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getGroupList");

		return new ResponseEntity<ApiResponse<DashBoardDataResponse>>(dashBoardService.getGroupList(divCode,uType,loginId,myear), HttpStatus.OK);
	
	}
	
	@GetMapping("${mrc_dashboardStockiestCombo_path}")
	public ResponseEntity<ApiResponse<StockiestResponse>> getStockiestList(@PathVariable("myear") int myear,@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode,HttpServletRequest request)
	{

        int loginId=getLoginIdFromToken(request)[0];
        int uType=getLoginIdFromToken(request)[1];

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getStockiestList", myear,divCode,depoCode,uType,loginId);

		return new ResponseEntity<ApiResponse<StockiestResponse>>(dashBoardService.getStockiestList(myear,divCode,depoCode,uType,loginId), HttpStatus.OK);
	
	}

    private int[] getLoginIdFromToken(HttpServletRequest request)
    {
		String authHeader = request.getHeader("Authorization");
		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
        return requestValues;
    }

	
}
