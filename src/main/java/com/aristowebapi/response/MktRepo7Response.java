package com.aristowebapi.response;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MktRepo7Response { 
	
	private String name;
	private long saleable;
	private long expiry;
	private long breakage;
	private long ratediff;
	private long shortReceived;
	private long lossInTransit;
	private long total;
	private int color;

}
