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
	
	
	@Query(value="CALL getDashboardMainChart(:myear,:div_code,:depo_code,:login_id,:utype);", nativeQuery=true)
	List<DashBoardSalesChart> getDashboardMainChart(@Param("myear") int myear,@Param("div_code") int div_code,
	@Param("depo_code") int depo_code,@Param("login_id") int login_id,@Param("utype") int utype);

	@Query(value="CALL getDashboardPanelData(:myear,:div_code,:depo_code,:cmon,:login_id,:utype,:rep_type);", nativeQuery=true)
	DashBoardCurrentMonthChart getDashboardCurrentMonthChart(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype,@Param("rep_type") int rep_type);


	@Query(value="CALL getDashboardPanelData(:myear,:div_code,:depo_code,:cmon,:login_id,:utype,:rep_type);", nativeQuery=true)
	List<DashBoardData> getDashboardPanelData(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype,@Param("rep_type") int rep_type);

	

	@Query(value="CALL getDashboardTop5Stockiest(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<DashBoardData> getDashboardTop5Stockiest(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);

	@Query(value="CALL getDashboardTheraputicSales(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<DashBoardData> getDashboardTheraputicSales(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);


	@Query(value="CALL getDashboardPendingPI(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<DashBoardData> getDashboardPendingPI(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);

	

	@Query(value="CALL getDashboardGroupwiseAch(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<DashBoardData> getDashboardGroupwiseAch(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);

	@Query(value="CALL getDashboardBranchwiseAch(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<DashBoardData> getDashboardBranchwiseAch(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);

	@Query(value="CALL getDashboardHqwiseAch(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<DashBoardData> getDashboardHqwiseAch(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);

	@Query(value="CALL getDashboardTop5Products(:myear,:div_code,:depo_code,:cmon,:login_id);", nativeQuery=true)
	List<DashBoardData> getDashboardTop5Products(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id);
	
	@Query(value="CALL getDashboardMonthIndex(:myear,:div_code,:depo_code,:login_id,:utype);", nativeQuery=true)
	int  getDashboardMonthIndex(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("login_id") int login_id,@Param("utype") int utype);

	@Query(value="CALL getDashboardYearCombo();", nativeQuery=true)
	List<DashBoardData>  getDashboardYearCombo();
	
	@Query(value = "select div_code val,div_name name from divmast where div_code in (select div_code from userdiv where user_id=:userId and user_status='Y' order by div_code) ", nativeQuery = true)
	List<DashBoardData> getDivList(@Param("userId") int userId);

	@Query(value = "select depo_code val,depo_name name from branch_comp where depo_code in (select depo_code from user_branch08 where user_id=:userId and status='Y')  order by depo_code ", nativeQuery = true)
	List<DashBoardData> getBranchList(@Param("userId") int userId);


}
