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
	private long tgt;
	private long sale;
	private double  ach;
	private long sd;
	private long cummTgt;
	private long cummSale;
	private double cummAch;
	private long cummSd;
	private long lyr;
	private double growth;
	private double pmr;
	
	
	private int color;


}
