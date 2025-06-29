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

import lombok.Data;

@Data
@Table(name = "farm_locks")
@Entity
public class FarmLocks {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer lockId;
	private Integer empId;
	private String month;
	private String year;
	private Integer facultyLock;
	private Integer hodLock;
	private Integer principalLock;
	private String challengesFaced;
	
	private Boolean active;
	
	@Column(updatable = false)
	private Integer createdBy;
	private Integer modifiedBy;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;

	@Column(updatable = false)
	private String createdUsername;
	private String modifiedUsername;

}
