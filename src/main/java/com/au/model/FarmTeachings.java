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
@Table(name = "farm_teachings")
public class FarmTeachings {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer teachingsId;
	private Integer empId;
	private String month;
	private String year;
	private String teachingAchieved;
	private String teachingProposed;
	private Integer teachingMaxMarks;
	private Integer teachingSelfMarks;
	private String innovationsAchieved;
	private String innovationsProposed;
	private Integer innovationsMaxMarks;
	private Integer innovationsSelfMarks;
	private String courseReductionAchieved;
	private String courseReductionProposed;
	private Integer courseReductionMaxMarks;
	private Integer courseReductionSelfMarks;
	private String proctoringStdAchieved;
	private String proctoringStdProposed;
	private Integer proctoringStdMaxMarks;
	private Integer proctoringStdSelfMarks;	
	private Integer teachingHodMarks;
	private Integer innovationsHodMarks;
	private Integer courseReductionHodMarks;
	private Integer proctoringStdHodMarks;
	private String attachmentPath;
	
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
