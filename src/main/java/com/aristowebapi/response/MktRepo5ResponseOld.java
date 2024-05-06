package com.aristowebapi.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MktRepo5ResponseOld { 
	
	private String name;
	private int fs;
	private double budget;
	private double gross;
	private double credit;
	private double net;
	private double ach;
	private double lys;
	private double gth;
	private double pmr;
	private double sd;
	private double incrSls;
	private double pendingPi;
	private int color;
}
