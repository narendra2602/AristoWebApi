package com.aristowebapi.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MktRepo2SelectiveItemResponse {

	
	private String branch;
	private String hqName;
	private int fs;
	private long mthBudget;
	private long mthSale;
	private double  mthAchPer;
	private long mthSurSlashDef;
	private long cumBudget;
	private long cumSale;
	private double cumAchPer;
	private long cumSurSlashDef;
	private long cumLys;
	private double cumGthPer;
	private int pmr;
		private int color;
	
}
