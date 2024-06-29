package com.aristowebapi.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MktRepo8Response { 
	
	private String branch;
	private String name;
	private long gross;
	private long expiry;
	private double expiryRaio;
	private int color;

}
