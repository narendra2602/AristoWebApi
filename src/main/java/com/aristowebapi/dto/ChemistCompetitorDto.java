package com.aristowebapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChemistCompetitorDto {

    @JsonProperty("competitor_top_selling_brand")
    private String competitorTopSellingBrand;

    @JsonProperty("top_prescribing_doctor_1_name")
    private String doctor1Name;

    @JsonProperty("top_prescribing_doctor_1_patients_per_day")
    private Integer doctor1Patients;

    @JsonProperty("top_prescribing_doctor_2_name")
    private String doctor2Name;

    @JsonProperty("top_prescribing_doctor_2_patients_per_day")
    private Integer doctor2Patients;

    @JsonProperty("top_prescribing_doctor_3_name")
    private String doctor3Name;

    @JsonProperty("top_prescribing_doctor_3_patients_per_day")
    private Integer doctor3Patients;
}