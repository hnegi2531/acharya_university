package com.au.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "fee_template_sub_amount_history")
public class FeeTemplateSubAmountHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fee_sub_amt_his_id;
	private Integer fee_sub_amt_id;
	private Integer fee_template_id; // Fk
	private String voucher_head; // Fk
	private String board_unique_name; // Fk
	private String alias_name; // Fk
	private String year1_amt;
	private String year2_amt;
	private String year3_amt;
	private String year4_amt;
	private String year5_amt;
	private String year6_amt;
	private String year7_amt;
	private String year8_amt;
	private String year9_amt;
	private String year10_amt;
	private String year11_amt;
	private String year12_amt;
	private String total_amt;
	private Integer created_by;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	private Boolean active;
	private String remarks;
	private String created_username;

	public FeeTemplateSubAmountHistory() {
		super();
	}

	public Integer getFee_sub_amt_his_id() {
		return fee_sub_amt_his_id;
	}

	public void setFee_sub_amt_his_id(Integer fee_sub_amt_his_id) {
		this.fee_sub_amt_his_id = fee_sub_amt_his_id;
	}

	public Integer getFee_sub_amt_id() {
		return fee_sub_amt_id;
	}

	public void setFee_sub_amt_id(Integer fee_sub_amt_id) {
		this.fee_sub_amt_id = fee_sub_amt_id;
	}

	public Integer getFee_template_id() {
		return fee_template_id;
	}

	public void setFee_template_id(Integer fee_template_id) {
		this.fee_template_id = fee_template_id;
	}

	public String getVoucher_head() {
		return voucher_head;
	}

	public void setVoucher_head(String voucher_head) {
		this.voucher_head = voucher_head;
	}

	public String getBoard_unique_name() {
		return board_unique_name;
	}

	public void setBoard_unique_name(String board_unique_name) {
		this.board_unique_name = board_unique_name;
	}

	public String getAlias_name() {
		return alias_name;
	}

	public void setAlias_name(String alias_name) {
		this.alias_name = alias_name;
	}

	public String getYear1_amt() {
		return year1_amt;
	}

	public void setYear1_amt(String year1_amt) {
		this.year1_amt = year1_amt;
	}

	public String getYear2_amt() {
		return year2_amt;
	}

	public void setYear2_amt(String year2_amt) {
		this.year2_amt = year2_amt;
	}

	public String getYear3_amt() {
		return year3_amt;
	}

	public void setYear3_amt(String year3_amt) {
		this.year3_amt = year3_amt;
	}

	public String getYear4_amt() {
		return year4_amt;
	}

	public void setYear4_amt(String year4_amt) {
		this.year4_amt = year4_amt;
	}

	public String getYear5_amt() {
		return year5_amt;
	}

	public void setYear5_amt(String year5_amt) {
		this.year5_amt = year5_amt;
	}

	public String getYear6_amt() {
		return year6_amt;
	}

	public void setYear6_amt(String year6_amt) {
		this.year6_amt = year6_amt;
	}

	public String getYear7_amt() {
		return year7_amt;
	}

	public void setYear7_amt(String year7_amt) {
		this.year7_amt = year7_amt;
	}

	public String getYear8_amt() {
		return year8_amt;
	}

	public void setYear8_amt(String year8_amt) {
		this.year8_amt = year8_amt;
	}

	public String getYear9_amt() {
		return year9_amt;
	}

	public void setYear9_amt(String year9_amt) {
		this.year9_amt = year9_amt;
	}

	public String getYear10_amt() {
		return year10_amt;
	}

	public void setYear10_amt(String year10_amt) {
		this.year10_amt = year10_amt;
	}

	public String getYear11_amt() {
		return year11_amt;
	}

	public void setYear11_amt(String year11_amt) {
		this.year11_amt = year11_amt;
	}

	public String getYear12_amt() {
		return year12_amt;
	}

	public void setYear12_amt(String year12_amt) {
		this.year12_amt = year12_amt;
	}

	public String getTotal_amt() {
		return total_amt;
	}

	public void setTotal_amt(String total_amt) {
		this.total_amt = total_amt;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Date getModified_date() {
		return modified_date;
	}

	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}

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
}
