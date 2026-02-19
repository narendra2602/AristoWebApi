package com.aristowebapi.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.aristowebapi.dao.AbmReportingDao;
import com.aristowebapi.dto.AbmReportingDto;
import com.aristowebapi.entity.ChemistAuditReport;
import com.aristowebapi.entity.ChemistBrand;
import com.aristowebapi.entity.ChemistCompetitor;
import com.aristowebapi.entity.ChemistSheet;
import com.aristowebapi.repository.ChemistAuditReportRepository;
import com.aristowebapi.response.ChemistAuditReportResponse;
import com.aristowebapi.service.ChemistAuditService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
@Transactional
public class ChemistAuditServiceImpl implements ChemistAuditService {

	

	
	private final ChemistAuditReportRepository repository;
    private final ObjectMapper objectMapper;
    private final AbmReportingDao abmReportingDao;
    
    public ChemistAuditServiceImpl(ChemistAuditReportRepository repository,
                                  ObjectMapper objectMapper, AbmReportingDao abmReportingDao) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.abmReportingDao=abmReportingDao;
    }

    // ================= SAVE =================

    @Transactional
    @Override
    public ChemistAuditReportResponse saveAudit(JsonNode root, int loginId) {

        int reportId = root.get("autid_report_id").asInt();
        Optional<ChemistAuditReport> existingOpt = repository.findByReportId(reportId);

        ChemistAuditReport auditReport;

        if (existingOpt.isPresent()) {
            auditReport = existingOpt.get();
        } else {
            auditReport = new ChemistAuditReport();
            auditReport.setSheets(new ArrayList<>()); // initialize once for new entity
        }

        List<AbmReportingDto> reportingList = abmReportingDao.getLine1Reporting(loginId);

        AbmReportingDto abmReportingDto =
                reportingList != null && !reportingList.isEmpty()
                        ? reportingList.get(0)
                        : null;

        auditReport.setTitle(root.get("audit_report_title").asText());
        auditReport.setReportId(reportId);
        auditReport.setStatus(root.get("audit_report_status").asText());
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

        repository.save(auditReport);

        ChemistAuditReportResponse response = new ChemistAuditReportResponse();
        response.setMessage("Record Created Successfully");
        response.setStatus("Draft");
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    // ================= GET =================

    @Override
    public JsonNode getAuditById(Long id) {

        ChemistAuditReport report = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        ObjectNode root = objectMapper.createObjectNode();

        root.put("audit_report_title", report.getTitle());
        root.put("autid_report_id", report.getReportId());
        root.put("audit_report_status", report.getStatus());

        ArrayNode sheetArray = objectMapper.createArrayNode();

        for (ChemistSheet sheet : report.getSheets()) {

            ObjectNode sheetNode = objectMapper.createObjectNode();

            sheetNode.put("sheet_name", sheet.getSheetName());
            sheetNode.put("month", sheet.getMonth());
            sheetNode.put("Division", sheet.getDivision());
            sheetNode.put("Branch", sheet.getBranch());
            sheetNode.put("HQ", sheet.getHq());
            sheetNode.put("AREA", sheet.getArea());

            ObjectNode aristoBrandNode = objectMapper.createObjectNode();

            for (ChemistBrand brand : sheet.getBrands()) {

                ObjectNode brandNode = objectMapper.createObjectNode();

                brandNode.put("potential_per_month_strips",
                        brand.getPotentialPerMonthStrips());

                brandNode.put("our_sales_per_month_strips",
                        brand.getOurSalesPerMonthStrips());

                ArrayNode dataArray = objectMapper.createArrayNode();

                for (ChemistCompetitor cd : brand.getCompetitorDataList()) {

                    ObjectNode dataNode = objectMapper.createObjectNode();

                    dataNode.put("competitor_top_selling_brand",
                            cd.getCompetitorTopSellingBrand());

                    dataNode.put("top_prescribing_doctor_1_name",
                            cd.getDoctor1Name());
                    dataNode.put("top_prescribing_doctor_1_patients_per_day",
                            cd.getDoctor1Patients());

                    dataNode.put("top_prescribing_doctor_2_name",
                            cd.getDoctor2Name());
                    dataNode.put("top_prescribing_doctor_2_patients_per_day",
                            cd.getDoctor2Patients());

                    dataNode.put("top_prescribing_doctor_3_name",
                            cd.getDoctor3Name());
                    dataNode.put("top_prescribing_doctor_3_patients_per_day",
                            cd.getDoctor3Patients());

                    dataArray.add(dataNode);
                }

                brandNode.set("data", dataArray);
                aristoBrandNode.set(brand.getBrandName(), brandNode);
            }

            sheetNode.set("ARISTO Brand", aristoBrandNode);
            sheetArray.add(sheetNode);
        }

        root.set("sheets", sheetArray);

        return root;
    }

    private String getText(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull()
                ? node.get(field).asText()
                : null;
    }

    private Integer getInt(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull()
                ? node.get(field).asInt()
                : null;
    }
}
