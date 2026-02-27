package com.aristowebapi.response;

import java.util.List;

import com.aristowebapi.dto.SheetDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({
    "audit_report_title",
    "autid_report_id",
    "audit_inner_sheet_id",
    "audit_report_status",
    "sheets"
})
public class AuditSheetResponse {

    @JsonProperty("audit_report_title")
    private String auditReportTitle;

    @JsonProperty("audit_report_id")
    private Long auditReportId;

    @JsonProperty("audit_inner_sheet_id")
    private Long auditInnerSheetId;

    @JsonProperty("audit_report_status")
    private String auditReportStatus;

    private List<SheetDto> sheets;
}