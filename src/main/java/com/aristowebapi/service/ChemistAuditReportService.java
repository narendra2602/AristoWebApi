package com.aristowebapi.service;

import com.aristowebapi.dto.ChemistAuditFinalRequestDto;
import com.aristowebapi.entity.ChemistAuditReport;
import com.aristowebapi.request.InitChemistAuditRequest;
import com.aristowebapi.response.AuditSheetResponse;
import com.aristowebapi.response.ChemistAuditMetaResponse;
import com.aristowebapi.response.ChemistAuditReportResponse;

public interface ChemistAuditReportService {

	
	String regenerateAllFinalReports(int monthCode, int divCode,long reportId) throws Exception;
	
    ChemistAuditReport initReport(InitChemistAuditRequest request);

    ChemistAuditMetaResponse getReportMeta(Long auditReportId, int loginId, Long psrCode);

    void saveAuditJson(Long auditReportId,
                       AuditSheetResponse request);

    AuditSheetResponse getAuditJson(Long auditReportId, Long auditInnerSheetId);

/*    // ✅ DTO Based
    ChemistAuditReportResponse saveFinalAudit(
            ChemistAuditFinalRequestDto request,
            int loginId);
*/    
    String saveFinalAudit(Long reportId, Long psrCode, int loginId) throws Exception;
    
    

    void deleteByReportId(Long reportId);
    
   
}