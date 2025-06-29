package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name = "no_dues_assignment")
@Data
public class NoDuesAssignment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no_dues_assignment_id;
	@ManyToOne
	@JoinColumn(name = "emp_id")
	private EmployeeDetails employee_details;
	@ManyToOne
	@JoinColumn(name = "dept_id")
	private Department department;
	private Boolean no_due_status;
	private String comments;
	@ManyToOne
	@JoinColumn(name = "resignation_id")
	private Resignation resignation;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	@Column(name = "created_by", updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;
	
	private String ip_address;
	
	private Integer approver_id;
	private String approver_date;
}
