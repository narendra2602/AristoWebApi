package com.aristowebapi.serviceimpl;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristowebapi.entity.AuditInnerSheet;
import com.aristowebapi.repository.AuditInnerSheetRepository;
import com.aristowebapi.request.AuditInnerSheetRequest;
import com.aristowebapi.service.AuditInnerSheetService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class AuditInnerSheetServiceImpl implements AuditInnerSheetService {

    private final AuditInnerSheetRepository repository;
    private final ObjectMapper objectMapper;

    public AuditInnerSheetServiceImpl(AuditInnerSheetRepository repository,
                                      ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public AuditInnerSheet saveOrUpdate(AuditInnerSheetRequest request) {

        try {
            Optional<AuditInnerSheet> optional =
                    repository.findByAuditInnerSheetId(request.getAuditInnerSheetId());

            AuditInnerSheet entity;

            if (optional.isPresent()) {
                // ✅ UPDATE
                entity = optional.get();
            } else {
                // ✅ INSERT
                entity = new AuditInnerSheet();
                entity.setAuditInnerSheetId(request.getAuditInnerSheetId());
            }

            entity.setAuditReportId(request.getAuditReportId());
            entity.setAuditReportStatus(request.getAuditReportStatus());

            // Convert full request JSON to string
            String jsonString = objectMapper.writeValueAsString(request);
            entity.setJsonData(jsonString);

            return repository.save(entity);

        } catch (Exception e) {
            throw new RuntimeException("Error while saving/updating audit sheet", e);
        }
    }
    
    
    @Override
    public Map<String, Object> getByReportIdAndInnerSheetId(
            Long auditReportId,
            Long auditInnerSheetId) {

        AuditInnerSheet entity = repository
                .findByAuditReportIdAndAuditInnerSheetId(
                        auditReportId, auditInnerSheetId)
                .orElseThrow(() ->
                        new RuntimeException("Record not found"));

        try {

            // Convert JSON string to Map
            Map<String, Object> originalMap =
                    objectMapper.readValue(
                            entity.getJsonData(),
                            Map.class);

            // Create ordered map
            Map<String, Object> orderedMap = new java.util.LinkedHashMap<>();

            // Put TOP fields first
            orderedMap.put("abm_draft_id", originalMap.get("abm_draft_id"));
            orderedMap.put("audit_report_id", originalMap.get("audit_report_id"));
            orderedMap.put("audit_report_title", originalMap.get("audit_report_title"));
            orderedMap.put("audit_report_status", originalMap.get("audit_report_status"));
            orderedMap.put("audit_inner_sheet_id", originalMap.get("audit_inner_sheet_id"));

            // Put sheets at BOTTOM
            orderedMap.put("sheets", originalMap.get("sheets"));

            return orderedMap;

        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }
}