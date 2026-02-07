package com.aristowebapi.service;

import java.util.List;

import com.aristowebapi.dto.AbmDraftReportingDto;
import com.aristowebapi.request.AbmReportingDraftRequest;
import com.aristowebapi.response.FullReportResponse;

public interface AbmDraftReportService {
	
	FullReportResponse saveAbmDraftReport(AbmReportingDraftRequest abmReportingDraftRequest);
	String updateAbmDraftReport(String draftJson ) throws Exception;
	List<AbmDraftReportingDto> getByMonthAndYearAndLoginId(int month, int year,int loginId);
	List<AbmDraftReportingDto> getByDivCodeAndDepoCodeAndMnthCodeAndMyear(int divCode,int depoCode,int mnthCode, int myear);

	String getDraftJsonByDraftId(Long draftId);
	
}
