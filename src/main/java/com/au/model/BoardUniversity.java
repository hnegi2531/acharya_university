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
@Table(name = "board_university")
public class BoardUniversity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Integer board_university_id;
	
	private String board_university_name; 
	private String board_university_type;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	@Column(updatable = false)
	private Integer created_by;
	
	private Integer modified_by;
	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;

	

	public BoardUniversity() {
		super();
		}

	public Integer getBoard_university_id() {
		return board_university_id;
	}

	public void setBoard_university_id(Integer board_university_id) {
		this.board_university_id = board_university_id;
	}



	public String getBoard_university_name() {
		return board_university_name;
	}

	public void setBoard_university_name(String board_university_name) {
		this.board_university_name = board_university_name;
	}

	public String getBoard_university_type() {
		return board_university_type;
	}

	public void setBoard_university_type(String board_university_type) {
		this.board_university_type = board_university_type;
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
	
	
	
	

}
