package com.aristowebapi.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.MisRepo31;
import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.NearExpiry;

public interface NearExpiryDao extends JpaRepository<MktDataDto, Integer> { 
	
	
	
	@Query(value="CALL productwisenearexp(:depo_code,:login_id,:div_code,:code,:utype,:rep_type);", nativeQuery=true)
	List<NearExpiry> getNearExpiryReport(@Param("depo_code") int depo_code,
			@Param("login_id") int login_id,
			@Param("div_code") int div_code,
			@Param("code") int code,
			@Param("utype") int utype,
		   @Param("rep_type") int rep_type);
	

	
	
	
	@Query(value="CALL near_expiry_report_branchwise(:depo_code,:login_id,:div_code,:rep_type,:utype);", nativeQuery=true)
	List<NearExpiry> getNearExpiryReportBranchwise(@Param("depo_code") int depo_code,
			@Param("login_id") int login_id,
			@Param("div_code") int div_code,
			@Param("rep_type") int rep_type,
			@Param("utype") int utype);

	
	


}
