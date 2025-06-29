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
@Table(name = "hostel_beds")
public class HostelBeds {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer hostelBedId;	
	
	private Integer hostelBedNumber;	
	private String bedName;	
	private Integer hostelsFloorId;	
	private Integer hostelsBlockId;	
	private Integer hostelsRoomId;

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
	@Column(updatable = false)
	private String createdUsername;
	private String modifiedUsername;
	private String bedStatus;
	private Integer ac_year_id;
	
}
