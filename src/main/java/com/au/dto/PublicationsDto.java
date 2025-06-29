package com.au.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.au.model.NationalityType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class PublicationsDto {

	@NotNull(message = "Please enter the empId")
	private Integer empId;
	
	@NotNull(message = "Please enter journalName")
	@NotBlank(message = "Please enter journalName")
	private String journalName;
	
	@NotNull(message = "Please enter journalName")
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yy")
	private Date date;
	
	@NotNull(message = "Please enter volume")
	@NotBlank(message = "Please enter volume")
	private String volume;
	
	@NotNull(message = "Please set the issueNumber")
	private Long issueNumber;
	
	@NotNull(message = "Please enter paperTitle")
	@NotBlank(message = "Please enter paperTitle")
	private String paperTitle;
	
	private NationalityType type;
	
	@NotNull(message = "Please set the priority")
	private Integer priority;

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getJournalName() {
		return journalName;
	}

	public void setJournalName(String journalName) {
		this.journalName = journalName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public Long getIssueNumber() {
		return issueNumber;
	}

	public void setIssueNumber(Long issueNumber) {
		this.issueNumber = issueNumber;
	}

	public String getPaperTitle() {
		return paperTitle;
	}

	public void setPaperTitle(String paperTitle) {
		this.paperTitle = paperTitle;
	}

	public NationalityType getType() {
		return type;
	}

	public void setType(NationalityType type) {
		this.type = type;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	
}
