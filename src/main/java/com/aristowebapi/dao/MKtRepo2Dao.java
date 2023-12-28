package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.MktRepo2;

public interface MKtRepo2Dao extends JpaRepository<MktDataDto, Integer> {
	
	
	@Query(value="CALL web_mkt_2all(:myear,:div_code,:depo_code,:smon,:emon,:utype,:login_id,:rep_type);", nativeQuery=true)
	List<MktRepo2> getWebMkt2All(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,@Param("utype") int utype,@Param("login_id") int login_id,
			@Param("rep_type") int rep_type);

}
