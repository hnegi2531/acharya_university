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
import lombok.NoArgsConstructor;

@Entity
@Table(name="board_received_amount")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardReceivedAmount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer boardReceivedAmountId;
	private Integer receivedYear;
	private Integer acYearId;
	private Integer boardId;
	private String yearSem;
	private String neftNo;
	private Float amount;
	private String bulkReceiptNo;
	private String remarks;
	private Boolean paidByStudent;
	private Date receivedDate;
	private Integer schoolId;
	private Float taggedAmount;
	private Boolean lockStatus;
	private Integer feeTemplateId;
	@Column(updatable = false)
	private String createdUsername;
	
	private String modifiedUsername;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)	
	@UpdateTimestamp
	private Date modifiedDate;
	
	@Column(updatable = false)
	private Integer createdBy;
	
	private Integer modifiedBy;
	private boolean active;
	
	
	

}
