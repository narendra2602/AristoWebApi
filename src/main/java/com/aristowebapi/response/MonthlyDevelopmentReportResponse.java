package com.aristowebapi.response;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MonthlyDevelopmentReportResponse {

	private String company;
    private String reportTitle;
    private String month;
    private Integer year;
    private String name;
    private String designation;
    private String to;
    private String hq;
    private List<String> cc;
    private String ref;
    private String date;
    private List<String> guidelines;
    
    private Integer id;
    private String mrName;
    private String mrHq;
    private List<String> priorities;
    private String communicationRating;
    private List<String> advice;
    private String monthTarget;
    private String secondarySalesAsOn30;
    private String expectedSalesNextMonth;
    private String selfRating;
    private String selfImprovementPlan;
    private String doctorName;
    private String speciality;
    private String quantity;
    private String doctorCode;
    private String district;
    private String area;
    private String product;

}
