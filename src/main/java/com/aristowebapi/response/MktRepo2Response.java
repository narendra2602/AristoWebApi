package com.aristowebapi.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class MktRepo2Response { 

	private int code;
	private String name;
	private String pack;
	private long budget;
	private long sale;
	private double  achPer;
	private long surSlashDef;
	private long cumBudget;
	private long cumSale;
	private double cumAchPer;
	private long cumSurSlashDef;
	private long cumLys;
	private double gthPer;
	private int pmr;
	private int rankAch;
	private int rankPmr;
	private int color;

}
