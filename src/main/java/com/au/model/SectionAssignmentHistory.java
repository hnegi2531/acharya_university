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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "section_assignment_history")
public class SectionAssignmentHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer section_assignment_history_id;
	private Integer section_assignment_id;
	private Integer section_id;
	private Integer school_id; 
	private Integer program_id;
	private Integer program_specialization_id;
	private Integer ac_year_id;
	private Integer current_year_sem;
	@Lob
	@Column(columnDefinition="LONGTEXT")
	private String student_ids;
	private String remarks;
	@Lob
	@Column(columnDefinition="LONGTEXT")
	private String emp_ids;
	private String contract_emp_ids;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date created_date;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified_date;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private boolean active;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Integer program_assignment_id;
	
	public SectionAssignmentHistory() {
		super();
	}

	public Integer getSection_assignment_history_id() {
		return section_assignment_history_id;
	}

	public void setSection_assignment_history_id(Integer section_assignment_history_id) {
		this.section_assignment_history_id = section_assignment_history_id;
	}

	public Integer getSection_assignment_id() {
		return section_assignment_id;
	}

	public void setSection_assignment_id(Integer section_assignment_id) {
		this.section_assignment_id = section_assignment_id;
	}

	public Integer getSection_id() {
		return section_id;
	}

	public void setSection_id(Integer section_id) {
		this.section_id = section_id;
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

	public Integer getCurrent_year_sem() {
		return current_year_sem;
	}

	public void setCurrent_year_sem(Integer current_year_sem) {
		this.current_year_sem = current_year_sem;
	}

	public String getStudent_ids() {
		return student_ids;
	}

	public void setStudent_ids(String student_ids) {
		this.student_ids = student_ids;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getEmp_ids() {
		return emp_ids;
	}

	public void setEmp_ids(String emp_ids) {
		this.emp_ids = emp_ids;
	}

	public String getContract_emp_ids() {
		return contract_emp_ids;
	}

	public void setContract_emp_ids(String contract_emp_ids) {
		this.contract_emp_ids = contract_emp_ids;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public Integer getProgram_assignment_id() {
		return program_assignment_id;
	}

	public void setProgram_assignment_id(Integer program_assignment_id) {
		this.program_assignment_id = program_assignment_id;
	}
	
	
}
