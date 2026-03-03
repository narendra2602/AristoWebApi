package com.aristowebapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aristowebapi.entity.ChemistSheet;

public interface ChemistSheetRepository extends JpaRepository<ChemistSheet, Long> {
}