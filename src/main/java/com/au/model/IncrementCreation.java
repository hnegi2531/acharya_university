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

import lombok.Data;


@Data
@Entity
@Table(name = "increment_creation")
public class IncrementCreation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long incrementCreationId;
	
	private Integer empId;

	private String previousDesignation;

	private String proposedDesignation;
	private Integer previousDesignationId;
	private String previousDepartment;
	private String proposedDepartment;
	private Integer previousDepartmentId;
	private String previousSalaryStructure;
	private String proposedSalaryStructure;
	private Integer previousSalaryStructureId;
	private Float previousBasic;
	private Float proposedBasic;
	private Float previousSplPay;
	private Float proposedSplPay;
	private Float previousGrosspay;
	private Float proposedGrosspay;
	private Float previousCtc;
	private Float proposedCtc;
	private Float previousTa;
	private Float proposedTa;
	private Float grossDifference;
	private Float ctcDifference;
	private Integer month;
	private Integer year;
	@Lob
	private String remarks;
	
	private Integer createdBy;
	
	private Integer proposedDesignationId;
	private Integer proposedDepartmentId;
	private Integer proposedSalaryStructureId;
	
	private Float previousMedicalReimburesment;
	private Float proposedMedicalReimburesment;
	private String empCode;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	private Boolean active;
	
	private Boolean isFinalize=Boolean.FALSE;
	private Boolean isApproved=Boolean.FALSE;
	private Boolean isRejected=Boolean.FALSE;
	private String attachmentPath;
	private String attachmentPathType;
	
	public IncrementCreation(String empCode, String proposedDesignation,String proposedDepartment,String proposedSalaryStructure, String proposedBasic,
			String proposedSplPay, String proposedGrosspay, String proposedCtc, String remarks,Integer month,Integer year ) {
		this.empCode=empCode;
		this.proposedDesignation=proposedDesignation;
		this.proposedDepartment=proposedDepartment;
		this.proposedSalaryStructure=proposedSalaryStructure;
		this.proposedSplPay=parseFloat(proposedSplPay) ;
		this.proposedGrosspay=parseFloat(proposedGrosspay) ;
		this.proposedCtc=parseFloat(proposedCtc);
		this.remarks=remarks;
		this.month=month;
		this.year=year;
	}
	
	
	public IncrementCreation() {
		// TODO Auto-generated constructor stub
	}


	private Float parseFloat(String value) {
	    try {
	        return (value != null) ? Float.parseFloat(value) : null;
	    } catch (NumberFormatException e) {
	        // Handle the case where the string cannot be parsed to a float
	        return null;
	    }
	}
}
