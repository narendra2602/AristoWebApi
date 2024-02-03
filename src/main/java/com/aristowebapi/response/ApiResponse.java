package com.aristowebapi.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiResponse<T> {
	
	private String title;
	private int noOfRecords;
	@JsonIgnore
	private String lastUpdate;
	private int index;
	private List<T> data;
	
	
	public ApiResponse(String title,int noOfRecords,String lastUpdate,List<T> data)
	{
		this.title=title;
		this.noOfRecords=noOfRecords;
		this.lastUpdate=lastUpdate;
		this.data=data;
		
	}
	

	public ApiResponse(String title,int noOfRecords,List<T> data)
	{
		this.title=title;
		this.noOfRecords=noOfRecords;
		this.data=data;
	}

	public ApiResponse(String title,int noOfRecords,List<T> data,int index)
	{
		this.title=title;
		this.noOfRecords=noOfRecords;
		this.data=data;
		this.index=index;

	}
}
