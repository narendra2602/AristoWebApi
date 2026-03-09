package com.aristowebapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.aristowebapi.entity.SelfAssessmentEntity;

@Repository
public interface SelfAssessmentRepository extends JpaRepository<SelfAssessmentEntity, Long> {

    // ✅ Find self-assessment by reportId
    Optional<SelfAssessmentEntity> findByReportId(Long reportId);
    
    @Modifying(clearAutomatically = true)
    void deleteByReportId(Long reportId);

}
