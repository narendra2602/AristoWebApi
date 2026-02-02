package com.aristowebapi.service;

import com.aristowebapi.request.AbmReportingDraftRequest;
import com.aristowebapi.request.MonthlyReportRequest;
import com.aristowebapi.response.FullReportResponse;

public interface AbmDraftReportService {
	
	FullReportResponse saveAbmDraftReport(AbmReportingDraftRequest abmReportingDraftRequest);
	String updateAbmDraftReport(String draftJson ) throws Exception;
    		
}
