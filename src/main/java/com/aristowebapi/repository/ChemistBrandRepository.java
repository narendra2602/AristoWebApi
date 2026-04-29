package com.aristowebapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aristowebapi.entity.ChemistBrand;

@Repository
public interface ChemistBrandRepository extends JpaRepository<ChemistBrand, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM abm_chemist_brand " +
                   "WHERE sheet_id IN ( " +
                   "SELECT id FROM abm_chemist_sheet " +
                   "WHERE report_id = :reportId " +
                   "AND psr_id = :psrId )",
           nativeQuery = true)
    void deleteBrand(@Param("reportId") Long reportId,
                     @Param("psrId") Long psrId);
}