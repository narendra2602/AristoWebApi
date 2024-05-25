package com.aristowebapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.Iqvia;
import com.aristowebapi.dto.MktDataDto;

public interface IqviaDao extends JpaRepository<MktDataDto, Integer>{
	
	@Query(value="CALL aristo_web.iqvia_procnew(:myear,:div_code,:depo_code,:mon);", nativeQuery=true)
	List<Iqvia> getIqvia(@Param("myear") int myear,@Param("div_code") int div_code,@Param("depo_code") int depo_code,
			@Param("mon") int mon);

	@Query(value = "SELECT depo_name FROM branch_comp where depo_code=:depo", nativeQuery = true)
	String getBranch(@Param("depo") int depo);

}
