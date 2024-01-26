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

}
