package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristowebapi.dao.AbmReportingDao;
import com.aristowebapi.dto.AbmReportingDto;
import com.aristowebapi.dto.HeaderDto;
import com.aristowebapi.dto.MonthlyDevelopmentReportDto;
import com.aristowebapi.dto.SelfAssessmentDto;
import com.aristowebapi.entity.AbmDraftReportEntity;
import com.aristowebapi.entity.SelfAssessmentEntity;
import com.aristowebapi.repository.AbmDraftReportRepository;
import com.aristowebapi.request.AbmReportingDraftRequest;
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
    
    
    

    @Transactional
    public FullReportResponse saveAbmDraftReport(
           AbmReportingDraftRequest abmReportingDraftRequest
            
    ) {

    	MonthlyDevelopmentReportDto report = null;
 
    	int mnthCode=abmReportingDao.getMonthCode(abmReportingDraftRequest.getMyear(), abmReportingDraftRequest.getMnthCode());
    	abmReportingDraftRequest.setMnthCode(mnthCode);
    	
        // 5️⃣ Fetch Self Assessment
        SelfAssessmentEntity selfAssessment = new SelfAssessmentEntity();

        
		List<AbmReportingDto> AbmReportingList=abmReportingDao.getLine1Reporting(abmReportingDraftRequest.getLoginId());;
		AbmReportingDto abmReportingDto=null;
		if(AbmReportingList!=null)
			abmReportingDto = AbmReportingList.get(0);

		HeaderDto header = new HeaderDto();

        // 6️⃣ Fetch Header
		if(abmReportingDto!=null)
		{
			header.setFromName(abmReportingDto.getLine1_empname());
			header.setDesignation(abmReportingDto.getLine1_desg());
			header.setHq(abmReportingDto.getHq());
			header.setTo(abmReportingDto.getLine2_desg()+" :"+abmReportingDto.getLine2_empname());
			header.setRef(null);
			header.setCc(abmReportingDto.getLine3_desg()+" :"+abmReportingDto.getLine3_empname());
			header.setDate(abmReportingDraftRequest.getEntryDate());
		}
        
	       MonthlyDevelopmentReportDto reportDto=new MonthlyDevelopmentReportDto();
	        reportDto.setCompany("Aristo");
	        reportDto.setMonth(abmReportingDraftRequest.getMnthCode());
	        reportDto.setReportTitle("ABM");
	        reportDto.setYear(abmReportingDraftRequest.getMyear());
	        reportDto.setCreatedBy(abmReportingDraftRequest.getLoginId());
	        reportDto.setReportId(0); 
	        reportDto.setDivCode(abmReportingDraftRequest.getDivCode());
	        reportDto.setDepoCode(abmReportingDraftRequest.getDepoCode());

		
        FullReportResponse fullReportResponse = new FullReportResponse();
        fullReportResponse.setAbmDraftStatus("Draft");
        fullReportResponse.setReport(reportDto);
        fullReportResponse.setDoctors(new ArrayList<>());
        fullReportResponse.setMrs(new ArrayList<>()); 
        fullReportResponse.setHeader(header);
        fullReportResponse.setSelfAssessment(new SelfAssessmentDto());
        
        // 1️⃣ Save Master Report first to generate abmDraftId
        String jsonString=null;
		try {
			jsonString = objectMapper.writeValueAsString(fullReportResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        AbmDraftReportEntity entity = new AbmDraftReportEntity();
        
        entity.setDraftJson(jsonString); // ✅ String saved
        entity.setDraftStatus("Draft");
        entity.setEntryDate(abmReportingDraftRequest.getEntryDate());
        entity.setLoginId(abmReportingDraftRequest.getLoginId());
        entity.setMnthCode(abmReportingDraftRequest.getMnthCode());
        entity.setMyear(abmReportingDraftRequest.getMyear());
       
        AbmDraftReportEntity abmSavedReport = reportRepository.save(entity);
       

        Long draftId = abmSavedReport.getDraftId();
        fullReportResponse.setAbmDraftId(draftId);

        
        
        return fullReportResponse;
    }




	@Override
	public String  updateAbmDraftReport(String draftJson) throws Exception {
		// TODO Auto-generated method stub

		String status= extractDraftStatus(draftJson);
		
		Long id=extractId(draftJson);
        AbmDraftReportEntity entity = reportRepository.findByDraftId(id);
        
        if(entity!=null)
        {
        	entity.setDraftJson(draftJson); // ✅ String saved
        	entity.setDraftStatus(status);
            AbmDraftReportEntity abmSavedReport = reportRepository.save(entity);
        }
        
       
        return draftJson;

		
	}
    
    
    
	public Long extractId(String json) throws Exception {

	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode rootNode = mapper.readTree(json);

	    return rootNode.get("abm_draft_id").asLong();
	}


	public String extractDraftStatus(String json) throws Exception {

	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode rootNode = mapper.readTree(json);

	    return rootNode.get("abm_draft_status").asText();
	}

	

}
