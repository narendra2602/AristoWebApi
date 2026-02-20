package com.aristowebapi.service;

import com.aristowebapi.response.ChemistAuditReportResponse;
import com.fasterxml.jackson.databind.JsonNode;

public interface ChemistAuditService {

	
//	ApiResponse<ChemistAuditReportResponse> saveAudit(JsonNode jsonNode, int loginId);
    JsonNode getAuditById(Long id);

	ChemistAuditReportResponse saveFinalAudit(JsonNode root, int loginId);
}
