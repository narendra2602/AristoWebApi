package com.aristowebapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aristowebapi.dao.AbmReportingDao;
import com.aristowebapi.dto.AbmDraftReportingDto;
import com.aristowebapi.entity.MonthlyDevelopmentReportEntity;
import com.aristowebapi.exception.DataNotFoundException;
import com.aristowebapi.request.AbmReportingDraftRequest;
import com.aristowebapi.request.MonthlyReportRequest;
import com.aristowebapi.response.FullReportResponse;
import com.aristowebapi.service.AbmDraftReportService;
import com.aristowebapi.service.MonthlyDevelopmentReportService;
import com.aristowebapi.utility.AppRequestParameterUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${mrc_base_path}")
@RequiredArgsConstructor
public class MonthlyDevelopmentReportController {

	Logger logger = LoggerFactory.getLogger(MonthlyDevelopmentReportController.class);
    private final MonthlyDevelopmentReportService reportService;
    private final AbmDraftReportService draftReportService;
    private final ObjectMapper objectMapper;
    private final AbmReportingDao abmReportingDao;
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

    
    /// 1  /abm/report/savedraft
    @PostMapping("${mrc_abmdraftreport_savepath}")
    public FullReportResponse saveReport(@RequestBody AbmReportingDraftRequest request,HttpServletRequest req
    ) {
    	
    	int requestValues[]=getRequestData(req);
		request.setLoginId(requestValues[0]);
		FullReportResponse response = draftReportService.saveAbmDraftReport(request);

	    if (response == null) {
	        throw new DataNotFoundException("Duplicate Entry");
	    }

	    return response;
    }

    
    //// 2 /abm/report/updatedraft
    @PostMapping("${mrc_abmdraftreport_updatepath}")
    public ResponseEntity<JsonNode>  updateDraftReport(@RequestBody String  jsonRequest ) throws Exception {
    	
    	String returnJson = draftReportService.updateAbmDraftReport(jsonRequest);
        return ResponseEntity.ok(objectMapper.readTree(returnJson));
    }

    
    //// 3 /abm/report/savefinaldraft/{draftId}
    @PostMapping("${mrc_abmfinalreport_savepath}")
    public ResponseEntity<JsonNode> saveFinalNewReport(@PathVariable Long draftId) throws Exception {

        String returnJson = reportService.saveFinalDraftReport(draftId);
        JsonNode response = objectMapper.readTree(returnJson);

        return ResponseEntity.ok(response);
    }

    /// 4 /abm/report/deletefinaldraft/{reportId}
    @DeleteMapping("${mrc_abmfinalreport_deletepath}")
    public ResponseEntity<Map<String, Object>> deleteFinalReport( @PathVariable Long reportId) {

        String response = reportService.deleteReportByReportId(reportId);

        Map<String, Object> responseJson = new HashMap<>();
        responseJson.put("status", "SUCCESS");
        responseJson.put("message", response);
        responseJson.put("reportId", reportId);
        
        return ResponseEntity.ok(responseJson);
    }
    
/*    /// 5 /abm/report/{reportId}
    @GetMapping("${mrc_abmreport_path}")
    public FullReportResponse getFullReport(@PathVariable Long reportId) {
        return reportService.getFullReport(reportId);
    }

*/
    
    @GetMapping("${mrc_abmdraftreport_path}")
    public List<AbmDraftReportingDto> getDraftList(
            @PathVariable("mnth") int mnth,
            @PathVariable("myear") int myear, HttpServletRequest req) {

    	int requestValues[]=getRequestData(req);
		int loginId=requestValues[0];
		int mnthCode = abmReportingDao.getMonthCode(myear,mnth);
		
        return draftReportService.getByMonthAndYearAndLoginId(mnthCode, myear,loginId);
    }
    
    @GetMapping("${mrc_abmdraftlist_path}")
    public List<AbmDraftReportingDto> getDraftList(
    	 @PathVariable("divCode") int divCode,
    	 @PathVariable("depoCode") int depoCode,
         @PathVariable("mnth") int mnth,
         @PathVariable("myear") int myear, HttpServletRequest req) {

    	int requestValues[]=getRequestData(req);
		int loginId=requestValues[0];
		int mnthCode = abmReportingDao.getMonthCode(myear,mnth);
		
        return draftReportService.getByDivCodeAndDepoCodeAndMnthCodeAndMyear(divCode,depoCode,mnthCode, myear);
    }

    

    @GetMapping(value = "${mrc_abmreport_path}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<JsonNode> getDraftJson(@PathVariable Long draftId) {

            try {
                String draftJson = draftReportService.getDraftJsonByDraftId(draftId);
                JsonNode jsonNode = objectMapper.readTree(draftJson);
                return ResponseEntity.ok(jsonNode);

            } catch (Exception e) {
                throw new RuntimeException(
                        "Invalid JSON stored for draftId " + draftId, e);
            }
        }    
    
    @GetMapping("${mrc_abmjsonreport_path}")
    public ResponseEntity<List<FullReportResponse>> getFullReport(
            @PathVariable Integer divCode,
            @PathVariable Integer depoCode,
            @PathVariable Integer mnthCode,
            @PathVariable Integer myear,
            @PathVariable Integer hqCode) {

        List<FullReportResponse> response =
                reportService.getAllReportJson(
                        divCode,
                        depoCode,
                        mnthCode,
                        myear,
                        hqCode
                );

        return ResponseEntity.ok(response);
    }

    
    
    
    private int[] getRequestData(HttpServletRequest req)
	{
		String authHeader = req.getHeader("Authorization");
		int requestValues[]=appRequestParameterUtils.getRequestBodyParameters(authHeader);
		
		return requestValues;
	}

}
