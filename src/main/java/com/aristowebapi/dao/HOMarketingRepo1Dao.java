package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.HOMarketingRepo1;
import com.aristowebapi.dto.MktDataDto;

public interface HOMarketingRepo1Dao extends JpaRepository<MktDataDto, Integer> { 
	
	@Query(value="CALL aristo_web.HoMarketingRepo1Product(:myear,:div_code,:code,:smon,:emon,:utype,:login_id,:rep_type);", nativeQuery=true)
	List<HOMarketingRepo1> getHoMarketingRepo1Product (@Param("myear") int myear,@Param("div_code") int div_code,@Param("code") int code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,@Param("rep_type") int rep_type);


	@Query(value="CALL aristo_web.HoMarketingRepo1Group(:myear,:div_code,:code,:smon,:emon,:utype,:login_id,:rep_type);", nativeQuery=true)
	List<HOMarketingRepo1> getHoMarketingRepo1Group (@Param("myear") int myear,@Param("div_code") int div_code,@Param("code") int code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,@Param("rep_type") int rep_type);

	@Query(value="CALL aristo_web.HoMarketingRepo1All(:myear,:div_code,:smon,:emon,:utype,:login_id,:rep_type);", nativeQuery=true)
	List<HOMarketingRepo1> getHoMarketingRepo1All (@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,@Param("rep_type") int rep_type);
	
}
