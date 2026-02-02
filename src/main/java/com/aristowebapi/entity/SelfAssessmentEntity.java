package com.aristowebapi.entity;

import javax.persistence.*;

import com.aristowebapi.dto.SelfAssessmentDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "self_assessment")
@Data
@NoArgsConstructor
public class SelfAssessmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ COMMON KEY
    @Column(name = "report_id")
    private Long reportId;

    private String selfRating;
    private String selfImprovementPlan;
	public SelfAssessmentEntity(SelfAssessmentDto selfAssessmentDto) {
		this.reportId = selfAssessmentDto.getReportId();
		this.selfRating = selfAssessmentDto.getSelfRating();
		this.selfImprovementPlan = selfAssessmentDto.getSelfImprovementPlan();
	}
    
    
}
