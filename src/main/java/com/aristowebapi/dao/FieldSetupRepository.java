package com.aristowebapi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aristowebapi.entity.FieldSetup;

@Repository
public interface FieldSetupRepository extends JpaRepository<FieldSetup, Long> {

    /**
     * STEP 1: Clear existing data
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM FieldSetup f")
    void deleteAllData();

    /**
     * STEP 3: Call stored procedure after upload
     */
    @Transactional
    @Query(value = "CALL fieldsetup()", nativeQuery = true)
    void updateAllData();
}
