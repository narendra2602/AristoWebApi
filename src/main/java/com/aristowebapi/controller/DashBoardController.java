package com.aristowebapi.controller;

import java.util.List;

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
import com.aristowebapi.response.DashBoardChartResponse;
import com.aristowebapi.response.DashBoardDataResponse;
import com.aristowebapi.service.DashBoardService;

@RestController
@CrossOrigin
@RequestMapping("/api/mis")
public class DashBoardController {

	Logger logger = LoggerFactory.getLogger(DashBoardController.class);
	
	@Autowired
	private DashBoardService dashBoardService;
	
	@GetMapping("${mrc_dashboardchart_path}")
	public ResponseEntity<DashBoardChartResponse> getDashboardMainChart(@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode)
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardMainChart");

		return new ResponseEntity<DashBoardChartResponse>(dashBoardService.getDashboardMainChart(divCode,depoCode), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardcurrentchart_path}")
	public ResponseEntity<DashBoardChartResponse> getDashboardCurrentMonth(@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode)
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardMainChart");

		return new ResponseEntity<DashBoardChartResponse>(dashBoardService.getDashboardCurrentMonth(divCode,depoCode), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardtop5_path}")
	public ResponseEntity<List<DashBoardDataResponse>> getDashboardTop5(@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode)
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardTop5");

		return new ResponseEntity<List<DashBoardDataResponse>>(dashBoardService.getDashboardTop5(divCode,depoCode), HttpStatus.OK);
	
	}
	@GetMapping("${mrc_dashboardthept_path}")
	public ResponseEntity<List<DashBoardDataResponse>> getDashboardThept(@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode)
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardThept");

		return new ResponseEntity<List<DashBoardDataResponse>>(dashBoardService.getDashboardThept(divCode,depoCode), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardpendingpi_path}")
	public ResponseEntity<List<DashBoardDataResponse>> getDashboardPendingPI(@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode)
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardPendingPI");

		return new ResponseEntity<List<DashBoardDataResponse>>(dashBoardService.getDashboardPendingPI(divCode,depoCode), HttpStatus.OK);
	
	}

	@GetMapping("${mrc_dashboardpaneldata_path}")
	public ResponseEntity<List<DashBoardDataResponse>> getDashboardPanelData(@PathVariable("divCode") int divCode,@PathVariable("depoCode") int depoCode)
	{

		logger.info(AristoWebLogMsgConstant.DASH_BOARD_CONTROLLER,"getDashboardPanelData");

		return new ResponseEntity<List<DashBoardDataResponse>>(dashBoardService.getDashboardPanelData(divCode,depoCode), HttpStatus.OK);
	
	}

}
