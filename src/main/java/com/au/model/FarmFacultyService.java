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
@Table(name = "farm_faculty_service")
public class FarmFacultyService {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer facultyServiceId;
	private Integer empId;
	private String month;
	private String year;
	private String universityColgAchieved;
	private String universityColgProposed;
	private Integer universityColgMaxMarks;
	private Integer universityColgSelfMarks;
	private String administrativeColgAchieved;
	private String administrativeColgProposed;
	private Integer administrativeColgMaxMarks;
	private Integer administrativeColgSelfMarks;
	private String professionalServiceAchieved;
	private String professionalServiceProposed;
	private Integer professionalServiceMaxMarks;
	private Integer professionalServiceSelfMarks;
	private String communityServiceAchieved;
	private String communityServiceProposed;
	private Integer communityServiceMaxMarks;
	private Integer communityServiceSelfMarks;	
	private String awardsAchieved;
	private String awardsProposed;
	private Integer awardsMaxMarks;
	private Integer awardsSelfMarks;
	private Integer universityColgHodMarks;	
	private Integer administrativeColgHodMarks;
	private Integer professionalServiceHodMarks;
	private Integer communityServiceHodMarks;
	private Integer awardsHodMarks;
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
