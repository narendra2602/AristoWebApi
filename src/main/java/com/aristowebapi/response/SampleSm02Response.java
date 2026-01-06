package com.aristowebapi.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({
    "name",
    "pack",
    "branch",
    "hq_name",
    "data_type",
    "months",
    "color"
})



public class SampleSm02Response {

	
    @Getter(AccessLevel.NONE)
    @JsonIgnore
    private String branch;

    @Getter(AccessLevel.NONE)
    @JsonIgnore
    private String hqName;

    private String dataType;
    private Map<String, Long> months;
    private int color;

    @JsonIgnore
    private boolean renameFields;

    // ===== Custom JSON getters =====

    @JsonProperty("branch")
    public String getBranchValue() {
        return renameFields ? null : branch;
    }

    @JsonProperty("hq_name")
    public String getHqNameValue() {
        return renameFields ? null : hqName;
    }

    @JsonProperty("name")
    public String getName() {
        if (!renameFields) return null;
        return ".".equals(branch) ? null : branch;
    }

    @JsonProperty("pack")
    public String getPack() {
        if (!renameFields) return null;
        return ".".equals(hqName) ? null : hqName;
    }

}
