package com.aristowebapi.dto;
import java.util.List;

import com.aristowebapi.entity.MrEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MrDto {

	@JsonIgnore
	private long reportId;
    private long id;
    private String name;
    private String hq;
    private List<String> priorities;
    private String communicationRating;
    private List<String> advice;
    private String monthTarget;
    private String secondarySalesAsOn30;
    private String closingStockAsOn30;
    private String expectedSalesNextMonth;  
//    private MonthlyDevelopmentReportDto report;
    
    
	public MrDto(MrEntity mrEntity) {
		this.reportId=mrEntity.getReportId();
		this.id = mrEntity.getMrId();
		this.name = mrEntity.getName();
		this.hq = mrEntity.getHq();
		this.priorities = mrEntity.getPriorities();
		this.communicationRating = mrEntity.getCommunicationRating();
		this.advice = mrEntity.getAdvice();
		this.monthTarget = mrEntity.getMonthTarget();
		this.secondarySalesAsOn30 = mrEntity.getSecondarySalesAsOn30();
		this.closingStockAsOn30=mrEntity.getClosingStockAsOn30();
		this.expectedSalesNextMonth = mrEntity.getExpectedSalesNextMonth();
		
		//this.report=new MonthlyDevelopmentReportDto(mrEntity.getReport());
	}
    
    
}
