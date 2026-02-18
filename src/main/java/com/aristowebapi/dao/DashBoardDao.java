package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.DashBoardAchGthTrend;
import com.aristowebapi.dto.DashBoardCurrentMonthChart;
import com.aristowebapi.dto.DashBoardData;
import com.aristowebapi.dto.DashBoardDataAchGTh;
import com.aristowebapi.dto.DashBoardDataDouble;
import com.aristowebapi.dto.DashBoardSalesChart;
import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.OptionMasterDto;
import com.aristowebapi.dto.StockiestMaster;

public interface DashBoardDao extends JpaRepository<MktDataDto, Integer> { 
	
	
	@Query(value="CALL getDashboardMainChart(:myear,:div_code,:depo_code,:login_id,:utype);", nativeQuery=true)
	List<DashBoardSalesChart> getDashboardMainChart(@Param("myear") int myear,@Param("div_code") int div_code,
	@Param("depo_code") int depo_code,@Param("login_id") int login_id,@Param("utype") int utype);

	@Query(value="CALL getDashboardPanelData(:myear,:div_code,:depo_code,:cmon,:login_id,:utype,:rep_type);", nativeQuery=true)
	DashBoardCurrentMonthChart getDashboardCurrentMonthChart(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype,@Param("rep_type") int rep_type);


	@Query(value="CALL getDashboardAchGthReport(:myear,:div_code,:depo_code,:utype,:login_id);", nativeQuery=true)
	List<DashBoardAchGthTrend> getDashboardAchGthReport(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("utype") int utype,@Param("login_id") int login_id);

	
	@Query(value="CALL getDashboardPanelData(:myear,:div_code,:depo_code,:cmon,:login_id,:utype,:rep_type);", nativeQuery=true)
	List<DashBoardDataDouble> getDashboardPanelData(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype,@Param("rep_type") int rep_type);

	

	@Query(value="CALL getDashboardTop5Stockiest(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<DashBoardDataDouble> getDashboardTop5Stockiest(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);

	@Query(value="CALL getDashboardTheraputicSales(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<DashBoardDataDouble> getDashboardTheraputicSales(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);


	@Query(value="CALL getDashboardPendingPI(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<DashBoardDataDouble> getDashboardPendingPI(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);

	

	@Query(value="CALL getDashboardGroupwiseAch(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<DashBoardDataAchGTh> getDashboardGroupwiseAch(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);

	@Query(value="CALL getDashboardBranchwiseAch(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<DashBoardDataAchGTh> getDashboardBranchwiseAch(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);

	@Query(value="CALL getDashboardHqwiseAch(:myear,:div_code,:depo_code,:cmon,:login_id,:utype);", nativeQuery=true)
	List<DashBoardDataAchGTh> getDashboardHqwiseAch(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id,@Param("utype") int utype);

	@Query(value="CALL getDashboardTop5Products(:myear,:div_code,:depo_code,:cmon,:login_id);", nativeQuery=true)
	List<DashBoardDataDouble> getDashboardTop5Products(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("cmon") int cmon,@Param("login_id") int login_id);
	
	@Query(value="CALL getDashboardMonthIndex(:myear,:div_code,:depo_code,:login_id,:utype);", nativeQuery=true)
	int  getDashboardMonthIndex(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("login_id") int login_id,@Param("utype") int utype);

	@Query(value="CALL getDashboardYearCombo();", nativeQuery=true)
	List<DashBoardData>  getDashboardYearCombo();
	
	@Query(value = "select div_code val,div_name name from divmast where div_code in (select div_code from userdiv where user_id=:userId and user_status='Y' order by div_code) ", nativeQuery = true)
	List<DashBoardData> getDivList(@Param("userId") int userId);

	@Query(value = "select depo_code val,depo_name name from branch_comp where depo_code in (select depo_code from user_branch08 where user_id=:userId and status='Y')  order by depo_name ", nativeQuery = true)
	List<DashBoardData> getBranchList(@Param("userId") int userId);

	@Query(value = "CALL daily_entry_branch(:userId,:divCode);", nativeQuery=true)
	List<DashBoardData> getHoBranchList(@Param("userId") int userId,@Param("divCode") int divCode);

	@Query(value = "SELECT rep_type,rep_name FROM optionmast order by rep_type", nativeQuery = true)
	List<OptionMasterDto> getAllOption();
	
	
	@Query(value="CALL getHqList(:myear,:div_code,:depo_code,:utype,:login_id);", nativeQuery=true)
	List<DashBoardData> getHqList(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("utype") int utype,@Param("login_id") int login_id);

	
	@Query(value="CALL getRegionList(:myear,:div_code,:depo_code,:utype,:login_id);", nativeQuery=true)
	List<DashBoardData> getRegionList(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("utype") int utype,@Param("login_id") int login_id);

	
	@Query(value="CALL getAreaList(:myear,:div_code,:depo_code,:utype,:login_id);", nativeQuery=true)
	List<DashBoardData> getAreaList(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("utype") int utype,@Param("login_id") int login_id);

	
	

	
	@Query(value="CALL getProductList(:div_code,:utype,:login_id,:myear,:gp_code);", nativeQuery=true)
	List<DashBoardData> getProductList(@Param("div_code") int div_code,	@Param("utype") int utype,@Param("login_id") int login_id,@Param("myear") int myear,@Param("gp_code") int gp_code);

	@Query(value="CALL getGroupList(:div_code,:utype,:login_id,:myear);", nativeQuery=true)
	List<DashBoardData> getGroupList(@Param("div_code") int div_code,	@Param("utype") int utype,@Param("login_id") int login_id,@Param("myear") int myear);

	
	@Query(value="CALL getIqviaMaster(:div_code);", nativeQuery=true)
	List<DashBoardData> getIqviaGroupList(@Param("div_code") int div_code);

	
	
	@Query(value="CALL getStockiestList(:myear,:div_code,:depo_code,:utype,:login_id);", nativeQuery=true)
	List<StockiestMaster> getStockiestList(@Param("myear") int myear,@Param("div_code") int div_code,
			@Param("depo_code") int depo_code,@Param("utype") int utype,@Param("login_id") int login_id);


	@Query(value="CALL getSampleProductList(:div_code,:utype,:login_id);", nativeQuery=true)
	List<DashBoardData> getSampleProductList(@Param("div_code") int div_code,	@Param("utype") int utype,@Param("login_id") int login_id);

}
