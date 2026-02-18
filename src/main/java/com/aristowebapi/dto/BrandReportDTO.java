package com.aristowebapi.dto;

public class BrandReportDTO {

    private String brandName;
    private String branch;
    private String hq;
    private Integer potentialPerMonthStrips;
    private Integer ourSalesPerMonthStrips;

    public BrandReportDTO(String brandName,
                          String branch,
                          String hq,
                          Integer potentialPerMonthStrips,
                          Integer ourSalesPerMonthStrips) {

        this.brandName = brandName;
        this.branch = branch;
        this.hq = hq;
        this.potentialPerMonthStrips = potentialPerMonthStrips;
        this.ourSalesPerMonthStrips = ourSalesPerMonthStrips;
    }

    // getters only (no need setters)
}
