package com.aristowebapi.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "abm_chemist_audit_reportfinal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChemistAuditReportFinal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer reportId;
   
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

    
    
    
    @OneToMany(mappedBy = "auditReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
 private List<ChemistSheet> sheets = new ArrayList<>();
    
}
