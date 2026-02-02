package com.aristowebapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aristowebapi.entity.AbmDraftReportEntity;

@Repository
public interface AbmDraftReportRepository extends JpaRepository<AbmDraftReportEntity, Long>{

	AbmDraftReportEntity findByDraftId(Long draftId);
}
