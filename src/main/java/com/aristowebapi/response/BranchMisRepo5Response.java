package com.aristowebapi.response;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BranchMisRepo5Response {  
	 
	private String branch;
	private String hqName;
	private long budget;
	private long grossSale;
	private long creditNote;
	private long netSale;
	private double achPer;
	private long surSlashdef;
	private long lastYearSale;
	private int color;

}
