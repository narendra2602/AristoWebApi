package com.aristowebapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aristowebapi.entity.MonthlyDevelopmentReportEntity;

@Repository
public interface MonthlyDevelopmentReportRepository extends JpaRepository<MonthlyDevelopmentReportEntity, Long> {

    // ✅ Finds a report by the common reportId
    Optional<MonthlyDevelopmentReportEntity> findByDraftId(Long draftId);
    Long findByReportMonthAndReportYearAndCreatedBy(int month,int year,int loginId);
    boolean existsByDraftId(Long draftId);
    
    void deleteByReportId(Long reportId);
    
    List<MonthlyDevelopmentReportEntity> 
    findAllByDivCodeAndDepoCodeAndReportMonthAndReportYearAndHqCode(int divCode,int  depoCode,int  mnthCode,int myear, int hqCode);
    
    // missing employee code in monthly reporting table 
/*    SELECT distinct line1_name,line1_empcode,line1_empname
    FROM fieldreporting fr
    WHERE fr.div_code = 30
      AND fr.depo_code = 61
      AND  NOT EXISTS (
            SELECT 1
            FROM abm_draft_report_entry amdr
            WHERE amdr.div_code = fr.div_code
              AND amdr.depo_code = fr.depo_code
              AND amdr.mnth_code = 202510
              AND amdr.myear = 2025
              AND amdr.emp_code = fr.line1_empcode
      );

*/
}
