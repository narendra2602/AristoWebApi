package com.aristowebapi.response;

import java.sql.Time;
import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class DailyReportResponse {  
	
	private int br;
	private String branch;
	private String division;
	private double budget;
	private double sales;
	private double achPer;
	private double surDef;
	private double cn100;
	private double todaySales;
	private double lmSale;
	private double lySale;
	private double collCumm;
	private double remiitToday;
	private double remitCumm;
	private double osAsOn;
	private String status;
	private String entryDate;
	private Time time;
	private int color;

}
