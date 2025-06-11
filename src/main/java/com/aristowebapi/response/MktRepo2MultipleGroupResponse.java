package com.aristowebapi.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MktRepo2MultipleGroupResponse {

	
	private String branch;
	private String hqName;
	private int fs;
	private String name;
	private long mthBudget;
	private long mthSale;
	private long mthLys;
	private double  mthAchPer;
	private double  mthGthPer;
	private long mthSurSlashDef;
	private long mthIncr;
	
	private long cumBudget;
	private long cumSale;
	private long cumLys;
	private long cumExpiry;
	private double cumAchPer;
	private double cumGthPer;
	private long cumSurSlashDef;
	private long cumIncr;
	private int pmr;
	private int lysPmr;
	private int incrPmr;
	private double expiryRatio;

	
		private int color;
	
}
