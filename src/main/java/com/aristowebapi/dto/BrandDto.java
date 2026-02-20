package com.aristowebapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BrandDto {

    @JsonProperty("potential_per_month_strips")
    private Integer potentialPerMonthStrips;

    @JsonProperty("our_sales_per_month_strips")
    private Integer ourSalesPerMonthStrips;

    private List<CompetitorDto> data;
}