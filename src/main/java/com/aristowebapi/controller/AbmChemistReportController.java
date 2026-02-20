package com.aristowebapi.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.entity.ChemistAuditReport;
import com.aristowebapi.request.InitChemistAuditRequest;
import com.aristowebapi.response.AuditSheetResponse;
import com.aristowebapi.response.ChemistAuditMetaResponse;
import com.aristowebapi.service.ChemistAuditReportService;
import com.aristowebapi.utility.AppRequestParameterUtils;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@CrossOrigin 
@RequestMapping("${mrc_base_path}")
public class AbmChemistReportController {
	
	
	@Autowired
    private ChemistAuditReportService service;
	

	@Autowired
	private AppRequestParameterUtils appRequestParameterUtils;
	
	
    @PostMapping("${mrc_abmchemistcreate}")
    public ResponseEntity<?> initChemistAudit(
            @RequestBody InitChemistAuditRequest request,HttpServletRequest req) {

	 	int requestValues[]=getRequestData(req);
	 	int loginId=requestValues[0];
	 	request.setLoginId(loginId);
        ChemistAuditReport report = service.initReport(request);

        Map<String, Object> response = new HashMap<>();
        response.put("audit_report_id", report.getAuditReportId());
        response.put("audit_report_status", report.getAuditReportStatus());
        response.put("message", "Chemist audit report created successfully");

        return ResponseEntity.ok(response);
    }

   
    @GetMapping("${mrc_chemistauditreportmeta_path}")
    public ResponseEntity<ChemistAuditMetaResponse> getAuditMeta(
            @PathVariable Long id,HttpServletRequest req) {
	 	int requestValues[]=getRequestData(req);
	 	int loginId=requestValues[0];

        return ResponseEntity.ok(service.getReportMeta(id,loginId));
    }
	
    
   
    @PostMapping("${mrc_saveauditsheet}")
    public ResponseEntity<?> saveAuditSheet(
            @PathVariable Long auditReportId,
            @RequestBody AuditSheetResponse request) {

        service.saveAuditJson(auditReportId, request);

        return ResponseEntity.ok("Audit JSON Saved Successfully");
    }
    
   
    @GetMapping("${mrc_chemistgetauditsheet_path}")
    public ResponseEntity<?> getAuditSheet(
            @PathVariable Long auditReportId,@PathVariable Long  auditInnerSheetId) {

        return ResponseEntity.ok(
                service.getAuditJson(auditReportId, auditInnerSheetId));
    }
    
    
	 @PostMapping("${mrc_abmchemistreport_savepath}")
	 public ResponseEntity<?> save(@RequestBody JsonNode jsonNode,HttpServletRequest req) {
		 	int requestValues[]=getRequestData(req);
		 	int loginId=requestValues[0];
	        return ResponseEntity.ok(service.saveFinalAudit(jsonNode,loginId));
	 }
	 
  /*  @GetMapping("/getauditsheet/{reportId}/{sheetId}")
    public ResponseEntity<AuditSheetResponse> getAuditSheet(
            @PathVariable Long reportId,
            @PathVariable Long sheetId) {

        return ResponseEntity.ok(
                chemistAuditReportService.getAuditSheet(reportId, sheetId));
    }
 */   
	
/*	private final ChemistAuditService service;
	 @GetMapping("${mrc_abmchemistreport_path}")
	 public ResponseEntity<JsonNode> getById(@PathVariable Long id) {
	        return ResponseEntity.ok(service.getAuditById(id));
	    }
	
*/
	 
	 private int[] getRequestData(HttpServletRequest req)
		{
			String authHeader = req.getHeader("Authorization");
			int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
			
			return requestValues;
		}

}
