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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="library_assigment")
public class LibraryAssigment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer libraryAssigmentId;
	
	private Integer libraryId;
	
	private Integer currentSem;
	
	private Integer programSpecailizationId;
	
	
	private Integer instituteId;
	
	private Date renewalPeriod;
	
	private Integer totalCheckOuts;
	
	private Integer totalRenewals;
	
	private Integer createdBy;
	
	private Double finePerDay;

	
	private String dueDate;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	private Boolean isIssueAvailable;
}

