package com.aristowebapi.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "abm_chemist_audit_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChemistAuditReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditReportId;

    @Column(name = "abm_draft_id")
    private Long abmDraftId;
    
    private String auditReportTitle;

    private String auditReportStatus; // DRAFT, SUBMITTED, APPROVED

    private String entryDate;

    private Integer divCode;
    private Integer depoCode;
    private Integer hq;

    private Integer monthCode; // YYYYMM
    private Integer myear;
    private Integer month;
    @Column(name = "login_id")
    private int loginId;
    
    @Column(name = "emp_code")
    private int empCode;

    
    @Column(name = "draft_json", columnDefinition = "TEXT")
    private String draftJson;

    @ElementCollection
    @CollectionTable(name = "audit_inner_sheets",
            joinColumns = @JoinColumn(name = "audit_report_id"))
    @Column(name = "sheet_id")
    private List<Long> auditInnerSheetIds = new ArrayList<>();

    // getters & setters
}