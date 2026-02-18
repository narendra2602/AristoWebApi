package com.aristowebapi.response;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChemistAuditReportResponse {

    private String status;
    private String message;
    private LocalDateTime timestamp;
//    private ChemistAuditReportData data;
}
