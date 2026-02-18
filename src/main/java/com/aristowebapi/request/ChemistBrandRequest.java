package com.aristowebapi.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ChemistBrandRequest {

    private String brandName;
    private Integer monthlyPotentialStrips;
    private Integer ourMonthlySalesStrips;
    private List<ChemistCompetitorRequest> competitors =new ArrayList<>();
}
