package com.aristowebapi.entity;

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
@Table(name = "abm_chemist_competitor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ChemistCompetitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String competitorTopSellingBrand;

    private String doctor1Name;
    private Integer doctor1Patients;

    private String doctor2Name;
    private Integer doctor2Patients;

    private String doctor3Name;
    private Integer doctor3Patients;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private ChemistBrand brand;
}
