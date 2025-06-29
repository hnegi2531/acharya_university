package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name ="student_permission")
public class StudentPermission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer studentPermissionId;
	
	private String auid;
	private String studentName;
	private Integer currentYear;
	private Integer currentSem;
	private String permissionType;
	private Float totalDue;
	private String permittedBy;
	private Integer allowSem;
	private Boolean isAllowExamPermit=Boolean.FALSE;
	private Boolean isAllowAttendencePermit=Boolean.FALSE;
	private Boolean isAllowPartFeePermit=Boolean.FALSE;
	private Boolean isAllowFineWaiver=Boolean.FALSE;
	private String tillDate;
	private String attachment;
	@Lob
	private String remarks;
	
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	@Column(updatable = false)
	private int created_by;
	private int modified_by;
	private Boolean active;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;

}
