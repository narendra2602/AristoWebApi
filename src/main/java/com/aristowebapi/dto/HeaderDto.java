package com.aristowebapi.dto;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class HeaderDto {

	@JsonIgnore
	private long reportId;
	private String fromName;
    private String designation;
    private String to;
    private String hq;
    private String cc;
    private String ref;
    private String date;
    
}
