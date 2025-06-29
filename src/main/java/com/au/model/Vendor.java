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
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "vendor")
public class Vendor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer vendor_id;
	
	@Column(unique = true)
	@NotBlank(message = "vendor_name should not be Empty OR Null")
	private String vendor_name;
	private String vendor_email;
	private String vendor_contact_no;

	private String vendor_gst_no;
	private String vendor_bank_account_holder_name;
	private String vendor_bank_name;
	private String bank_branch;
	private String vendor_bank_ifsc_code;
	private Integer state_id;
	private String pin_code;
	private Integer vendor_city_id;
	@Column(unique = true)
	@NotBlank(message = "pan number should not be Empty OR Null")
	private String pan_number;
	private String street_name;
	private String area;
	@Column(unique = true)
	@NotBlank(message = "Account no should not be Empty OR Null")
	private String account_no;
	private String vendor_type;
	private Integer tds_percentage;
	
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	private Boolean active;
	private Integer ledger_id;
	private String nature_of_business;
	private Integer credit_period;
	private String vendor_address;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	
//	private String vendor_bank_ifo_code;
	private Integer voucher_head_id;
	private Integer voucher_head_new_id;
	private Integer country_id;
	private Date account_verifier_date;
	private Integer verifier_user_id;
	private Boolean account_verification_status;

	private String vendor_tin_no;
	
	public Vendor() {
		super();
		
	}

	public Integer getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(Integer vendor_id) {
		this.vendor_id = vendor_id;
	}

	public String getVendor_name() {
		return vendor_name;
	}

	public void setVendor_name(String vendor_name) {
		this.vendor_name = vendor_name;
	}

	public String getVendor_email() {
		return vendor_email;
	}

	public void setVendor_email(String vendor_email) {
		this.vendor_email = vendor_email;
	}

	public String getVendor_contact_no() {
		return vendor_contact_no;
	}

	public void setVendor_contact_no(String vendor_contact_no) {
		this.vendor_contact_no = vendor_contact_no;
	}

	public String getVendor_gst_no() {
		return vendor_gst_no;
	}

	public void setVendor_gst_no(String vendor_gst_no) {
		this.vendor_gst_no = vendor_gst_no;
	}

	public String getVendor_bank_account_holder_name() {
		return vendor_bank_account_holder_name;
	}

	public void setVendor_bank_account_holder_name(String vendor_bank_account_holder_name) {
		this.vendor_bank_account_holder_name = vendor_bank_account_holder_name;
	}

	public String getVendor_bank_name() {
		return vendor_bank_name;
	}

	public void setVendor_bank_name(String vendor_bank_name) {
		this.vendor_bank_name = vendor_bank_name;
	}

	public String getBank_branch() {
		return bank_branch;
	}

	public void setBank_branch(String bank_branch) {
		this.bank_branch = bank_branch;
	}

	public String getVendor_bank_ifsc_code() {
		return vendor_bank_ifsc_code;
	}

	public void setVendor_bank_ifsc_code(String vendor_bank_ifsc_code) {
		this.vendor_bank_ifsc_code = vendor_bank_ifsc_code;
	}

	public Integer getState_id() {
		return state_id;
	}

	public void setState_id(Integer state_id) {
		this.state_id = state_id;
	}

	public String getPin_code() {
		return pin_code;
	}

	public void setPin_code(String pin_code) {
		this.pin_code = pin_code;
	}

	public Integer getVendor_city_id() {
		return vendor_city_id;
	}

	public void setVendor_city_id(Integer vendor_city_id) {
		this.vendor_city_id = vendor_city_id;
	}

	public String getPan_number() {
		return pan_number;
	}

	public void setPan_number(String pan_number) {
		this.pan_number = pan_number;
	}

	public String getStreet_name() {
		return street_name;
	}

	public void setStreet_name(String street_name) {
		this.street_name = street_name;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getVendor_type() {
		return vendor_type;
	}

	public void setVendor_type(String vendor_type) {
		this.vendor_type = vendor_type;
	}

	public Integer getTds_percentage() {
		return tds_percentage;
	}

	public void setTds_percentage(Integer tds_percentage) {
		this.tds_percentage = tds_percentage;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getLedger_id() {
		return ledger_id;
	}

	public void setLedger_id(Integer ledger_id) {
		this.ledger_id = ledger_id;
	}

	public String getNature_of_business() {
		return nature_of_business;
	}

	public void setNature_of_business(String nature_of_business) {
		this.nature_of_business = nature_of_business;
	}

	public Integer getCredit_period() {
		return credit_period;
	}

	public void setCredit_period(Integer credit_period) {
		this.credit_period = credit_period;
	}

	public String getVendor_address() {
		return vendor_address;
	}

	public void setVendor_address(String vendor_address) {
		this.vendor_address = vendor_address;
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

	public Integer getVoucher_head_id() {
		return voucher_head_id;
	}

	public void setVoucher_head_id(Integer voucher_head_id) {
		this.voucher_head_id = voucher_head_id;
	}

	public Integer getVoucher_head_new_id() {
		return voucher_head_new_id;
	}

	public void setVoucher_head_new_id(Integer voucher_head_new_id) {
		this.voucher_head_new_id = voucher_head_new_id;
	}

	public Integer getCountry_id() {
		return country_id;
	}

	public void setCountry_id(Integer country_id) {
		this.country_id = country_id;
	}

	public Date getAccount_verifier_date() {
		return account_verifier_date;
	}

	public void setAccount_verifier_date(Date account_verifier_date) {
		this.account_verifier_date = account_verifier_date;
	}

	public Integer getVerifier_user_id() {
		return verifier_user_id;
	}

	public void setVerifier_user_id(Integer verifier_user_id) {
		this.verifier_user_id = verifier_user_id;
	}

	public Boolean getAccount_verification_status() {
		return account_verification_status;
	}

	public void setAccount_verification_status(Boolean account_verification_status) {
		this.account_verification_status = account_verification_status;
	}

	public String getVendor_tin_no() {
		return vendor_tin_no;
	}

	public void setVendor_tin_no(String vendor_tin_no) {
		this.vendor_tin_no = vendor_tin_no;
	}
	
	
	
	
}
