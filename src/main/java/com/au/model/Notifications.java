package com.au.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "notification")
public class Notifications {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer notification_id;
	private String school_ids;
	private String dept_ids;
	private Integer notify;
	private String title;
	
	@Lob
	private String description;
	
	private Integer notification_by;
	private Boolean lockFlag;
	private Boolean sent_flag;
	private Boolean active;
	private String notification_date;
	private String notify_to;
	private String notification_type;

	@Column(updatable = false)
	private String created_username;
	private String modified_username;

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
	private String notification_attach_path;
	
	public Notifications() {
	}

	public Integer getNotification_id() {
		return notification_id;
	}

	public void setNotification_id(Integer notification_id) {
		this.notification_id = notification_id;
	}

	public String getDept_ids() {
		return dept_ids;
	}

	public void setDept_ids(String dept_ids) {
		this.dept_ids = dept_ids;
	}

	public Integer getNotify() {
		return notify;
	}

	public void setNotify(Integer notify) {
		this.notify = notify;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNotification_by() {
		return notification_by;
	}

	public void setNotification_by(Integer notification_by) {
		this.notification_by = notification_by;
	}

	public Boolean getSent_flag() {
		return sent_flag;
	}

	public Boolean getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(Boolean lockFlag) {
		this.lockFlag = lockFlag;
	}

	public void setSent_flag(Boolean sent_flag) {
		this.sent_flag = sent_flag;
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

	public String getSchool_ids() {
		return school_ids;
	}

	public void setSchool_ids(String school_ids) {
		this.school_ids = school_ids;
	}

	public String getNotification_attach_path() {
		return notification_attach_path;
	}

	public String setNotification_attach_path(String notification_attach_path) {
		return this.notification_attach_path = notification_attach_path;
	}

	public String getNotification_date() {
		return notification_date;
	}

	public void setNotification_date(String notification_date) {
		this.notification_date = notification_date;
	}

	public String getNotify_to() {
		return notify_to;
	}

	public void setNotify_to(String notify_to) {
		this.notify_to = notify_to;
	}

	public String getNotification_type() {
		return notification_type;
	}

	public void setNotification_type(String notification_type) {
		this.notification_type = notification_type;
	}
	
	

}
