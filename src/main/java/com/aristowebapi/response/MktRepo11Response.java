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
	private double monthTgt;
	private double monthSale;
	private double monthLys;
	private double monthAch;
	private double monthGth;
	private double monthPmr;
	private double monthSd;
	private double cummTgt;
	private double cummSale;
	private double cummLys;
	private double cummAch;
	private double cummGth;
	private double cummPmr;
	private int cummFs;
	private double cummSd;
	private double cummIncrSale;
	private int color;
	

}
