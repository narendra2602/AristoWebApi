package com.aristowebapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.aristowebapi.dto.AbmDraftReportingDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
	    name = "abm_draft_report_entry",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = {"login_id", "mnth_code"})
	    }
	)

@Data
@NoArgsConstructor
public class AbmDraftReportEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "draft_id")
    private Long draftId;

    @Column(name = "draft_status")
    private String draftStatus;

    @Column(name = "div_code")
    private int divCode;

    @Column(name = "depo_code")
    private int depoCode;

    @Column(name = "hq_code")
    private int hqCode;

    @Column(name = "mnth_code")
    private int mnthCode;

    @Column(name = "myear")
    private int myear;

    @Column(name = "entry_date")
    private String entryDate;

    @Column(name = "login_id")
    private int loginId;
    
    @Column(name = "emp_code")
    private int empCode;

    
    @Column(name = "draft_json", columnDefinition = "TEXT")
    private String draftJson;
	public AbmDraftReportEntity(AbmDraftReportingDto abmDraftReportingDto) {
		
//		this.draftId = abmDraftReportingDto.getDraftId();
		this.draftStatus = abmDraftReportingDto.getDraftStatus();
		this.divCode=abmDraftReportingDto.getDivCode();
		this.depoCode=abmDraftReportingDto.getDepoCode();
		this.hqCode=abmDraftReportingDto.getHqCode();
		this.mnthCode = abmDraftReportingDto.getMnthCode();
		this.myear = abmDraftReportingDto.getMyear();
		this.entryDate = abmDraftReportingDto.getEntryDate();
		this.loginId = abmDraftReportingDto.getLoginId();
		this.empCode=abmDraftReportingDto.getEmpCode();
//		this.draftJson = abmDraftReportingDto.getDraftJson();
	}

	

	
	
}
