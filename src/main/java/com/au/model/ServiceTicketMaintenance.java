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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name = "service_ticket_maintenance")
public class ServiceTicketMaintenance {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer serviceTicketId;
	private Integer complaintAttendedBy;
	private String complaintStage;
	private String complaintStatus;
	private String remarks;
	private String purchaseNeed;
	private String dateOfAttended;
	private String dateOfClosed;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
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
	
	private boolean active;
	private Integer instituteId;
	private Integer branchId;
	private String attendedByUserName;
	
	private String complaintDetails;
	private String floorAndExtension;
	private Integer userId;
	private Long serviceTypeId;
	private Integer attendedBy;

	private Integer financial_year_id;
	private Integer blockId;
	private String attachment_path;

	private Integer deptId;
	private String date;
	
	private String from_date;
	private String to_date;
	private String program_id;
	private String year_sem;
	private String program_specialization_id;
	private Boolean event_status;
	private Integer event_id;
}
