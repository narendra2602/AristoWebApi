package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.MktRepo6;
import com.aristowebapi.dto.MktRepo6a;
import com.aristowebapi.dto.MonthDto;

public interface MktRepo6Dao extends JpaRepository<MktDataDto, Integer> { 
	
	@Query(value="CALL web_report_hq_trend(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:gp_code);", nativeQuery=true)
	List<MktRepo6> getWebReportHqSaleTrend(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("gp_code") int gp_code);

	@Query(value="CALL web_report_hq_trendallhq(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:gp_code);", nativeQuery=true)
	List<MktRepo6> getWebReportHqSaleTrendAll(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("gp_code") int gp_code);

	
	@Query(value="CALL web_report_hq_trend_pack(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:pcode);", nativeQuery=true)
	List<MktRepo6> getWebReportHqSaleTrendPack(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("pcode") int pcode);

	@Query(value="CALL web_report_hq_trend_packallhq(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:pcode);", nativeQuery=true)
	List<MktRepo6> getWebReportHqSaleTrendPackAll(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("pcode") int pcode);

	
	@Query(value="CALL web_report_hq_target_trend(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:gp_code);", nativeQuery=true)
	List<MktRepo6> getWebReportHqTargetTrend(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("gp_code") int gp_code);

	
	@Query(value = "SELECT mnth_code,mnth_abbr FROM monthfl where mkt_year=:myear order by mnth_code", nativeQuery = true)
	List<MonthDto> getAllMonth(@Param("myear") int myear);

	@Query(value = "SELECT depo_name FROM branch_comp where depo_code=:depo", nativeQuery = true)
	String getBranch(@Param("depo") int depo);
	
	
	@Query(value="CALL web_report_item_trend(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:hq_code);", nativeQuery=true)
	List<MktRepo6a> getWebReportItemTrend(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("hq_code") int hq_code);

}


