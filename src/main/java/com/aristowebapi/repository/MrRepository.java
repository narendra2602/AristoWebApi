package com.aristowebapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.aristowebapi.entity.MrEntity;

@Repository
public interface MrRepository extends JpaRepository<MrEntity, Long> {

    // ✅ Find all MRs for a specific report using the common reportId
    List<MrEntity> findAllByReportId(Long reportId);
    
    @Modifying(clearAutomatically = true)
    void deleteByReportId(Long reportId);

}
