package com.aristowebapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.aristowebapi.entity.DoctorPrescriptionEntity;

@Repository
public interface DoctorPrescriptionRepository extends JpaRepository<DoctorPrescriptionEntity, Long> {

    // ✅ Find all doctor prescriptions for a given reportId
    List<DoctorPrescriptionEntity> findAllByReportId(Long reportId);
    
    @Modifying(clearAutomatically = true)
    void deleteByReportId(Long reportId);

}
