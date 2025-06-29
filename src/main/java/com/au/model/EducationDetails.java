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
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "education_details")
public class EducationDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer edu_id;
	private Integer job_id;
	private Integer graduation_id;
	private String graduation;
	private String school;
	private String university;
	private Float academic_score;
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date academic_year_joining;
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date academic_year_completed;
//	@CreationTimestamp
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(updatable = false)
//	private Date created_date;
//	@UpdateTimestamp
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date modified_date;
	private Boolean active;

	private String convocation;
	private String attach;
	public EducationDetails() {
		super();
	}

	public Integer getEdu_id() {
		return edu_id;
	}

	public void setEdu_id(Integer edu_id) {
		this.edu_id = edu_id;
	}

	public Integer getJob_id() {
		return job_id;
	}

	public void setJob_id(Integer job_id) {
		this.job_id = job_id;
	}

	public Integer getGraduation_id() {
		return graduation_id;
	}

	public void setGraduation_id(Integer graduation_id) {
		this.graduation_id = graduation_id;
	}

	public String getGraduation() {
		return graduation;
	}

	public void setGraduation(String graduation) {
		this.graduation = graduation;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public Float getAcademic_score() {
		return academic_score;
	}

	public void setAcademic_score(Float academic_score) {
		this.academic_score = academic_score;
	}

	public Date getAcademic_year_joining() {
		return academic_year_joining;
	}

	public void setAcademic_year_joining(Date academic_year_joining) {
		this.academic_year_joining = academic_year_joining;
	}

	public Date getAcademic_year_completed() {
		return academic_year_completed;
	}

	public void setAcademic_year_completed(Date academic_year_completed) {
		this.academic_year_completed = academic_year_completed;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getConvocation() {
		return convocation;
	}

	public void setConvocation(String convocation) {
		this.convocation = convocation;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}



	

}
