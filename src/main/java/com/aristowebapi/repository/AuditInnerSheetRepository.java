package com.aristowebapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristowebapi.entity.AuditInnerSheet;

public interface AuditInnerSheetRepository
        extends JpaRepository<AuditInnerSheet, Long> {

    List<AuditInnerSheet> findByAuditReportId(Long auditReportId);
    
   

    
    Optional<AuditInnerSheet> findByAuditReportIdAndAuditInnerSheetIdAndPsrCode(
            Long auditReportId,
            Long auditInnerSheetId,
            Long psrCode
    );
    
    Optional<AuditInnerSheet> 
    findByAuditReportIdAndAuditInnerSheetId(
            Long auditReportId,
            Long auditInnerSheetId);
    
    
//    List<Long> findByAuditReportIdAndPsrCode(Long auditReportId, Long psrCode);
    
    List<AuditInnerSheet> 
    findAllByAuditReportIdAndPsrCode(Long auditReportId, Long psrCode);
    
    
    @Query("SELECT a.auditInnerSheetId FROM AuditInnerSheet a " +
    	       "WHERE a.auditReportId = :auditReportId " +
    	       "AND a.psrCode = :psrCode")
    	List<Long> findInnerSheetIdsByAuditReportIdAndPsrCode(Long auditReportId, Long psrCode);
    
    
    List<AuditInnerSheet> 
    findAllByAuditReportIdAndPsrCodeAndAuditReportStatus(
            Long reportId, Long psrCode, String status);
    
}