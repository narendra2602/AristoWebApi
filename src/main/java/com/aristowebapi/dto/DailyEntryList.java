package com.aristowebapi.dto;

import java.sql.Time;

public interface DailyEntryList {
	
	public double getBudget_per();
	public double getBudget();
	public double getSales_today();
	public double getCn100();
	public double getSales_trade();
	public double getAch();
	public double getSurdef();
	public double getLast_month();
	public double getLast_year();
	public double getCollection();
	public double getCollection_cumm();
	public double getRemit();
	public double getRemit_cumm();
	public double getOutstand();
	public String getEnt_Date();
	public String getStatus();

	

}
