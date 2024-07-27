package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.DivisionMasterDto;
import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.PendingData;

public interface PendingDao extends JpaRepository<MktDataDto, Integer> {
	
	
	@Query(value="CALL web_report_PendingPI(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<PendingData> getPendingPI(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);

	@Query(value = "SELECT div_code,div_name FROM divmast where div_code not in (9,12,13,16) order by div_Code", nativeQuery = true)
	List<DivisionMasterDto> getAllDivision();


}
