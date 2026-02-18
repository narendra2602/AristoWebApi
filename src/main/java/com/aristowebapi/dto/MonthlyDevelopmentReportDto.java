package com.aristowebapi.dto;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

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
    private int hqCode;
    private int month;
    private int year;
    private int createdBy;
    private int line1EmpCode;
    private String  line1EmpName;
    private int line2EmpCode;
    private String  line2EmpName;
    private int line3EmpCode;
    private String  line3EmpName;    
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
	
    
    

}
