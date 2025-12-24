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
	
	private String branch;
	private String pname;
	private String batchNo;
	private String expiryDate;
	private double ptsRate;
	private int expiredStock;
	private int expiry_31To_90Days;
	private int expiry_91To_180Days;
	private int expiry_181To_240Days;
	private int expiry_241To_300Days;
	private int expiry_301To_365Days;
	private int above_365Days;
	private double totalQty;
	
	
	
	private int color;

}
