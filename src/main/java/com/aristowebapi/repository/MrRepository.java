package com.aristowebapi.repository;

import com.aristowebapi.entity.MrEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MrRepository extends JpaRepository<MrEntity, Long> {

    // ✅ Find all MRs for a specific report using the common reportId
    List<MrEntity> findAllByReportId(Long reportId);

}
