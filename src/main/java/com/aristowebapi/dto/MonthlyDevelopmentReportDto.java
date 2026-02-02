package com.aristowebapi.dto;
import lombok.Data;
import java.util.List;

import com.aristowebapi.entity.MonthlyDevelopmentReportEntity;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MonthlyDevelopmentReportDto {

    /*private ReportMeta report_meta;
    private Header header;
    private List<String> guidelines;
    private List<MrDto> mrs;
    private SelfAssessment self_assessment;
    private List<DoctorPrescriptionDto> doctor_prescription;*/

    private long reportId;
	private String company;
	private String reportTitle;
	private int divCode;
    private int depoCode;
    private int month;
    private int year;
    private int createdBy;
    

    
	
    
    

}
