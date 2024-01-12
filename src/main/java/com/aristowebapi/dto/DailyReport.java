package com.aristowebapi.dto;

import java.sql.Time;
import java.util.Date;

public interface DailyReport {
	
	public String getBr_name();
	public double getBudget();
	public double getSales_trade();
	public double getCn100();
	public double getAch();
	public double getSurdef();
	public double getLast_month();
	public double getLast_year();
	public double getCollection();
	public double getCollection_cumm();
	public double getRemit();
	public double getRemit_cumm();
	public double getOutstand();
	public int getDepo_code();
	public int getDiv_code();
	public double getBudget_per();
	public double getSales_today();
	public int getMgr_code();
	public String getMgr_name();
	public String getEnt_Date();
	public Time getEnt_time();
	public String getStatus();
	public int getHo_seq();
	public String getDiv_name();
	public int getTp();
	
}
