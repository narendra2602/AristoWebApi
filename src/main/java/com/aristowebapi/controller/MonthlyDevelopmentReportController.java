package com.aristowebapi.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.entity.MonthlyDevelopmentReportEntity;
import com.aristowebapi.request.AbmReportingDraftRequest;
import com.aristowebapi.request.MonthlyReportRequest;
import com.aristowebapi.response.FullReportResponse;
import com.aristowebapi.service.AbmDraftReportService;
import com.aristowebapi.serviceimpl.MonthlyDevelopmentReportServiceImpl;
import com.aristowebapi.utility.AppRequestParameterUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${mrc_base_path}")
@RequiredArgsConstructor
public class MonthlyDevelopmentReportController {

	Logger logger = LoggerFactory.getLogger(MonthlyDevelopmentReportController.class);
    private final MonthlyDevelopmentReportServiceImpl reportService;
    private final AbmDraftReportService draftReportService;

    
    private final AppRequestParameterUtils appRequestParameterUtils;
    
    @PostMapping("${mrc_abmreport_savepath}")
    public MonthlyDevelopmentReportEntity saveReport(
            @RequestBody MonthlyReportRequest request,HttpServletRequest req
    ) {
    	logger.info("Json Request "+request.getReport());
    	logger.info("Json Mrs Request "+request.getMrs());
    	logger.info("Json Doctor Request "+request.getDoctors());
    	
    	int requestValues[]=getRequestData(req);
		request.getReport().setCreatedBy(requestValues[0]);
        return reportService.saveFullReport(
                request.getReport(),
                request.getMrs(),
                request.getDoctors(),
                request.getSelfAssessment(),
                request.getHeader()
               
        );
    }

    
    
    @PostMapping("${mrc_abmdraftreport_savepath}")
    public FullReportResponse saveReport(@RequestBody AbmReportingDraftRequest request,HttpServletRequest req
    ) {
    	
    	int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
        return draftReportService.saveAbmDraftReport(request);
    }

    @PostMapping("${mrc_abmdraftreport_updatepath}")
    public ResponseEntity<JsonNode>  updateDraftReport(@RequestBody String  jsonRequest ) throws Exception {
    	
    	String returnJson = draftReportService.updateAbmDraftReport(jsonRequest);
        return ResponseEntity.ok(new ObjectMapper().readTree(returnJson));
    }

    
    @GetMapping("${mrc_abmfinalreport_savepath}")
    public ResponseEntity<JsonNode> saveFinalNewReport(@PathVariable Long draftId) throws Exception{
    	
    	String returnJson = reportService.saveFinalDraftReport(draftId);
        return ResponseEntity.ok(new ObjectMapper().readTree(returnJson));
    }

    
    @GetMapping("${mrc_abmreport_path}")
    public FullReportResponse getFullReport(@PathVariable Long reportId) {
        return reportService.getFullReport(reportId);
    }
    
    
    private int[] getRequestData(HttpServletRequest req)
	{
		String authHeader = req.getHeader("Authorization");
		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
		return requestValues;
	}

}
