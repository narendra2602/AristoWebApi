package com.aristowebapi.response;
import java.util.List;

import lombok.Data;

@Data
public class ChemistBrandResponse {

    private Long brandId;
    private String brandName;
    private Integer monthlyPotentialStrips;
    private Integer ourMonthlySalesStrips;
    private Integer achievementPercentage;
    private List<ChemistCompetitorResponse> competitors;
}
