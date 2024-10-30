package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.Iqvia;
import com.aristowebapi.dto.MktDataDto;

public interface IqviaDao extends JpaRepository<MktDataDto, Integer>{
	
	@Query(value="CALL aristo_web.iqvia_procnew(:div_code,:depo_code,:userType,:userId,:gpCode);", nativeQuery=true)
	List<Iqvia> getIqvia(@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("userType") int userType,@Param("userId") int userId,@Param("gpCode") int gpCode);

	@Query(value = "SELECT depo_name FROM branch_comp where depo_code=:depo", nativeQuery = true)
	String getBranch(@Param("depo") int depo);

}
