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
    private String terName;
    @JsonIgnore
    private String draftJson;
    private String  line1EmpName;
    private int line2EmpCode;
    private String  line2EmpName;
    private int line3EmpCode;
    private String  line3EmpName;
    private String  line1Name;
    
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
        this.loginName = entity.getLine1EmpName() != null ? entity.getLine1EmpName() : "";
        this.terName = entity.getLine1Name() != null ? entity.getLine1Name() : "";
        
        this.draftJson = entity.getDraftJson();
        this.line1EmpName=entity.getLine1EmpName();
        this.line2EmpCode=entity.getLine2EmpCode();
        this.line2EmpName=entity.getLine2EmpName();
        this.line3EmpCode=entity.getLine3EmpCode();
        this.line3EmpName=entity.getLine3EmpName();
        this.line1Name=entity.getLine1Name();
    }
}
