package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.MonthDto;
import com.aristowebapi.dto.StkRepo2;

public interface StkRepo2Dao extends JpaRepository<MktDataDto, Integer> {
	
	
	@Query(value="CALL aristo_web.web_stockiest_repo2New(:myear,:div_code,:depo_code,:smon,:emon,:rep_type,:code,:login_id,:opt_type,:hq_code);", nativeQuery=true)
	List<StkRepo2> getStockiestRepo2(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,
			@Param("rep_type") int rep_type,@Param("code") String code,@Param("login_id") int login_id,@Param("opt_type") int opt_type,@Param("hq_code") int hq_code);

	@Query(value="CALL aristo_web.web_stockiest_repo2_credit(:myear,:div_code,:depo_code,:smon,:emon,:crn_type,:code,:login_id,:opt_type,:hq_code);", nativeQuery=true)
	List<StkRepo2> getStockiestRepo2Credit(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,
			@Param("crn_type") int crn_type,@Param("code") String code,@Param("login_id") int login_id,@Param("opt_type") int opt_type,@Param("hq_code") int hq_code);
	
	@Query(value="CALL aristo_web.web_stockiest_repo2_credit_group(:myear,:div_code,:depo_code,:smon,:emon,:crn_type,:code,:login_id,:opt_type,:hq_code);", nativeQuery=true)
	List<StkRepo2> getStockiestRepo2CreditGroup(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,
			@Param("crn_type") int crn_type,@Param("code") String code,@Param("login_id") int login_id,@Param("opt_type") int opt_type,@Param("hq_code") int hq_code);
	
	@Query(value = "SELECT mnth_code,mnth_abbr FROM monthfl where mkt_year=:myear order by mnth_code", nativeQuery = true)
	List<MonthDto> getAllMonth(@Param("myear") int myear);

	@Query(value = "SELECT concat(mac_name,',',mcity) name FROM aris.partyfst where div_code=:div and depo_code=:depo and mac_code=:code", nativeQuery = true)
	String getPartyName(@Param("div") int div,@Param("depo") int depo,@Param("code") String code);

	@Query(value = "SELECT ter_name FROM aris.hqmast where div_code=:div and depo_code=:depo and mkt_year=:myear and ter_code=:code ", nativeQuery = true)
	String getHqName(@Param("div") int div,@Param("depo") int depo,@Param("myear") int myear,@Param("code") int code);
	
	
/*	@Query(value="CALL aristo_web.StockiestRepo2(:myear,:div_code,:depo_code,:smon,:emon,:rep_type,:code,:uv);", nativeQuery=true)
	List<StkRepo2> getStockiestRepo2(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,
			@Param("rep_type") int rep_type,@Param("code") String code,@Param("uv") int uv);

*/
}
