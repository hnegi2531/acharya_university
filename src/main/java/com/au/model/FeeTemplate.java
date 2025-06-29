package com.au.model;

import java.time.LocalDate;
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
@Table(name = "fee_template")
public class FeeTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fee_template_id;
	private String fee_template_name;
	private Integer ac_year_id;
	private Integer school_id;
	private Integer program_id;
	// private String program_specialization;
	private String program_specialization_id;
	private Integer currency_type_id;
	private Integer fee_admission_category_id;
	private Integer fee_admission_sub_category_id;
	private Boolean Is_paid_at_board;
	private String nationality;
	private Boolean Is_nri;
	private Integer program_type_id;
	private Integer approved_by;
	private Boolean approved_status;
	private LocalDate approved_date;
	
	@Column(name = "created_by" , updatable = false)
	private Integer created_by;
	
	private Integer modified_by;
	private Integer fee_year1_amt;
	private Integer fee_year2_amt;
	private Integer fee_year3_amt;
	private Integer fee_year4_amt;
	private Integer fee_year5_amt;
	private Integer fee_year6_amt;
	private Integer fee_year7_amt;
	private Integer fee_year8_amt;
	private Integer fee_year9_amt;
	private Integer fee_year10_amt;
	private Integer fee_year11_amt;
	private Integer fee_year12_amt;
	private Integer fee_year_total_amount;

