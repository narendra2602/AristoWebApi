package com.aristowebapi.request;

import java.util.List;

import com.aristowebapi.dto.DoctorPrescriptionDto;
import com.aristowebapi.dto.HeaderDto;
import com.aristowebapi.dto.MonthlyDevelopmentReportDto;
import com.aristowebapi.dto.MrDto;
import com.aristowebapi.dto.SelfAssessmentDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true) // IMPORTANT
public class MonthlyReportRequest {

/*    private MonthlyDevelopmentReportEntity report;
    private List<MrEntity> mrs;
    private List<DoctorPrescriptionEntity> doctors;
    private SelfAssessmentEntity selfAssessment;
    private HeaderEntity header;
*/    
    private Long abmDraftId;
    private MonthlyDevelopmentReportDto report;
    private List<MrDto> mrs;
    private List<DoctorPrescriptionDto> doctors;
    private SelfAssessmentDto selfAssessment;
    private HeaderDto header;
    
}
