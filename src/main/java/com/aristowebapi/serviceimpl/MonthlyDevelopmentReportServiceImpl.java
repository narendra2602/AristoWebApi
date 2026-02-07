package com.aristowebapi.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import com.aristowebapi.entity.DoctorPrescriptionEntity;
import com.aristowebapi.entity.HeaderEntity;
import com.aristowebapi.entity.MonthlyDevelopmentReportEntity;
import com.aristowebapi.entity.MrEntity;
import com.aristowebapi.entity.SelfAssessmentEntity;
import com.aristowebapi.exception.DataAlreadyException;
import com.aristowebapi.exception.DataNotFoundException;
import com.aristowebapi.repository.AbmDraftReportRepository;
import com.aristowebapi.repository.DoctorPrescriptionRepository;
import com.aristowebapi.repository.HeaderRepository;
import com.aristowebapi.repository.MonthlyDevelopmentReportRepository;
import com.aristowebapi.repository.MrRepository;
import com.aristowebapi.repository.SelfAssessmentRepository;
import com.aristowebapi.request.MonthlyReportRequest;
import com.aristowebapi.response.FullReportResponse;
import com.aristowebapi.service.MonthlyDevelopmentReportService;
import com.aristowebapi.utility.DraftStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonthlyDevelopmentReportServiceImpl implements MonthlyDevelopmentReportService {

	 private final AbmDraftReportRepository abmReportRepository;
    private final MonthlyDevelopmentReportRepository reportRepository;
    private final MrRepository mrRepository;
    private final DoctorPrescriptionRepository doctorRepository;
    private final SelfAssessmentRepository selfAssessmentRepository;
    private final HeaderRepository headerRepository;
    private final AbmReportingDao abmReportingDao;
    @Autowired
    private  ObjectMapper objectMapper;
    
    

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

    
    
    @Transactional(rollbackFor = Exception.class)
    public String saveFinalDraftReport(Long draftId) throws Exception
    
    {
     	
    	
    	AbmDraftReportEntity entity = abmReportRepository.findByDraftId(draftId);
    	
        List<AbmReportingDto> reportingList =
                abmReportingDao.getLine1Reporting(entity.getLoginId());

        AbmReportingDto abmReportingDto =
                reportingList != null && !reportingList.isEmpty()
                        ? reportingList.get(0)
                        : null;
    	

    	if (entity == null) {
    	    throw new IllegalArgumentException("Draft not found for id: " + draftId);
    	}

    	
    	if (reportRepository.existsByDraftId(draftId)) {
    	    throw new DataAlreadyException("Final report already created for draftId " + draftId);
    	}
    	

    	entity.setDraftStatus(DraftStatus.FINAL.name());

    	entity = abmReportRepository.save(entity);
    	
    	String jsonRequest= entity.getDraftJson();

    	

    	if (jsonRequest == null || jsonRequest.isEmpty()) {
    	    throw new DataNotFoundException("Draft JSON not found");
    	}

    	MonthlyReportRequest reportRequest = objectMapper.readValue(jsonRequest, MonthlyReportRequest.class);

   	


    	
    	
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

    	
        // 1️⃣ Save Master Report first to generate reportId
        MonthlyDevelopmentReportEntity savedReport = reportRepository.save(reportEntity);

        Long reportId = savedReport.getReportId();

        // 2️⃣ Set reportId in all child entities

        mrs.forEach(mr -> mr.setReportId(reportId));
        doctors.forEach(d -> d.setReportId(reportId));
        selfAssessment.setReportId(reportId);
        header.setReportId(reportId);
        

        // 3️⃣ Save children
 
        if (mrs != null && !mrs.isEmpty()) {
            mrRepository.saveAll(mrs.stream().map(MrEntity::new).collect(Collectors.toList()));
        }

        
        if (doctors != null && !doctors.isEmpty()) {
        	doctorRepository.saveAll(doctors.stream().map(DoctorPrescriptionEntity::new).collect(Collectors.toList()));
        }
        
        if(selfAssessment!=null)
        	selfAssessmentRepository.save(new SelfAssessmentEntity(selfAssessment));
        if(header!=null)
        	headerRepository.save(new HeaderEntity(header));
        
        return jsonRequest;
        

    }

    @Transactional(rollbackFor = Exception.class)
    public String deleteReportByReportId(Long draftId) {

    	

    	
        // 1️⃣ Fetch final report safely
        MonthlyDevelopmentReportEntity reportEntity = reportRepository.findByDraftId(draftId)
                        .orElseThrow(() -> new DataNotFoundException("Final report not found for draftId: " + draftId));

        Long reportId = reportEntity.getReportId();

 
     // 2️⃣ Reset draft status to DRAFT
        AbmDraftReportEntity draftEntity = abmReportRepository.findByDraftId(draftId);

        if (draftEntity != null) {
            draftEntity.setDraftStatus(DraftStatus.DRAFT.name());
            abmReportRepository.save(draftEntity);
        }
    	
     // 3️⃣ Delete children first (FK-safe)    	
       
        mrRepository.deleteByReportId(reportId);
        doctorRepository.deleteByReportId(reportId);
        selfAssessmentRepository.deleteByReportId(reportId);
        headerRepository.deleteByReportId(reportId);

     // 4️⃣ Delete parent last
        reportRepository.deleteById(reportId);
        
        return "Delete Successfully";
    }



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
