package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.MisRepo31;
import com.aristowebapi.dto.MktDataDto;

public interface MisRepo31Dao extends JpaRepository<MktDataDto, Integer>{
	
	
		@Query(value="CALL web_mis_repo31Hq(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:hq_code);", nativeQuery=true)
		List<MisRepo31> getMisRepo31Hq(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
				@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,@Param("hq_code") int hq_code);
				
		@Query(value="CALL web_mis_repo31Branch(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:hq_code);", nativeQuery=true)
		List<MisRepo31> getMisRepo31Branch(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
				@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,@Param("hq_code") int hq_code);

		@Query(value="CALL web_misrepo31PMT(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:hq_code);", nativeQuery=true)
		List<MisRepo31> getMisRepo31Pmt(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
				@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,@Param("hq_code") int hq_code);

		@Query(value="CALL web_misrepo31AllIndiaPMT(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:hq_code);", nativeQuery=true)
		List<MisRepo31> getMisRepo31AllIndiaPmt(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
				@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,@Param("hq_code") int hq_code);

		
		@Query(value = "SELECT depo_name FROM branch_comp where depo_code=:depo", nativeQuery = true)
		String getBranch(@Param("depo") int depo);


	}