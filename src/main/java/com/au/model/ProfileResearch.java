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
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter 
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="profile_research")
public class ProfileResearch {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer profileResearchId;
	private Integer empId;
	private String universityRegisterNumber;
	private String tenureStatus;
	private String universityName;
	private String titleOfThesis;
	private Integer peerViewed;
	private Integer noOfConferences;
	private String professionalOrganisation;
	private String partOfResearchProject;
	private Integer yesNumberOfProjects;
	private String keywordsResearch;
	private String techniquesExpert;
	private String currentProfessional;
	private String areasOfExpertise;
	private String researchForCollaboration;
	private String researchAttachment;
	private String phdRegisterDate;
	private String phdCompletedDate;
	private String googleScholar;
	private String otherCitationDatabase;
	private String phdHolderPursuing;
	private String phdCount;
	private String linkedInLink;
	
	
	
	private Integer createdBy;
	private Integer modifiedBy;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;

	private String createdUsername;
	private String modifiedUsername;
	private Boolean active;
	

}
