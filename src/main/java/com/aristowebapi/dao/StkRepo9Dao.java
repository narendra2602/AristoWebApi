package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.StkRepo5;
import com.aristowebapi.dto.StkRepo9;

public interface StkRepo9Dao extends JpaRepository<MktDataDto, Integer> { 
	
	@Query(value="CALL aristo_web.web_stockiest_repo9new(:myear,:div_code,:depo_code,:smon,:emon,:code,:login_id);", nativeQuery=true)
	List<StkRepo9> getStockiestRepo9(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,
			@Param("code") int code,@Param("login_id") int login_id);

	@Query(value="CALL aristo_web.web_stockiest_repo9newGroup(:myear,:div_code,:depo_code,:smon,:emon,:code,:login_id);", nativeQuery=true)
	List<StkRepo9> getStockiestRepo9Group(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,
			@Param("code") int code,@Param("login_id") int login_id);

	
	@Query(value = "SELECT pname FROM aris.prd where div_code=:div and pcode=:code", nativeQuery = true)
	String getPname(@Param("div") int div,@Param("code") int code);

	@Query(value="CALL aristo_web.web_stockiest_repo5(:myear,:div_code,:depo_code,:smon,:emon,:code,:login_id);", nativeQuery=true)
	List<StkRepo5> getStockiestRepo5(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,
			@Param("code") int code,@Param("login_id") int login_id);


}
