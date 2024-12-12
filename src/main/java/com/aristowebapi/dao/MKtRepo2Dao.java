package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.MktRepo2;
import com.aristowebapi.dto.MktRepo2New;
import com.aristowebapi.dto.MktRepo2NewTrend;
import com.aristowebapi.dto.MonthDto;

public interface MKtRepo2Dao extends JpaRepository<MktDataDto, Integer> { 
	
// user type2 for all depo	
	@Query(value="CALL web_mkt_2all(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type);", nativeQuery=true)
	List<MktRepo2> getWebMkt2All(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type);

// user type 2 with selective depo	
	@Query(value="CALL web_mkt_2(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type);", nativeQuery=true)
	List<MktRepo2> getWebMkt2(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type);

// user type 3 with all depo PMT	
	
	@Query(value="CALL web_mkt_2allpmt(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type);", nativeQuery=true)
	List<MktRepo2> getWebMkt2AllPmt(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type);

// user type 3 with selective depo PMT	
	
	@Query(value="CALL web_mkt_2pmt(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type);", nativeQuery=true)
	List<MktRepo2> getWebMkt2Pmt(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type);

// user type 3 with selective depo/hq  PMT		
	@Query(value="CALL web_mkt_2hqpmt(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type);", nativeQuery=true)
	List<MktRepo2> getWebMkt2HqPmt(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type);

// user type 5 with all depo		
	
	@Query(value="CALL web_mkt_5(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type);", nativeQuery=true)
	List<MktRepo2> getWebMkt5(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type);

// user type 4 with all  depo 		

	@Query(value="CALL web_mkt2hq4(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type);", nativeQuery=true)
	List<MktRepo2> getWebMkt2Hq4(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type);

// user type 4 with selective  depo 		
	
	@Query(value="CALL web_mkt2depo4(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type);", nativeQuery=true)
	List<MktRepo2> getWebMkt2Depo4(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type);

// all hq of selective branch		
	
	@Query(value="CALL web_mkt_2hq(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type);", nativeQuery=true)
	List<MktRepo2> getWebMkt2Hq(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type);

	
	
	// user type2 for all depo	mkt 2.1
		@Query(value="CALL web_mkt_2allSelectiveCode(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:pcode,:repType);", nativeQuery=true)
		List<MktRepo2> getWebMkt2SelectiveCode(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
				@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
				@Param("pcode") int pcode,@Param("repType") int repType);

		// user type2 for all depo	mkt 2.1
		@Query(value="CALL web_mkt_2allSelectiveGroup(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:pcode,:repType);", nativeQuery=true)
		List<MktRepo2> getWebMkt2SelectiveGroup(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
				@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
				@Param("pcode") int pcode,@Param("repType") int repType);

		// user type2 for all depo	mkt 2.2
		@Query(value="CALL web_mkt_2allMultipleGroupNew(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:pcode,:repType);", nativeQuery=true)
		List<MktRepo2New> getWebMkt2MultipleGroup(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
				@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
				@Param("pcode") String pcode,@Param("repType") int repType);

		// user type2 for all depo	mkt 2.2 Trend
		@Query(value="CALL web_mkt_2allMultipleGroupTrend(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:pcode,:repType);", nativeQuery=true)
		List<MktRepo2NewTrend> getWebMkt2MultipleGroupTrend(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
				@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
				@Param("pcode") String pcode,@Param("repType") int repType);

		@Query(value = "SELECT mnth_code,mnth_abbr FROM monthfl where mkt_year=:myear order by mnth_code", nativeQuery = true)
		List<MonthDto> getAllMonth(@Param("myear") int myear);

		
}
