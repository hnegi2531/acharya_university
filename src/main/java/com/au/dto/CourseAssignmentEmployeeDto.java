package com.au.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
public class CourseAssignmentEmployeeDto {

	
	   private Integer user_id;
	   private List<Integer> course_id; 
	    private String remark;
	   
	    
	    
	    @Column(updatable = false)
		private Integer created_by;
		private Integer modified_by;

		@Column(updatable = false)
		@Temporal(TemporalType.TIMESTAMP)
		@CreationTimestamp
		private Date created_date;

		@Temporal(TemporalType.TIMESTAMP)
		@UpdateTimestamp
		private Date modified_date;

		private Boolean active;
		
		@Column(updatable = false)
		private String created_username;
		private String modified_username;
}
