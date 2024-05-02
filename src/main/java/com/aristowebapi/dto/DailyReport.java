package com.aristowebapi.dto;

import java.sql.Time;
import java.util.Date;

public interface DailyReport extends DailyEntryList {
	
	public String getBr_name();
	public int getDepo_code();
	public int getDiv_code();
	public int getMgr_code();
	public String getMgr_name();
	public Time getEnt_time();
	public int getHo_seq();
	public String getDiv_name();
	public int getTp();
	
}
