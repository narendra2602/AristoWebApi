package com.aristowebapi.response;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginResponse {

	private int loginId;
	private String fname;
	private int utype;
	private String lastLoginDate;
	private String lastLoginTime;
	private List <DivResponse> divisions;
	private List <BranchResponse> branches;
	private List<ReportTabResponse> tabResponse;
}
