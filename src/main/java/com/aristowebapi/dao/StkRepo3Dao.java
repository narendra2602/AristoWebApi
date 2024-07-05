package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.dto.StkRepo3;

public interface StkRepo3Dao extends JpaRepository<MktDataDto, Integer> { 
	
	@Query(value="CALL aristo_web.web_stockiest_repo3(:myear,:div_code,:depo_code,:smon,:emon,:rep_type,:login_id);", nativeQuery=true)
	List<StkRepo3> getStockiestRepo3(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,
			@Param("rep_type") int rep_type,@Param("login_id") int login_id);

	
	@Query(value="CALL aristo_web.web_stockiest_repo3_Credit(:myear,:div_code,:depo_code,:smon,:emon,:rep_type,:login_id);", nativeQuery=true)
	List<StkRepo3> getStockiestRepo3Credit(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,
			@Param("rep_type") int rep_type,@Param("login_id") int login_id);

	@Query(value = "SELECT mnth_code,mnth_abbr FROM monthfl where mkt_year=:myear order by mnth_code", nativeQuery = true)
	List<MonthDto> getAllMonth(@Param("myear") int myear);

}
