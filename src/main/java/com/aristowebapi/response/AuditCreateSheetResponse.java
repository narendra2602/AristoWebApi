package com.aristowebapi.response;

import java.util.List;

import com.aristowebapi.dto.SheetDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditCreateSheetResponse {

    private String auditReportTitle;
    private Long autidReportId;
    private Long auditInnerSheetId;
    private String auditReportStatus;
    private List<SheetDto> sheets;
}