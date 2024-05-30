package com.aristowebapi.response;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StkRepo9Response {
	
	private String name;
	private int salesQty;
	private long salesVal;
	private int saleableRetQty;
	private long saleableRetVal;
	private int expQty;
	private long expVal;
	private int brkSpoilQty;
	private long brkSpoilVal;
	private int lostInTrQty;
	private long lossInTrVal;
	private long rateDiffVal;
	private int otherRetQty;
	private long otherRetVal;
	private int netSalesQty;
	private long netSalesVal;
	private int color;
	

}
