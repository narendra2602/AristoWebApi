package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.BranchMktRepo5;
import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.MonthDto;

public interface BranchMktDao extends JpaRepository<MktDataDto, Integer> { 
	
	@Query(value="CALL BranchMktRepo5(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type,:hq_code,:data_type);", nativeQuery=true)
	List<BranchMktRepo5> getBranchMktRepo5(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type,@Param("hq_code") int hq_code,@Param("data_type") int data_type);

	@Query(value = "SELECT mnth_code,mnth_abbr FROM monthfl where mkt_year=:myear order by mnth_code", nativeQuery = true)
	List<MonthDto> getAllMonth(@Param("myear") int myear);



}
