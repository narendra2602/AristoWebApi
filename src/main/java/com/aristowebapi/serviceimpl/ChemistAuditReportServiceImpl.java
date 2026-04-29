package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aristowebapi.dao.AbmReportingDao;
import com.aristowebapi.dto.AbmReportingDto;
import com.aristowebapi.dto.ChemistAuditFinalRequestDto;
import com.aristowebapi.dto.ChemistBrandDto;
import com.aristowebapi.dto.ChemistCompetitorDto;
import com.aristowebapi.dto.ChemistSheetDto;
import com.aristowebapi.dto.PsrDto;
import com.aristowebapi.entity.AuditInnerSheet;
import com.aristowebapi.entity.ChemistAuditReport;
import com.aristowebapi.entity.ChemistAuditReportFinal;
import com.aristowebapi.entity.ChemistBrand;
import com.aristowebapi.entity.ChemistCompetitor;
import com.aristowebapi.entity.ChemistSheet;
import com.aristowebapi.repository.AuditInnerSheetRepository;
import com.aristowebapi.repository.ChemistAuditReportRepository;
import com.aristowebapi.repository.ChemistAuditReportRepositoryFinal;
import com.aristowebapi.repository.ChemistBrandRepository;
import com.aristowebapi.repository.ChemistCompetitorRepository;
import com.aristowebapi.repository.ChemistSheetRepository;
import com.aristowebapi.request.InitChemistAuditRequest;
import com.aristowebapi.response.AuditSheetResponse;
import com.aristowebapi.response.ChemistAuditMetaResponse;
import com.aristowebapi.service.ChemistAuditReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class ChemistAuditReportServiceImpl implements ChemistAuditReportService {

	@Autowired
	private ChemistAuditReportService chemistAuditReportService;
	
	
    @Autowired
    private ChemistAuditReportRepository repository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    
    @Autowired
    private ChemistCompetitorRepository competitorRepository;
    
    @Autowired
    private ChemistSheetRepository sheetRepository;
    
    
    @Autowired
    private ChemistBrandRepository brandRepository;
 
    
    @Autowired
    private ChemistAuditReportRepositoryFinal finalrepository;
    
    @Autowired
	private AbmReportingDao abmReportingDao;

    @Autowired
    private AuditInnerSheetRepository auditInnerSheetRepository;
    

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
        response.setAuditReportId(report.getAuditReportId());
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
    public ChemistAuditMetaResponse getReportMeta(Long auditReportId,int loginId,Long psrCode) {

        ChemistAuditReport report = repository.findByAuditReportId(auditReportId)
                .orElseThrow(() -> new RuntimeException("Chemist Audit report not found"));

        return mapToMetaResponse(report,psrCode);
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
    private ChemistAuditMetaResponse mapToMetaResponse(
            ChemistAuditReport report,
            Long psrCode) {

        ChemistAuditMetaResponse response = new ChemistAuditMetaResponse();

        response.setAuditReportTitle(report.getAuditReportTitle());
        response.setAuditReportId(report.getAuditReportId());
        response.setAbmDraftId(report.getAbmDraftId());
        response.setDivCode(report.getDivCode());
        response.setDepoCode(report.getDepoCode());
        response.setHq(report.getHq());
        response.setMonthCode(report.getMonthCode());
        response.setMyear(report.getMyear());
        response.setMonth(report.getMonth());
        response.setPsrCode(psrCode);
        response.setCreateBy(report.getLoginId());
        // 🔹 Get inner sheet IDs filtered by PSR
        List<Long> filteredIds =
                auditInnerSheetRepository
                        .findInnerSheetIdsByAuditReportIdAndPsrCode(
                                report.getAuditReportId(),
                                psrCode);

        response.setAuditInnerSheetIds(filteredIds);

        // 🔹 Get all inner sheets
        List<AuditInnerSheet> innerSheets =
        	    auditInnerSheetRepository
        	        .findAllByAuditReportIdAndPsrCode(
        	            report.getAuditReportId(),
        	            psrCode);
        
        // 🔹 Determine status from inner sheets
        String finalStatus = "DRAFT";

        if (innerSheets != null && !innerSheets.isEmpty()) {

            boolean allFinal = innerSheets.stream()
                    .allMatch(sheet ->
                            "FINAL".equalsIgnoreCase(sheet.getAuditReportStatus()));

            if (allFinal) {
                finalStatus = "FINAL";
            }
        }

        response.setAuditReportStatus(finalStatus);

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
                .findByAuditReportId(auditReportId)
                .orElseThrow(() ->
                        new RuntimeException("Audit Report not found"));
        System.out.println("id is ****"+report.getAuditReportId());

        try {

            // Convert DTO → JSON string
            String jsonString =
                    objectMapper.writeValueAsString(request);

            report.setDraftJson(jsonString);
            System.out.println(report.getDraftJson());
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
    public AuditSheetResponse getAuditJson(Long auditReportId,Long auditInnerSheetId) {

    	
        ChemistAuditReport report = repository
                .findByAuditReportId(auditReportId)
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public String saveFinalAudit(Long auditReportId, Long psrCode, int loginId) throws Exception {

     	
        // 1️⃣ Get draft sheets
    	
    	List<AuditInnerSheet> draftSheets =
    	        auditInnerSheetRepository
    	                .findAllByAuditReportIdAndPsrCodeAndAuditReportStatus(
    	                        auditReportId, psrCode, "DRAFT");

    	if (draftSheets.isEmpty()) {
     	        throw new RuntimeException("No DRAFT records found"); // ✅ keep API behavior
    	}
    	
 
        // 2️⃣ Get main audit report
        ChemistAuditReport mainReport = repository.findByAuditReportId(auditReportId)
                .orElseThrow(() -> new RuntimeException("Audit Report not found"));
        
        
 
        ObjectMapper objectMapper = new ObjectMapper();

        // 3️⃣ Get reporting hierarchy
        List<AbmReportingDto> reportingList = abmReportingDao.getLine1Reporting(loginId);

        AbmReportingDto abmReportingDto = null;
        if (reportingList != null && !reportingList.isEmpty()) {
            abmReportingDto = reportingList.get(0);
        }

        // 4️⃣ Find existing final report
        Optional<ChemistAuditReportFinal> existing =
                finalrepository.findByReportId(auditReportId);

        ChemistAuditReportFinal auditReport;

        
        
        if (existing.isPresent()) {

            auditReport = existing.get();
             
        } else {

            auditReport = new ChemistAuditReportFinal();

            auditReport.setTitle("Audit Report");
            auditReport.setReportId(auditReportId);
            auditReport.setCreatedBy(loginId);

            if (abmReportingDto != null) {

                auditReport.setLine1Name(abmReportingDto.getLine1_name());
                auditReport.setLine1EmpCode(abmReportingDto.getLine1_empcode());
                auditReport.setLine1EmpName(abmReportingDto.getLine1_empname());

                auditReport.setLine2EmpCode(abmReportingDto.getLine2_empcode());
                auditReport.setLine2EmpName(abmReportingDto.getLine2_empname());

                auditReport.setLine3EmpCode(abmReportingDto.getLine3_empcode());
                auditReport.setLine3EmpName(abmReportingDto.getLine3_empname());
            }

            auditReport.setDivCode(mainReport.getDivCode());
            auditReport.setDepoCode(mainReport.getDepoCode());
            auditReport.setHqCode(mainReport.getHq());

            auditReport.setReportMonth(mainReport.getMonthCode());
            auditReport.setReportYear(mainReport.getMyear());

            auditReport.setSheets(new ArrayList<>());
        }

        // 5️⃣ Prevent duplicate PSR finalization
        
 
         
        boolean psrExists = auditReport.getSheets()
                .stream()
                .anyMatch(s -> s.getPsrId() != null && s.getPsrId().equals(psrCode));


      
        // 6️⃣ Convert draft JSON → sheets
        for (AuditInnerSheet draft : draftSheets) {

            ChemistAuditFinalRequestDto request =
                    objectMapper.readValue(
                            draft.getJsonData(),
                            ChemistAuditFinalRequestDto.class);

            for (ChemistSheetDto sheetDto : request.getSheets()) {

                ChemistSheet sheet = new ChemistSheet();

                sheet.setSheetName(sheetDto.getSheetName());
                sheet.setMonth(sheetDto.getMonth());
                sheet.setDivision(sheetDto.getDivision());
                sheet.setBranch(sheetDto.getBranch());
                sheet.setHq(sheetDto.getHq());
                sheet.setArea(sheetDto.getArea());
                sheet.setChemistName(sheetDto.getChemistName());

                sheet.setPsrId(sheetDto.getPsrId());
                sheet.setPsrName(sheetDto.getPsrName());

                sheet.setSheetStatus("FINAL");

                // 🔗 Important relationship
                sheet.setAuditReport(auditReport);
                
                if (sheetDto.getBrands() != null) {
                    for (Map.Entry<String, ChemistBrandDto> entry : sheetDto.getBrands().entrySet()) {

                        String brandName = entry.getKey();          // ✅ key = brand name
                        ChemistBrandDto brandDto = entry.getValue(); // ✅ value = data

                        ChemistBrand brand = new ChemistBrand();

                        brand.setBrandName(brandName); // 🔥 IMPORTANT (comes from key)
                        brand.setPotentialPerMonthStrips(brandDto.getPotentialPerMonthStrips());
                        brand.setOurSalesPerMonthStrips(brandDto.getOurSalesPerMonthStrips());

                        brand.setSheet(sheet);

                        // 👉 competitors
                        if (brandDto.getData() != null) {
                            for (ChemistCompetitorDto compDto : brandDto.getData()) {

                                ChemistCompetitor comp = new ChemistCompetitor();

                                comp.setCompetitorTopSellingBrand(compDto.getCompetitorTopSellingBrand());

                                comp.setDoctor1Name(compDto.getDoctor1Name());
                                comp.setDoctor1Patients(compDto.getDoctor1Patients());

                                comp.setDoctor2Name(compDto.getDoctor2Name());
                                comp.setDoctor2Patients(compDto.getDoctor2Patients());

                                comp.setDoctor3Name(compDto.getDoctor3Name());
                                comp.setDoctor3Patients(compDto.getDoctor3Patients());

                                comp.setBrand(brand);

                                brand.getCompetitorDataList().add(comp);
                            }
                        }

                        sheet.getBrands().add(brand);
                    }
                }
                auditReport.getSheets().add(sheet);
            }
        }

        // 7️⃣ Save parent (cascade will save sheets)
        finalrepository.save(auditReport);
        
        // 8️⃣ Update draft status
        for (AuditInnerSheet sheet : draftSheets) {
            sheet.setAuditReportStatus("FINAL");
        }

        auditInnerSheetRepository.saveAll(draftSheets);

        return "PSR finalized successfully";
    } 
    
    
    

    public String regenerateAllFinalReports(int monthCode, int divCode, long reportId) throws Exception {

/*        List<AuditInnerSheet> finalSheets =
                auditInnerSheetRepository.findFilteredFinalSheets(
                        "FINAL", monthCode, divCode);
        List<AuditInnerSheet> finalSheets =
                auditInnerSheetRepository.findFilteredFinalSheets(
                        "DRAFT", monthCode, divCode,reportId);
       */
    	
    	System.out.println("month code "+monthCode+" div "+divCode);
        List<AuditInnerSheet> finalSheets =
                auditInnerSheetRepository.findFilteredFinalSheets(divCode,monthCode);
        
        
        
        if (finalSheets.isEmpty()) {
            return "No FINAL records found";
        }

        // group by parent report
        Map<Long, List<AuditInnerSheet>> groupedByReport =
                finalSheets.stream()
                        .collect(Collectors.groupingBy(AuditInnerSheet::getAuditReportId));

        for (Map.Entry<Long, List<AuditInnerSheet>> entry : groupedByReport.entrySet()) {

            try {
                processSingleReport(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                System.out.println("❌ Error reportId " + entry.getKey() + ": " + e.getMessage());
                entityManager.clear(); // reset broken session
            }
        }

        return "Regeneration completed successfully";
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processSingleReport(Long reportId, List<AuditInnerSheet> reportRows) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        // 1️⃣ Load parent report
        ChemistAuditReport mainReport =
                repository.findByAuditReportId(reportId)
                        .orElseThrow(() -> new RuntimeException("Parent not found: " + reportId));

        int loginId = mainReport.getLoginId();

        Integer abmId = abmReportingDao.getId(loginId);

        if (abmId == null) {
            throw new RuntimeException("ABM not found for loginId "+loginId);
        }

        
        // 2️⃣ Get reporting hierarchy (IMPORTANT)
        List<AbmReportingDto> reportingList = abmReportingDao.getLine1Reporting(abmId);

        if (reportingList == null || reportingList.isEmpty()) {
            throw new RuntimeException("Reporting not found for loginId " + loginId);
        }

        AbmReportingDto dto = reportingList.get(0);

        Integer empCode = dto.getLine1_empcode();
        Integer month = mainReport.getMonthCode();

        if (empCode == null || empCode == 0) {
            throw new RuntimeException("Invalid empCode for reportId " + reportId);
        }

        // 3️⃣ FIND EXISTING using UNIQUE constraint fields ✅
        Optional<ChemistAuditReportFinal> existing =
                finalrepository.findByLine1EmpCodeAndReportMonth(empCode, month);

        ChemistAuditReportFinal finalReport;

        if (existing.isPresent()) {
            finalReport = existing.get();
            finalReport.getSheets().clear(); // ✅ important (orphanRemoval will delete old data)
        } else {
            finalReport = new ChemistAuditReportFinal();

            finalReport.setTitle("Audit Report");
            finalReport.setReportId(reportId);
            finalReport.setCreatedBy(loginId);

            finalReport.setDivCode(mainReport.getDivCode());
            finalReport.setDepoCode(mainReport.getDepoCode());
            finalReport.setHqCode(mainReport.getHq());

            finalReport.setReportMonth(mainReport.getMonthCode());
            finalReport.setReportYear(mainReport.getMyear());

            // 👇 reporting data
            finalReport.setLine1Name(dto.getLine1_name());
            finalReport.setLine1EmpCode(dto.getLine1_empcode());
            finalReport.setLine1EmpName(dto.getLine1_empname());

            finalReport.setLine2EmpCode(dto.getLine2_empcode());
            finalReport.setLine2EmpName(dto.getLine2_empname());

            finalReport.setLine3EmpCode(dto.getLine3_empcode());
            finalReport.setLine3EmpName(dto.getLine3_empname());
        }

        // 4️⃣ Build sheets
        for (AuditInnerSheet draft : reportRows) {

            ChemistAuditFinalRequestDto request =
                    objectMapper.readValue(draft.getJsonData(),
                            ChemistAuditFinalRequestDto.class);

            for (ChemistSheetDto sheetDto : request.getSheets()) {

                ChemistSheet sheet = new ChemistSheet();

                sheet.setSheetName(sheetDto.getSheetName());
                sheet.setMonth(sheetDto.getMonth());
                sheet.setDivision(sheetDto.getDivision());
                sheet.setBranch(sheetDto.getBranch());
                sheet.setHq(sheetDto.getHq());
                sheet.setArea(sheetDto.getArea());
                sheet.setChemistName(sheetDto.getChemistName());

                sheet.setPsrId(sheetDto.getPsrId());
                sheet.setPsrName(sheetDto.getPsrName());
                sheet.setSheetStatus("FINAL");

                sheet.setAuditReport(finalReport);

                // brands
                if (sheetDto.getBrands() != null) {
                    for (Map.Entry<String, ChemistBrandDto> b :
                            sheetDto.getBrands().entrySet()) {

                        ChemistBrandDto brandDto = b.getValue();

                        ChemistBrand brand = new ChemistBrand();
                        brand.setBrandName(b.getKey());
                        brand.setPotentialPerMonthStrips(brandDto.getPotentialPerMonthStrips());
                        brand.setOurSalesPerMonthStrips(brandDto.getOurSalesPerMonthStrips());

                        brand.setSheet(sheet);

                        if (brandDto.getData() != null) {
                            for (ChemistCompetitorDto c : brandDto.getData()) {

                                ChemistCompetitor comp = new ChemistCompetitor();
                                comp.setCompetitorTopSellingBrand(c.getCompetitorTopSellingBrand());

                                comp.setDoctor1Name(c.getDoctor1Name());
                                comp.setDoctor1Patients(c.getDoctor1Patients());
                                comp.setDoctor2Name(c.getDoctor2Name());
                                comp.setDoctor2Patients(c.getDoctor2Patients());
                                comp.setDoctor3Name(c.getDoctor3Name());
                                comp.setDoctor3Patients(c.getDoctor3Patients());

                                comp.setBrand(brand);
                                brand.getCompetitorDataList().add(comp);
                            }
                        }

                        sheet.getBrands().add(brand);
                    }
                }

                finalReport.getSheets().add(sheet);
            }
        }

        // 5️⃣ SAVE
        finalrepository.save(finalReport);
        finalrepository.flush(); // ✅ catch DB errors here

        // 6️⃣ Update status
        for (AuditInnerSheet s : reportRows) {
            s.setAuditReportStatus("FINAL");
        }

        auditInnerSheetRepository.saveAll(reportRows);
    }
    
    
     
    @Override
    @Transactional
    public void deleteByReportId(Long reportId) {

        ChemistAuditReportFinal report1 =
                finalrepository.findByReportId(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        // delete final report (cascade deletes sheets)
        finalrepository.delete(report1);

        // update audit inner sheet status
        List<AuditInnerSheet> sheets =
                auditInnerSheetRepository.findByAuditReportId(reportId);

        for (AuditInnerSheet sheet : sheets) {
            sheet.setAuditReportStatus("DRAFT");
        }

        auditInnerSheetRepository.saveAll(sheets);
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