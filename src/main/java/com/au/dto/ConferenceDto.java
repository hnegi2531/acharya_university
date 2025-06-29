package com.au.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.au.model.ConferenceType;
import com.au.model.NationalityType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class ConferenceDto {

	@NotNull(message = "Employee ID cannot be null")
    private Integer empId;

    @NotBlank(message = "Nature of visit cannot be blank")
    private String conferenceNatureOfVisit;

    @NotBlank(message = "State cannot be blank")
    private String state;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotNull(message = "From date cannot be null")
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yy")
    private Date fromDate;

    @NotNull(message = "To date cannot be null")
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yy")
    private Date toDate;

    @NotNull(message = "Nationality type cannot be null")
    private NationalityType nationalityType;

    @NotNull(message = "Conference type cannot be null")
    private ConferenceType conferenceType;

    private String paperTitle;
    
    @NotNull(message = "Priority cannot be null")
    private Integer priority;

    @NotBlank(message = "Organizer cannot be blank")
    private String organizer;
	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getConferenceNatureOfVisit() {
		return conferenceNatureOfVisit;
	}

	public void setConferenceNatureOfVisit(String conferenceNatureOfVisit) {
		this.conferenceNatureOfVisit = conferenceNatureOfVisit;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public NationalityType getNationalityType() {
		return nationalityType;
	}

	public void setNationalityType(NationalityType nationalityType) {
		this.nationalityType = nationalityType;
	}

	public ConferenceType getConferenceType() {
		return conferenceType;
	}

	public void setConferenceType(ConferenceType conferenceType) {
		this.conferenceType = conferenceType;
	}

	public String getPaperTitle() {
		return paperTitle;
	}

	public void setPaperTitle(String paperTitle) {
		this.paperTitle = paperTitle;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	
	
}
