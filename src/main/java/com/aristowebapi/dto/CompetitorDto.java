package com.aristowebapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompetitorDto {

    @JsonProperty("competitor_top_selling_brand")
    private String competitorTopSellingBrand;

    @JsonProperty("top_prescribing_doctor_1_name")
    private String topPrescribingDoctor1Name;

    @JsonProperty("top_prescribing_doctor_1_patients_per_day")
    private Integer topPrescribingDoctor1PatientsPerDay;

    @JsonProperty("top_prescribing_doctor_2_name")
    private String topPrescribingDoctor2Name;

    @JsonProperty("top_prescribing_doctor_2_patients_per_day")
    private Integer topPrescribingDoctor2PatientsPerDay;

    @JsonProperty("top_prescribing_doctor_3_name")
    private String topPrescribingDoctor3Name;

    @JsonProperty("top_prescribing_doctor_3_patients_per_day")
    private Integer topPrescribingDoctor3PatientsPerDay;
}