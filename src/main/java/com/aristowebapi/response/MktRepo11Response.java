package com.aristowebapi.response;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MktRepo11Response {
	 
	private String month;
	private int fs;
	private double mthBudget;
	private double mthSale;
	private double mthLys;
	private double mthAchPer;
	private double mthGthPer;
	private double mthPmr;
	private double mthSurSlashdef;
	private double cumBudget;
	private double cumSale;
	private double cumLys;
	private double cumAchPer;
	private double cumGthPer;
	private double cumPmr;
	private int cumFs;
	private double cumSurSlashdef;
	private double cumIncrSale;
	private int color;
	

}
