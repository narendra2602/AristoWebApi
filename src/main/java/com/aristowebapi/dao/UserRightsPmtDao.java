package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.UserRightsPmt;
import com.aristowebapi.entity.UserPmt;

public interface UserRightsPmtDao  extends JpaRepository<UserPmt	, Integer>{
	
	
/*	@Query(value = "select g.gp_code val ,g.gp_name name,ifnull(p.status,'N') user_status,ifnull(p.id,0) id  from aris.grpmast g" + 
			"   left join aristo_web.pmt_group p on g.div_code=p.div_code and g.gp_code=p.gp_code " + 
			"     and  p.user_id=:userId where g.div_code=:div_code order by g.gp_name;", nativeQuery = true)
	List<UserRights> gePmtGroupList(@Param("userId") int userId,@Param("div_code") int div_code);
*/
	@Query(value="CALL PmtUserRights(:userId);", nativeQuery=true)
	List<UserRightsPmt> getPmtGroupList(@Param("userId") int userId);

	
}
