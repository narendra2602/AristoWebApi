package com.aristowebapi.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristowebapi.service.ChemistAuditReportService;
import com.aristowebapi.service.MonthlyDevelopmentReportService;

@Service
public class FinalizationService {

    @Autowired
    private ChemistAuditReportService chemistAuditReportService;
    
    @Autowired
    private MonthlyDevelopmentReportService  reportService;

    @Transactional(rollbackFor = Exception.class)
    public void finalizeAll(
            Long auditReportId,
            List<Long> psrCodes,
            int loginId,
            Long draftId) throws Exception {

        // 1️⃣ finalize all PSRs
        for (Long psrCode : psrCodes) {

            chemistAuditReportService
                    .saveFinalAudit(auditReportId, psrCode, loginId);
        }

        // 2️⃣ finalize report once
        reportService.saveFinalDraftReport(draftId);

        // if anything fails -> rollback everything
    }
}