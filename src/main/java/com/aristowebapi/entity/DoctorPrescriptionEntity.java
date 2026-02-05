package com.aristowebapi.entity;

import javax.persistence.*;

import com.aristowebapi.dto.DoctorPrescriptionDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "abm_doctor_prescription")
@Data
@NoArgsConstructor
public class DoctorPrescriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ COMMON KEY
    @Column(name = "report_id")
    private Long reportId;

    private String doctorName;
    private String speciality;
    private String dmlNo;
    private String pcode;
    private String hq;
    private String town;
    private String brand;
    
    
	public DoctorPrescriptionEntity(DoctorPrescriptionDto doctorPrescriptionDto) {
		
		
		this.reportId = doctorPrescriptionDto.getReportId();
		this.doctorName = doctorPrescriptionDto.getDoctorName();
		this.speciality = doctorPrescriptionDto.getSpeciality();
		this.dmlNo = doctorPrescriptionDto.getDmlNo();
		this.pcode = doctorPrescriptionDto.getPcode();
		this.hq = doctorPrescriptionDto.getHq();
		this.town = doctorPrescriptionDto.getTown();
		this.brand = doctorPrescriptionDto.getBrand();
	}
    
    
}
