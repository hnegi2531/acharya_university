package com.au.model;

import java.util.Date;
import javax.persistence.Basic;
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
@Table(name = "preadmission_process")
public class PreAdmissionProcess {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pre_admission_id;
	private String student_name;
	private Integer school_id;
	private Integer program_id;
	private Integer program_specialization_id;
	private Integer ac_year_id;
	private Boolean is_scholarship;

	@Basic
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date date_of_birth;

	private String address;
	private Integer fee_admission_category_id;
	private Integer fee_admission_sub_category_id;
	private Integer fee_template_id;

	private String bank_details;
	private String process_type;
	private Boolean active;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	private String gender;
	private String account_name;
	private String account_number;
	private String ifsc_code;
	private String bank_branch;
	private String father_name;
	private String mobile;

	private Integer candidate_id;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@Column(updatable = false)
	private Date program_start;

	private Integer receipt;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Boolean is_hostel;
	private Integer program_assignment_id;
	private Boolean mail_sent_status;
	private Integer hostel_fee_template_id;

	public Boolean getIs_scholarship() {
		return is_scholarship;
	}

	public void setIs_scholarship(Boolean is_scholarship) {
		this.is_scholarship = is_scholarship;
	}

	public String getCreated_username() {
		return created_username;
	}

	public void setCreated_username(String created_username) {
		this.created_username = created_username;
	}

	public String getModified_username() {
		return modified_username;
	}

	public void setModified_username(String modified_username) {
		this.modified_username = modified_username;
	}

	public PreAdmissionProcess() {
		super();
	}

	public Integer getFee_admission_sub_category_id() {
		return fee_admission_sub_category_id;
	}

	public void setFee_admission_sub_category_id(Integer fee_admission_sub_category_id) {
		this.fee_admission_sub_category_id = fee_admission_sub_category_id;
	}

	public Integer getPre_admission_id() {
		return pre_admission_id;
	}

	public void setPre_admission_id(Integer pre_admission_id) {
		this.pre_admission_id = pre_admission_id;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getProgram_id() {
		return program_id;
	}

	public void setProgram_id(Integer program_id) {
		this.program_id = program_id;
	}

	public Integer getProgram_specialization_id() {
		return program_specialization_id;
	}

	public void setProgram_specialization_id(Integer program_specialization_id) {
		this.program_specialization_id = program_specialization_id;
	}

	public Integer getAc_year_id() {
		return ac_year_id;
	}

	public void setAc_year_id(Integer ac_year_id) {
		this.ac_year_id = ac_year_id;
	}

	public Date getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getFee_admission_category_id() {
		return fee_admission_category_id;
	}

	public void setFee_admission_category_id(Integer fee_admission_category_id) {
		this.fee_admission_category_id = fee_admission_category_id;
	}

	public Integer getFee_template_id() {
		return fee_template_id;
	}

	public void setFee_template_id(Integer fee_template_id) {
		this.fee_template_id = fee_template_id;
	}

	public String getBank_details() {
		return bank_details;
	}

	public void setBank_details(String bank_details) {
		this.bank_details = bank_details;
	}

	public String getProcess_type() {
		return process_type;
	}

	public void setProcess_type(String process_type) {
		this.process_type = process_type;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Integer getModified_by() {
		return modified_by;
	}

	public void setModified_by(Integer modified_by) {
		this.modified_by = modified_by;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getAccount_number() {
		return account_number;
	}

	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}

	public String getIfsc_code() {
		return ifsc_code;
	}

	public void setIfsc_code(String ifsc_code) {
		this.ifsc_code = ifsc_code;
	}

	public String getBank_branch() {
		return bank_branch;
	}

	public void setBank_branch(String bank_branch) {
		this.bank_branch = bank_branch;
	}

	public String getFather_name() {
		return father_name;
	}

	public void setFather_name(String father_name) {
		this.father_name = father_name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getCandidate_id() {
		return candidate_id;
	}

	public void setCandidate_id(Integer candidate_id) {
		this.candidate_id = candidate_id;
	}

	public Date getProgram_start() {
		return program_start;
	}

	public void setProgram_start(Date program_start) {
		this.program_start = program_start;
	}

	public Integer getReceipt() {
		return receipt;
	}

	public void setReceipt(Integer receipt) {
		this.receipt = receipt;
	}

	public Boolean getIs_hostel() {
		return is_hostel;
	}

	public void setIs_hostel(Boolean is_hostel) {
		this.is_hostel = is_hostel;
	}

	public Integer getProgram_assignment_id() {
		return program_assignment_id;
	}

	public void setProgram_assignment_id(Integer program_assignment_id) {
		this.program_assignment_id = program_assignment_id;
	}

	public Boolean getMail_sent_status() {
		return mail_sent_status;
	}

	public void setMail_sent_status(Boolean mail_sent_status) {
		this.mail_sent_status = mail_sent_status;
	}

	public Integer getHostel_fee_template_id() {
		return hostel_fee_template_id;
	}

	public void setHsotel_fee_template_id(Integer hostel_fee_template_id) {
		this.hostel_fee_template_id = hostel_fee_template_id;
	}
}