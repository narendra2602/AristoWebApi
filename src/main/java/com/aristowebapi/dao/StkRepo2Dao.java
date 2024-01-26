package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.StkRepo2;

public interface StkRepo2Dao extends JpaRepository<MktDataDto, Integer> {
	
	
	@Query(value="CALL aristo_web.StockiestRepo2(:myear,:div_code,:depo_code,:smon,:emon,:rep_type,:code,:uv);", nativeQuery=true)
	List<StkRepo2> getStockiestRepo2(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("smon") int smon,@Param("emon") int emon,
			@Param("rep_type") int rep_type,@Param("code") String code,@Param("uv") int uv);


}
