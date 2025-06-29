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
@Table(name= "farm_research_progress")
public class FarmResearchProgress {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer researchProgressId;
	private Integer empId;
	private String month;
	private String year;
	private String activityName;
	private String title;
	private String fromDate;
	private String toDate;
	private String plannedWork;
	private String status;
	private String activityDetails;
	private String progressSummary;
	
	private Boolean active;
	
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
