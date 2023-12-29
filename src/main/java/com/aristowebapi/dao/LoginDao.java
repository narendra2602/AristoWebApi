package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.BranchMasterDto;
import com.aristowebapi.dto.DivDto;
import com.aristowebapi.dto.LoginDto;
import com.aristowebapi.dto.MktDataDto;
import com.aristowebapi.dto.ReportMenuDto;

public interface LoginDao extends JpaRepository<MktDataDto, Integer> {
	
	@Query(value = "Select id,f_name,opt,last_ldate,last_ltime from login where login_name=:userName and password=:password and status=:active", nativeQuery = true)
	LoginDto authenticateUser(@Param("userName") String userName,@Param("password") String password,@Param("active") String active);

	@Query(value = "select div_code,div_name from divmast where div_code in (select div_code from userdiv where user_id=:userId and user_status='Y' order by div_code) ", nativeQuery = true)
	List<DivDto> getDivList(@Param("userId") int userId);

	@Query(value = "select depo_code,depo_name from branch_comp where depo_code in (select depo_code from user_branch08 where user_id=:userId and status='Y')  order by depo_code ", nativeQuery = true)
	List<BranchMasterDto> getBranchList(@Param("userId") int userId);

	@Query(value = "select r.tab_id,t.tab_name,r.repo_id,r.repo_name from repo_master08 r,tab_master08 t where r.tab_id=t.tabid and r.repo_id in (select repo_id from user_rights08 where user_id=:userId and status='Y')  order by r.tab_id,r.repo_id ", nativeQuery = true)
	List<ReportMenuDto> getMenuList(@Param("userId") int userId);
	
	
}
