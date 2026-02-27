package com.aristowebapi.serviceimpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristowebapi.dao.AbmReportingDao;
import com.aristowebapi.dto.AbmDraftReportingDto;
import com.aristowebapi.dto.AbmReportingDto;
import com.aristowebapi.dto.HeaderDto;
import com.aristowebapi.dto.MonthlyDevelopmentReportDto;
import com.aristowebapi.dto.SelfAssessmentDto;
import com.aristowebapi.entity.AbmDraftReportEntity;
import com.aristowebapi.entity.ChemistAuditReport;
import com.aristowebapi.repository.AbmDraftReportRepository;
import com.aristowebapi.repository.ChemistAuditReportRepository;
import com.aristowebapi.request.AbmReportingDraftRequest;
import com.aristowebapi.request.InitChemistAuditRequest;
import com.aristowebapi.response.AuditSheetResponse;
import com.aristowebapi.response.FullReportResponse;
import com.aristowebapi.service.AbmDraftReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AbmDraftReportServiceImpl implements AbmDraftReportService {

    private final ObjectMapper objectMapper;
    private final AbmDraftReportRepository reportRepository;
    private final AbmReportingDao abmReportingDao;
    private final ChemistAuditReportRepository chemistRepository;

    // =====================================================
    // SAVE/INITIATE DRAFT
    // =====================================================
    @Transactional
    @Override
    public FullReportResponse saveAbmDraftReport(
            AbmReportingDraftRequest abmReportingDraftRequest) {

        int mnthCode = abmReportingDao.getMonthCode(
                abmReportingDraftRequest.getMyear(),
                abmReportingDraftRequest.getMnthCode());

    	
    	boolean exists = reportRepository
                .existsByLoginIdAndMnthCodeAndMyear(
                		abmReportingDraftRequest.getLoginId(),
                        mnthCode,
                        abmReportingDraftRequest.getMyear()
                );

        if (exists) {
            return null; // NO exception → NO rollback
        }
    	
 
 
        abmReportingDraftRequest.setMnthCode(mnthCode);

        List<AbmReportingDto> reportingList =
                abmReportingDao.getLine1Reporting(abmReportingDraftRequest.getLoginId());

        AbmReportingDto abmReportingDto =
                reportingList != null && !reportingList.isEmpty()
                        ? reportingList.get(0)
                        : null;

        HeaderDto header = new HeaderDto();
        if (abmReportingDto != null) {
            header.setFromName(abmReportingDto.getLine1_empname());
            header.setDesignation(abmReportingDto.getLine1_desg());
            header.setHq(abmReportingDto.getLine1_name());
            header.setTo(abmReportingDto.getLine2_desg() + " :" + abmReportingDto.getLine2_empname());
            header.setCc(abmReportingDto.getLine3_desg() + " :" + abmReportingDto.getLine3_empname());
            header.setDate(abmReportingDraftRequest.getEntryDate());
        }

        MonthlyDevelopmentReportDto reportDto = new MonthlyDevelopmentReportDto();
        reportDto.setCompany("Aristo");
        reportDto.setMonth(mnthCode);
        reportDto.setReportTitle("ABM");
        reportDto.setYear(abmReportingDraftRequest.getMyear());
        reportDto.setCreatedBy(abmReportingDraftRequest.getLoginId());
        reportDto.setDivCode(abmReportingDraftRequest.getDivCode());
        reportDto.setDepoCode(abmReportingDraftRequest.getDepoCode());
        reportDto.setHqCode(abmReportingDraftRequest.getHqCode());

        FullReportResponse response = new FullReportResponse();
        response.setAbmDraftStatus("Draft");
        response.setReport(reportDto);
        response.setDoctors(new ArrayList<>());
        response.setMrs(new ArrayList<>());
        response.setHeader(header);
        response.setSelfAssessment(new SelfAssessmentDto());

        try {
            String jsonString = null;

            AbmDraftReportEntity entity = new AbmDraftReportEntity();
            entity.setDraftJson(jsonString);
            entity.setDraftStatus("Draft");
            entity.setEntryDate(abmReportingDraftRequest.getEntryDate());
            entity.setLoginId(abmReportingDraftRequest.getLoginId());
            entity.setMnthCode(mnthCode);
            entity.setMyear(abmReportingDraftRequest.getMyear());
            entity.setDivCode(abmReportingDraftRequest.getDivCode());
            entity.setDepoCode(abmReportingDraftRequest.getDepoCode());
            entity.setHqCode(abmReportingDraftRequest.getHqCode());
            entity.setEmpCode(abmReportingDto.getLine1_empcode());

            AbmDraftReportEntity saved = reportRepository.save(entity);
            response.setAbmDraftId(saved.getDraftId());
            jsonString = objectMapper.writeValueAsString(response);
            entity.setDraftJson(jsonString);
            saved = reportRepository.save(entity);
            
           
            
            // ============================
            // 1️⃣ initiate chemist audit form  
            // ============================
            ChemistAuditReport report = new ChemistAuditReport();

            report.setAbmDraftId(saved.getDraftId());
            report.setEntryDate(abmReportingDraftRequest.getEntryDate());
            report.setDivCode(abmReportingDraftRequest.getDivCode());
            report.setDepoCode(abmReportingDraftRequest.getDepoCode());
            report.setHq(abmReportingDraftRequest.getHqCode());
            report.setMonthCode(mnthCode);
            report.setMyear(abmReportingDraftRequest.getMyear());
            report.setMonth(0);
//            report.setMonth(abmReportingDraftRequest.getMonth());
            report.setEmpCode(abmReportingDto.getLine1_empcode());
            report.setLoginId(abmReportingDraftRequest.getLoginId());
            report.setAuditReportStatus("DRAFT");
            report.setAuditReportTitle("CHEMIST AUDIT REPORT - "+ mnthCode + " ABM");

            report.setAuditInnerSheetIds(new ArrayList<Long>());

            // save once to get ID
            report = chemistRepository.save(report);

            // ============================
            // 2️⃣ Build JSON WITH ID
            // ============================
            AuditSheetResponse response1 = new AuditSheetResponse();

            response1.setAuditReportTitle(report.getAuditReportTitle());
            response1.setAuditReportId(report.getAuditReportId());
            response1.setAuditInnerSheetId(0L); // or null    
            response1.setAuditReportStatus(report.getAuditReportStatus());
            response1.setSheets(new ArrayList<>());

           
            String draftJson=null;
    		try {
    			draftJson = objectMapper.writeValueAsString(response1);
    		} catch (JsonProcessingException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

            // ============================
            // 3️⃣ Update JSON
            // ============================
            report.setDraftJson(draftJson);

            chemistRepository.save(report);

            
            return response;

        } 
        catch (DataIntegrityViolationException e) {
            // duplicate entry
            return null;
        }
        catch (Exception e) {
            throw new RuntimeException("Error saving draft report", e);
        }
    }

    

    
    // =====================================================
    // UPDATE DRAFT
    // =====================================================
    @Override
    public String updateAbmDraftReport(String draftJson) throws Exception {

        Long id = extractId(draftJson);
        String status = extractDraftStatus(draftJson);

        AbmDraftReportEntity entity = reportRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Draft not found: " + id));

        entity.setDraftJson(draftJson);
        entity.setDraftStatus(status);

        reportRepository.save(entity);
        return draftJson;
    }

    // =====================================================
    // GET RAW JSON BY DRAFT ID  ✅ USED BY CONTROLLER
    // =====================================================
    @Override
    public String getDraftJsonByDraftId(Long draftId) {
        return reportRepository.findById(draftId)
                .map(AbmDraftReportEntity::getDraftJson)
                .orElseThrow(() ->
                        new RuntimeException("Draft not found for id " + draftId));
    }

    // =====================================================
    // HELPERS
    // =====================================================
    private Long extractId(String json) throws Exception {
        JsonNode rootNode = objectMapper.readTree(json);
        return rootNode.get("abm_draft_id").asLong();
    }

    private String extractDraftStatus(String json) throws Exception {
        JsonNode rootNode = objectMapper.readTree(json);
        return rootNode.get("abm_draft_status").asText();
    }

    // =====================================================
    // LIST BY MONTH & YEAR
    // =====================================================
    @Override
    public List<AbmDraftReportingDto> getByMonthAndYearAndLoginId(
            int month, int year, int loginId) {

        List<AbmReportingDto> reportingList = abmReportingDao.getLine1Reporting(loginId);
        final String loginName =
                (reportingList != null && !reportingList.isEmpty())
                        ? reportingList.get(0).getLine1_empname()
                        : null;
                        
       final String terName =
                (reportingList != null && !reportingList.isEmpty())
                ? reportingList.get(0).getLine1_name()
                : null;
               

                
        return reportRepository
                .findByMnthCodeAndMyearAndLoginId(month, year, loginId)
                .stream()
                .map(entity -> {
                    AbmDraftReportingDto dto = new AbmDraftReportingDto(entity);
                     dto.setLoginName(loginName); 
                     dto.setTerName(terName); // ✅ NO ERROR
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // =====================================================
    // LIST BY DIV_CODE & DEPO_cODE AND MONTH & YEAR
    // =====================================================

	@Override
	public List<AbmDraftReportingDto> getByDivCodeAndMnthCodeAndMyear(int divCode, int mnthCode, int myear) {
		
		
  
        return reportRepository
                .findByDivCodeAndMnthCodeAndMyear(divCode,mnthCode,myear)
                .stream()
                .map(entity -> {
                    AbmDraftReportingDto dto = new AbmDraftReportingDto(entity);
                    List<AbmReportingDto> reportingList = abmReportingDao.getLine1Reporting(dto.getLoginId());
                    final String loginName =
                            (reportingList != null && !reportingList.isEmpty())
                                    ? reportingList.get(0).getLine1_empname()
                                    : null;
                    dto.setLoginName(loginName); 
                    final String terName =
                            (reportingList != null && !reportingList.isEmpty())
                                    ? reportingList.get(0).getLine1_name()
                                    : null;
                    dto.setTerName(terName); 
                    return dto;
                })
                .collect(Collectors.toList());
    
	}

	@Override
	public List<AbmDraftReportingDto> getMissingAbmReportingList(int myear, int divCode,int depoCode, int mnthCode,int userType,int loginId) {

		
	       List<AbmReportingDto> psrdataList = abmReportingDao.getLine1Reporting(loginId);
			int empCode=psrdataList.get(0).getLine1_empcode();

		List<Object[]> reportingList = abmReportingDao.getMissingAbmReportingList(myear,divCode,depoCode,mnthCode,userType,empCode);
		
 		   
		List<AbmDraftReportingDto> dataList = new ArrayList();
		int size=reportingList.size();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		 for (Object[] row : reportingList) {

		        AbmDraftReportingDto dto = new AbmDraftReportingDto(); // ✅ inside loop

		        dto.setHqCode(0);
		        dto.setDivCode(divCode);
		        dto.setMnthCode(mnthCode);
		        dto.setMyear(myear);
		        dto.setDepoCode(((Number) row[0]).intValue());
		        dto.setEmpCode(Integer.parseInt(row[2].toString()));
		        dto.setLoginName((String) row[3]);
		        dto.setTerName((String) row[1]);
		        dto.setDraftStatus("Pending");
		        dto.setDraftId(0L);
		        dto.setEntryDate(LocalDate.now().format(formatter));
		        dataList.add(dto);
		    }
		return dataList;
	}


}
