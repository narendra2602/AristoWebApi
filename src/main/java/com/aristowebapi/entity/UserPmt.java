package com.aristowebapi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="pmt_group")
public class UserPmt {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;
	private int user_id;
	private int gp_code;
	private int div_code;
	private String status;

}