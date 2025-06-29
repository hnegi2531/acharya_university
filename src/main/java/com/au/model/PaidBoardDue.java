package com.au.model;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="paid_board_due")
public class PaidBoardDue {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer paidBoardDueId;
	private Integer studentId;
	private Integer feeTemplateId;
	private Integer feeAdmissionCategoryId;
	private  Integer boardUniqueId;
	private Integer toPay;
	private Integer received;
	private Integer balance;
}
