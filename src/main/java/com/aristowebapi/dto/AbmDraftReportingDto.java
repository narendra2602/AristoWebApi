package com.aristowebapi.dto;

import javax.persistence.Column;

import com.aristowebapi.entity.AbmDraftReportEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor   // ✅ REQUIRED
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AbmDraftReportingDto {

	@Column(name = "draft_id")
    private Long draftId;
    private String draftStatus;
	private int divCode;
	private int depoCode;
	private int hqCode;

    private int mnthCode;
    private int myear;
    private String entryDate;
    private int loginId;
    private int empCode;
    private String loginName;
    @JsonIgnore
    private String draftJson;

    public AbmDraftReportingDto(AbmDraftReportEntity entity) {
        this.draftId = entity.getDraftId();
        this.draftStatus = entity.getDraftStatus();
		this.divCode=entity.getDivCode();
		this.depoCode=entity.getDepoCode();
		this.hqCode=entity.getHqCode();
        this.mnthCode = entity.getMnthCode();
        this.myear = entity.getMyear();
        this.entryDate = entity.getEntryDate();
        this.loginId = entity.getLoginId();
        this.empCode=entity.getEmpCode();
        this.draftJson = entity.getDraftJson();
    }
}
