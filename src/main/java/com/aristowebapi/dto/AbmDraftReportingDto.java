package com.aristowebapi.dto;

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

    private Long draftId;
    private String draftStatus;
    private int mnthCode;
    private int myear;
    private String entryDate;
    private int loginId;

    @JsonIgnore
    private String draftJson;

    public AbmDraftReportingDto(AbmDraftReportEntity entity) {
        this.draftId = entity.getDraftId();
        this.draftStatus = entity.getDraftStatus();
        this.mnthCode = entity.getMnthCode();
        this.myear = entity.getMyear();
        this.entryDate = entity.getEntryDate();
        this.loginId = entity.getLoginId();
        this.draftJson = entity.getDraftJson();
    }
}
