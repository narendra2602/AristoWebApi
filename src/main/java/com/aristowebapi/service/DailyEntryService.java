package com.aristowebapi.service;

import com.aristowebapi.dto.DailyEntry;
import com.aristowebapi.request.DailyEntryListRequest;
import com.aristowebapi.response.ApiResponse;
import com.aristowebapi.response.DailyEntryListResponse;

public interface DailyEntryService {
	
	DailyEntry saveDailyEntry(DailyEntry dailyentry);
	
	ApiResponse<DailyEntryListResponse> getDailyEntryList(DailyEntryListRequest request);

}
