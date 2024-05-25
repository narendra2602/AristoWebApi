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
	private long invSaleVal;
	private int piSaleQty;
	private long piSaleVal;
	private int grossSaleQty;
	private long grossSaleVal;
	private int salableQty;
	private long salableVal;
	private int expQty;
	private long expVal;
	private int brkQty;
	private long brkVal;
	private int othQty;
	private long othVal;
	private long pdVal;
	private int netQty;
	private long netVal;
	private int color;

}
