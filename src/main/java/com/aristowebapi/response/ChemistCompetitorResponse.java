package com.aristowebapi.response;
import java.util.List;

import lombok.Data;

@Data
public class ChemistCompetitorResponse {

    private Long competitorId;
    private Integer rank;
    private String competitorBrand;
    private Integer totalDoctorCount;
    private List<ChemistDoctorResponse> topDoctors;
}
