package com.aristowebapi.request;

import lombok.Data;

@Data
public class ChemistDoctorRequest {

    private Integer priority;
    private String doctorName;
    private Integer patientsPerDay;
}
