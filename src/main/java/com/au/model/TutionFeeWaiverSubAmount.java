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


@Entity
@Table(name = "tution_fee_waiver_sub_amount")
public class TutionFeeWaiverSubAmount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tution_fee_waiver_sub_amt_id;
	private Integer tution_fee_waiver_id; // Fk
	private Integer voucher_head_new_id; // Fk
	private Double offer_waiver_amount;
	private Integer year1_amt;
	private Integer year2_amt;
	private Integer year3_amt;
	private Integer year4_amt;
	private Integer year5_amt;
	private Integer year6_amt;
	private Integer year7_amt;
	private Integer year8_amt;
	private Integer year9_amt;
	private Integer year10_amt;
	private Integer year11_amt;
	private Integer year12_amt;
	private Integer total_amt;
	
	@Column(updatable=false)
	private Integer created_by;
	
	private Integer modified_by;
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
//	private Boolean receive_for_all_year;
	private Boolean active;
	private String remarks;
	
	@Column(updatable=false)
	private String created_username;
	private String modified_username;
	
	public TutionFeeWaiverSubAmount() {
		super();
	}

	public Integer getTution_fee_waiver_sub_amt_id() {
		return tution_fee_waiver_sub_amt_id;
	}

	public void setTution_fee_waiver_sub_amt_id(Integer tution_fee_waiver_sub_amt_id) {
		this.tution_fee_waiver_sub_amt_id = tution_fee_waiver_sub_amt_id;
	}

	

	public Integer getTution_fee_waiver_id() {
		return tution_fee_waiver_id;
	}

	public void setTution_fee_waiver_id(Integer tution_fee_waiver_id) {
		this.tution_fee_waiver_id = tution_fee_waiver_id;
	}

	public Integer getVoucher_head_new_id() {
		return voucher_head_new_id;
	}

	public void setVoucher_head_new_id(Integer voucher_head_new_id) {
		this.voucher_head_new_id = voucher_head_new_id;
	}

	public Integer getYear1_amt() {
		return year1_amt;
	}

	public void setYear1_amt(Integer year1_amt) {
		this.year1_amt = year1_amt;
	}

	public Integer getYear2_amt() {
		return year2_amt;
	}

	public void setYear2_amt(Integer year2_amt) {
		this.year2_amt = year2_amt;
	}

	public Integer getYear3_amt() {
		return year3_amt;
	}

	public void setYear3_amt(Integer year3_amt) {
		this.year3_amt = year3_amt;
	}

	public Integer getYear4_amt() {
		return year4_amt;
	}

	public void setYear4_amt(Integer year4_amt) {
		this.year4_amt = year4_amt;
	}

	public Integer getYear5_amt() {
		return year5_amt;
	}

	public void setYear5_amt(Integer year5_amt) {
		this.year5_amt = year5_amt;
	}

	public Integer getYear6_amt() {
		return year6_amt;
	}

	public void setYear6_amt(Integer year6_amt) {
		this.year6_amt = year6_amt;
	}

	public Integer getYear7_amt() {
		return year7_amt;
	}

	public void setYear7_amt(Integer year7_amt) {
		this.year7_amt = year7_amt;
	}

	public Integer getYear8_amt() {
		return year8_amt;
	}

	public void setYear8_amt(Integer year8_amt) {
		this.year8_amt = year8_amt;
	}

	public Integer getYear9_amt() {
		return year9_amt;
	}

	public void setYear9_amt(Integer year9_amt) {
		this.year9_amt = year9_amt;
	}

	public Integer getYear10_amt() {
		return year10_amt;
	}

	public void setYear10_amt(Integer year10_amt) {
		this.year10_amt = year10_amt;
	}

	public Integer getYear11_amt() {
		return year11_amt;
	}

	public void setYear11_amt(Integer year11_amt) {
		this.year11_amt = year11_amt;
	}

	public Integer getYear12_amt() {
		return year12_amt;
	}

	public void setYear12_amt(Integer year12_amt) {
		this.year12_amt = year12_amt;
	}

	public Integer getTotal_amt() {
		return total_amt;
	}

	public void setTotal_amt(Integer total_amt) {
		this.total_amt = total_amt;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Integer getModified_by() {
		return modified_by;
	}

	public void setModified_by(Integer modified_by) {
		this.modified_by = modified_by;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Date getModified_date() {
		return modified_date;
	}

	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}

//	public Boolean getReceive_for_all_year() {
//		return receive_for_all_year;
//	}
//
//	public void setReceive_for_all_year(Boolean receive_for_all_year) {
//		this.receive_for_all_year = receive_for_all_year;
//	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreated_username() {
		return created_username;
	}

	public void setCreated_username(String created_username) {
		this.created_username = created_username;
	}

	public String getModified_username() {
		return modified_username;
	}

	public void setModified_username(String modified_username) {
		this.modified_username = modified_username;
	}

	public Double getOffer_waiver_amount() {
		return offer_waiver_amount;
	}

	public void setOffer_waiver_amount(Double offer_waiver_amount) {
		this.offer_waiver_amount = offer_waiver_amount;
	}



}
