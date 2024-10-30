package com.aristowebapi.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
 
@Entity
@Data
@NoArgsConstructor
@Table(name ="iqviauploadrecord")
public class IqviaUploadRecord {

	    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) 
	    private long id;
		private int user_id;
		private String file_path;
		private String file_name;
//		private Date upload_time;
		private int total_records;
		private int records_uploaded;
	
}
