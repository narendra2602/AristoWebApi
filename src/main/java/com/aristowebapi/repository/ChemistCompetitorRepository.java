package com.aristowebapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aristowebapi.entity.ChemistCompetitor;

@Repository
public interface ChemistCompetitorRepository extends JpaRepository<ChemistCompetitor, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM abm_chemist_doctor " +
                   "WHERE brand_id IN ( " +
                   "SELECT b.id FROM abm_chemist_brand b " +
                   "JOIN abm_chemist_sheet s ON b.sheet_id = s.id " +
                   "WHERE s.report_id = :reportId " +
                   "AND s.psr_id = :psrId )",
           nativeQuery = true)
    void deleteDoctor(@Param("reportId") Long reportId,
                      @Param("psrId") Long psrId);
}