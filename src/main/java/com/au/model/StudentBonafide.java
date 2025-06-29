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
@Table(name = "student_bonafide")
public class StudentBonafide {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer student_bonafide_id;
	
	private String bonafide_type;
	private String auid;
	private Integer	hostel_fee_template_id;
	private Integer current_sem;
	private Integer current_year;
	private String bonafide_number;
	
	@Column(updatable = false)
	private Integer created_by;
	
	private Integer modified_by;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_Date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_Date;
   
	private Boolean active;
    
	@Column(name = "created_username", updatable = false)
    private String created_username;
	private String modified_username;
	
	
	public StudentBonafide() {
		super();
		
	}


	public Integer getStudent_bonafide_id() {
		return student_bonafide_id;
	}


	public void setStudent_bonafide_id(Integer student_bonafide_id) {
		this.student_bonafide_id = student_bonafide_id;
	}


	public String getBonafide_type() {
		return bonafide_type;
	}


	public void setBonafide_type(String bonafide_type) {
		this.bonafide_type = bonafide_type;
	}


	public String getAuid() {
		return auid;
	}


	public void setAuid(String auid) {
		this.auid = auid;
	}


	public Integer getHostel_fee_template_id() {
		return hostel_fee_template_id;
	}


	public void setHostel_fee_template_id(Integer hostel_fee_template_id) {
		this.hostel_fee_template_id = hostel_fee_template_id;
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


	public Date getCreated_Date() {
		return created_Date;
	}


	public void setCreated_Date(Date created_Date) {
		this.created_Date = created_Date;
	}


	public Date getModified_Date() {
		return modified_Date;
	}


	public void setModified_Date(Date modified_Date) {
		this.modified_Date = modified_Date;
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


	public String getBonafide_number() {
		return bonafide_number;
	}


	public void setBonafide_number(String bonafide_number) {
		this.bonafide_number = bonafide_number;
	}


	public Integer getCurrent_sem() {
		return current_sem;
	}


	public void setCurrent_sem(Integer current_sem) {
		this.current_sem = current_sem;
	}


	public Integer getCurrent_year() {
		return current_year;
	}


	public void setCurrent_year(Integer current_year) {
		this.current_year = current_year;
	}
	
	
	
}
