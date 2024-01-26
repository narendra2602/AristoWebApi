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
	private Map<String,Long> sales;
	private Map<String,Long> target;
	private Map<String,Long> lys;
	private Map<String,Long> incr;
	private Map<String,Double> ach;
	private Map<String,Double> gth;
	private Map<String,Double> pmr;
	private Map<String,Double> sd;
	private int color;
	

}
