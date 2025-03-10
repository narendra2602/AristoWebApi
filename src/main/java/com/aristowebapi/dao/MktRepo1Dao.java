package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.BranchMasterDto;
import com.aristowebapi.dto.DashBoardData;
import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.MktRepo1;
import com.aristowebapi.dto.MktRepo1Ach;
import com.aristowebapi.dto.MtbStockiestDto;

public interface MktRepo1Dao extends JpaRepository<MktDataDto, Integer>{
	
	// this is also for mtb report
	@Query(value="CALL web_report_gross(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type,:data_type);", nativeQuery=true)
	List<MktRepo1> getWebReportGross(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type,@Param("data_type") int data_type);

	
	@Query(value="CALL web_report_net(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type);", nativeQuery=true)
	List<MktRepo1> getWebReportNet(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type);

	@Query(value="CALL web_report_credit(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type,:data_type);", nativeQuery=true)
	List<MktRepo1> getWebReportCredit(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type,@Param("data_type") int data_type);
	

	// doc_type 10=target   20=last year
	@Query(value="CALL web_report_target(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type,:doc_type);", nativeQuery=true)
	List<MktRepo1> getWebReportTarget(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type,@Param("doc_type") int doc_type);

	
	@Query(value="CALL web_report_ach(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type);", nativeQuery=true)
	List<MktRepo1Ach> getWebReportAch(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type);

	@Query(value="CALL web_report_gthnew(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type,:doc_type);", nativeQuery=true)
	List<MktRepo1Ach> getWebReportGth(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type,@Param("doc_type") int doc_type);

	
	@Query(value="CALL getNoOfRep(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id);", nativeQuery=true)
	List<DashBoardData> getNoOfRep(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id);

	
	@Query(value = "SELECT depo_code,depo_name FROM branch_comp where depo_code <>32 order by depo_code", nativeQuery = true)
	List<BranchMasterDto> getAllBranch();

	
	@Query(value = "SELECT ter_code depo_code,ter_name depo_name FROM hqmast  where mkt_year=:myear and div_code=:div_code and depo_code=:depo and ter_code<>0",  nativeQuery = true)
	List<BranchMasterDto> getAllHq(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo") int depo);

	
	@Query(value = "SELECT ter_code depo_code,ter_name depo_name FROM hqmast  where mkt_year=:myear and div_code=:div_code and depo_code=:depo_code and ter_code<>0 and (depo_code,ter_code) in  (Select depo_code,ter_code from user_ter where user_id=:loginId and status='Y' ) order by depo_code,ter_code ",  nativeQuery = true)
	List<BranchMasterDto> getUtype4Hq(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,@Param("loginId") int loginId);

	@Query(value = "SELECT ter_code depo_code,ter_name depo_name FROM hqmast  where mkt_year=:myear and div_code=:div_code and ter_code<>0 and (depo_code,ter_code) in  (Select depo_code,ter_code from user_ter where user_id=:loginId and status='Y' ) order by depo_code,ter_code ",  nativeQuery = true)
	List<BranchMasterDto> getUtype4HqAll(@Param("myear") int myear,@Param("div_code") int div_code,@Param("loginId") int loginId);

	
	@Query(value = "SELECT depo_code,depo_name FROM branch_comp  where depo_code<>32 and depo_code in  (Select depo_code from user_branch08 where user_id=:loginId and status='Y') order by depo_code ",  nativeQuery = true)
	List<BranchMasterDto> getUtype5Branch(@Param("loginId") int loginId);

	@Query(value = "SELECT depo_name FROM branch_comp where depo_code=:depo", nativeQuery = true)
	String getBranch(@Param("depo") int depo);

	
	@Query(value="CALL web_stockist_mtb(:myear,:smon,:emon,:login_id);", nativeQuery=true)
	List<MtbStockiestDto> getWebStockiestMtb(@Param("myear") int myear,
			@Param("smon") int smon,@Param("emon") int emon,@Param("login_id") int login_id);

}
