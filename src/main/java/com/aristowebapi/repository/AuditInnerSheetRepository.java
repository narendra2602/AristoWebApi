package com.aristowebapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.entity.AuditInnerSheet;

public interface AuditInnerSheetRepository
        extends JpaRepository<AuditInnerSheet, Long> {

    List<AuditInnerSheet> findByAuditReportId(Long auditReportId);
    
    List<AuditInnerSheet> findAllByAuditReportStatus(String auditReportStatus);

    
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
    
    
 /*   @Query("SELECT s FROM AuditInnerSheet s " +
            "JOIN ChemistAuditReport r ON s.auditReportId = r.auditReportId " +
            "WHERE s.auditReportStatus = :status " +
            "AND r.monthCode = :monthCode " +
            "AND r.divCode = :divCode")
     List<AuditInnerSheet> findFilteredFinalSheets(
             @Param("status") String status,
             @Param("monthCode") int monthCode,
             @Param("divCode") int divCode);
*/    
    /*@Query("SELECT s FROM AuditInnerSheet s " +
            "JOIN ChemistAuditReport r ON s.auditReportId = r.auditReportId " +
            "WHERE s.auditReportStatus = :status " +
            "AND r.monthCode = :monthCode " +
            "AND r.divCode = :divCode "+
            "AND r.auditReportId=:reportId")
     List<AuditInnerSheet> findFilteredFinalSheets(
             @Param("status") String status,
             @Param("monthCode") int monthCode,
             @Param("divCode") int divCode,
             @Param("reportId") long reportId);*/
    
    
    @Query(value =
    	       "SELECT ais.* " +
    	       "FROM abm_chemist_audit_report r " +
    	       "LEFT JOIN abm_chemist_audit_reportfinal f " +
    	       "ON f.report_id = r.audit_report_id " +
    	       "INNER JOIN audit_inner_sheet ais " +
    	       "ON ais.audit_report_id = r.audit_report_id " +
    	       "WHERE r.abm_draft_id IN ( " +
    	       "   SELECT draft_id " +
    	       "   FROM Abm_draft_report_entry " +
    	       "   WHERE draft_status = 'FINAL' " +
    	       ") " +
    	       "AND f.report_id IS NULL " +
    	       "AND r.div_code = :divCode " +
    	       "AND r.month_code = :monthCode",
    	       nativeQuery = true)
    	List<AuditInnerSheet> findFilteredFinalSheets(
    	        @Param("divCode") int divCode,
    	        @Param("monthCode") int monthCode);
    
    
    
}