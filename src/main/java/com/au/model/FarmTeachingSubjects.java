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

import lombok.Data;

@Data
@Entity
@Table(name = "farm_teaching_subjects")
public class FarmTeachingSubjects {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer teachingSubjectId;
	private Integer empId;
	private String month;
	private String year;
	private Integer courseAssignmentId;
	private Integer currentYear;
	private Integer currentSem;
	private String modules;
	private Boolean useOfIctTool;
	private Integer duration;
	private Integer totalClasses;
	private Boolean active;
	private String courseType;
	
	@Column(updatable = false)
	private Integer createdBy;
	private Integer modifiedBy;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;

	@Column(updatable = false)
	private String createdUsername;
	private String modifiedUsername;
	
	

}
