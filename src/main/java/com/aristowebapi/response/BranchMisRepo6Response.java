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
	
	private String branch;
	private String hqName;
	private int invSaleQty;
	private long invSaleVal;
	private int piSaleQty;
	private long piSaleVal;
	private int grossSaleQty;
	private long grossSaleVal;
	private int saleableQty;
	private long saleableVal;
	private int expQty;
	private long expVal;
	private int brkgQty;
	private long brkgVal;
	private int lossInTrQty;
	private long lossInTrVal;
	private int otherQty;
	private long otherVal;
	private long rateDiff;
	private int netQty;
	private long netVal;
	private int color;

}
