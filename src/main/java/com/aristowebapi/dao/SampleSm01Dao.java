package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.BranchMasterDto;
import com.aristowebapi.dto.BranchMisRepo8;
import com.aristowebapi.dto.DashBoardData;
import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.MktRepo1;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.dto.SampleSalesRatioDto;

public interface SampleSm01Dao extends JpaRepository<MktDataDto, Integer> {
	
	@Query(value="CALL web_sample_sm01(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:reptype,:hqcode);", nativeQuery=true)
	List<MktRepo1> getWebSampleSm01(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,@Param("reptype") int reptype,@Param("hqcode") int hqcode);

	
	
	@Query(value="CALL web_sample_sm02(:myear,:div_code,:depo_code,:smon,:emon,:login_id,:utype,:code,:reptype,:optn);", nativeQuery=true)
	List<BranchMisRepo8> getSampleSm02(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,
			@Param("login_id") int login_id,@Param("utype") int utype,@Param("code") int code,@Param("reptype") int reptype,@Param("optn") int optn);
	
	@Query(value="CALL web_rep_sales_sample_ratio(:myear,:div_code,:depo_code,:smon,:emon);", nativeQuery=true)
	List<SampleSalesRatioDto> getSampleSm03(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon);
	
	@Query(value = "SELECT depo_code,depo_name FROM branch_comp where depo_code <>32 order by depo_code", nativeQuery = true)
	List<BranchMasterDto> getAllBranch();
	
	@Query(value = "SELECT mnth_code,mnth_abbr FROM monthfl where mkt_year=:myear order by mnth_code", nativeQuery = true)
	List<MonthDto> getAllMonth(@Param("myear") int myear);
	

	@Query(value = "SELECT ter_code depo_code,ter_name depo_name FROM hqmast where mkt_year=:myear "
			+ " and div_code=:div and depo_code=:depo and ter_code<>0 order by 1", nativeQuery = true)
	List<BranchMasterDto> getAllHq(@Param("myear") int myear,@Param("div") int div,@Param("depo") int depo);

	
/*	@Query(value = "SELECT distinct fsid depo_code,cust_name depo_name FROM aristo_web_sample.sample_issue where mkt_year=:myear "
			+ " and div_code=:div and depo_code=:depo and ter_code=:hcode and mnth_code between :smon and :emon order by 1", nativeQuery = true)
	List<BranchMasterDto> getAllRm(@Param("myear") int myear,@Param("div") int div,@Param("depo") int depo,
			@Param("hcode") int hcode,@Param("smon") int smon,@Param("emon") int emon);
*/
	@Query(value="CALL web_sample_Rm(:myear,:div,:depo,:hcode,:smon,:emon);", nativeQuery=true)
	List<BranchMasterDto> getAllRm(@Param("myear") int myear,@Param("div") int div,@Param("depo") int depo,
			@Param("hcode") int hcode,@Param("smon") int smon,@Param("emon") int emon);


}
