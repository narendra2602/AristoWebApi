package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.BranchMisRepo5;
import com.aristowebapi.dto.BranchMisRepo6;
import com.aristowebapi.dto.BranchMisRepo8;
import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.MonthDto;

public interface BranchMisDao extends JpaRepository<MktDataDto, Integer> {
	
	@Query(value="CALL aristo_web.web_branch_mis_repo5(:myear,:div_code,:depo_code,:smon,:emon,:rep_type,:login_id,:utype,:code);", nativeQuery=true)
	List<BranchMisRepo5> getBranchMisRepo5(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("rep_type") int rep_type,
			@Param("login_id") int login_id,@Param("utype") int utype,@Param("code") int code);
	
	@Query(value="CALL aristo_web.web_branch_mis_repo6(:myear,:div_code,:depo_code,:smon,:emon,:rep_type,:login_id,:utype,:code);", nativeQuery=true)
	List<BranchMisRepo6> getBranchMisRepo6(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("rep_type") int rep_type,
			@Param("login_id") int login_id,@Param("utype") int utype,@Param("code") int code);

	@Query(value="CALL aristo_web.web_branch_mis_repo8HQ(:myear,:div_code,:depo_code,:smon,:emon,:rep_type,:login_id,:utype,:code);", nativeQuery=true)
	List<BranchMisRepo8> getBranchMisRepo8HQ(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("rep_type") int rep_type,
			@Param("login_id") int login_id,@Param("utype") int utype,@Param("code") int code);

	@Query(value="CALL aristo_web.web_branch_mis_repo8Region(:myear,:div_code,:depo_code,:smon,:emon,:rep_type,:login_id,:utype,:code);", nativeQuery=true)
	List<BranchMisRepo8> getBranchMisRepo8Region(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("rep_type") int rep_type,
			@Param("login_id") int login_id,@Param("utype") int utype,@Param("code") int code);

	@Query(value="CALL aristo_web.web_branch_mis_repo8Area(:myear,:div_code,:depo_code,:smon,:emon,:rep_type,:login_id,:utype,:code);", nativeQuery=true)
	List<BranchMisRepo8> getBranchMisRepo8Area(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("rep_type") int rep_type,
			@Param("login_id") int login_id,@Param("utype") int utype,@Param("code") int code);
	
	@Query(value="CALL aristo_web.web_branch_mis_repo8Branch(:myear,:div_code,:depo_code,:smon,:emon,:rep_type,:login_id,:utype,:code);", nativeQuery=true)
	List<BranchMisRepo8> getBranchMisRepo8Branch(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("rep_type") int rep_type,
			@Param("login_id") int login_id,@Param("utype") int utype,@Param("code") int code);

	@Query(value = "SELECT mnth_code,mnth_abbr FROM monthfl where mkt_year=:myear order by mnth_code", nativeQuery = true)
	List<MonthDto> getAllMonth(@Param("myear") int myear);
}