//	@Column(updatable = false)
//	@Temporal(TemporalType.TIMESTAMP)
//	@CreationTimestamp
	@Column(name = "created_date" , updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	private Boolean active;
	private String remarks;
	
	@Column(name = "created_username" , updatable = false)
	private String created_username;
	private String modified_username;
	private String program_specialization;
	private String fee_template_path;
	private String is_saarc;
	private Integer lat_year_sem;         /*   Lateral Entry   */
	
	private Boolean uniform_status;
	private Boolean laptop_status;

	public FeeTemplate() {
		super();
	}

	public Integer getFee_template_id() {
		return fee_template_id;
	}

	public void setFee_template_id(Integer fee_template_id) {
		this.fee_template_id = fee_template_id;
	}

	public String getFee_template_name() {
		return fee_template_name;
	}

	public void setFee_template_name(String fee_template_name) {
		this.fee_template_name = fee_template_name;
	}

	public Integer getAc_year_id() {
		return ac_year_id;
	}

	public void setAc_year_id(Integer ac_year_id) {
		this.ac_year_id = ac_year_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getProgram_id() {
		return program_id;
	}

	public void setProgram_id(Integer program_id) {
		this.program_id = program_id;
	}

	public String getProgram_specialization_id() {
		return program_specialization_id;
	}

	public void setProgram_specialization_id(String program_specialization_id) {
		this.program_specialization_id = program_specialization_id;
	}

	public Integer getCurrency_type_id() {
		return currency_type_id;
	}

	public void setCurrency_type_id(Integer currency_type_id) {
		this.currency_type_id = currency_type_id;
	}

	public Integer getFee_admission_category_id() {
		return fee_admission_category_id;
	}

	public void setFee_admission_category_id(Integer fee_admission_category_id) {
		this.fee_admission_category_id = fee_admission_category_id;
	}

	public Integer getFee_admission_sub_category_id() {
		return fee_admission_sub_category_id;
	}

	public void setFee_admission_sub_category_id(Integer fee_admission_sub_category_id) {
		this.fee_admission_sub_category_id = fee_admission_sub_category_id;
	}

	public Boolean getIs_paid_at_board() {
		return Is_paid_at_board;
	}

	public void setIs_paid_at_board(Boolean is_paid_at_board) {
		Is_paid_at_board = is_paid_at_board;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Boolean getIs_nri() {
		return Is_nri;
	}

	public void setIs_nri(Boolean is_nri) {
		Is_nri = is_nri;
	}

	public Integer getProgram_type_id() {
		return program_type_id;
	}

	public void setProgram_type_id(Integer program_type_id) {
		this.program_type_id = program_type_id;
	}

	public Integer getApproved_by() {
		return approved_by;
	}

	public void setApproved_by(Integer approved_by) {
		this.approved_by = approved_by;
	}

	public Boolean getApproved_status() {
		return approved_status;
	}

	public void setApproved_status(Boolean approved_status) {
		this.approved_status = approved_status;
	}

	public LocalDate getApproved_date() {
		return approved_date;
	}

	public void setApproved_date(LocalDate approved_date) {
		this.approved_date = approved_date;
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

	public Integer getFee_year1_amt() {
		return fee_year1_amt;
	}

	public void setFee_year1_amt(Integer fee_year1_amt) {
		this.fee_year1_amt = fee_year1_amt;
	}

	public Integer getFee_year2_amt() {
		return fee_year2_amt;
	}

	public void setFee_year2_amt(Integer fee_year2_amt) {
		this.fee_year2_amt = fee_year2_amt;
	}

	public Integer getFee_year3_amt() {
		return fee_year3_amt;
	}

	public void setFee_year3_amt(Integer fee_year3_amt) {
		this.fee_year3_amt = fee_year3_amt;
	}

	public Integer getFee_year4_amt() {
		return fee_year4_amt;
	}

	public void setFee_year4_amt(Integer fee_year4_amt) {
		this.fee_year4_amt = fee_year4_amt;
	}

	public Integer getFee_year5_amt() {
		return fee_year5_amt;
	}

	public void setFee_year5_amt(Integer fee_year5_amt) {
		this.fee_year5_amt = fee_year5_amt;
	}

	public Integer getFee_year6_amt() {
		return fee_year6_amt;
	}

	public void setFee_year6_amt(Integer fee_year6_amt) {
		this.fee_year6_amt = fee_year6_amt;
	}

	public Integer getFee_year7_amt() {
		return fee_year7_amt;
	}

	public void setFee_year7_amt(Integer fee_year7_amt) {
		this.fee_year7_amt = fee_year7_amt;
	}

	public Integer getFee_year8_amt() {
		return fee_year8_amt;
	}

	public void setFee_year8_amt(Integer fee_year8_amt) {
		this.fee_year8_amt = fee_year8_amt;
	}

	public Integer getFee_year9_amt() {
		return fee_year9_amt;
	}

	public void setFee_year9_amt(Integer fee_year9_amt) {
		this.fee_year9_amt = fee_year9_amt;
	}

	public Integer getFee_year10_amt() {
		return fee_year10_amt;
	}

	public void setFee_year10_amt(Integer fee_year10_amt) {
		this.fee_year10_amt = fee_year10_amt;
	}

	public Integer getFee_year11_amt() {
		return fee_year11_amt;
	}

	public void setFee_year11_amt(Integer fee_year11_amt) {
		this.fee_year11_amt = fee_year11_amt;
	}

	public Integer getFee_year12_amt() {
		return fee_year12_amt;
	}

	public void setFee_year12_amt(Integer fee_year12_amt) {
		this.fee_year12_amt = fee_year12_amt;
	}

	public Integer getFee_year_total_amount() {
		return fee_year_total_amount;
	}

	public void setFee_year_total_amount(Integer fee_year_total_amount) {
		this.fee_year_total_amount = fee_year_total_amount;
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

	public String getProgram_specialization() {
		return program_specialization;
	}

	public void setProgram_specialization(String program_specialization) {
		this.program_specialization = program_specialization;
	}

	public String getFee_template_path() {
		return fee_template_path;
	}

	public String setFee_template_path(String fee_template_path) {
		return this.fee_template_path = fee_template_path;
	}

	public String getIs_saarc() {
		return is_saarc;
	}

	public void setIs_saarc(String is_saarc) {
		this.is_saarc = is_saarc;
	}

	public Integer getLat_year_sem() {
		return lat_year_sem;
	}

	public void setLat_year_sem(Integer lat_year_sem) {
		this.lat_year_sem = lat_year_sem;
	}

	public Boolean getUniform_status() {
		return uniform_status;
	}

	public void setUniform_status(Boolean uniform_status) {
		this.uniform_status = uniform_status;
	}

	public Boolean getLaptop_status() {
		return laptop_status;
	}

	public void setLaptop_status(Boolean laptop_status) {
		this.laptop_status = laptop_status;
	}
	
	

}