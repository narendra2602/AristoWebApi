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
@Table(name ="user_branch08")
public class UserDepo {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;
	private int user_id;
	private int depo_code;
	private String status;

}
