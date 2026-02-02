package com.aristowebapi.response;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class AbmReportingResponse {
	private String hq;
	private String  line1_empname;
	private String line1_desg;
	private String line2_empname;
	private String line2_desg;
	private String line3_empname;
	private String line3_desg;

}
