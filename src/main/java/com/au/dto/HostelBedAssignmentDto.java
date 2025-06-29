package com.au.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.au.model.HostelRooms;

import lombok.Data;

@Data
public class HostelBedAssignmentDto {
	
	private Integer hostelBlockId;
	private Integer hostelFloorId;
	private Integer hostelRoomId;
	private String commentsVacate;
	private Integer acYearId;
	private Integer hostelBedId;
	private Integer studentId;
	private Integer hostelFeeTemplateId;
	private String remarks;
	@Temporal(TemporalType.DATE)
	private Date fromDate;
	@Temporal(TemporalType.DATE)
	private Date toDate;
	private Boolean confirmJoin;
	private Integer vacateBy;
	private Integer bookingType;
	private Integer cancelHostel;
	private String cancelledRemarks;
	private String studentCancelledRemarks;
	private String hostelCancelledAttachmentPath;
	private String hostelCancelledAttachmentFileName;
	private Integer photoUploadStatus;
	private String foodStatus;
	private Integer idCardAcStatus;
	private Boolean active;
	private String bedStatus;
	private Date expectedJoiningDate;
	private Integer assigned_year;

}
