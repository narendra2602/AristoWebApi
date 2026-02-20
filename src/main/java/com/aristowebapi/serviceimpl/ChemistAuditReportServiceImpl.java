package com.aristowebapi.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristowebapi.dao.AbmReportingDao;
import com.aristowebapi.dto.AbmReportingDto;
import com.aristowebapi.dto.PsrDto;
import com.aristowebapi.entity.ChemistAuditReport;
import com.aristowebapi.entity.ChemistAuditReportFinal;
import com.aristowebapi.entity.ChemistBrand;
import com.aristowebapi.entity.ChemistCompetitor;
import com.aristowebapi.entity.ChemistSheet;
import com.aristowebapi.repository.ChemistAuditReportRepository;
import com.aristowebapi.repository.ChemistAuditReportRepositoryFinal;
import com.aristowebapi.request.InitChemistAuditRequest;
import com.aristowebapi.response.AuditSheetResponse;
import com.aristowebapi.response.ChemistAuditMetaResponse;
import com.aristowebapi.response.ChemistAuditReportResponse;
import com.aristowebapi.service.ChemistAuditReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class ChemistAuditReportServiceImpl implements ChemistAuditReportService {

    @Autowired
    private ChemistAuditReportRepository repository;
    
    @Autowired
    private ChemistAuditReportRepositoryFinal finalrepository;
    
    @Autowired
	private AbmReportingDao abmReportingDao;


    @Autowired
    private ObjectMapper objectMapper;
    
    
    @Override
    @Transactional
    public ChemistAuditReport initReport(InitChemistAuditRequest request) {

        int mnthCode = abmReportingDao
                .getMonthCode(request.getMyear(), request.getMonthCode());

        List<AbmReportingDto> reportingList =
                abmReportingDao.getLine1Reporting(request.getLoginId());

        if (reportingList == null || reportingList.isEmpty()) {
            throw new RuntimeException("No reporting data found for loginId: "
                    + request.getLoginId());
        }

        AbmReportingDto abmReportingDto = reportingList.get(0);

        // ============================
        // 1️⃣ Save report FIRST
        // ============================
        ChemistAuditReport report = new ChemistAuditReport();

        report.setEntryDate(request.getEntryDate());
        report.setDivCode(request.getDivCode());
        report.setDepoCode(request.getDepoCode());
        report.setHq(request.getHq());
        report.setMonthCode(mnthCode);
        report.setMyear(request.getMyear());
        report.setMonth(request.getMonth());
        report.setEmpCode(abmReportingDto.getLine1_empcode());
        report.setLoginId(request.getLoginId());
        report.setAuditReportStatus("DRAFT");
        report.setAuditReportTitle(generateTitle(request));

        report.setAuditInnerSheetIds(generateDefaultSheetIds(request));

        // save once to get ID
        report = repository.save(report);

        // ============================
        // 2️⃣ Build JSON WITH ID
        // ============================
        AuditSheetResponse response = new AuditSheetResponse();

        response.setAuditReportTitle(report.getAuditReportTitle());
        response.setAutidReportId(report.getAuditReportId());
        response.setAuditInnerSheetId(
                report.getAuditInnerSheetIds().get(0)   // or null
        );
        response.setAuditReportStatus(report.getAuditReportStatus());
        response.setSheets(new ArrayList<>());

       
        String draftJson=null;
		try {
			draftJson = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // ============================
        // 3️⃣ Update JSON
        // ============================
        report.setDraftJson(draftJson);

        return repository.save(report);
    }
    @Override
    @Transactional(readOnly = true)
    public ChemistAuditMetaResponse getReportMeta(Long auditReportId,int loginId) {

        ChemistAuditReport report = repository.findById(auditReportId)
                .orElseThrow(() -> new RuntimeException("Audit report not found"));

        return mapToMetaResponse(report);
    }


    private String generateTitle(InitChemistAuditRequest request) {
        return "CHEMIST AUDIT REPORT - "
                + request.getMonthCode()
                + " ABM";
    }

    
    private List<Long> generateDefaultSheetIds(InitChemistAuditRequest request) {

        List<PsrDto> dataList = abmReportingDao.getPsrList(request.getLoginId());

        if (dataList == null || dataList.isEmpty()) {
            throw new RuntimeException("No PSR found for loginId");
        }

        return dataList.stream()
        		 .map(psr -> Long.valueOf(psr.getVal()))  // ✅ Correct conversion
                .collect(Collectors.toList());
    }
    
 /*   private List<Long> generateDefaultSheetIds(InitChemistAuditRequest request) {
    	List<PsrDto> dataList= abmReportingDao.getPsrList(request.getLoginId());
    	
        return Arrays.asList(1001L, 1002L, 1003L);
    }
*/
    private ChemistAuditMetaResponse mapToMetaResponse(ChemistAuditReport report) {

        ChemistAuditMetaResponse response = new ChemistAuditMetaResponse();

        response.setAuditReportTitle(report.getAuditReportTitle());
        response.setAuditReportId(report.getAuditReportId());
        response.setAuditInnerSheetIds(report.getAuditInnerSheetIds());
        response.setAuditReportStatus(report.getAuditReportStatus());
        response.setDivCode(report.getDivCode());
        response.setDepoCode(report.getDepoCode());
        response.setHq(report.getHq());
        response.setMonthCode(report.getMonthCode());
        response.setMyear(report.getMyear());
        response.setMonth(report.getMonth());

        return response;
    }
    
    
    
/*    @Override
    @Transactional(readOnly = true)
    public AuditSheetResponse getAuditSheet(Long reportId, Long sheetId) {

        ChemistAuditReport report = repository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        // Optional: validate sheetId belongs to report
        if (!report.getAuditInnerSheetIds().contains(sheetId)) {
            throw new RuntimeException("Sheet not found for this report");
        }

        // TODO: Fetch actual sheet data from DB
        return buildDummyResponse(report, sheetId);
    }*/
    
    
    
    @Override
    public void saveAuditJson(Long auditReportId,
                              AuditSheetResponse request) {

        ChemistAuditReport report = repository
                .findById(auditReportId)
                .orElseThrow(() ->
                        new RuntimeException("Audit Report not found"));

        try {

            // Convert DTO → JSON string
            String jsonString =
                    objectMapper.writeValueAsString(request);

            report.setDraftJson(jsonString);

            // optional update status
            report.setAuditReportStatus(
                    request.getAuditReportStatus());

            repository.save(report);

        } catch (Exception e) {
            throw new RuntimeException("Error saving JSON", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AuditSheetResponse getAuditJson(Long auditReportId,
                                            Long auditInnerSheetId) {

        ChemistAuditReport report = repository
                .findById(auditReportId)
                .orElseThrow(() ->
                        new RuntimeException("Audit Report not found"));

        try {

            // Convert JSON → DTO
            AuditSheetResponse response =
                    objectMapper.readValue(
                            report.getDraftJson(),
                            AuditSheetResponse.class);

            // Validate inner sheet id
            if (!auditInnerSheetId.equals(
                    response.getAuditInnerSheetId())) {

                throw new RuntimeException(
                        "Inner sheet not found for this report");
            }

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error reading JSON", e);
        }
    }
  
    // ================= SAVE final  =================

    @Transactional
    @Override
    public ChemistAuditReportResponse saveFinalAudit(JsonNode root, int loginId) {

        int reportId = root.get("autid_report_id").asInt();
        Optional<ChemistAuditReportFinal> existingOpt = finalrepository.findByReportId(reportId);

        ChemistAuditReportFinal auditReport;

        if (existingOpt.isPresent()) {
            auditReport = existingOpt.get();
        } else {
            auditReport = new ChemistAuditReportFinal();
            auditReport.setSheets(new ArrayList<>()); // initialize once for new entity
        }

        List<AbmReportingDto> reportingList = abmReportingDao.getLine1Reporting(loginId);

        AbmReportingDto abmReportingDto =
                reportingList != null && !reportingList.isEmpty()
                        ? reportingList.get(0)
                        : null;

        auditReport.setTitle(root.get("audit_report_title").asText());
        auditReport.setReportId(reportId);
//        auditReport.setStatus(root.get("audit_report_status").asText());
        auditReport.setCreatedBy(loginId);
        auditReport.setLine1EmpCode(abmReportingDto.getLine1_empcode());
        auditReport.setLine1EmpName(abmReportingDto.getLine1_empname());
        auditReport.setLine2EmpCode(abmReportingDto.getLine2_empcode());
        auditReport.setLine2EmpName(abmReportingDto.getLine2_empname());
        auditReport.setLine3EmpCode(abmReportingDto.getLine3_empcode());
        auditReport.setLine3EmpName(abmReportingDto.getLine3_empname());

        JsonNode sheetsNode = root.get("sheets");

        List<ChemistSheet> sheetList = new ArrayList<>();

        for (JsonNode sheetNode : sheetsNode) {

            ChemistSheet sheet = new ChemistSheet();
            sheet.setSheetName(sheetNode.get("sheet_name").asText());
            sheet.setMonth(sheetNode.get("month").asText());
            sheet.setDivision(sheetNode.get("Division").asText());
            sheet.setBranch(sheetNode.get("Branch").asText());
            sheet.setHq(sheetNode.get("HQ").asText());
            sheet.setArea(sheetNode.get("AREA").asText());

            sheet.setAuditReport(auditReport);

            List<ChemistBrand> brandList = new ArrayList<>();

            JsonNode aristoBrand = sheetNode.get("ARISTO Brand");
            Iterator<String> brandNames = aristoBrand.fieldNames();

            while (brandNames.hasNext()) {

                String brandName = brandNames.next();
                JsonNode brandNode = aristoBrand.get(brandName);

                ChemistBrand brand = new ChemistBrand();
                brand.setBrandName(brandName);
                brand.setPotentialPerMonthStrips(
                        brandNode.get("potential_per_month_strips").asInt());
                brand.setOurSalesPerMonthStrips(
                        brandNode.get("our_sales_per_month_strips").asInt());

                brand.setSheet(sheet);

                List<ChemistCompetitor> competitorList = new ArrayList<>();

                for (JsonNode dataNode : brandNode.get("data")) {

                    ChemistCompetitor cd = new ChemistCompetitor();

                    cd.setCompetitorTopSellingBrand(
                            dataNode.get("competitor_top_selling_brand").asText());

                    cd.setDoctor1Name(getText(dataNode, "top_prescribing_doctor_1_name"));
                    cd.setDoctor1Patients(getInt(dataNode, "top_prescribing_doctor_1_patients_per_day"));

                    cd.setDoctor2Name(getText(dataNode, "top_prescribing_doctor_2_name"));
                    cd.setDoctor2Patients(getInt(dataNode, "top_prescribing_doctor_2_patients_per_day"));

                    cd.setDoctor3Name(getText(dataNode, "top_prescribing_doctor_3_name"));
                    cd.setDoctor3Patients(getInt(dataNode, "top_prescribing_doctor_3_patients_per_day"));

                    cd.setBrand(brand);

                    competitorList.add(cd);
                }

                brand.setCompetitorDataList(competitorList);
                brandList.add(brand);
            }

            sheet.setBrands(brandList);
            sheetList.add(sheet);
        }

        // ✅ VERY IMPORTANT FIX
        if (auditReport.getSheets() == null) {
            auditReport.setSheets(new ArrayList<>());
        } else {
            auditReport.getSheets().clear();  // clear old children safely
        }

        auditReport.getSheets().addAll(sheetList);  // keep same collection reference

        finalrepository.save(auditReport);

        ChemistAuditReportResponse response = new ChemistAuditReportResponse();
        response.setMessage("Record Created Successfully");
        response.setStatus("Draft");
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    private String getText(JsonNode node, String fieldName) {
        JsonNode value = node.get(fieldName);
        return (value != null && !value.isNull()) ? value.asText() : null;
    }

    private Integer getInt(JsonNode node, String fieldName) {
        JsonNode value = node.get(fieldName);
        return (value != null && !value.isNull()) ? value.asInt() : null;
    }
}