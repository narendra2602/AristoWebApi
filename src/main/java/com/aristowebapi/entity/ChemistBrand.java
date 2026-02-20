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
@Table(name = "abm_chemist_brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChemistBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brandName;
    private Integer potentialPerMonthStrips;
    private Integer ourSalesPerMonthStrips;

    @ManyToOne
    @JoinColumn(name = "sheet_id")
    private ChemistSheet sheet;

    @OneToMany(mappedBy = "brand",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<ChemistCompetitor> competitorDataList = new ArrayList<>();
}
