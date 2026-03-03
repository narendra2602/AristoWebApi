package com.aristowebapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChemistFinalReportViewDto {

    private String brandName;

    private String competitorTopSellingBrand;

    private String doctor1Name;
    private Integer doctor1Patients;

    private String doctor2Name;
    private Integer doctor2Patients;

    private String doctor3Name;
    private Integer doctor3Patients;

    private Integer potentialPerMonthStrips;
    private Integer ourSalesPerMonthStrips;
}