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
public class PendingRequest {
	
	private int myear;
	private int cmon;
	private int divCode;
	private int depoCode;
	private int utype;
	private int loginId;
	private int repType;
}
