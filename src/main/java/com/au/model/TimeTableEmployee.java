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

@Entity
@Table(name = "time_table_employee")
public class TimeTableEmployee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer time_table_employee_id;
	private Integer emp_id;
	private Integer time_table_id;
	private Integer section_assignment_id;
	private Integer batch_assignment_id;
	private Integer time_slots_id;
	@Temporal(TemporalType.DATE)
	private Date selected_date;
	private Boolean active;
	private Integer subject_assignment_id;
	
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
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	
	private Integer program_assignment_id;
	
	public TimeTableEmployee() {
		super();
	}

	public Integer getTime_table_employee_id() {
		return time_table_employee_id;
	}

	public void setTime_table_employee_id(Integer time_table_employee_id) {
		this.time_table_employee_id = time_table_employee_id;
	}

	public Integer getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(Integer emp_id) {
		this.emp_id = emp_id;
	}

	public Integer getTime_table_id() {
		return time_table_id;
	}

	public void setTime_table_id(Integer time_table_id) {
		this.time_table_id = time_table_id;
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

	public Integer getSection_assignment_id() {
		return section_assignment_id;
	}

	public void setSection_assignment_id(Integer section_assignment_id) {
		this.section_assignment_id = section_assignment_id;
	}

	public Integer getTime_slots_id() {
		return time_slots_id;
	}

	public void setTime_slots_id(Integer time_slots_id) {
		this.time_slots_id = time_slots_id;
	}

	public Date getSelected_date() {
		return selected_date;
	}

	public void setSelected_date(Date selected_date) {
		this.selected_date = selected_date;
	}

	public Integer getBatch_assignment_id() {
		return batch_assignment_id;
	}

	public void setBatch_assignment_id(Integer batch_assignment_id) {
		this.batch_assignment_id = batch_assignment_id;
	}

	public Integer getSubject_assignment_id() {
		return subject_assignment_id;
	}

	public void setSubject_assignment_id(Integer subject_assignment_id) {
		this.subject_assignment_id = subject_assignment_id;
	}

	public Integer getProgram_assignment_id() {
		return program_assignment_id;
	}

	public void setProgram_assignment_id(Integer program_assignment_id) {
		this.program_assignment_id = program_assignment_id;
	}
	
	
	
	
}
