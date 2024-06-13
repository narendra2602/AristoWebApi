package com.aristowebapi.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MisRepo31Response {
	
	private String product;
	private String pack;
	
	private long budgetUnit;
	private double budgetValue;
	private long invSalesUnit;
	private long piSalesUnit;
	private long bonusUnit;
	private long grossSaleValue;
	private long saleableReturn;
	private long expiryReturn;
	private long brkgReturn;
	private long rateDiffCn;
	private long lossInTransitCn;
	private long otherReturn;
	private long totalCnQty;
	private long totalCnValue;
	private long netUnit;
	private long netValue;
	private double achPer;
	private int color;
	

}
