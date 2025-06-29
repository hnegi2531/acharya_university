package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "job_profile")
public class JobProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer job_id;
	private String firstname;
	//private String last_name;
	private Character gender;
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dateofbirth;
	//private String father_name;
	private String mobile;
	// private String residential_contact_no;
	@Email()
	private String email;
	private Character martial_status;
	// private String spouse_name;
	//private String door_no;
	// private String building;
	private String street;
	private String locality;
	// private String city;
	// private String state;
	private Integer pincode;
	// private String present_address;
	private String key_skills;
	// private Integer exp_in_years;
	// private Integer exp_in_months;
	// private Integer annual_salary_lakhs; // last_ctc
	private String link;
	private String linkedin_id;
	private String resume_headline;
	// private String department_choice;
	// private Boolean applied_before;
	// @JsonFormat(pattern = "dd-MM-yyyy")
	// private Date earlier_applied_date;
	// private String earlier_applied_position;
	// private Boolean refer_by_acharya_emp;
	// private String name_dep;
	// private String acharyan_contact;
	// private Boolean refer_by_any;
	// private String reference_name_dept;
	// private String reference_contact;
	private String current_location; // (combination of door+building+street+locality)
	private String reference_no;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	// private String hod_comment;
	// private Integer created_by;
	// private Integer modified_by;
	private Integer active;
	private Integer country_id;
	private Integer state_id;
	private Integer city_id;
	private Integer mail_sent_status;
	private Integer comment_status;
	private Integer mail_sent_to_candidate;
	private Integer emp_code_status;
	private Integer job_type_id;
	private Integer designation_id;
	
	@Size(max=30)
	private String hr_status;
	
	private String hr_remark;
	@Lob
	private String hr_feedback_attachment;
	private String marks_scored;
	private String mailSentToCandidateDate;
	private String mailSentToInterviewersDate;

	public JobProfile() {
		super();
	}

	public Integer getJob_id() {
		return job_id;
	}

	public void setJob_id(Integer job_id) {
		this.job_id = job_id;
	}

//	public String getLast_name() {
//		return last_name;
//	}
//
//	public void setLast_name(String last_name) {
//		this.last_name = last_name;
//	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public Date getDateofbirth() {
		return dateofbirth;
	}

	public void setDateofbirth(Date dateofbirth) {
		this.dateofbirth = dateofbirth;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


//	public String getDoor_no() {
//		return door_no;
//	}
//
//	public void setDoor_no(String door_no) {
//		this.door_no = door_no;
//	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	public String getKey_skills() {
		return key_skills;
	}

	public void setKey_skills(String key_skills) {
		this.key_skills = key_skills;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLinkedin_id() {
		return linkedin_id;
	}

	public void setLinkedin_id(String linkedin_id) {
		this.linkedin_id = linkedin_id;
	}

	public String getResume_headline() {
		return resume_headline;
	}

	public void setResume_headline(String resume_headline) {
		this.resume_headline = resume_headline;
	}

	public String getCurrent_location() {
		return current_location;
	}

	public void setCurrent_location(String current_location) {
		this.current_location = current_location;
	}

	public String getReference_no() {
		return reference_no;
	}

	public void setReference_no(String reference_no) {
		this.reference_no = reference_no;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Date getModified_date() {
		return modified_date;
	}

	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Integer getCountry_id() {
		return country_id;
	}

	public void setCountry_id(Integer country_id) {
		this.country_id = country_id;
	}

	public Integer getState_id() {
		return state_id;
	}

	public void setState_id(Integer state_id) {
		this.state_id = state_id;
	}

	public Integer getCity_id() {
		return city_id;
	}

	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}

	public Character getMartial_status() {
		return martial_status;
	}

	public void setMartial_status(Character martial_status) {
		this.martial_status = martial_status;
	}

	public Integer getMail_sent_status() {
		return mail_sent_status;
	}

	public void setMail_sent_status(Integer mail_sent_status) {
		this.mail_sent_status = mail_sent_status;
	}

	public Integer getComment_status() {
		return comment_status;
	}

	public void setComment_status(Integer comment_status) {
		this.comment_status = comment_status;
	}

	public Integer getMail_sent_to_candidate() {
		return mail_sent_to_candidate;
	}

	public void setMail_sent_to_candidate(Integer mail_sent_to_candidate) {
		this.mail_sent_to_candidate = mail_sent_to_candidate;
	}

	public Integer getEmp_code_status() {
		return emp_code_status;
	}

	public void setEmp_code_status(Integer emp_code_status) {
		this.emp_code_status = emp_code_status;
	}

	public Integer getJob_type_id() {
		return job_type_id;
	}

	public void setJob_type_id(Integer job_type_id) {
		this.job_type_id = job_type_id;
	}

	public Integer getDesignation_id() {
		return designation_id;
	}

	public void setDesignation_id(Integer designation_id) {
		this.designation_id = designation_id;
	}

	public String getHr_status() {
		return hr_status;
	}

	public void setHr_status(String hr_status) {
		this.hr_status = hr_status;
	}

	public String getHr_remark() {
		return hr_remark;
	}

	public void setHr_remark(String hr_remark) {
		this.hr_remark = hr_remark;
	}

	public String getHr_feedback_attachment() {
		return hr_feedback_attachment;
	}

	public String setHr_feedback_attachment(String hr_feedback_attachment) {
		return this.hr_feedback_attachment = hr_feedback_attachment;
	}

	public String getMarks_scored() {
		return marks_scored;
	}

	public void setMarks_scored(String marks_scored) {
		this.marks_scored = marks_scored;
	}

	public String getMailSentToCandidateDate() {
		return mailSentToCandidateDate;
	}

	public void setMailSentToCandidateDate(String mailSentToCandidateDate) {
		this.mailSentToCandidateDate = mailSentToCandidateDate;
	}

	public String getMailSentToInterviewersDate() {
		return mailSentToInterviewersDate;
	}

	public void setMailSentToInterviewersDate(String mailSentToInterviewersDate) {
		this.mailSentToInterviewersDate = mailSentToInterviewersDate;
	}


	
}
