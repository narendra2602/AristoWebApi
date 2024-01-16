package com.aristowebapi.response;

import java.sql.Time;
import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DailyStatusResponse {

	private int code;
	private String branchName;
	private String billingDate;
	private String entryDate;
	private Time time;
	private String remark;
}
