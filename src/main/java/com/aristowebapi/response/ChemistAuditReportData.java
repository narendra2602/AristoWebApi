package com.aristowebapi.response;
import java.util.List;

import lombok.Data;

@Data
public class ChemistAuditReportData {

    private Long reportId;
    private ChemistReportHeader reportHeader;
    private List<ChemistBrandResponse> brands;
}
