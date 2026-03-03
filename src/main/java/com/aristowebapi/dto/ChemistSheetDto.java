package com.aristowebapi.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChemistSheetDto {

    private String title;
    private String month;

    @JsonProperty("sheet_name")
    private String sheetName;

    @JsonProperty("Division")
    private String division;

    @JsonProperty("Branch")
    private String branch;

    @JsonProperty("HQ")
    private String hq;

    @JsonProperty("Area")
    private String area;

    @JsonProperty("chemist_name")
    private String chemistName;
    
    @JsonProperty("psr_id")
    private Long psrId;

    @JsonProperty("psr_name")
    private String psrName;

    // Dynamic brand mapping
    @JsonProperty("ARISTO Brand")
    private Map<String, ChemistBrandDto> brands;
}