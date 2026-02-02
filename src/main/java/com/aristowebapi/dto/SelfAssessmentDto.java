package com.aristowebapi.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SelfAssessmentDto {

	@JsonIgnore
	private long reportId;
	private String selfRating;
    private String selfImprovementPlan;

}
