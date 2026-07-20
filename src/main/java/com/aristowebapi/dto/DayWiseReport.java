package com.aristowebapi.dto;

import java.sql.Time;

public interface DayWiseReport {
	
	
	public int getDiv_code();
	public String getDiv_name();
	public int getSdepo_code();
	public String getBranch_name();
	public int getMnth_code();
	public int getDt();
	public double getAmt();

}
