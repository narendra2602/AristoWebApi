package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.UserRights;
import com.aristowebapi.entity.UserDepo;

public interface UserRightsBranchDao  extends JpaRepository<UserDepo, Integer>{

	@Query(value = "select b.depo_code val,b.depo_name name,ifnull(u.status,'N') user_status,ifnull(u.id,0) id from branch_comp b "
			   + " left join user_branch08 u on b.depo_code=u.depo_code and  user_id=:userId  order by b.depo_name;", nativeQuery = true)
			List<UserRights> getDepoList(@Param("userId") int userId);
	
}
