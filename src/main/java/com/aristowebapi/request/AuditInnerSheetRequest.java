package com.aristowebapi.request;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuditInnerSheetRequest {

    @JsonProperty("audit_report_title")
    private String auditReportTitle;

    @JsonProperty("audit_report_id")
    private Long auditReportId;

    @JsonProperty("abm_draft_id")
    private Long abmDraftId;

    @JsonProperty("audit_inner_sheet_id")
    private Long auditInnerSheetId;

    @JsonProperty("audit_report_status")
    private String auditReportStatus;

    private List<Map<String, Object>> sheets;

    // getters & setters
}