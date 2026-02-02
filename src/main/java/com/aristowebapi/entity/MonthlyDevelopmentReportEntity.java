package com.aristowebapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.aristowebapi.dto.MonthlyDevelopmentReportDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "monthly_development_report")
@Data
@NoArgsConstructor
public class MonthlyDevelopmentReportEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "report_id")
    private Long reportId;
	
	private int reportMonth;
	private int reportYear;
	private int createdBy;
	private int divCode;
	private int depoCode;
    private Long draftId;

	
	
	public MonthlyDevelopmentReportEntity(MonthlyDevelopmentReportDto mDevelopmentReportDto) {
//		this.reportId = mDevelopmentReportDto.getReportId();
		
		this.reportMonth = mDevelopmentReportDto.getMonth();
		this.reportYear = mDevelopmentReportDto.getYear();
		this.createdBy = mDevelopmentReportDto.getCreatedBy();
		this.divCode = mDevelopmentReportDto.getDivCode();
		this.depoCode = mDevelopmentReportDto.getDepoCode();
//		this.draftId=mDevelopmentReportDto.getDraftId();
	}
	
	
	

}
