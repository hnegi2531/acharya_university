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
@Table(name = "salary_structure_head")
public class SalaryStructureHead {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer salary_structure_head_id;
	private String print_name;
	@Column(unique = true)
	private Integer priority;
	private String category_short_name;
	private String category_name_type;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	@Column(updatable = false)
	@Temporal(TemporalType.DATE)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	private Boolean active;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	@Column(unique = true)
	private Integer voucher_head_new_id;

	public SalaryStructureHead() {
		super();
	}

	public Integer getSalary_structure_head_id() {
		return salary_structure_head_id;
	}

	public void setSalary_structure_head_id(Integer salary_structure_head_id) {
		this.salary_structure_head_id = salary_structure_head_id;
	}

	public String getPrint_name() {
		return print_name;
	}

	public void setPrint_name(String print_name) {
		this.print_name = print_name;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getCategory_short_name() {
		return category_short_name;
	}

	public void setCategory_short_name(String category_short_name) {
		this.category_short_name = category_short_name;
	}

	public String getCategory_name_type() {
		return category_name_type;
	}

	public void setCategory_name_type(String category_name_type) {
		this.category_name_type = category_name_type;
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

	public Integer getVoucher_head_new_id() {
		return voucher_head_new_id;
	}

	public void setVoucher_head_new_id(Integer voucher_head_new_id) {
		this.voucher_head_new_id = voucher_head_new_id;
	}
	
	

}