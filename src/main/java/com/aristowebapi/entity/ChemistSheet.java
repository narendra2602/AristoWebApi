package com.aristowebapi.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "abm_chemist_sheet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChemistSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sheetName;
    private String month;
    private String division;
    private String branch;
    private String hq;
    private String area;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private ChemistAuditReportFinal auditReport;

    @OneToMany(mappedBy = "sheet",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<ChemistBrand> brands = new ArrayList<>();

	
}
