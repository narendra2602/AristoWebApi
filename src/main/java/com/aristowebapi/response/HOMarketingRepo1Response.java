package com.aristowebapi.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HOMarketingRepo1Response { 
	
	private String name;
	private double monTgt;
	private double monSale;
	private double ach;
	private double cumTgt;
	private double cumSale;
	private double lastYear;
	private double cumAch;
	private double cumGth;
	private double pmr;
	private int color;
	

}
