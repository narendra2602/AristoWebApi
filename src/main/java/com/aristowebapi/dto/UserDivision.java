package com.aristowebapi.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="userdiv")
public class UserDivision {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;
	private int user_id;
	private int div_code;
	private String user_status;

}