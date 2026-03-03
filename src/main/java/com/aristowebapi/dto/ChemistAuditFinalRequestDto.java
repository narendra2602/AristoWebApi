package com.aristowebapi.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChemistAuditFinalRequestDto {

    @JsonProperty("audit_report_title")
    private String auditReportTitle;

    @JsonProperty("audit_report_id")
    private Long auditReportId;

    @JsonProperty("abm_draft_id")
    private Long abmDraftId;

    @JsonProperty("audit_inner_sheet_id")
    private Long auditInnerSheetId;

    @JsonProperty("psr_code")
    private Long psrCode;

    
    @JsonProperty("audit_report_status")
    private String auditReportStatus;

    private List<ChemistSheetDto> sheets;
}