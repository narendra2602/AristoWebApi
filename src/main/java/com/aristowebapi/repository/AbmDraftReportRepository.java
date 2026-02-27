package com.aristowebapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aristowebapi.entity.AbmDraftReportEntity;

@Repository
public interface AbmDraftReportRepository extends JpaRepository<AbmDraftReportEntity, Long>{

	AbmDraftReportEntity findByDraftId(Long draftId);
	
	
	List<AbmDraftReportEntity> findByMnthCodeAndMyearAndLoginId(int mnthCode, int myear, int loginId);
	List<AbmDraftReportEntity> findByDivCodeAndMnthCodeAndMyear(int divCode,int mnthCode, int myear);
	
	boolean existsByLoginIdAndMnthCodeAndMyear(int loginId,int mnthCode,int myear);

	
}
