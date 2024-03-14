package com.aristowebapi.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristowebapi.dao.DailyEntryDao;
import com.aristowebapi.dto.DailyEntry;
import com.aristowebapi.service.DailyEntryService;

@Service
public class DailyEntryServiceImpl implements DailyEntryService{

	@Autowired
	private DailyEntryDao dailyeEntryDao;
	
	@Override
	public DailyEntry saveDailyEntry(DailyEntry dailyentry) {
		
		return dailyeEntryDao.save(dailyentry);
		
	}

}
