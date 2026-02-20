package com.aristowebapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "abm_chemistaudit_sheet_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class AuditSheetData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "audit_report_id")
    private Long auditReportId;

    @Column(name = "audit_inner_sheet_id")
    private Long auditInnerSheetId;

    @Lob
    @Column(name = "sheet_json", columnDefinition = "LONGTEXT")
    private String sheetJson;

    // getters & setters
}