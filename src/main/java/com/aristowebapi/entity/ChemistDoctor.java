package com.aristowebapi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "abm_chemist_doctor")
public class ChemistDoctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer priority;
    private String doctorName;
    private Integer patientsPerDay;

    @ManyToOne
    @JoinColumn(name = "competitor_id")
    @JsonIgnore
    private ChemistCompetitor competitor;

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Integer getPatientsPerDay() {
        return patientsPerDay;
    }

    public void setPatientsPerDay(Integer patientsPerDay) {
        this.patientsPerDay = patientsPerDay;
    }

    public ChemistCompetitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(ChemistCompetitor competitor) {
        this.competitor = competitor;
    }
}
