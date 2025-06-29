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
@Table(name = "exam_details")
public class ExamDetails {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer exam_details_id;
	
	private String exam_date;
	private String exam_center;
	
	private Integer internal_master_id;
	private String start_time;
	private String end_time;
	private String address;
	private String duration;
	
	@Column(updatable = false)
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
	
	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	
	
	private String windows_start_date;
	private String windows_end_date;
	
	public ExamDetails() {
		super();
     }

	public Integer getExam_details_id() {
		return exam_details_id;
	}

	public void setExam_details_id(Integer exam_details_id) {
		this.exam_details_id = exam_details_id;
	}

	public String getExam_date() {
		return exam_date;
	}

	public void setExam_date(String exam_date) {
		this.exam_date = exam_date;
	}

	public String getExam_center() {
		return exam_center;
	}

	public void setExam_center(String exam_center) {
		this.exam_center = exam_center;
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

    public Integer getInternal_master_id() {
		return internal_master_id;
	}

	public void setInternal_master_id(Integer internal_master_id) {
		this.internal_master_id = internal_master_id;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getWindows_start_date() {
		return windows_start_date;
	}

	public void setWindows_start_date(String windows_start_date) {
		this.windows_start_date = windows_start_date;
	}

	public String getWindows_end_date() {
		return windows_end_date;
	}

	public void setWindows_end_date(String windows_end_date) {
		this.windows_end_date = windows_end_date;
	}
	
	
}
