package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.MktRepo5;

public interface MktRepo5Dao extends JpaRepository<MktDataDto, Integer> { 
	@Query(value="CALL web_report_24new(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:gp_code);", nativeQuery=true)
	List<MktRepo5> getWebReport24(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("gp_code") int gp_code);

	
	@Query(value="CALL web_report_24pack(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:pcode);", nativeQuery=true)
	List<MktRepo5> getWebReport24Pack(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("pcode") int pcode);

	
	@Query(value="CALL web_report_24newallhq(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:gp_code);", nativeQuery=true)
	List<MktRepo5> getWebReport24all(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("gp_code") int gp_code);

	
	@Query(value = "SELECT depo_name FROM branch_comp where depo_code=:depo", nativeQuery = true)
	String getBranch(@Param("depo") int depo);

}
