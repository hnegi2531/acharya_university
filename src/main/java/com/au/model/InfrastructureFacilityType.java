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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "infrastructure_facility_type")
public class InfrastructureFacilityType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer facility_type_id;
	
	@Column(unique = true)
	private String facility_type_name;
	@Column(unique = true)
	private String facility_short_name;
	@Column(unique = true)
	private String facility_code;
	private String description;
	private Integer timetable_status;
	private String remarks;
	private Boolean active;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	
	private Boolean tt_status;
	
}
