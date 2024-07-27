package com.aristowebapi.response;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserReportRightsTabResponse {

	private int userId;
	private int tabId;
	private String tabName;
	private List<UserReportRightsMenuResponse> menuList;
}
