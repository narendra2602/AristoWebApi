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
@Table(name = "abm_monthly_development_report")
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
	private int hqCode;
    private Long draftId;
    private int line1EmpCode;
    private String  line1EmpName;
    private int line2EmpCode;
    private String  line2EmpName;
    private int line3EmpCode;
    private String  line3EmpName;

	
	
	public MonthlyDevelopmentReportEntity(MonthlyDevelopmentReportDto mDevelopmentReportDto) {
//		this.reportId = mDevelopmentReportDto.getReportId();
		
		this.reportMonth = mDevelopmentReportDto.getMonth();
		this.reportYear = mDevelopmentReportDto.getYear();
		this.createdBy = mDevelopmentReportDto.getCreatedBy();
		this.divCode = mDevelopmentReportDto.getDivCode();
		this.depoCode = mDevelopmentReportDto.getDepoCode();
		this.hqCode=mDevelopmentReportDto.getHqCode();
		this.line1EmpCode=mDevelopmentReportDto.getLine1EmpCode();
		this.line1EmpName=mDevelopmentReportDto.getLine1EmpName();
		this.line2EmpCode=mDevelopmentReportDto.getLine2EmpCode();
		this.line2EmpName=mDevelopmentReportDto.getLine2EmpName();
		this.line3EmpCode=mDevelopmentReportDto.getLine3EmpCode();
		this.line3EmpName=mDevelopmentReportDto.getLine3EmpName();

		//		this.draftId=mDevelopmentReportDto.getDraftId();
	}
	
	
	

}
