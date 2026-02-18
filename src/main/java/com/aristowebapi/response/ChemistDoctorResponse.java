package com.aristowebapi.response;
import lombok.Data;

@Data
public class ChemistDoctorResponse {

    private Long doctorId;
    private Integer priority;
    private String doctorName;
    private Integer patientsPerDay;
}
