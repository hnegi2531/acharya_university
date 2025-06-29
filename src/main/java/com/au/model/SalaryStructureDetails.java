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
@Table(name = "salary_structure_details")
public class SalaryStructureDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer salary_structure_details_id;
	private Integer salary_structure_id;
	private Integer salary_structure_head_id;
	private String salary_category;
	private String print_names;
	private Long value;
	private Integer slab_details_id;
	private String from_date;
	private String to_date;
	private String remarks;
	private String formula;
	private String formula_name;
	private Float percentage;
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
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private String testing_expression;
	private Integer priority;
	private Long gross_limit;
	private Float gross_percentage;
	private String gross_expression;
	private Float pension_fund;
	private String voucher_head_new_ids;
	
	private Boolean isPayDay;

	public SalaryStructureDetails() {
		super();
	}

	public Integer getSalary_structure_details_id() {
		return salary_structure_details_id;
	}

	public void setSalary_structure_details_id(Integer salary_structure_details_id) {
		this.salary_structure_details_id = salary_structure_details_id;
	}

	public Integer getSalary_structure_id() {
		return salary_structure_id;
	}

	public void setSalary_structure_id(Integer salary_structure_id) {
		this.salary_structure_id = salary_structure_id;
	}

	public Integer getSalary_structure_head_id() {
		return salary_structure_head_id;
	}

	public void setSalary_structure_head_id(Integer salary_structure_head_id) {
		this.salary_structure_head_id = salary_structure_head_id;
	}

	public String getSalary_category() {
		return salary_category;
	}

	public void setSalary_category(String salary_category) {
		this.salary_category = salary_category;
	}

	public String getPrint_names() {
		return print_names;
	}

	public void setPrint_names(String print_names) {
		this.print_names = print_names;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public Integer getSlab_details_id() {
		return slab_details_id;
	}

	public void setSlab_details_id(Integer slab_details_id) {
		this.slab_details_id = slab_details_id;
	}

	public String getFrom_date() {
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public String getTo_date() {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getFormula_name() {
		return formula_name;
	}

	public void setFormula_name(String formula_name) {
		this.formula_name = formula_name;
	}

	public Float getPercentage() {
		return percentage;
	}

	public void setPercentage(Float percentage) {
		this.percentage = percentage;
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

	public String getTesting_expression() {
		return testing_expression;
	}

	public void setTesting_expression(String testing_expression) {
		this.testing_expression = testing_expression;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Long getGross_limit() {
		return gross_limit;
	}

	public void setGross_limit(Long gross_limit) {
		this.gross_limit = gross_limit;
	}

	public Float getGross_percentage() {
		return gross_percentage;
	}

	public void setGross_percentage(Float gross_percentage) {
		this.gross_percentage = gross_percentage;
	}

	public String getGross_expression() {
		return gross_expression;
	}

	public void setGross_expression(String gross_expression) {
		this.gross_expression = gross_expression;
	}

	public Float getPension_fund() {
		return pension_fund;
	}

	public void setPension_fund(Float pension_fund) {
		this.pension_fund = pension_fund;
	}

	public String getVoucher_head_new_ids() {
		return voucher_head_new_ids;
	}

	public void setVoucher_head_new_ids(String voucher_head_new_ids) {
		this.voucher_head_new_ids = voucher_head_new_ids;
	}

	public Boolean getIsPayDay() {
		return isPayDay;
	}

	public void setIsPayDay(Boolean isPayDay) {
		this.isPayDay = isPayDay;
	}
	
	
}