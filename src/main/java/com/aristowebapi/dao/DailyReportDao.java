package com.aristowebapi.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.DailyEntryStatus; 
import com.aristowebapi.dto.DailyReport;
import com.aristowebapi.dto.MktDataDto;

public interface DailyReportDao extends JpaRepository<MktDataDto, Integer> {
	
	@Query(value="CALL daily_bill_detail(:div_code,:login_id,:entry_date,:month);", nativeQuery=true)
	List<DailyReport> getDailyBillDetail(@Param("div_code") int div_code,
			@Param("login_id") int login_id,
			@Param("entry_date") Date entry_date,@Param("month") int month);

	@Query(value="CALL daily_bill_detail_branch(:div_code,:login_id,:entry_date,:month);", nativeQuery=true)
	List<DailyReport> getDailyBillDetailBranch(@Param("div_code") int div_code,
			@Param("login_id") int login_id,
			@Param("entry_date") Date entry_date,@Param("month") int month);

	@Query(value="CALL daily_updation_status(:login_id,:entry_date,:month);", nativeQuery=true)
	List<DailyEntryStatus> getDailyUpdationStatus(@Param("login_id") int login_id,
			@Param("entry_date") Date entry_date,@Param("month") int month);

	
}
