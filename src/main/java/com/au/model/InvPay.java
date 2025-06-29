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
@Table(name="inv_pay")
public class InvPay {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "inv_pay_id")
	private Long invPayId;
	
	@Column(name = "employee_name")
	private String employeeName;
	
	@Column(name = "emp_code")
	private String empCode;
	
	@Column(name = "inv_pay")
	private Double invPay;
	
    private Integer month;
	
	private Integer year;
	
	@Column(name = "is_active")
	private Boolean isActive = true;

	@Column(name = "remarks")
	private String remarks;

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
}
