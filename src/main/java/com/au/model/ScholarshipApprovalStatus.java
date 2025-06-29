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
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "scholarship_approved_status")
public class ScholarshipApprovalStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer scholarship_approved_status_id;
	private Integer scholarship_id;
	private Integer candidate_id;
	
	@Column(unique = true)
	private Integer student_id;
	private Integer counselor_id;
	private String is_approved;
	private Integer approved_amount;
	private String is_verified;
	private String verified_by;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date verified_date;

	@Column( updatable = false)
	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date applied_date;

	private Integer approved_by;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date approved_date;

	private String approval;
	private Float year1_amount;
	private Float year2_amount;
	private Float year3_amount;
	private Float year4_amount;
	private Float year5_amount;
	private Float year6_amount;
	private Float year7_amount;
	private Float year8_amount;
	private String doc1;
	private String comments;
	private Float year9_amount;
	private Float year10_amount;
	private Float prev_approved_amount;
	private Boolean pre_approval_status;
	private String pre_approval_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_approved_amount_date;
	
	private String cancel_remarks;

	@Temporal(TemporalType.TIMESTAMP)
	private Date cancel_date;

	private Integer verified_amount;
	private Float year11_amount;
	private Float year12_amount;
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
	private Boolean active;
	
	private String verifier_remarks;
	private String pre_approver_remarks;
	private Integer pre_approver_by;
	private String requestedByRemarks;
	private String ipAddress;
	private Integer cancelBy;
	private String adminRemarks;

	

}
