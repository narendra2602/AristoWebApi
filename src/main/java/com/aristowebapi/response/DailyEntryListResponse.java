package com.aristowebapi.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DailyEntryListResponse {
	
	private double budgetPer;
	private double budget;
	private double cn100;
	private double netSalesToday;
	private double netSalesUptoDate;
	private double lastMonthSales;
	private double lastYearSales;
	private double collectionToday;
	private double collectionCumm;
	private double remittanceToday;
	private double remittanceCumm;
	private double outstandingAsOnDate;
	private String monthlyBillingStatus;
	

}
