package com.aristowebapi.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.DailyEntry;
import com.aristowebapi.dto.DailyEntryList;

public interface DailyEntryDao extends JpaRepository<DailyEntry, Integer>{
	
	
	@Query(value="CALL daily_bill_entryNew(:myear,:div_code,:depo_code,:month,:depoName);", nativeQuery=true)
	List<DailyEntryList> getLastEntry(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,
			@Param("month") int month,@Param("depoName") String depoName);


}
