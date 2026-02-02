package com.aristowebapi.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

}
