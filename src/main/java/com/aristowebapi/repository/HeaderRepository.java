package com.aristowebapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.aristowebapi.entity.HeaderEntity;

@Repository
public interface HeaderRepository extends JpaRepository<HeaderEntity, Long> {

    // ✅ Find header by reportId
    Optional<HeaderEntity> findByReportId(Long reportId);
    
    @Modifying(clearAutomatically = true)
    void deleteByReportId(Long reportId);
}
