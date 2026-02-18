package com.aristowebapi.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ChemistCompetitorRequest {

    private Integer competitorrank;
    private String competitorBrand;
    private List<ChemistDoctorRequest> topDoctors= new ArrayList<>();
}
