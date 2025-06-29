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
@Table(name = "hostel_blocks")
public class HostelBlocks {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer hostelBlockId;
	
	private String blockName;
	private String blockShortName;	
	private String hostelType;	
	private String doctorsName;	
	private Integer serviceProviderId;	
	private Integer wardensId;	
	private Integer noOfFreeBeds;
	private Integer totalBeds;
	private Integer totalFreeRooms;	
	private Integer totalNoRooms;
	private Integer totalFloors;	
	private String address;	
	private Integer noOfOccupiedBed;
	private Integer noOfBedUnderMaintenance;	
	private String remarks;
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
	private Boolean active;
	
	
}
