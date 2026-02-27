package com.aristowebapi.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "audit_inner_sheet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditInnerSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "audit_report_id", nullable = false)
    private Long auditReportId;

    @Column(name = "audit_inner_sheet_id", nullable = false)
    private Long auditInnerSheetId;

    @Column(name = "audit_report_status")
    private String auditReportStatus;

    @Column(name = "json_data", columnDefinition = "JSON", nullable = false)
    private String jsonData;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // getters & setters
}