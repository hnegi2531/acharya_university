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
@Table(name = "commencement_type")
public class CommencementType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer commencement_id;
	@Column(unique = true)
	private String commencement_type;
	
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	@Column(name = "created_by", updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;
	private String date_selection;
	private Boolean restriction_status;
	private Integer category_details_id;
	
	public CommencementType() {
		super();
	 }


	public Integer getCommencement_id() {
		return commencement_id;
	}


	public void setCommencement_id(Integer commencement_id) {
		this.commencement_id = commencement_id;
	}


	public String getCommencement_type() {
		return commencement_type;
	}


	public void setCommencement_type(String commencement_type) {
		this.commencement_type = commencement_type;
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


	public String getDate_selection() {
		return date_selection;
	}


	public void setDate_selection(String date_selection) {
		this.date_selection = date_selection;
	}


	public Boolean getRestriction_status() {
		return restriction_status;
	}


	public void setRestriction_status(Boolean restriction_status) {
		this.restriction_status = restriction_status;
	}


	public Integer getCategory_details_id() {
		return category_details_id;
	}


	public void setCategory_details_id(Integer category_details_id) {
		this.category_details_id = category_details_id;
	}

	
}
