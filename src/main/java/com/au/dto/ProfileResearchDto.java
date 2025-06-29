package com.au.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

public class ProfileResearchDto {

	
	private List<ProfileResearchInterest> researchInterests;
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

	public ProfileResearchDto() {
		super();
	}
	
	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getUniversityRegisterNumber() {
		return universityRegisterNumber;
	}

	public void setUniversityRegisterNumber(String universityRegisterNumber) {
		this.universityRegisterNumber = universityRegisterNumber;
	}

	public String getTenureStatus() {
		return tenureStatus;
	}

	public void setTenureStatus(String tenureStatus) {
		this.tenureStatus = tenureStatus;
	}

	public String getTitleOfThesis() {
		return titleOfThesis;
	}

	public void setTitleOfThesis(String titleOfThesis) {
		this.titleOfThesis = titleOfThesis;
	}

	public String getProfessionalOrganisation() {
		return professionalOrganisation;
	}

	public void setProfessionalOrganisation(String professionalOrganisation) {
		this.professionalOrganisation = professionalOrganisation;
	}

	public String getPartOfResearchProject() {
		return partOfResearchProject;
	}

	public void setPartOfResearchProject(String partOfResearchProject) {
		this.partOfResearchProject = partOfResearchProject;
	}

	public String getResearchForCollaboration() {
		return researchForCollaboration;
	}

	public void setResearchForCollaboration(String researchForCollaboration) {
		this.researchForCollaboration = researchForCollaboration;
	}

	public String getResearchAttachment() {
		return researchAttachment;
	}

	public void setResearchAttachment(String researchAttachment) {
		this.researchAttachment = researchAttachment;
	}

	public String getPhdRegisterDate() {
		return phdRegisterDate;
	}

	public void setPhdRegisterDate(String phdRegisterDate) {
		this.phdRegisterDate = phdRegisterDate;
	}

	public String getPhdCompletedDate() {
		return phdCompletedDate;
	}

	public void setPhdCompletedDate(String phdCompletedDate) {
		this.phdCompletedDate = phdCompletedDate;
	}

	public String getGoogleScholar() {
		return googleScholar;
	}

	public void setGoogleScholar(String googleScholar) {
		this.googleScholar = googleScholar;
	}

	public String getPhdHolderPursuing() {
		return phdHolderPursuing;
	}

	public void setPhdHolderPursuing(String phdHolderPursuing) {
		this.phdHolderPursuing = phdHolderPursuing;
	}

	public String getPhdCount() {
		return phdCount;
	}

	public void setPhdCount(String phdCount) {
		this.phdCount = phdCount;
	}

	public String getLinkedInLink() {
		return linkedInLink;
	}

	public void setLinkedInLink(String linkedInLink) {
		this.linkedInLink = linkedInLink;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getCreatedUsername() {
		return createdUsername;
	}

	public void setCreatedUsername(String createdUsername) {
		this.createdUsername = createdUsername;
	}

	public String getModifiedUsername() {
		return modifiedUsername;
	}

	public void setModifiedUsername(String modifiedUsername) {
		this.modifiedUsername = modifiedUsername;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<ProfileResearchInterest> getResearchInterests() {
		return researchInterests;
	}

	public void setResearchInterests(List<ProfileResearchInterest> researchInterests) {
		this.researchInterests = researchInterests;
	}

	public String getUniversityName() {
		return universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}

	public String getOtherCitationDatabase() {
		return otherCitationDatabase;
	}

	public void setOtherCitationDatabase(String otherCitationDatabase) {
		this.otherCitationDatabase = otherCitationDatabase;
	}

	public Integer getPeerViewed() {
		return peerViewed;
	}

	public void setPeerViewed(Integer peerViewed) {
		this.peerViewed = peerViewed;
	}

	public Integer getNoOfConferences() {
		return noOfConferences;
	}

	public void setNoOfConferences(Integer noOfConferences) {
		this.noOfConferences = noOfConferences;
	}

	public Integer getYesNumberOfProjects() {
		return yesNumberOfProjects;
	}

	public void setYesNumberOfProjects(Integer yesNumberOfProjects) {
		this.yesNumberOfProjects = yesNumberOfProjects;
	}





	
}
