package com.au.model;

import java.util.Date;

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

@Entity
@Table(name = "lock_dates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LockDates {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer lock_id;
	
	private Integer lock_month;
	

	private Integer lock_year;
	
	private Date leave_lock_date;
	private Date payroll_lock_date;
	
	private Integer active;
	
	private String created_by;
	private String modified_by;
	private String remarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	
}
