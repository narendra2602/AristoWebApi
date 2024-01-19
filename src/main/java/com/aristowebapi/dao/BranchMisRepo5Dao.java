package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.BranchMisRepo5;
import com.aristowebapi.dto.MktDataDto;

public interface BranchMisRepo5Dao extends JpaRepository<MktDataDto, Integer> {
	
	@Query(value="CALL aristo.branch_mis_repo5(:myear,:depo_code,:utype,:login_id,:smon,:emon);", nativeQuery=true)
	List<BranchMisRepo5> getBranchMisRepo5(@Param("myear") int myear,@Param("depo_code") int depo_code,
			@Param("utype") int utype,@Param("login_id") int login_id,@Param("smon") int smon,@Param("emon") int emon);


}
