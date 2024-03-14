package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.MktRepo4;

public interface MktRepo4Dao extends JpaRepository<MktDataDto, Integer>{ 
	
	@Query(value="CALL web_report_allgroupvalue(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:gp_code);", nativeQuery=true)
	List<MktRepo4> getWebReportAllGroupValue(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("gp_code") int gp_code);

	@Query(value="CALL web_report_allvalues(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:gp_code);", nativeQuery=true)
	List<MktRepo4> getWebReportAllValue(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("gp_code") int gp_code);

	
	@Query(value = "SELECT depo_name FROM branch_comp where depo_code=:depo", nativeQuery = true)
	String getBranch(@Param("depo") int depo);


}
