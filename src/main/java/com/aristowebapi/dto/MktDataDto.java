package com.aristowebapi.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Entity
@Table(name="GrossSale")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MktDataDto {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private int pcode;
	private String pname;
	private String pack;
	private String depo_name;
	private int sales_val;
}
