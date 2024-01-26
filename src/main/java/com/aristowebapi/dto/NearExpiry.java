package com.aristowebapi.dto;

public interface NearExpiry {
	
	public String getBranch_name();
	public String getBatch_no();
	public String getExpiry_date();
	public double getNet_rate();
	public int getExpired_stock();
	public int getExpired90();
	public int getExpired180();
	public int getExpiredAbove180();
	public double getTotal_value();
	public String getPname();

}
