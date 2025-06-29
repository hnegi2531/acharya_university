package com.au.model;

import java.util.Date;

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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="hostel_bed_change")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostelBedChange {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer hostelBedChangeId;
	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student_Details student;
	
	@ManyToOne
	@JoinColumn(name = "hostel_block_change_id")
	private HostelBlocks hostelBlock;
	
	@ManyToOne
	@JoinColumn(name = "hostel_room_change_id")
	private HostelRooms hostelRoom;
	
	@ManyToOne
	@JoinColumn(name = "hostel_fee_template_change_id")
	private HostelFeeTemplate hostelFeeTemplate;
	
	@ManyToOne
	@JoinColumn(name = "bed_change_id")
	private HostelBeds hostelBed;
	private String approveStatus;
	
	@ManyToOne
	@JoinColumn(name = "old_hosel_bed_assignment_id")
	private HostelBedAssignment hoselBedAssignment;
	
	@ManyToOne
	@JoinColumn(name = "approver_id")
	private UserAuthentication approver;
	
	@ManyToOne
	@JoinColumn(name = "ac_year_id")
	private Academic_year acYear;
	
	@ManyToOne
	@JoinColumn(name = "previous_hostel_fee_template_id")
	private HostelFeeTemplate previousHostelFeeTemplate;
	
	@ManyToOne
	@JoinColumn(name = "previous_hostel_bed_id")
	private HostelBeds previousHostelBed;
	
	@ManyToOne
	@JoinColumn(name = "previous_hostel_room_id")
	private HostelRooms previousHostelRoom;
	
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
}
