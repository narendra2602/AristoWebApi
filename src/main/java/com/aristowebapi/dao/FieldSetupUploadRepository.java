package com.aristowebapi.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristowebapi.entity.IqviaUploadRecord;

public interface FieldSetupUploadRepository  extends JpaRepository<IqviaUploadRecord, Long>{

}
