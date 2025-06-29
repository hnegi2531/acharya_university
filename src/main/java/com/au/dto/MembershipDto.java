package com.au.dto;

import java.time.Year;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class MembershipDto {

	@NotNull(message = "All fields Required")
    private Integer empId;
	
	@NotNull(message = "All fields Required")
	@NotBlank(message = "All fields Required")
	@NotEmpty(message = "All fields Required")
	private String society;
	
	@NotNull(message = "All fields Required")
	@Pattern(regexp = "^(?:\\d{4})$" , message = "Year should be in YYYY")
	private String yearOfJoining;
	
	@NotNull(message = "All fields Required")
	@NotBlank(message = "All fields Required")
	@NotEmpty(message = "All fields Required")
	private String natureOdMembership;
	
	@NotNull(message = "All fields Required")
	private Integer priority;

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getSociety() {
		return society;
	}

	public void setSociety(String society) {
		this.society = society;
	}



	public String getYearOfJoining() {
		return yearOfJoining;
	}

	public void setYearOfJoining(String yearOfJoining) {
		this.yearOfJoining = yearOfJoining;
	}

	public String getNatureOdMembership() {
		return natureOdMembership;
	}

	public void setNatureOdMembership(String natureOdMembership) {
		this.natureOdMembership = natureOdMembership;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

}
