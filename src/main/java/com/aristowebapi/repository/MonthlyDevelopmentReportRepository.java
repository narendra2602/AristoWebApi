package com.aristowebapi.repository;

import com.aristowebapi.entity.MonthlyDevelopmentReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonthlyDevelopmentReportRepository extends JpaRepository<MonthlyDevelopmentReportEntity, Long> {

    // ✅ Finds a report by the common reportId
    Optional<MonthlyDevelopmentReportEntity> findByReportId(Long reportId);
    Long findByReportMonthAndReportYearAndCreatedBy(int month,int year,int loginId);

}
