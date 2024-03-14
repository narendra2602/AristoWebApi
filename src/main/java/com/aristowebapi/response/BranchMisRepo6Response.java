package com.aristowebapi.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BranchMisRepo6Response { 
	
	private String name;
	private int invSaleQty;
	private double invSaleVal;
	private int piSaleQty;
	private double piSaleVal;
	private int grossSaleQty;
	private double grossSaleVal;
	private int salableQty;
	private double salableVal;
	private int expQty;
	private double expVal;
	private int brkQty;
	private double brkVal;
	private int othQty;
	private double othVal;
	private double pdVal;
	private int netQty;
	private double netVal;
	private int color;

}
