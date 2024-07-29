package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.DashBoardData;
import com.aristowebapi.dto.UserRights;
import com.aristowebapi.entity.UserTer;

public interface UserRightsTerDao extends JpaRepository<UserTer	, Integer>{
	
	@Query(value = "select h.ter_code val,h.ter_name name,ifnull(u.status,'N') user_status,ifnull(u.id,0) id  from aris.hqmast h" + 
			"	   left join aristo_web.user_ter u on h.depo_code=u.depo_code and h.ter_code=u.ter_code " + 
			"	   and  u.user_id=:userId where h.mkt_year=:myear and h.div_code=:div_code and h.depo_code=:depo_code and h.ter_code> 0  order by h.ter_code", nativeQuery = true)
	List<UserRights> geTerList(@Param("userId") int userId,@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code);
	

	@Query(value = "select d.div_code val,d. div_name name from divmast d " + 
			"   join userdiv u on d.div_code=u.div_code where  u.user_id=:userId and u.user_status='Y' order by d.div_code ", nativeQuery = true)
	List<DashBoardData> getDivList(@Param("userId") int userId);

	@Query(value = "select d.depo_code val,d.depo_name name from branch_comp d " + 
			"   join user_branch08 u on  d.depo_code=u.depo_code  where user_id=:userId and status='Y' order by d.depo_name   ", nativeQuery = true)
	List<DashBoardData> getBranchList(@Param("userId") int userId);

}
