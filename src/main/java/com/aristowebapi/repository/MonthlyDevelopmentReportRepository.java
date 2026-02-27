package com.aristowebapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aristowebapi.dto.MisRepo31;
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
    
 
}
