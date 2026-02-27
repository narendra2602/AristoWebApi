package com.aristowebapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aristowebapi.entity.ChemistAuditReport;

@Repository
public interface ChemistAuditReportRepository
        extends JpaRepository<ChemistAuditReport, Long> {


	

	Optional<ChemistAuditReport> findByAuditReportId(Long reportId);
	
	
	
	
	
}