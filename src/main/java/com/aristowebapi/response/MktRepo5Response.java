package com.aristowebapi.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MktRepo5Response {
	
	private String name;
	private long budget;
	private long gross;
	private long credit;
	private long net;
	private double ach;
	private long sd;
	private long pisale;
	private int color;
}
