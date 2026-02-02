package com.aristowebapi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name ="fieldsetup")

public class FieldSetup {
	
	@Id
	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
	 @SequenceGenerator(name = "seqGen", sequenceName = "seq", initialValue = 1, allocationSize = 100)    
	private Long id;
	private  String  bu;
	private  String  level_3;
	private  String  level_2;
	private  String  level_1;;
	private  String  location;
	private  String  level;
	private  String  user_name;
	private  String  user_code;
	private int login_id;
	private String password;
	private String user_role;
	private String designation;
	private String user_type;
	private String parent_location;
	private String reporting_to;
	private int geoseqno;
	private int geodtseqno;
}
