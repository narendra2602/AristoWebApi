package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.LoginDto;
import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.ReportMenuDto;

public interface LoginDao extends JpaRepository<MktDataDto, Integer> { 
	
	@Query(value = "Select id,f_name,opt,last_ldate,last_ltime from login where login_name=:userName and password=:password and status=:active", nativeQuery = true)
	LoginDto authenticateUser(@Param("userName") String userName,@Param("password") String password,@Param("active") String active);


	@Query(value = "select r.tab_id,t.tab_name,r.repo_id,r.repo_name from repo_master r,tab_master t where r.tab_id=t.tabid and r.user_lock='Y' and r.repo_id in (select repo_id from user_rights where user_id=:userId and user_status='Y')  order by r.tab_id,r.repo_name ", nativeQuery = true)
	List<ReportMenuDto> getMenuList(@Param("userId") int userId);

//	@Query(value = "Select distinct concat(b.ter_name,'-',u.u_date) msg from upload u,a_branch08 b where u.depo_code=:depoCode and u.depo_code = b.depo_code   ", nativeQuery = true)
//	String getMessage(@Param("depoCode") int depoCode);

/*	@Query(value = "Select max(date_format(u.u_date,'%d/%m/%Y %h:%i:%s'))  msg from upload u  ", nativeQuery = true)
	String getMessage();
*/
	@Query(value = "Select concat(m.msg,' ',max(date_format(u.u_date,'%d/%m/%Y %h:%i:%s')))  msg from aristo_web.upload u,	(SELECT distinct message msg FROM aristo_web.MSG) m ", nativeQuery = true)
	String getMessage();
//	SELECT CONCAT(m.msg,' ',MAX(DATE_FORMAT(u.u_date,'%d/%m/%Y %h:%i:%s')))  msg FROM aristo_web.upload u,	(SELECT DISTINCT message msg FROM aristo_web.MSG) m ;
}
