package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.BranchMasterDto;
import com.aristowebapi.dto.BranchMisRepo8;
import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.MktRepo1;
import com.aristowebapi.dto.MonthDto;

public interface SampleSm01Dao extends JpaRepository<MktDataDto, Integer> {
	
	@Query(value="CALL web_sample_sm01(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id);", nativeQuery=true)
	List<MktRepo1> getWebSampleSm01(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id);

	
	@Query(value="CALL web_sample_sm02(:myear,:div_code,:depo_code,:smon,:emon,:login_id,:utype,:code);", nativeQuery=true)
	List<BranchMisRepo8> getSampleSm02(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,
			@Param("login_id") int login_id,@Param("utype") int utype,@Param("code") int code);
	
	
	@Query(value = "SELECT depo_code,depo_name FROM branch_comp where depo_code <>32 order by depo_code", nativeQuery = true)
	List<BranchMasterDto> getAllBranch();
	
	@Query(value = "SELECT mnth_code,mnth_abbr FROM monthfl where mkt_year=:myear order by mnth_code", nativeQuery = true)
	List<MonthDto> getAllMonth(@Param("myear") int myear);
	
}
