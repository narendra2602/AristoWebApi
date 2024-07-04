package com.aristowebapi.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MktRepo5Response { 
	
	private String branch;
	private String hqName;
	private int mthFs;
	private double mthBudget;
	private double mthGross;
	private double mthCredit;
	private double mthNet;
	private double mthAchPer;
	private double mthLys;
	private double mthGthPer;
	private double mthPmr;
	private double mthSurSlashDeff;
	private double mthIncrSale;
	private double mthPendingPi;
	
	private int cumFs;
	private double cumBudget;
	private double cumGross;
	private double cumCredit;
	private double cumNet;
	private double cumAchPer;
	private double cumLys;
	private double cumGthPer;
	private double cumPmr;
	private double cumSurSlashDef;
	private double cumIncrSale;
	private double cumPendingPi;
	private int color;
}
