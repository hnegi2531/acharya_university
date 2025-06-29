package com.au.model;

import java.util.Date;
import java.util.List;

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
@Table(name = "hostel_warden")
public class HostelWarden {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer wardenId;
	private String wardenName;
	
//	private Integer usersId;
	private String address;
//	private String city;
//	private String state;
	private String mobile;
//	private String country;
	
	private String emailId;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;
	
	@Column(updatable = false)
	private Integer createdBy;
	private Integer modifiedBy;
	private Boolean active;
	private String createdUsername;
	private String modifiedUsername;
	

}
