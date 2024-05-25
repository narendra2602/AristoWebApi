package com.aristowebapi.response;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MktRepo4Response { 

	private Map<String,Integer> fs;
	private Map<String,Long> budget;
	private Map<String,Long> sales;
	private Map<String,Long> lys;
	private Map<String,Double> achPer;
	private Map<String,Double> gthPer;
	private Map<String,Integer> pmr;
	private Map<String,Double> surSlashdef;
	private Map<String,Long> incrSale;
	private Map<String,Integer> rankAch;
	private Map<String,Integer> rankPmr;
	private int color;
	

}
