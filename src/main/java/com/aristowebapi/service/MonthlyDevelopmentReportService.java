package com.aristowebapi.service;

import java.util.List;

import com.aristowebapi.dto.DoctorPrescriptionDto;
import com.aristowebapi.dto.HeaderDto;
import com.aristowebapi.dto.MonthlyDevelopmentReportDto;
import com.aristowebapi.dto.MrDto;
import com.aristowebapi.dto.SelfAssessmentDto;
import com.aristowebapi.entity.MonthlyDevelopmentReportEntity;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.FullReportResponse;

public interface MonthlyDevelopmentReportService {
	
	MonthlyDevelopmentReportEntity saveFullReport(MonthlyDevelopmentReportDto report,
    		List<MrDto> mrs,List<DoctorPrescriptionDto> doctors,SelfAssessmentDto selfAssessment,
            HeaderDto header);
	
	FullReportResponse getFullReport(Long reportId);
	
	String saveFinalDraftReport(Long draftId) throws Exception;
	
	String deleteReportByReportId(Long reportId);
	
	List<FullReportResponse> getAllReportJson(int divCode,int  depoCode,int  mnthCode,int myear, int hqCode);
}
