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
	private int saleQty;
	private double saleVal;
	private int salableQty;
	private double salableVal;
	private int ebspdQty;
	private double ebspdVal;
	private int netQty;
	private double netVal;
	private int color;
	

}
