package com.aristowebapi.response;

import java.util.List;

import com.aristowebapi.dto.DoctorPrescriptionDto;
import com.aristowebapi.dto.HeaderDto;
import com.aristowebapi.dto.MonthlyDevelopmentReportDto;
import com.aristowebapi.dto.MrDto;
import com.aristowebapi.dto.SelfAssessmentDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;


@JsonPropertyOrder({
    "abm_draft_id",
    "abm_draft_status",
    "report",
    "header",
    "mrs",
    "self_assessment",
    "doctors"
})


@Data
public class FullReportResponse {

	@JsonProperty("abm_draft_id")
	private Long abmDraftId;

	@JsonProperty("abm_draft_status")
	private String abmDraftStatus;
    private MonthlyDevelopmentReportDto report;
    

    private List<MrDto> mrs ;
    private List<DoctorPrescriptionDto> doctors;        // ✅ EMPTY ARRAY;;

    @JsonProperty("self_assessment")
    private SelfAssessmentDto selfAssessment;
    private HeaderDto header;
  
    
}
