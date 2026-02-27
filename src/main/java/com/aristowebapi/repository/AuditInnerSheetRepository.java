package com.aristowebapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristowebapi.entity.AuditInnerSheet;

public interface AuditInnerSheetRepository
        extends JpaRepository<AuditInnerSheet, Long> {

    List<AuditInnerSheet> findByAuditReportId(Long auditReportId);
    Optional<AuditInnerSheet> findByAuditInnerSheetId(Long auditInnerSheetId);
    
    Optional<AuditInnerSheet> 
    findByAuditReportIdAndAuditInnerSheetId(
            Long auditReportId,
            Long auditInnerSheetId);
}