package com.aristowebapi.service;

import com.aristowebapi.response.ChemistAuditReportResponse;
import com.fasterxml.jackson.databind.JsonNode;

public interface ChemistAuditService {

	ChemistAuditReportResponse saveAudit(JsonNode jsonNode, int loginId);
//	ApiResponse<ChemistAuditReportResponse> saveAudit(JsonNode jsonNode, int loginId);
    JsonNode getAuditById(Long id);
}
