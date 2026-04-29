package com.aristowebapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.BrandReportDTO;
import com.aristowebapi.dto.ChemistFinalReportViewDto;
import com.aristowebapi.entity.ChemistAuditReportFinal;

public interface ChemistAuditReportRepositoryFinal  extends JpaRepository<ChemistAuditReportFinal, Long>{
	
	
	Optional<ChemistAuditReportFinal> 
	findByLine1EmpCodeAndReportMonth(Integer line1EmpCode, Integer reportMonth);
	
	Optional<ChemistAuditReportFinal> findByReportId(Long reportId);
	
	@Modifying(clearAutomatically = true)
	void deleteByReportId(Long reportId);
	
	boolean existsByReportId(Long reportId);
	
	
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

	

    @Query("SELECT new com.aristowebapi.dto.ChemistFinalReportViewDto(" +
    	       "b.brandName, " +
    	       "c.competitorTopSellingBrand, " +
    	       "c.doctor1Name, " +
    	       "c.doctor1Patients, " +
    	       "c.doctor2Name, " +
    	       "c.doctor2Patients, " +
    	       "c.doctor3Name, " +
    	       "c.doctor3Patients, " +
    	       "b.potentialPerMonthStrips, " +
    	       "b.ourSalesPerMonthStrips) " +
    	       "FROM ChemistAuditReportFinal r " +
    	       "JOIN r.sheets s " +
    	       "JOIN s.brands b " +
    	       "JOIN b.competitorDataList c " +
    	       "WHERE r.divCode = :division " +
    	       "AND r.depoCode = :depoCode " +
    	       "AND r.reportMonth = :month " +
    	       "AND r.reportYear = :year " +
    	       "ORDER BY b.brandName")
    	List<ChemistFinalReportViewDto> getFinalReport(
    	        @Param("division") int division,
    	        @Param("depoCode") int depoCode,
    	        @Param("month") int month,
    	        @Param("year") int year);    

    // this is query run by stored procedure 
/*    SELECT 
    b.brand_name,
    c.competitor_top_selling_brand,
    c.doctor1name,
    c.doctor1patients,
    c.doctor2name,
    c.doctor2patients,
    c.doctor3name,
    c.doctor3patients,
    b.potential_per_month_strips,
    b.our_sales_per_month_strips
FROM aristo_web.abm_chemist_audit_reportfinal r
JOIN aristo_web.abm_chemist_sheet s 
      ON s.report_id = r.id
JOIN aristo_web.abm_chemist_brand b 
      ON b.sheet_id = s.id
JOIN aristo_web.abm_chemist_doctor c 
      ON c.brand_id = b.id
WHERE r.div_code = 3
AND r.depo_code = 30
AND r.report_month = 202510
AND r.report_year = 2025
ORDER BY b.brand_name;
*/    

}
