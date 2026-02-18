package com.aristowebapi.entity;

import java.util.List;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "abm_chemist_audit_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChemistAuditReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer reportId;
    private String status;

	private int reportMonth;
	private int reportYear;
	private int createdBy;
	private int divCode;
	private int depoCode;
	private int hqCode;
    private int line1EmpCode;
    private String  line1EmpName;
    private int line2EmpCode;
    private String  line2EmpName;
    private int line3EmpCode;
    private String  line3EmpName;

    
    
    @OneToMany(mappedBy = "auditReport", cascade = CascadeType.ALL)
    private List<ChemistSheet> sheets;
}
