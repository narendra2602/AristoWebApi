package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.DashBoardCurrentMonthChart;
import com.aristowebapi.dto.DashBoardData;
import com.aristowebapi.dto.DashBoardSalesChart;
import com.aristowebapi.dto.MktDataDto;

public interface DashBoardDao extends JpaRepository<MktDataDto, Integer> {
	
	
	@Query(value="CALL getDashboardMainChart(:div_code,:depo_code);", nativeQuery=true)
	List<DashBoardSalesChart> getDashboardMainChart(@Param("div_code") int div_code,@Param("depo_code") int depo_code);

	@Query(value="CALL getDashboardPanelData(:div_code,:depo_code,:rep_type);", nativeQuery=true)
	DashBoardCurrentMonthChart getDashboardCurrentMonthChart(@Param("div_code") int div_code,@Param("depo_code") int depo_code,@Param("rep_type") int rep_type);

	@Query(value="CALL getDashboardTop5Stockiest(:div_code,:depo_code);", nativeQuery=true)
	List<DashBoardData> getDashboardTop5Stockiest(@Param("div_code") int div_code,@Param("depo_code") int depo_code);


	@Query(value="CALL getDashboardTheraputicSales(:div_code,:depo_code);", nativeQuery=true)
	List<DashBoardData> getDashboardTheraputicSales(@Param("div_code") int div_code,@Param("depo_code") int depo_code);

	@Query(value="CALL getDashboardPendingPI(:div_code,:depo_code);", nativeQuery=true)
	List<DashBoardData> getDashboardPendingPI(@Param("div_code") int div_code,@Param("depo_code") int depo_code);

	@Query(value="CALL getDashboardGroupwiseAch(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<DashBoardData> getDashboardGroupwiseAch(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);

	@Query(value="CALL getDashboardBranchwiseAch(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<DashBoardData> getDashboardBranchwiseAch(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);

	@Query(value="CALL getDashboardHqwiseAch(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<DashBoardData> getDashboardHqwiseAch(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);

	
}
