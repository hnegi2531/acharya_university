package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="temporary_inv_pay")
public class TemporaryInvPay {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "temporary_inv_pay_batch_id")
	private Long temporaryInvPayId;
	
	@Column(name = "employee_name")
	private String employeeName;
	
	@Column(name = "emp_code")
	private String empCode;
	
	@Column(name = "inv_pay")
	private Double invPay;
	
    private Integer month;
	
	private Integer year;
	
	@Column(name="error_reason")
	private String errorReason;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(name = "is_active")
	private Boolean isActive = true;


	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_by_name")
	private String createdByName;
	
	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "created_at")
	@CreationTimestamp
	private Date createdAt;

	@Column(name = "updated_at")
	@UpdateTimestamp
	private Date updatedAt;
	
	@ManyToOne
	@JoinColumn(name="inv_pay_batch_id")
	private InvPayBatch invPayBatch;
	
	private String type;

	private String remarks;
	
	private enum Status{
		SUCCESS,ERROR
	}


	public TemporaryInvPay(String employeeName, String empCode, Double invPay, String type, String remarks) {

		this.employeeName = employeeName;
		this.empCode = empCode;
		this.invPay = invPay;
		this.type = type;
		this.remarks = remarks;
	}

	

}
