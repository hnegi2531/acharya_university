package com.au.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "proctor_head_history")
public class ProctorHeadHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer proctor_head_history_id;
	private String proctor_id;
	private Integer student_id;
	private String student_name;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date from_date;
	private Date to_date;
	private String modified_username;

	public ProctorHeadHistory() {
		super();
	}

	public Integer getProctor_head_history_id() {
		return proctor_head_history_id;
	}

	public void setProctor_head_history_id(Integer proctor_head_history_id) {
		this.proctor_head_history_id = proctor_head_history_id;
	}

	public String getProctor_id() {
		return proctor_id;
	}

	public void setProctor_id(String proctor_id) {
		this.proctor_id = proctor_id;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public Date getFrom_date() {
		return from_date;
	}

	public void setFrom_date(Date from_date) {
		this.from_date = from_date;
	}

	public Date getTo_date() {
		return to_date;
	}

	public void setTo_date(Date to_date) {
		this.to_date = to_date;
	}

	public String getModified_username() {
		return modified_username;
	}

	public void setModified_username(String modified_username) {
		this.modified_username = modified_username;
	}

}
