package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.AbmReportingDto;
import com.aristowebapi.dto.MktDataDto;

public interface AbmReportingDao  extends JpaRepository<MktDataDto, Integer> {

	@Query(value="CALL getLine1Reporting(:login_id);", nativeQuery=true)
	List<AbmReportingDto> getLine1Reporting(@Param("login_id") int login_id);
	
	@Query(value = "select mnth_code from perdmast where mkt_year=:myear and mkt_ord=:emon", nativeQuery = true)
	int getMonthCode(@Param("myear") int myear,@Param("emon") int emon);
}
