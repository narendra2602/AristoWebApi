package com.aristowebapi.dto;
import com.aristowebapi.entity.DoctorPrescriptionEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DoctorPrescriptionDto {

		@JsonIgnore
		private long reportId;
		private String doctorName;
	    private String speciality;
	    private String dmlNo;
	    private String pcode;
	    private String hq;
	    private String town;
	    private String brand;
	    
		public DoctorPrescriptionDto(DoctorPrescriptionEntity doctorPrescriptionEntity) {
			
			this.reportId=doctorPrescriptionEntity.getReportId();
			this.doctorName =doctorPrescriptionEntity.getDoctorName();
			this.speciality = doctorPrescriptionEntity.getSpeciality();
			this.dmlNo = doctorPrescriptionEntity.getDmlNo();
			this.pcode = doctorPrescriptionEntity.getPcode();
			this.hq = doctorPrescriptionEntity.getHq();
			this.town = doctorPrescriptionEntity.getTown();
			this.brand = doctorPrescriptionEntity.getBrand();
		}
	    
	    

}
