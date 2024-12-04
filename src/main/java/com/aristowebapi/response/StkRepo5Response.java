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
public class StkRepo5Response {
	private String branch;
	private String name;
	private String invoiceNo;
	private String invoiceDate;
	private String batchNo;
	private double supplyRate;
	private int salesQty;
	private long salesVal;
	private int color;
	

}
