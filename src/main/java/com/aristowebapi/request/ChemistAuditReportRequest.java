package com.aristowebapi.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ChemistAuditReportRequest {

    private String reportName;
    private String division;
    private String branch;
    private String hq;
    private String area;
    private String month;
    private String location;
    private List<ChemistBrandRequest> brands = new ArrayList<>();
}
