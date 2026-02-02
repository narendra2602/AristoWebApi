package com.aristowebapi.repository;

import com.aristowebapi.entity.HeaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeaderRepository extends JpaRepository<HeaderEntity, Long> {

    // ✅ Find header by reportId
    Optional<HeaderEntity> findByReportId(Long reportId);

}
