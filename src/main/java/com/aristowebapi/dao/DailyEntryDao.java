package com.aristowebapi.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristowebapi.dto.DailyEntry;

public interface DailyEntryDao extends JpaRepository<DailyEntry, Integer>{

}
