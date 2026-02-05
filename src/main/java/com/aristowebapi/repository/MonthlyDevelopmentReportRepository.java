package com.aristowebapi.repository;

import com.aristowebapi.entity.MonthlyDevelopmentReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonthlyDevelopmentReportRepository extends JpaRepository<MonthlyDevelopmentReportEntity, Long> {

    // ✅ Finds a report by the common reportId
    Optional<MonthlyDevelopmentReportEntity> findByDraftId(Long draftId);
    Long findByReportMonthAndReportYearAndCreatedBy(int month,int year,int loginId);
    boolean existsByDraftId(Long draftId);
    
    void deleteByReportId(Long reportId);

}
