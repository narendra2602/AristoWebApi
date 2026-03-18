package com.aristowebapi.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aristowebapi.entity.AbmDraftReportEntity;

@Repository
public interface AbmDraftReportRepository extends JpaRepository<AbmDraftReportEntity, Long>{


	
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<AbmDraftReportEntity> findByDraftId(Long draftId);
	
	
	
	Optional<AbmDraftReportEntity> 
	findByLoginIdAndMnthCodeAndMyear(int loginId, int mnthCode, int myear);
	
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT d FROM AbmDraftReportEntity d WHERE d.draftId = :draftId")
	Optional<AbmDraftReportEntity> findByDraftIdForUpdate(@Param("draftId") Long draftId);
	
	
//	AbmDraftReportEntity findByDraftId(Long draftId);
	
	
	
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
