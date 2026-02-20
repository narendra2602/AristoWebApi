package com.aristowebapi.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InitChemistAuditRequest {

    private String entryDate;
    private int divCode;
    private int depoCode;
    private int hq;
    private int monthCode;
    private int myear;
    private int month;
    private int loginId;

    // getters & setters
}