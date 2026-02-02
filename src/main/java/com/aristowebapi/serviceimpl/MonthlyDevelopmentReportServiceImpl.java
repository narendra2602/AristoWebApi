package com.aristowebapi.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristowebapi.dto.DoctorPrescriptionDto;
import com.aristowebapi.dto.HeaderDto;
import com.aristowebapi.dto.MonthlyDevelopmentReportDto;
import com.aristowebapi.dto.MrDto;
import com.aristowebapi.dto.SelfAssessmentDto;
import com.aristowebapi.entity.AbmDraftReportEntity;
import com.aristowebapi.entity.DoctorPrescriptionEntity;
import com.aristowebapi.entity.HeaderEntity;
import com.aristowebapi.entity.MonthlyDevelopmentReportEntity;
import com.aristowebapi.entity.MrEntity;
import com.aristowebapi.entity.SelfAssessmentEntity;
import com.aristowebapi.repository.AbmDraftReportRepository;
import com.aristowebapi.repository.DoctorPrescriptionRepository;
import com.aristowebapi.repository.HeaderRepository;
import com.aristowebapi.repository.MonthlyDevelopmentReportRepository;
import com.aristowebapi.repository.MrRepository;
import com.aristowebapi.repository.SelfAssessmentRepository;
import com.aristowebapi.request.MonthlyReportRequest;
import com.aristowebapi.response.FullReportResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonthlyDevelopmentReportServiceImpl  {

	 private final AbmDraftReportRepository abmReportRepository;
    private final MonthlyDevelopmentReportRepository reportRepository;
    private final MrRepository mrRepository;
    private final DoctorPrescriptionRepository doctorRepository;
    private final SelfAssessmentRepository selfAssessmentRepository;
    private final HeaderRepository headerRepository;
    
    
    
    
    

    @Transactional
    public MonthlyDevelopmentReportEntity saveFullReport(MonthlyDevelopmentReportDto report,
    		List<MrDto> mrs,List<DoctorPrescriptionDto> doctors,SelfAssessmentDto selfAssessment,
            HeaderDto header) 
    
    {
    	// select json from abm_draftTable; retrun string
    	// now change sring to json (i.e. json Request) 
    	// now from json Request MonthlyDevelopmentReportDto report = jsonRequest.getREprt() and so on....
    	// baki ka aise hi chalega.
        // 1️⃣ Save Master Report first to generate reportId
        MonthlyDevelopmentReportEntity savedReport = reportRepository.save(new MonthlyDevelopmentReportEntity(report));

        Long reportId = savedReport.getReportId();

        // 2️⃣ Set reportId in all child entities
        mrs.forEach(mr -> mr.setReportId(reportId));
        doctors.forEach(d -> d.setReportId(reportId));
        selfAssessment.setReportId(reportId);
        header.setReportId(reportId);
        

        // 3️⃣ Save children
        mrRepository.saveAll(mrs.stream().map(MrEntity::new).collect(Collectors.toList()));
        
        doctorRepository.saveAll(doctors.stream().map(DoctorPrescriptionEntity::new).collect(Collectors.toList()));
 
        
        selfAssessmentRepository.save(new SelfAssessmentEntity(selfAssessment));
        headerRepository.save(new HeaderEntity(header));
        
        return savedReport;
    }
    
    
    
    @Transactional(readOnly = true)
    public FullReportResponse getFullReport(Long reportId) {

    	
    	 //Long reportId = reportRepository.findByReportMonthAndReportYearAndCreatedBy(month,year,loginId);
        // 1️⃣ Fetch master report
        MonthlyDevelopmentReportEntity report = reportRepository
                .findByReportId(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        // 2️⃣ Fetch MRs
        List<MrEntity> mrs = mrRepository.findAllByReportId(reportId);



        // 4️⃣ Fetch Doctors
        List<DoctorPrescriptionEntity> doctors = doctorRepository.findAllByReportId(reportId);

        // 5️⃣ Fetch Self Assessment
        SelfAssessmentEntity selfAssessment = selfAssessmentRepository.findByReportId(reportId)
                .orElse(null);

        // 6️⃣ Fetch Header
        HeaderEntity header = headerRepository.findByReportId(reportId)
                .orElse(null);

 
        MonthlyDevelopmentReportDto reportDto=new MonthlyDevelopmentReportDto();
        reportDto.setCompany("Aristo");
        reportDto.setMonth(report.getReportMonth());
        reportDto.setReportTitle("ABM");
        reportDto.setYear(report.getReportYear());
        reportDto.setCreatedBy(report.getCreatedBy());
        reportDto.setReportId(report.getReportId()); 
        reportDto.setDivCode(report.getDivCode());
        reportDto.setDepoCode(report.getDepoCode());
        
        
        List<MrDto> mrDtoList = mrs.stream()
                .map(MrDto::new)
                .collect(Collectors.toList());


 
        List<DoctorPrescriptionDto> drDtoList = doctors.stream()
                .map(DoctorPrescriptionDto::new)
                .collect(Collectors.toList());

      
        
        SelfAssessmentDto selfDto = new SelfAssessmentDto();
        
        selfDto.setSelfRating(selfAssessment.getSelfRating());
        selfDto.setSelfImprovementPlan(selfAssessment.getSelfImprovementPlan());

        HeaderDto headerDto= new HeaderDto();
        
        headerDto.setFromName(header.getFromName());
        headerDto.setDesignation(header.getDesignation());
        headerDto.setHq(header.getHq());
        headerDto.setTo(header.getToName());
        headerDto.setRef(header.getReference());
        headerDto.setCc(header.getCc());
        headerDto.setDate(header.getRefDate());
        
        // 8️⃣ Build Response DTO
        FullReportResponse response = new FullReportResponse();
        response.setReport(reportDto);
        response.setMrs(mrDtoList);
        response.setDoctors(drDtoList);
        response.setSelfAssessment(selfDto);
        response.setHeader(headerDto);
       

        return response;
    }

    
    
    @Transactional
    public String saveFinalDraftReport(Long draftId) throws Exception
    
    {
    	// select json from abm_draftTable; retrun string
    	// now change sring to json (i.e. json Request) 
    	// now from json Request MonthlyDevelopmentReportDto report = jsonRequest.getREprt() and so on....
    	// baki ka aise hi chalega.
    	
    	
    	AbmDraftReportEntity entity = abmReportRepository.findByDraftId(draftId);
    	entity.setDraftStatus("Final");
    	entity = abmReportRepository.save(entity);
    	
    	String jsonRequest= entity.getDraftJson();
    	MonthlyReportRequest reportRequest = new ObjectMapper().readValue(jsonRequest,MonthlyReportRequest.class);
    	
    	MonthlyDevelopmentReportDto report=reportRequest.getReport();
		List<MrDto> mrs=reportRequest.getMrs();
		List<DoctorPrescriptionDto> doctors=reportRequest.getDoctors();
		SelfAssessmentDto selfAssessment=reportRequest.getSelfAssessment();
        HeaderDto header=reportRequest.getHeader();

        MonthlyDevelopmentReportEntity reportEntity= new MonthlyDevelopmentReportEntity(report);
        reportEntity.setDraftId(draftId);
    	
        // 1️⃣ Save Master Report first to generate reportId
        MonthlyDevelopmentReportEntity savedReport = reportRepository.save(reportEntity);

        Long reportId = savedReport.getReportId();

        // 2️⃣ Set reportId in all child entities
        mrs.forEach(mr -> mr.setReportId(reportId));
        doctors.forEach(d -> d.setReportId(reportId));
        selfAssessment.setReportId(reportId);
        header.setReportId(reportId);
        

        // 3️⃣ Save children
        mrRepository.saveAll(mrs.stream().map(MrEntity::new).collect(Collectors.toList()));
        
        doctorRepository.saveAll(doctors.stream().map(DoctorPrescriptionEntity::new).collect(Collectors.toList()));
 
        
        selfAssessmentRepository.save(new SelfAssessmentEntity(selfAssessment));
        headerRepository.save(new HeaderEntity(header));
        
        return jsonRequest;
    }

    
}
