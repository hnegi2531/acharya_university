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
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hostel_rooms_history")
public class HostelRoomsHistory {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Integer hostelRoomsHistoryId;
	private Integer hostelRoomId;
	private String roomName;
	private String standardAccessories;
	private Integer roomTypeId;
	private Integer templateId;
	private Integer hostelsBlockId;
	private Integer hostelsFloorId;
	

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
	private Boolean active;
	@Column(updatable = false)
	private String createdUsername;
	private String modifiedUsername;
	private Integer room_creation_number;
	private Integer ac_year_id;

}
