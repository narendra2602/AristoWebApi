package com.aristowebapi.request;

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
public class NearExpiryRequest { 
	
	private int divCode;
	private int depoCode;
	private int loginId;
	private Date currDate;
	private int code;
	private int mktYear;
}
