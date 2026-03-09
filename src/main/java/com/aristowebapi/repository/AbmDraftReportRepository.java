package com.aristowebapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aristowebapi.entity.AbmDraftReportEntity;

@Repository
public interface AbmDraftReportRepository extends JpaRepository<AbmDraftReportEntity, Long>{

	AbmDraftReportEntity findByDraftId(Long draftId);
	
	
	List<AbmDraftReportEntity> findByMnthCodeAndMyearAndLoginId(int mnthCode, int myear, int loginId);
	
	List<AbmDraftReportEntity> findByDivCodeAndMnthCodeAndMyear(int divCode,int mnthCode, int myear);
	
	List<AbmDraftReportEntity> findByMnthCodeAndMyearAndEmpCode(int mnthCode, int myear, int empCode);
	
	boolean existsByLoginIdAndMnthCodeAndMyear(int loginId,int mnthCode,int myear);

	@Query(value = "SELECT DISTINCT depo_code FROM user_branch08  WHERE user_id = :userId and depo_code not in(32,90,98,99,58)", nativeQuery = true)
	List<Integer> findDepoCodesByUserId(@Param("userId") int userId);	
	
/*	List<AbmDraftReportEntity> findByDivCodeAndMnthCodeAndMyearAndDepoCodeIn(
	        int divCode, int mnthCode, int myear, List<Integer> depoCodes);
*/
	
	List<AbmDraftReportEntity> findByDivCodeAndMnthCodeAndMyearAndDepoCodeIn(
	        int divCode,
	        int mnthCode,
	        int myear,
	        List<Integer> depoCodes);
	
}
