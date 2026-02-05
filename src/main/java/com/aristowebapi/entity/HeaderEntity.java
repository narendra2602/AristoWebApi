package com.aristowebapi.entity;

import javax.persistence.*;

import com.aristowebapi.dto.HeaderDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "abm_header")
@Data
@NoArgsConstructor
public class HeaderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ COMMON KEY
    @Column(name = "report_id")
    private Long reportId;

    // Header Fields
    private String fromName;
    private String designation;
    private String toName;
    private String hq;
    private String cc;
    private String reference;
    private String refDate;
    
	public HeaderEntity(HeaderDto headerDto) {
		this.reportId = headerDto.getReportId();
		this.fromName = headerDto.getFromName();
		this.designation = headerDto.getDesignation();
		this.toName = headerDto.getTo();
		this.hq = headerDto.getHq();
		this.cc = headerDto.getCc();
		this.reference = headerDto.getRef();
		this.refDate = headerDto.getDate();
	}
    
   
}
