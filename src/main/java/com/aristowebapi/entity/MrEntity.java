package com.aristowebapi.entity;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.aristowebapi.dto.MrDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "abm_mr")
@Data
@NoArgsConstructor
public class MrEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ COMMON KEY (Global Report Linking)
    @Column(name = "report_id")
    private Long reportId;

    // ✅ MR BUSINESS ID (From JSON → id field)
    @Column(name = "mr_id")
    private Long mrId;

    private String name;
    private String hq;

    @Column(name = "communication_rating")
    private String communicationRating;

    // ✅ MR PRIORITIES
    @ElementCollection
    @CollectionTable(
            name = "abm_mr_priorities",
            joinColumns = @JoinColumn(name = "mr_pk_id")
    )
    @Column(name = "priority")
    private List<String> priorities;

    // ✅ MR ADVICE
    @ElementCollection
    @CollectionTable(
            name = "abm_mr_advice",
            joinColumns = @JoinColumn(name = "mr_pk_id")
    )
    @Column(name = "advice")
    private List<String> advice;

    // ✅ JPA Relation → REPORT (READ ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", insertable = false, updatable = false)
    private MonthlyDevelopmentReportEntity report;

    private String monthTarget;
    
    private String secondarySalesAsOn30;
    private String closingStockAsOn30;
    private String expectedSalesNextMonth;
	public MrEntity(MrDto mrDto) {

		this.reportId = mrDto.getReportId();
		this.mrId = mrDto.getId();
		this.name = mrDto.getName();
		this.hq = mrDto.getHq();
		this.communicationRating = mrDto.getCommunicationRating();
		this.priorities = mrDto.getPriorities();
		this.advice = mrDto.getAdvice();
//		this.report = report;
		this.monthTarget = mrDto.getMonthTarget();
		this.secondarySalesAsOn30 = mrDto.getSecondarySalesAsOn30();
		this.closingStockAsOn30=mrDto.getClosingStockAsOn30();
		this.expectedSalesNextMonth = mrDto.getExpectedSalesNextMonth();
	}
    
    

}
