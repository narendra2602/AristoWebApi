package com.aristowebapi.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NearExpiryBranchWiseResponse {

	
	private String product;
	private String batchNo;
	private String expiryDate;
	private double netRate;
	private int expiredStock;
	private int expired_0To_90Days;
	private int expiry_90To_180Days;
	private int above_180Days;
	private double totalValue;
	private int color;

}
