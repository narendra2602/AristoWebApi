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
	
	@Query(value = "select ter_name from hqmast where mkt_year=:myear and div_Code=:div_code and depo_code=:depo_code and ter_code=:hq_code", nativeQuery = true)
	String getTerName(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,@Param("hq_code") int hq_code);
}
