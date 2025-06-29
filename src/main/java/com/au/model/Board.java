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
@Table(name = "board")
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer board_unique_id;
	
	@Column(unique = true)
	@NotBlank(message = "board_unique_name should not be Empty OR Null")
	private String board_unique_name;
	
	@Column(unique = true)
	@NotBlank(message = "board_unique_short_name should not be Empty OR Null")
	private String board_unique_short_name;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	@Column(updatable = false)
	private Integer created_by;
	
	private Integer modified_by;
	private Integer school_id;
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;
	private Boolean active;

	public Board() {
		super();
	}

	public Integer getBoard_unique_id() {
		return board_unique_id;
	}

	public void setBoard_unique_id(Integer board_unique_id) {
		this.board_unique_id = board_unique_id;
	}

	public String getBoard_unique_name() {
		return board_unique_name;
	}

	public void setBoard_unique_name(String board_unique_name) {
		this.board_unique_name = board_unique_name;
	}

	public String getBoard_unique_short_name() {
		return board_unique_short_name;
	}

	public void setBoard_unique_short_name(String board_unique_short_name) {
		this.board_unique_short_name = board_unique_short_name;
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

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	
	



}