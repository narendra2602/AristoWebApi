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
	private long monthTgt;
	private long monthSale;
	private long monthLys;
	private double monthAch;
	private double monthGth;
	private double monthPmr;
	private long monthSd;
	private long cummTgt;
	private long cummSale;
	private long cummLys;
	private double cummAch;
	private double cummGth;
	private double cummPmr;
	private int cummFs;
	private long cummSd;
	private long cummIncrSale;
	private int color;
	

}
