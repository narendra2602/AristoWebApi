package com.aristowebapi.service;

import com.aristowebapi.entity.ChemistAuditReport;
import com.aristowebapi.request.InitChemistAuditRequest;
import com.aristowebapi.response.AuditSheetResponse;
import com.aristowebapi.response.ChemistAuditMetaResponse;
import com.aristowebapi.response.ChemistAuditReportResponse;
import com.fasterxml.jackson.databind.JsonNode;

public interface ChemistAuditReportService {

    /**
     * Initialize new Chemist Audit Report
     */
    ChemistAuditReport initReport(InitChemistAuditRequest request);

    /**
     * Get Audit Report Metadata
     */
    ChemistAuditMetaResponse getReportMeta(Long auditReportId,int loginId);
    
    void saveAuditJson(Long auditReportId,
            AuditSheetResponse request);

    AuditSheetResponse getAuditJson(Long auditReportId,Long auditInnerSheetId);
    
    ChemistAuditReportResponse  saveFinalAudit(JsonNode jsonNode, int loginId);
}