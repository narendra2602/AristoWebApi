package com.aristowebapi.service;

import java.util.Map;

import com.aristowebapi.entity.AuditInnerSheet;
import com.aristowebapi.request.AuditInnerSheetRequest;

public interface AuditInnerSheetService {

    AuditInnerSheet saveOrUpdate(AuditInnerSheetRequest request);
    
    
    Map<String, Object> getByReportIdAndInnerSheetId(
            Long auditReportId,
            Long auditInnerSheetId);
}