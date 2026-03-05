package com.aristowebapi.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChemistAuditMetaResponse {

    private String auditReportTitle;
    private Long auditReportId;
    private Long abmDraftId;
    private Long psrCode;
    private List<Long> auditInnerSheetIds;
    private String auditReportStatus;
    private Integer divCode;
    private Integer depoCode;
    private Integer hq;
    private Integer monthCode;
    private Integer myear;
    private Integer month;
    private Integer createBy;

    // getters & setters
}