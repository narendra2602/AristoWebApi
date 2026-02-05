package com.aristowebapi.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.aristowebapi.dto.AbmDraftReportingDto;
import com.aristowebapi.dto.AbmReportingDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "abm_draft_report_entry")
@Data
@NoArgsConstructor
public class AbmDraftReportEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "draft_id")
    private Long draftId;
	
	private String draftStatus;
	
	private int mnthCode;
	private int myear;
	private String entryDate;
	private int loginId;
	
	@Column(columnDefinition = "TEXT")
	private String draftJson;

	public AbmDraftReportEntity(AbmDraftReportingDto abmDraftReportingDto) {
		
//		this.draftId = abmDraftReportingDto.getDraftId();
		this.draftStatus = abmDraftReportingDto.getDraftStatus();
		this.mnthCode = abmDraftReportingDto.getMnthCode();
		this.myear = abmDraftReportingDto.getMyear();
		this.entryDate = abmDraftReportingDto.getEntryDate();
		this.loginId = abmDraftReportingDto.getLoginId();
//		this.draftJson = abmDraftReportingDto.getDraftJson();
	}

	

	
	
}
