package com.aristowebapi.response;

import com.aristowebapi.request.UserRightsPmtRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRightsHqResponse {
	
	
	private int val;
	private int divCode;
	private int depoCode;
	private String name;
	private String userStatus;
	private int id;

}
