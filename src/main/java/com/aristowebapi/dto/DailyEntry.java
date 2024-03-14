package com.aristowebapi.dto;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@Entity
@Table(name="daily_entry")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DailyEntry {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int amdNo;
	
	private int depoCode;
	private String division;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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
	private String status;
	private String remarks;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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
