package com.aristowebapi.response;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MtbStockiestResponse {
	private String divName;
	private String depoName;
	private String stockiestName;
	private String city;
	private String gstNo;
	private String panNo;
	private String productName;
	private String invoiceNo;
	private String sapDocNo;
	private String invoiceDate;
	private String batchNo;
	private long quantity;
	private long schemeQty;
	private long value;
	private String paymentTag;
	
	





}
