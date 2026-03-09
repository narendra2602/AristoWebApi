package com.aristowebapi.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristowebapi.dao.AbmReportingDao;
import com.aristowebapi.dto.AbmReportingDto;
import com.aristowebapi.dto.DoctorPrescriptionDto;
import com.aristowebapi.dto.HeaderDto;
import com.aristowebapi.dto.MonthlyDevelopmentReportDto;
import com.aristowebapi.dto.MrDto;
import com.aristowebapi.dto.SelfAssessmentDto;
import com.aristowebapi.entity.AbmDraftReportEntity;
import com.aristowebapi.entity.AuditInnerSheet;
import com.aristowebapi.entity.ChemistAuditReportFinal;
import com.aristowebapi.entity.DoctorPrescriptionEntity;
import com.aristowebapi.entity.HeaderEntity;
import com.aristowebapi.entity.MonthlyDevelopmentReportEntity;
import com.aristowebapi.entity.MrEntity;
import com.aristowebapi.entity.SelfAssessmentEntity;
import com.aristowebapi.exception.DataAlreadyException;
import com.aristowebapi.exception.DataNotFoundException;
import com.aristowebapi.repository.AbmDraftReportRepository;
import com.aristowebapi.repository.AuditInnerSheetRepository;
import com.aristowebapi.repository.ChemistAuditReportRepositoryFinal;
import com.aristowebapi.repository.DoctorPrescriptionRepository;
import com.aristowebapi.repository.HeaderRepository;
import com.aristowebapi.repository.MonthlyDevelopmentReportRepository;
import com.aristowebapi.repository.MrRepository;
import com.aristowebapi.repository.SelfAssessmentRepository;
import com.aristowebapi.request.MonthlyReportRequest;
import com.aristowebapi.response.FullReportResponse;
import com.aristowebapi.service.MonthlyDevelopmentReportService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonthlyDevelopmentReportServiceImpl implements MonthlyDevelopmentReportService {

	
	Logger logger = LoggerFactory.getLogger(MonthlyDevelopmentReportServiceImpl.class);

	
	 private final AbmDraftReportRepository abmReportRepository;
    private final MonthlyDevelopmentReportRepository reportRepository;
    private final MrRepository mrRepository;
    private final DoctorPrescriptionRepository doctorRepository;
    private final SelfAssessmentRepository selfAssessmentRepository;
    private final HeaderRepository headerRepository;
    private final AbmReportingDao abmReportingDao;
    @Autowired
    private  ObjectMapper objectMapper;
    
    @Autowired
    private ChemistAuditReportRepositoryFinal finalrepository;
    
    @Autowired
    private AuditInnerSheetRepository auditInnerSheetRepository;
    

    @Transactional
    public MonthlyDevelopmentReportEntity saveFullReport(MonthlyDevelopmentReportDto report,
    		List<MrDto> mrs,List<DoctorPrescriptionDto> doctors,SelfAssessmentDto selfAssessment,
            HeaderDto header) 
    
    {
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
    public FullReportResponse getFullReport(Long draftId) {

    	
    	 //Long reportId = reportRepository.findByReportMonthAndReportYearAndCreatedBy(month,year,loginId);
        // 1️⃣ Fetch master report
        MonthlyDevelopmentReportEntity report = reportRepository
                .findByDraftId(draftId)
                .orElseThrow(() -> new DataNotFoundException("Report not found for Draft Id "+draftId));


        
        Long reportId = report.getReportId();
        System.out.println("draft id "+draftId+" report id "+reportId);
        
        
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
        response.setAbmDraftId(report.getDraftId());
        response.setAbmDraftStatus("Final");
/*		ApiResponse<FullReportResponse> apiResponse = new ApiResponse<>("",0,response);
		return apiResponse;
*/

        return response;
    }

    
 // method change on 08/03/2026   
    
    @Transactional(rollbackFor = Exception.class)
    public String saveFinalDraftReport(Long draftId) throws Exception {

        logger.info("Starting final report creation for draftId {}", draftId);

        // 1️⃣ Fetch Draft
        AbmDraftReportEntity entity = abmReportRepository.findByDraftId(draftId);

        if (entity == null) {
            throw new IllegalArgumentException("Draft not found for id: " + draftId);
        }

        // 2️⃣ Prevent duplicate final report
        if (reportRepository.existsByDraftId(draftId)) {
            throw new DataAlreadyException("Final report already created for draftId " + draftId);
        }

        // 3️⃣ Validate Draft JSON
        String jsonRequest = entity.getDraftJson();
        if (jsonRequest == null || jsonRequest.isEmpty()) {
            throw new DataNotFoundException("Draft JSON not found");
        }

        MonthlyReportRequest reportRequest =
                objectMapper.readValue(jsonRequest, MonthlyReportRequest.class);

        // 4️⃣ Fetch Reporting Data
        List<AbmReportingDto> reportingList =
                abmReportingDao.getLine1Reporting(entity.getLoginId());

        if (reportingList == null || reportingList.isEmpty()) {
            throw new DataNotFoundException("Reporting data not found");
        }

        AbmReportingDto abmReportingDto = reportingList.get(0);

        // 5️⃣ Extract Request Data
        MonthlyDevelopmentReportDto report = reportRequest.getReport();
        List<MrDto> mrs = reportRequest.getMrs();
        List<DoctorPrescriptionDto> doctors = reportRequest.getDoctors();
        SelfAssessmentDto selfAssessment = reportRequest.getSelfAssessment();
        HeaderDto header = reportRequest.getHeader();

        // 6️⃣ Create Master Report Entity
        MonthlyDevelopmentReportEntity reportEntity = new MonthlyDevelopmentReportEntity(report);

        reportEntity.setDraftId(draftId);
        reportEntity.setHqCode(report.getHqCode());

        reportEntity.setLine1EmpCode(abmReportingDto.getLine1_empcode());
        reportEntity.setLine1EmpName(abmReportingDto.getLine1_empname());

        reportEntity.setLine2EmpCode(abmReportingDto.getLine2_empcode());
        reportEntity.setLine2EmpName(abmReportingDto.getLine2_empname());

        reportEntity.setLine3EmpCode(abmReportingDto.getLine3_empcode());
        reportEntity.setLine3EmpName(abmReportingDto.getLine3_empname());

        reportEntity.setCreatedDate(LocalDateTime.now());

        // 7️⃣ Save Master Report
        MonthlyDevelopmentReportEntity savedReport = reportRepository.save(reportEntity);
        Long reportId = savedReport.getReportId();

        logger.info("Report created successfully with reportId {}", reportId);

        // 8️⃣ Save Child Entities

        if (mrs != null && !mrs.isEmpty()) {
            mrs.forEach(mr -> mr.setReportId(reportId));

            List<MrEntity> mrEntities =
                    mrs.stream()
                            .map(MrEntity::new)
                            .collect(Collectors.toList());

            mrRepository.saveAll(mrEntities);
        }

        if (doctors != null && !doctors.isEmpty()) {
            doctors.forEach(d -> d.setReportId(reportId));

            List<DoctorPrescriptionEntity> doctorEntities =
                    doctors.stream()
                            .map(DoctorPrescriptionEntity::new)
                            .collect(Collectors.toList());

            doctorRepository.saveAll(doctorEntities);
        }

        if (selfAssessment != null) {
            selfAssessment.setReportId(reportId);
            selfAssessmentRepository.save(new SelfAssessmentEntity(selfAssessment));
        }

        if (header != null) {
            header.setReportId(reportId);
            headerRepository.save(new HeaderEntity(header));
        }

        // 9️⃣ Update Draft Status AFTER successful report creation
        reportRequest.setAbmDraftStatus("FINAL");
        entity.setDraftStatus("FINAL");

        String updatedJson = objectMapper.writeValueAsString(reportRequest);
        entity.setDraftJson(updatedJson);

        abmReportRepository.save(entity);

        logger.info("Draft status updated to FINAL for draftId {}", draftId);

        // 🔟 Return Updated JSON
        return updatedJson;
    }   
    
 /*   @Transactional(rollbackFor = Exception.class)
    public String saveFinalDraftReport(Long draftId) throws Exception
    
    {
     	
    	 // 1️⃣ Fetch draft properly (PRIMARY KEY)
    	
    	AbmDraftReportEntity entity = abmReportRepository.findByDraftId(draftId);
    	
    	// 2️⃣ Check if final already created
    	
    	if (reportRepository.existsByDraftId(draftId)) {
    	    throw new DataAlreadyException("Final report already created for draftId " + draftId);
    	}
    	
    	
    	// 3️⃣ Get reporting data
        List<AbmReportingDto> reportingList =
                abmReportingDao.getLine1Reporting(entity.getLoginId());

        AbmReportingDto abmReportingDto =
                reportingList != null && !reportingList.isEmpty()
                        ? reportingList.get(0)
                        : null;
    	

    	if (entity == null) {
    	    throw new IllegalArgumentException("Draft not found for id: " + draftId);
    	}

    	
    	// 4️⃣ Read stored JSON
    	
    	String jsonRequest= entity.getDraftJson();

    	

    	if (jsonRequest == null || jsonRequest.isEmpty()) {
    	    throw new DataNotFoundException("Draft JSON not found");
    	}

    	MonthlyReportRequest reportRequest = objectMapper.readValue(jsonRequest, MonthlyReportRequest.class);


        // ===============================
        // 8️⃣ Now create final report
        // ===============================

    	
    	
    	MonthlyDevelopmentReportDto report=reportRequest.getReport();
		List<MrDto> mrs=reportRequest.getMrs();
		List<DoctorPrescriptionDto> doctors=reportRequest.getDoctors();
		SelfAssessmentDto selfAssessment=reportRequest.getSelfAssessment();
        HeaderDto header=reportRequest.getHeader();

        MonthlyDevelopmentReportEntity reportEntity= new MonthlyDevelopmentReportEntity(report);
        reportEntity.setDraftId(draftId);
        reportEntity.setHqCode(reportRequest.getReport().getHqCode());
        reportEntity.setLine1EmpCode(abmReportingDto.getLine1_empcode());
        reportEntity.setLine1EmpName(abmReportingDto.getLine1_empname());
        reportEntity.setLine2EmpCode(abmReportingDto.getLine2_empcode());
        reportEntity.setLine2EmpName(abmReportingDto.getLine2_empname());
        reportEntity.setLine3EmpCode(abmReportingDto.getLine3_empcode());
        reportEntity.setLine3EmpName(abmReportingDto.getLine3_empname());
        reportEntity.setCreatedDate(LocalDateTime.now());

    	
        // 1️⃣ Save Master Report first to generate reportId
        MonthlyDevelopmentReportEntity savedReport = reportRepository.save(reportEntity);
        Long reportId = savedReport.getReportId();

        logger.info("after save generated report id in service method  "+reportId);
        
        // 2️⃣ Set reportId in all child entities

        mrs.forEach(mr -> mr.setReportId(reportId));
        doctors.forEach(d -> d.setReportId(reportId));
        selfAssessment.setReportId(reportId);
        header.setReportId(reportId);
        

        // 3️⃣ Save children
 
        if (mrs != null && !mrs.isEmpty()) {
            mrRepository.saveAll(mrs.stream().map(MrEntity::new).collect(Collectors.toList()));
            logger.info("Saving mr repository for reportId {}", reportId);
        }

        
        if (doctors != null && !doctors.isEmpty()) {
        	doctorRepository.saveAll(doctors.stream().map(DoctorPrescriptionEntity::new).collect(Collectors.toList()));
        	logger.info("Saving doctor repository for reportId {}", reportId);
        }
        
        if(selfAssessment!=null)
        {
        	selfAssessmentRepository.save(new SelfAssessmentEntity(selfAssessment));
        	logger.info("Saving self assessment repository for reportId {}", reportId);
        }
        if(header!=null)
        {
        	headerRepository.save(new HeaderEntity(header));
        	logger.info("Saving header repository for reportId {}", reportId);
        }
        
     // 🔟 Return updated JSON (NOT old jsonRequest)
        
    	// 5️⃣ 🔥 UPDATE STATUS IN BOTH PLACES
        entity.setDraftStatus("FINAL");
        reportRequest.setAbmDraftStatus("FINAL");

        logger.info("draft status in entity   "+entity.getDraftStatus());
        
     // 6️⃣ Convert updated object back to JSON
        String updatedJson = objectMapper.writeValueAsString(reportRequest);
        entity.setDraftJson(updatedJson);

        // 7️⃣ Save updated draft entity
        abmReportRepository.saveAndFlush(entity);
        
       
        return updatedJson;
        

    }
*/
    
    @Transactional(rollbackFor = Exception.class)
    public String deleteReportByReportId(Long draftId) {

        MonthlyDevelopmentReportEntity reportEntity =
                reportRepository.findByDraftId(draftId)
                        .orElseThrow(() -> new DataNotFoundException("Final report not found"));

        Long reportId = reportEntity.getReportId();

        logger.info("DraftId : {}", draftId);
        logger.info("ReportId : {}", reportId);

        // Step 1 - delete MDR related tables
        mrRepository.deleteByReportId(reportId);
        doctorRepository.deleteByReportId(reportId);
        selfAssessmentRepository.deleteByReportId(reportId);
        headerRepository.deleteByReportId(reportId);

        reportRepository.delete(reportEntity);

        // Step 3 - delete ChemistAuditReportFinal
        Optional<ChemistAuditReportFinal> finalReport = finalrepository.findByReportId(draftId);

        if(finalReport.isPresent()){
        	System.out.println("Chemist audit report found, deleting..."+draftId);
            logger.info("Chemist audit report found, deleting..."+draftId);
            finalrepository.delete(finalReport.get());
        }else{
            logger.info("Chemist audit report NOT found for reportId {}", draftId);
        }

        // Step 4 - update audit inner sheet
        List<AuditInnerSheet> sheets = auditInnerSheetRepository.findByAuditReportId(draftId);

        logger.info("Total audit sheets found : {}", sheets.size());

        if(!sheets.isEmpty()){
            for (AuditInnerSheet sheet : sheets) {
            	System.out.println("inner sheet"+draftId);
                sheet.setAuditReportStatus("DRAFT");
            }
            auditInnerSheetRepository.saveAll(sheets);
        }

        // Step 5 - update draft
        AbmDraftReportEntity draftEntity = abmReportRepository.findByDraftId(draftId);

        if (draftEntity != null) {
            draftEntity.setDraftStatus("Draft");
            abmReportRepository.save(draftEntity);
        }

        return "Delete Successfully";
    }    
    
 /*   @Transactional(rollbackFor = Exception.class)
    public String deleteReportByReportId(Long draftId) {

        MonthlyDevelopmentReportEntity reportEntity =
                reportRepository.findByDraftId(draftId)
                .orElseThrow(() ->
                        new DataNotFoundException("Final report not found"));

        Long reportId = reportEntity.getReportId();
        
        logger.info("draft id in delete service method  "+draftId);
        
        logger.info("report id in delete service method  "+reportId);

        mrRepository.deleteByReportId(reportId);
        doctorRepository.deleteByReportId(reportId);
        selfAssessmentRepository.deleteByReportId(reportId);
        headerRepository.deleteByReportId(reportId);

        reportRepository.delete(reportEntity);

        AbmDraftReportEntity draftEntity = abmReportRepository.findByDraftId(draftId);
        if (draftEntity != null) {
            draftEntity.setDraftStatus("Draft");
            abmReportRepository.save(draftEntity);
            
            logger.info("inside draftEntity if condition  to set status as Draft ");

        }

        return "Delete Successfully";
    }
*/
	@Override
	public List<FullReportResponse> getAllReportJson(int divCode, int depoCode, int mnthCode, int myear, int hqCode) {
		// 1️⃣ Fetch ALL matching reports
	    List<MonthlyDevelopmentReportEntity> reports =
	            reportRepository
	                    .findAllByDivCodeAndDepoCodeAndReportMonthAndReportYearAndHqCode(divCode, depoCode, mnthCode, myear, hqCode);

	    if (reports.isEmpty()) {
	        throw new DataNotFoundException("No reports found");
	    }

	    List<FullReportResponse> responseList = new ArrayList<>();

	    // 2️⃣ Loop each report and build JSON
	    for (MonthlyDevelopmentReportEntity report : reports) {

	        Long reportId = report.getReportId();

	        // Fetch related data
	        List<MrEntity> mrs = mrRepository.findAllByReportId(reportId);
	        List<DoctorPrescriptionEntity> doctors =
	                doctorRepository.findAllByReportId(reportId);

	        SelfAssessmentEntity selfAssessment =
	                selfAssessmentRepository.findByReportId(reportId).orElse(null);

	        HeaderEntity header =
	                headerRepository.findByReportId(reportId).orElse(null);

	        // Report DTO
	        MonthlyDevelopmentReportDto reportDto = new MonthlyDevelopmentReportDto();
	        reportDto.setCompany("Aristo");
	        reportDto.setMonth(report.getReportMonth());
	        reportDto.setYear(report.getReportYear());
	        reportDto.setReportTitle("ABM");
	        reportDto.setCreatedBy(report.getCreatedBy());
	        reportDto.setReportId(report.getReportId());
	        reportDto.setDivCode(report.getDivCode());
	        reportDto.setDepoCode(report.getDepoCode());

	        // MR DTO
	        List<MrDto> mrDtoList = mrs.stream()
	                .map(MrDto::new)
	                .collect(Collectors.toList());

	        // Doctor DTO
	        List<DoctorPrescriptionDto> drDtoList = doctors.stream()
	                .map(DoctorPrescriptionDto::new)
	                .collect(Collectors.toList());

	        // Self Assessment DTO
	        SelfAssessmentDto selfDto = null;
	        if (selfAssessment != null) {
	            selfDto = new SelfAssessmentDto();
	            selfDto.setSelfRating(selfAssessment.getSelfRating());
	            selfDto.setSelfImprovementPlan(
	                    selfAssessment.getSelfImprovementPlan());
	        }

	        // Header DTO
	        HeaderDto headerDto = null;
	        if (header != null) {
	            headerDto = new HeaderDto();
	            headerDto.setFromName(header.getFromName());
	            headerDto.setDesignation(header.getDesignation());
	            headerDto.setHq(header.getHq());
	            headerDto.setTo(header.getToName());
	            headerDto.setRef(header.getReference());
	            headerDto.setCc(header.getCc());
	            headerDto.setDate(header.getRefDate());
	        }

	        // Build Response
	        FullReportResponse response = new FullReportResponse();
//	        response.setReport(reportDto);
	        response.setMrs(mrDtoList);
	        response.setDoctors(drDtoList);
	        response.setSelfAssessment(selfDto);
	        response.setHeader(headerDto);
	        response.setAbmDraftId(report.getDraftId());
	        response.setAbmDraftStatus("Final");

	        responseList.add(response);
	    }

	    return responseList;

	}



 
}
