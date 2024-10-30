package com.aristowebapi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aristowebapi.dto.IqviaUploadRecordDto;
import com.aristowebapi.entity.IqviaUploadRecord;

public interface IqviaUploadRepository extends JpaRepository<IqviaUploadRecord, Long> {
	
	
	@Query(value="CALL getIqviaUploadedRecords(:login_id);", nativeQuery=true)
	IqviaUploadRecordDto getIqviaUploadedRecords(@Param("login_id") int login_id);


}
