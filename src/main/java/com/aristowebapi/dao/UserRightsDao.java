package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.UserDivision;
import com.aristowebapi.dto.UserRights;

public interface UserRightsDao extends JpaRepository<UserDivision, Integer>{
	
	
	@Query(value = "select d.div_code val,d.div_name name,ifnull(u.user_status,'N') user_status,ifnull(u.id,0) id from divmast d  "
			+ "left join userdiv u on  d.div_code=u.div_code and  u.user_id=:userId  order by d.div_code;", nativeQuery = true)
	List<UserRights> getDivisionList(@Param("userId") int userId);
	
	
	
}
