package com.aristowebapi.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AbmReportingDraftRequest {
	private int divCode;
	private int depoCode;
	private int hqCode;
	private int mnthCode;
	private int myear;
	private String entryDate;
	private int loginId;

}
