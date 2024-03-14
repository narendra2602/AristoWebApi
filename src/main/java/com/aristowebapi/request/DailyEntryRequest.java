package com.aristowebapi.request;

import java.sql.Time;
import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DailyEntryRequest {
	
	
	private int depoCode;
	private String division;
	private Date ddate;
	private double budgetHo;
	private double budgetBr;
	private double salesTrade;
	private double salesInst;
	private double salesGmsd;
	private double pOrder;
	private double collBud;
	private double collection;
	private double remit;
	private double outstand;
	private int amdNo;
	private String status;
	private String remarks;
	private Date ent_date;
	private Time entTime;
	private String billMnth;
	private double budgetPer;
	private double salesToday;
	private double cn100;
	private double ach;
	private double surdef;
	private double lastMonth;
	private double lastYear;
	private double collectionCumm;
	private double remitCumm;
	private int mktYear;
	private int divCode;
	private int mnthCode;
	private String name; 


}
