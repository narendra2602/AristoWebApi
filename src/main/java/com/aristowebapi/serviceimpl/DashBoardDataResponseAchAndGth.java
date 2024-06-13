package com.aristowebapi.serviceimpl;

import com.aristowebapi.response.DashBoardDataResponseDouble;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DashBoardDataResponseAchAndGth {
	
	private String name;
	private double achPer;
	private double gthPer;


}
