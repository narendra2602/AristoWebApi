package com.aristowebapi.repository;

import com.aristowebapi.entity.SelfAssessmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SelfAssessmentRepository extends JpaRepository<SelfAssessmentEntity, Long> {

    // ✅ Find self-assessment by reportId
    Optional<SelfAssessmentEntity> findByReportId(Long reportId);

}
