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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "other_fee_details")
@Entity
public class OtherFeeDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer otherFeeDetailsId;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date created_date;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified_date;

	private String createdBy;

	private String modifiedBy;

	private Boolean active;

	private Integer templateId;

	private Integer voucherHeadId;

	private Float sem1;

	private Float sem2;

	private Float sem3;

	private Float sem4;

	private Float sem5;

	private Float sem6;

	private Float sem7;

	private Float sem8;

	private Float sem9;

	private Float sem10;
	private Float sem11;
	private Float sem12;

	private Float year1;
	private Float year2;
	private Float year3;
	private Float year4;

	private Float total;
	


}
