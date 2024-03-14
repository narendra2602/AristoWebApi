package com.aristowebapi.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.NearExpiry;

public interface NearExpiryDao extends JpaRepository<MktDataDto, Integer> { 
	
	
	@Query(value="CALL aristo.near_expiry_report(:year,:code,:login_id,:curr_date,:div_code);", nativeQuery=true)
	List<NearExpiry> getNearExpiryReport(@Param("year") int year,@Param("code") int code,
			@Param("login_id") int login_id,
			@Param("curr_date") Date curr_date,
			@Param("div_code") int div_code);
			


}
