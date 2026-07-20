package com.aristowebapi.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.DailyEntryStatus;
import com.aristowebapi.dto.DailyReport;
import com.aristowebapi.dto.DayWiseReport;
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

	@Query(value="CALL day_wise_billing_report(:div_code,:myear,:month,:rep_type);", nativeQuery=true)
	List<DayWiseReport> getDayWiseReport(@Param("div_code") int div_code,
			@Param("myear") int myear,
			@Param("month") int month,
			@Param("rep_type") int rep_type);

	
	
	@Query(value = "SELECT mnth_code FROM perdmast WHERE mkt_year = :myear AND mkt_ord = :mno", nativeQuery = true)
	String getMonth(@Param("myear") int myear,
	             @Param("mno") int mno);

	
	
}
