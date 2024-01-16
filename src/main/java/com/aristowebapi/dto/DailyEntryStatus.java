package com.aristowebapi.dto;

import java.sql.Time;

public interface DailyEntryStatus {
	
	public int getDepo_code();
	public String getBr_name();
	public String getDiv_name();
	public String getBilling_date();
	public String getEntry_date();
	public Time getEntry_time();
	public String getStatus();

}
