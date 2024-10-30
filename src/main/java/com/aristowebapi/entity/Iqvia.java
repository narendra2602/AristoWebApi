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
@Table(name ="iqvia")
public class Iqvia {

	//@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
	 @Id
	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
	 @SequenceGenerator(name = "seqGen", sequenceName = "seq", initialValue = 1, allocationSize = 100)    
	private Long id;
	private  String  Tag;
	private  String  branch;
	private  String  molecule;
	private  String  rank_mat1;
	private  String  rank_mat2;
	private  String  rank_mat3;
	private  String  rank_mat4;
	private  String  rank_mat5;
	private  String  product;
	private  String  compay;
	private  String  division;
	private  String  launch;
	private  String  mat_val_1;
	private  String  mat_val_2;
	private  String  mat_val_3;
	private  String  mat_val_4;
	private  String  mat_val_5;
	private  String  mat_msper_1;
	private  String  mat_msper_2;
	private  String  mat_msper_3;
	private  String  mat_msper_4;
	private  String  mat_msper_5;
	private  String  mat_msglper_1;
	private  String  mat_msglper_2;
	private  String  mat_msglper_3;
	private  String  mat_msglper_4;
	private  String  mat_grtper_1;
	private  String  mat_grtper_2;
	private  String  mat_grtper_3;
	private  String  mat_grtper_4;
	
	private  String  cy_cumm_val;
	private  String  cumm_grt_per;
	private  String  mat_unit_1;
	private  String  mat_unit_2;
	private  String  mat_unit_3;
	private  String  mat_unit_4;
	private  String  mat_unit_5;
	private  String  umat_msper_1;
	private  String  umat_msper_2;
	private  String  umat_msper_3;
	private  String  umat_msper_4;
	private  String  umat_msper_5;
	private  String  umat_msglper_1;
	private  String  umat_msglper_2;
	private  String  umat_msglper_3;
	private  String  umat_msglper_4;
	private  String  umat_grtper_1;
	private  String  umat_grtper_2;
	private  String  umat_grtper_3;
	private  String  umat_grtper_4;
	
	private  String  ucy_cumm_val;
	private  String  ucumm_grt_per;
	
	private  String  qtrval_5;
	private  String  qtrval_6;
	private  String  qtrval_7;
	private  String  qtrval_8;
	
	private  String  qtr_msper_5;
	private  String  qtr_msper_6;
	private  String  qtr_msper_7;
	private  String  qtr_msper_8;
	private  String  qtr_msgl_1;
	private  String  qtr_msgl_2;
	private  String  qtr_msgl_3;
	private  String  qtr_msgl_4;

	private  String  qtr_grt_5;
	private  String  qtr_grt_6;
	private  String  qtr_grt_7;
	private  String  qtr_grt_8;

	private  String  qtr_unit_5;
	private  String  qtr_unit_6;
	private  String  qtr_unit_7;
	private  String  qtr_unit_8;

	private  String  qtr_unitms_5;
	private  String  qtr_unitms_6;
	private  String  qtr_unitms_7;
	private  String  qtr_unitms_8;
	private  String  qtr_unitmsgl_1;
	private  String  qtr_unitmsgl_2;
	private  String  qtr_unitmsgl_3;
	private  String  qtr_unitmsgl_4;

	private  String  qtr_unitmsgrt_5;
	private  String  qtr_unitmsgrt_6;
	private  String  qtr_unitmsgrt_7;
	private  String  qtr_unitmsgrt_8;

	private  String  mnth_val13;
	private  String  mnth_val14;
	private  String  mnth_val15;
	private  String  mnth_val16;
	private  String  mnth_val17;
	private  String  mnth_val18;
	private  String  mnth_val19;
	private  String  mnth_val20;
	private  String  mnth_val21;
	private  String  mnth_val22;
	private  String  mnth_val23;
	private  String  mnth_val24;

	private  String  mnth_valms_13;
	private  String  mnth_valms_14;
	private  String  mnth_valms_15;
	private  String  mnth_valms_16;
	private  String  mnth_valms_17;
	private  String  mnth_valms_18;
	private  String  mnth_valms_19;
	private  String  mnth_valms_20;
	private  String  mnth_valms_21;
	private  String  mnth_valms_22;
	private  String  mnth_valms_23;
	private  String  mnth_valms_24;
	private  String  mnth_valgl_1;
	private  String  mnth_valgl_2;
	private  String  mnth_valgl_3;
	private  String  mnth_valgl_4;
	private  String  mnth_valgl_5;
	private  String  mnth_valgl_6;
	private  String  mnth_valgl_7;
	private  String  mnth_valgl_8;
	private  String  mnth_valgl_9;
	private  String  mnth_valgl_10;
	private  String  mnth_valgl_11;
	private  String  mnth_valgl_12;
	private  String  mnth_valgrt_1;
	private  String  mnth_valgrt_2;
	private  String  mnth_valgrt_3;
	private  String  mnth_valgrt_4;
	private  String  mnth_valgrt_5;
	private  String  mnth_valgrt_6;
	private  String  mnth_valgrt_7;
	private  String  mnth_valgrt_8;
	private  String  mnth_valgrt_9;
	private  String  mnth_valgrt_10;
	private  String  mnth_valgrt_11;
	private  String  mnth_valgrt_12;

	private  String  mnth_unit13;
	private  String  mnth_unit14;
	private  String  mnth_unit15;
	private  String  mnth_unit16;
	private  String  mnth_unit17;
	private  String  mnth_unit18;
	private  String  mnth_unit19;
	private  String  mnth_unit20;
	private  String  mnth_unit21;
	private  String  mnth_unit22;
	private  String  mnth_unit23;
	private  String  mnth_unit24;

	private  String  mnth_unitms_13;
	private  String  mnth_unitms_14;
	private  String  mnth_unitms_15;
	private  String  mnth_unitms_16;
	private  String  mnth_unitms_17;
	private  String  mnth_unitms_18;
	private  String  mnth_unitms_19;
	private  String  mnth_unitms_20;
	private  String  mnth_unitms_21;
	private  String  mnth_unitms_22;
	private  String  mnth_unitms_23;
	private  String  mnth_unitms_24;
	private  String  mnth_unitgl_1;
	private  String  mnth_unitgl_2;
	private  String  mnth_unitgl_3;
	private  String  mnth_unitgl_4;
	private  String  mnth_unitgl_5;
	private  String  mnth_unitgl_6;
	private  String  mnth_unitgl_7;
	private  String  mnth_unitgl_8;
	private  String  mnth_unitgl_9;
	private  String  mnth_unitgl_10;
	private  String  mnth_unitgl_11;
	private  String  mnth_unitgl_12;
	private  String  mnth_unitgrt_1;
	private  String  mnth_unitgrt_2;
	private  String  mnth_unitgrt_3;
	private  String  mnth_unitgrt_4;
	private  String  mnth_unitgrt_5;
	private  String  mnth_unitgrt_6;
	private  String  mnth_unitgrt_7;
	private  String  mnth_unitgrt_8;
	private  String  mnth_unitgrt_9;
	private  String  mnth_unitgrt_10;
	private  String  mnth_unitgrt_11;
	private  String  mnth_unitgrt_12;

}
