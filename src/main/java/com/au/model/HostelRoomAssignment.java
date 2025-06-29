package com.au.model;

import java.util.Date;

import javax.annotation.Generated;
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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name ="hostel_room_assignment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HostelRoomAssignment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer hostelRoomAssignmentId;
	private String name;
	private String contact;
	private String email;
	private String idProofNo;
	private Date fromDate;
	private Date toDate;
	private String status;
	@ManyToOne
	@JoinColumn(name="hostelRoomId")
	private HostelRooms hostelRoom;
	
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
	@Column(updatable = false)
	private String createdUsername;
	private String modifiedUsername;
	private Boolean active;
	private Boolean vacant=false;
	

}
