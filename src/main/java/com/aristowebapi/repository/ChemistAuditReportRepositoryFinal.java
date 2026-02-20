package com.aristowebapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.BrandReportDTO;
import com.aristowebapi.entity.ChemistAuditReportFinal;

public interface ChemistAuditReportRepositoryFinal  extends JpaRepository<ChemistAuditReportFinal, Long>{
	
	Optional<ChemistAuditReportFinal> findByReportId(int reportId);
	
	void deleteByReportId(int reportId);
	
    // Branch + HQ + Brand Wise Report
    @Query("SELECT new com.aristowebapi.dto.BrandReportDTO(" +
           "b.brandName, " +
           "s.branch, " +
           "s.hq, " +
           "b.potentialPerMonthStrips, " +
           "b.ourSalesPerMonthStrips) " +
           "FROM ChemistBrand b " +
           "JOIN b.sheet s " +
           "WHERE s.branch = :branch " +
           "AND s.hq = :hq " +
           "AND b.brandName = :brand")
    List<BrandReportDTO> getBrandReport(
            @Param("branch") String branch,
            @Param("hq") String hq,
            @Param("brand") String brand);


    // Competitor + Doctor Wise Report
    @Query("SELECT s.branch, s.hq, b.brandName, " +
           "c.competitorTopSellingBrand, " +
           "c.doctor1Name, c.doctor1Patients, " +
           "c.doctor2Name, c.doctor2Patients, " +
           "c.doctor3Name, c.doctor3Patients " +
           "FROM ChemistCompetitor c " +
           "JOIN c.brand b " +
           "JOIN b.sheet s " +
           "WHERE b.brandName = :brand")
    List<Object[]> getFullReport(
            @Param("brand") String brand);



}
