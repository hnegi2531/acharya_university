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

@Entity
@Table(name = "meal_vendor_assignment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealVendorAssignment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer meal_vendor_assignment_id;
//	@Column(unique = true)
	private Integer meal_id;
//	@Column(unique = true)
//	private Integer vendor_id;
	private Integer voucher_head_new_id;
	private Double rate_per_count;
	private String remarks;

	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;

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
	private boolean active;

	private Integer refreshment_id;

}
