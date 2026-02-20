package com.aristowebapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class SheetDto {

    @JsonProperty("sheet_name")
    private String sheetName;

    private String title;
    private String month;

    @JsonProperty("Division")
    private String division;

    @JsonProperty("Branch")
    private String branch;

    @JsonProperty("HQ")
    private String hq;

    @JsonProperty("AREA")
    private String area;

    @JsonProperty("ARISTO Brand")
    private Map<String, BrandDto> aristoBrand;
}