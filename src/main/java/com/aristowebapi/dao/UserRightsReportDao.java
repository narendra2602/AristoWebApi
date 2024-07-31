package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.UserRightsReport;
import com.aristowebapi.entity.UserReports;

public interface UserRightsReportDao extends JpaRepository<UserReports, Integer>{

	
	
	@Query(value = "select r.tab_id,t.tab_name,r.repo_id,r.repo_name,ifnull(u.user_status,'N') user_status,ifnull(u.id,0) id from repo_master r "+
	" join tab_master t on  r.tab_id=t.tabid and r.user_lock='Y' "+
	" left join user_rights u  on  r.repo_id= u.repo_id and  u.user_id=:userId "+
     " order by r.tab_id,r.repo_id  ;", nativeQuery = true)
	 List<UserRightsReport> getReportList(@Param("userId") int userId);
}
