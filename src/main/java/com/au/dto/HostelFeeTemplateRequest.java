package com.au.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

public class HostelFeeTemplateRequest {

	private String template_name;
	private String  hostels_block_id;
	private String hostel_block_shrot;
	private String hostels_floor_id;
	private Integer hostel_room_type_id;
	private String hostels_room_id;
	private Integer ac_year_id;
	private String fee_type;
	private Integer currency_type_id;
	private Integer common_acessories;
	private List<Integer> template_amount;
	private Integer standard_acessories;
	private List<Integer> 	fee_head_id;
	private String remarks;
	private String  school_ids;  //institute_ids;
	private String school_name_short;
	private List<Integer> minimum_amount;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modifiedDate;

	private Integer createdBy;
	private Integer modifiedBy;
	private String createdUsername;
	private String modifiedUsername;
	private Boolean active;
	public HostelFeeTemplateRequest() {
		super();
	}
	public String getTemplate_name() {
		return template_name;
	}
	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}
	public String getHostels_block_id() {
		return hostels_block_id;
	}
	public void setHostels_block_id(String hostels_block_id) {
		this.hostels_block_id = hostels_block_id;
	}
	public String getHostel_block_shrot() {
		return hostel_block_shrot;
	}
	public void setHostel_block_shrot(String hostel_block_shrot) {
		this.hostel_block_shrot = hostel_block_shrot;
	}
	public String getHostels_floor_id() {
		return hostels_floor_id;
	}
	public void setHostels_floor_id(String hostels_floor_id) {
		this.hostels_floor_id = hostels_floor_id;
	}
	public Integer getHostel_room_type_id() {
		return hostel_room_type_id;
	}
	public void setHostel_room_type_id(Integer hostel_room_type_id) {
		this.hostel_room_type_id = hostel_room_type_id;
	}
	public String getHostels_room_id() {
		return hostels_room_id;
	}
	public void setHostels_room_id(String hostels_room_id) {
		this.hostels_room_id = hostels_room_id;
	}
	
	public Integer getAc_year_id() {
		return ac_year_id;
	}
	public void setAc_year_id(Integer ac_year_id) {
		this.ac_year_id = ac_year_id;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	
	public Integer getCurrency_type_id() {
		return currency_type_id;
	}
	public void setCurrency_type_id(Integer currency_type_id) {
		this.currency_type_id = currency_type_id;
	}
	public Integer getCommon_acessories() {
		return common_acessories;
	}
	public void setCommon_acessories(Integer common_acessories) {
		this.common_acessories = common_acessories;
	}
	public List<Integer> getTemplate_amount() {
		return template_amount;
	}
	public void setTemplate_amount(List<Integer> template_amount) {
		this.template_amount = template_amount;
	}
	public Integer getStandard_acessories() {
		return standard_acessories;
	}
	public void setStandard_acessories(Integer standard_acessories) {
		this.standard_acessories = standard_acessories;
	}
	public List<Integer> getFee_head_id() {
		return fee_head_id;
	}
	public void setFee_head_id(List<Integer> fee_head_id) {
		this.fee_head_id = fee_head_id;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getSchool_ids() {
		return school_ids;
	}
	public void setSchool_ids(String school_ids) {
		this.school_ids = school_ids;
	}
	public String getSchool_name_short() {
		return school_name_short;
	}
	public void setSchool_name_short(String school_name_short) {
		this.school_name_short = school_name_short;
	}
	public List<Integer> getMinimum_amount() {
		return minimum_amount;
	}
	public void setMinimum_amount(List<Integer> minimum_amount) {
		this.minimum_amount = minimum_amount;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getCreatedUsername() {
		return createdUsername;
	}
	public void setCreatedUsername(String createdUsername) {
		this.createdUsername = createdUsername;
	}
	public String getModifiedUsername() {
		return modifiedUsername;
	}
	public void setModifiedUsername(String modifiedUsername) {
		this.modifiedUsername = modifiedUsername;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

	
	
}
