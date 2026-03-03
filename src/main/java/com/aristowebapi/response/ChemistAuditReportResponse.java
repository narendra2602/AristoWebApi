package com.aristowebapi.response;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChemistAuditReportResponse {

    private String status;
    private String message;
    private LocalDateTime timestamp;
//    private ChemistAuditReportData data;
}
