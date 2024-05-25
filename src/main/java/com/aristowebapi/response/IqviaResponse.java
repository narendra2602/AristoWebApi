package com.aristowebapi.response;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IqviaResponse {
	
	private int rankLy;
	private int rankCy;
	private String product;
	private String company;
	private int lauch;
	private double valInLacLy;
	private double valInLacCy;
	private double msLy;
	private double msCy;
	private double gthLy;
	private double gthCy;
	private double valInLacsQ1;
	private double valInLacsQ2;
	private double valInLacsQ3;
	private double valInLacsQ4;
	private double msQ1;
	private double msQ2;
	private double msQ3;
	private double msQ4;
	private double gthQ1;
	private double gthQ2;
	private double gthQ3;
	private double gthQ4;
	private int color;
	

}
