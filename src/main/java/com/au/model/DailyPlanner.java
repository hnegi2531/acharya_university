package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "daily_planner")
public class DailyPlanner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer daily_planner_id;
	
	 private Integer emp_id;
	 private String task_title;
	 private String task_priority;
	 private String description;
	 private String task_status;
	 private String from_date;
	 private String to_date;
	 private String from_time;
	 private String to_time;
	 
	 private String type;
	 private String contribution_type;
	 private String task_type;
	 
	 	@Column(updatable = false)
		@Temporal(TemporalType.TIMESTAMP)
		@CreationTimestamp
		private Date created_date;

		@Temporal(TemporalType.TIMESTAMP)
		@UpdateTimestamp
		private Date modified_date;
		
		@Column(updatable = false)
		private Integer created_by;
		
		private Integer modified_by;
		private Boolean active;
		
		@Column(updatable = false)
		private String created_username;
		private String modified_username;
}
