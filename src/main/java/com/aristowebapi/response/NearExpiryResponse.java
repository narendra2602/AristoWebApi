package com.aristowebapi.response;

import java.sql.Time;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NearExpiryResponse {
	
	private String branchName;
	private String batchNo;
	private String expiryDate;
	private double netRate;
	private int expiredStock;
	private int expired090Days;
	private int expiry90180Days;
	private int above180Days;
	private double totalValue;
	private int color;

}
