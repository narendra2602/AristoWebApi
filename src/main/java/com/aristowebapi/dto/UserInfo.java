package com.aristowebapi.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; 
  
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

	  
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;
	@Column(name="login_name")
    private String loginName; 
    private String password; 
	@Column(name="f_name")
    private String fname;
	@Column(name="user_type")
	private int userType;
	@Column(name="last_login_date_time")
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
    private Date lastLoginDateTime;
    private String roles; 
}
