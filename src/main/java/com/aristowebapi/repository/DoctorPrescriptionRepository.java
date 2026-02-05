package com.aristowebapi.repository;

import com.aristowebapi.entity.DoctorPrescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorPrescriptionRepository extends JpaRepository<DoctorPrescriptionEntity, Long> {

    // ✅ Find all doctor prescriptions for a given reportId
    List<DoctorPrescriptionEntity> findAllByReportId(Long reportId);
    void deleteByReportId(Long reportId);

}
