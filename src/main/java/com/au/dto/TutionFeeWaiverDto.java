package com.au.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.au.model.TutionFeeWaiver;
import com.au.model.TutionFeeWaiverSubAmount;



public class TutionFeeWaiverDto {
	
	private List<TutionFeeWaiverSubAmount> tut_fee_waiver_sub_amount;
	private TutionFeeWaiver tut_fee_wavier;
	private Integer tution_fee_waiver_id;
	private Double yearly_waiver_amount;
	private HashMap<Integer, Double> year_sem;
	private String created_username;
	private String modified_username;
	@Column(name = "created_date",updatable = false)
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)	
	@UpdateTimestamp
	private Date modified_date;
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	
	public TutionFeeWaiverDto() {
		super();
		
	}
	/*public List<TutionFeeWaiverReport> getTut_fee_report() {
		return tut_fee_report;
	}

	public void setTut_fee_report(List<TutionFeeWaiverReport> tut_fee_report) {
		this.tut_fee_report = tut_fee_report;
	}*/
	
	


	public List<TutionFeeWaiverSubAmount> getTut_fee_waiver_sub_amount() {
		return tut_fee_waiver_sub_amount;
	}





	public void setTut_fee_waiver_sub_amount(List<TutionFeeWaiverSubAmount> tut_fee_waiver_sub_amount) {
		this.tut_fee_waiver_sub_amount = tut_fee_waiver_sub_amount;
	}
	

	public TutionFeeWaiver getTut_fee_wavier() {
		return tut_fee_wavier;
	}



	public void setTut_fee_wavier(TutionFeeWaiver tut_fee_wavier) {
		this.tut_fee_wavier = tut_fee_wavier;
	}

	public Integer getTution_fee_waiver_id() {
		return tution_fee_waiver_id;
	}

	public void setTution_fee_waiver_id(Integer tution_fee_waiver_id) {
		this.tution_fee_waiver_id = tution_fee_waiver_id;
	}


	public Double getYearly_waiver_amount() {
		return yearly_waiver_amount;
	}




	public void setYearly_waiver_amount(Double yearly_waiver_amount) {
		this.yearly_waiver_amount = yearly_waiver_amount;
	}

	public HashMap<Integer, Double> getYear_sem() {
		return year_sem;
	}

	public void setYear_sem(HashMap<Integer, Double> year_sem) {
		this.year_sem = year_sem;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
	
	
	

}
