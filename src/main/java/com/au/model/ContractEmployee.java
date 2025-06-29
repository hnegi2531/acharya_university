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
@Table(name = "contract_employee")
public class ContractEmployee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer contract_emp_id;
	private String contract_empcode;
	private String master_code;
	private String contract_emp_name;
	private String mobile_no;
	private String dob;
	private String email_id;
	private String father_name;
	private String date_of_joining;
	private String experience;
	private String designation;
	private Integer shift_id;
	private String contract_emp_type;
	private String sex;
	private String address;
	private Integer job_type_id;
	private Float consolidated_amount;
	private String pincode;
	private Integer school_id;
	private String school;
	private Integer dept_id;
	private Integer reporting_to;
	private String pan_no;
	private String bank_name;
	private String account_no;
	private String bank_branch;
	private String ifsc;
	private String remarks;
	private String from_date;
	private String to_date;
	private String subject_skills;
	private String attachment_file_name;
	private String attachment_file_path;
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	private Integer created_by;
	private Integer modified_by;
	private String created_username;
	private String modified_username;
	private boolean active;
	private Integer new_join_status;
	private String transport_from_month;
	private Integer vehicle_route_id;
	private Integer proctor_assign_status;
	private Integer transport_assign_status;
	private String dlno;
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dlexpno;
	private String passportno;
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date passportexpno;
	private String bloodgroup;
	private Integer job_id;
	private String aadhar;

	public ContractEmployee() {
		super();
	}

	public Integer getContract_emp_id() {
		return contract_emp_id;
	}

	public void setContract_emp_id(Integer contract_emp_id) {
		this.contract_emp_id = contract_emp_id;
	}

	public String getContract_empcode() {
		return contract_empcode;
	}

	public void setContract_empcode(String contract_empcode) {
		this.contract_empcode = contract_empcode;
	}

	public String getMaster_code() {
		return master_code;
	}

	public void setMaster_code(String master_code) {
		this.master_code = master_code;
	}

	public String getContract_emp_name() {
		return contract_emp_name;
	}

	public void setContract_emp_name(String contract_emp_name) {
		this.contract_emp_name = contract_emp_name;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getFather_name() {
		return father_name;
	}

	public void setFather_name(String father_name) {
		this.father_name = father_name;
	}

	public String getDate_of_joining() {
		return date_of_joining;
	}

	public void setDate_of_joining(String date_of_joining) {
		this.date_of_joining = date_of_joining;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Integer getShift_id() {
		return shift_id;
	}

	public void setShift_id(Integer shift_id) {
		this.shift_id = shift_id;
	}

	public String getContract_emp_type() {
		return contract_emp_type;
	}

	public void setContract_emp_type(String contract_emp_type) {
		this.contract_emp_type = contract_emp_type;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getJob_type_id() {
		return job_type_id;
	}

	public void setJob_type_id(Integer job_type_id) {
		this.job_type_id = job_type_id;
	}

	public Float getConsolidated_amount() {
		return consolidated_amount;
	}

	public void setConsolidated_amount(Float consolidated_amount) {
		this.consolidated_amount = consolidated_amount;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public Integer getDept_id() {
		return dept_id;
	}

	public void setDept_id(Integer dept_id) {
		this.dept_id = dept_id;
	}

	public Integer getReporting_to() {
		return reporting_to;
	}

	public void setReporting_to(Integer reporting_to) {
		this.reporting_to = reporting_to;
	}

	public String getPan_no() {
		return pan_no;
	}

	public void setPan_no(String pan_no) {
		this.pan_no = pan_no;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getBank_branch() {
		return bank_branch;
	}

	public void setBank_branch(String bank_branch) {
		this.bank_branch = bank_branch;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getFrom_date() {
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public String getTo_date() {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

	public String getSubject_skills() {
		return subject_skills;
	}

	public void setSubject_skills(String subject_skills) {
		this.subject_skills = subject_skills;
	}

	public String getAttachment_file_name() {
		return attachment_file_name;
	}

	public void setAttachment_file_name(String attachment_file_name) {
		this.attachment_file_name = attachment_file_name;
	}

	public String getAttachment_file_path() {
		return attachment_file_path;
	}

	public void setAttachment_file_path(String attachment_file_path) {
		this.attachment_file_path = attachment_file_path;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Integer getNew_join_status() {
		return new_join_status;
	}

	public void setNew_join_status(Integer new_join_status) {
		this.new_join_status = new_join_status;
	}

	public String getTransport_from_month() {
		return transport_from_month;
	}

	public void setTransport_from_month(String transport_from_month) {
		this.transport_from_month = transport_from_month;
	}

	public Integer getVehicle_route_id() {
		return vehicle_route_id;
	}

	public void setVehicle_route_id(Integer vehicle_route_id) {
		this.vehicle_route_id = vehicle_route_id;
	}

	public Integer getProctor_assign_status() {
		return proctor_assign_status;
	}

	public void setProctor_assign_status(Integer proctor_assign_status) {
		this.proctor_assign_status = proctor_assign_status;
	}

	public Integer getTransport_assign_status() {
		return transport_assign_status;
	}

	public void setTransport_assign_status(Integer transport_assign_status) {
		this.transport_assign_status = transport_assign_status;
	}

	public String getDlno() {
		return dlno;
	}

	public void setDlno(String dlno) {
		this.dlno = dlno;
	}

	public Date getDlexpno() {
		return dlexpno;
	}

	public void setDlexpno(Date dlexpno) {
		this.dlexpno = dlexpno;
	}

	public String getPassportno() {
		return passportno;
	}

	public void setPassportno(String passportno) {
		this.passportno = passportno;
	}

	public Date getPassportexpno() {
		return passportexpno;
	}

	public void setPassportexpno(Date passportexpno) {
		this.passportexpno = passportexpno;
	}

	public String getBloodgroup() {
		return bloodgroup;
	}

	public void setBloodgroup(String bloodgroup) {
		this.bloodgroup = bloodgroup;
	}

	public Integer getJob_id() {
		return job_id;
	}

	public void setJob_id(Integer job_id) {
		this.job_id = job_id;
	}

	public String getAadhar() {
		return aadhar;
	}

	public void setAadhar(String aadhar) {
		this.aadhar = aadhar;
	}

}
