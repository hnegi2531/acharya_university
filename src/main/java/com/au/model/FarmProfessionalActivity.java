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
@Table(name = "farm_professional_activity")
public class FarmProfessionalActivity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer professionalActivityId;
	private Integer empId;
	private String month;
	private String year;
	private String professionalAchieved;
	private String professionalProposed;
	private Integer professionalMaxMarks;
	private Integer professionalSelfMarks;
	private String publicationsAchieved;
	private String publicationsProposed;
	private Integer publicationsMaxMarks;
	private Integer publicationsSelfMarks;
	private String conferencesAchieved;
	private String conferencesProposed;
	private Integer conferencesMaxMarks;
	private Integer conferencesSelfMarks;
	private String workProgressAchieved;
	private String workProgressProposed;
	private Integer workProgressMaxMarks;
	private Integer workProgressSelfMarks;	
	private String grantApplicationsAchieved;
	private String grantApplicationsProposed;
	private Integer grantApplicationsMaxMarks;
	private Integer grantApplicationsSelfMarks;
	private Integer professionalHodMarks;	
	private Integer publicationsHodMarks;
	private Integer conferencesHodMarks;
	private Integer workProgressHodMarks;
	private Integer grantApplicationsHodMarks;
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
