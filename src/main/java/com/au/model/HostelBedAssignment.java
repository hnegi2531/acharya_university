package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name ="hostel_bed_assignment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HostelBedAssignment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer hostelBedAssignmentId;
	
	@ManyToOne
	@JoinColumn(name = "hostelBlockId")
	private HostelBlocks hostelBlock;
	
	@ManyToOne
	@JoinColumn(name = "hostelFloorId")
	private HostelFloor hostelFloor;
	
	@ManyToOne
	@JoinColumn(name = "hostelRoomId")
	private HostelRooms hostelRoom;
	
	private String commentsVacate;
	
	@ManyToOne
	@JoinColumn(name = "acYearId")
	private Academic_year acYear;
	
	@ManyToOne
	@JoinColumn(name = "hostelBedId")
	private HostelBeds hostelBed;
	
	@ManyToOne
	@JoinColumn(name = "studentId")
	private Student_Details student;
	
	@ManyToOne
	@JoinColumn(name = "hostelFeeTemplateId")
	private HostelFeeTemplate hostelFeeTemplate;
	
	private String remarks;
	
	@Temporal(TemporalType.DATE)
	private Date fromDate;
	
	@Temporal(TemporalType.DATE)
	private Date toDate;
	
	private Boolean confirmJoin;
	@ManyToOne
	@JoinColumn(name = "vacateBy")
	private UserAuthentication vacateBy;
	private Integer bookingType;
	private Integer cancelHostel;
	
	private String cancelledRemarks;
	private String studentCancelledRemarks;
	private String hostelCancelledAttachmentPath;
	private String hostelCancelledAttachmentFileName;
	private Integer photoUploadStatus;
	private String foodStatus;
	private Integer idCardAcStatus;
	private Integer assigned_year;
	
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
	
	@Temporal(TemporalType.DATE)
	private Date expectedJoiningDate;
	

}
