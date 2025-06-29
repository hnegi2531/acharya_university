package com.au.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CoursesUndergoneDto {

    @NotNull(message = "Employee ID cannot be null")
    private Integer empId;

    @NotBlank(message = "Course title cannot be blank")
    private String courseTitle;

    @NotBlank(message = "Duration cannot be blank")
    private String duration;

    @NotBlank(message = "Unit cannot be blank")
    private String unit;

    @NotBlank(message = "Year cannot be blank")
    private String year;

    @NotBlank(message = "Conducted by cannot be blank")
    private String conductedBy;

    @NotBlank(message = "Institution cannot be blank")
    private String institution;

    @NotNull(message = "Priority cannot be null")
    private Integer priority;

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getConductedBy() {
		return conductedBy;
	}

	public void setConductedBy(String conductedBy) {
		this.conductedBy = conductedBy;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	
}
