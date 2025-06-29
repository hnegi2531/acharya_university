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
@Table(name = "schools")
public class Schools {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer school_id;
	@Column(unique = true)
	private String school_name;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date created_date;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified_date;
	private Integer org_id;
	@Column(unique = true)
	private String school_name_short;
	//private String school_desc;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private boolean active;
	private boolean week_off;
	private String job_type_id;
	private String ref_no;
	//private String mobile_no;
	private String school_color;
	private String org_name;
	@Column(unique = true)
	private Integer priority;
	//private String job_short_name;
	private String web_status;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Integer user_id_for_email;//this id is coming from user details table's id
	private String pricipals_email;
	
	private String display_name;
	private Boolean academic_status;
	private String principal_sign;

	public Schools() {
		super();

	}
	
	@Override
	public String toString() {
		return "Schools [school_id=" + school_id + ", school_name=" + school_name + ", created_date=" + created_date
				+ ", modified_date=" + modified_date + ", org_id=" + org_id + ", school_name_short=" + school_name_short
				+ ", created_by=" + created_by + ", modified_by=" + modified_by + ", active=" + active + ", week_off="
				+ week_off + ", job_type_id=" + job_type_id + ", ref_no=" + ref_no + ", school_color=" + school_color
				+ ", org_name=" + org_name + ", priority=" + priority
				+ ", web_status=" + web_status + ", created_username=" + created_username + ", modified_username="
				+ modified_username + ", user_id_for_email=" + user_id_for_email + "]";
	}

	
	
	
	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
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

	public Integer getOrg_id() {
		return org_id;
	}

	public void setOrg_id(Integer org_id) {
		this.org_id = org_id;
	}

	public String getSchool_name_short() {
		return school_name_short;
	}

	public void setSchool_name_short(String school_name_short) {
		this.school_name_short = school_name_short;
	}

//	public String getSchool_desc() {
//		return school_desc;
//	}
//
//	public void setSchool_desc(String school_desc) {
//		this.school_desc = school_desc;
//	}

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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isWeek_off() {
		return week_off;
	}

	public void setWeek_off(boolean week_off) {
		this.week_off = week_off;
	}

	public String getJob_type_id() {
		return job_type_id;
	}

	public void setJob_type_id(String job_type_id) {
		this.job_type_id = job_type_id;
	}

	public String getRef_no() {
		return ref_no;
	}

	public void setRef_no(String ref_no) {
		this.ref_no = ref_no;
	}

//	public String getMobile_no() {
//		return mobile_no;
//	}
//
//	public void setMobile_no(String mobile_no) {
//		this.mobile_no = mobile_no;
//	}

	public String getSchool_color() {
		return school_color;
	}

	public void setSchool_color(String school_color) {
		this.school_color = school_color;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getWeb_status() {
		return web_status;
	}

	public void setWeb_status(String web_status) {
		this.web_status = web_status;
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

	public Integer getUser_id_for_email() {
		return user_id_for_email;
	}

	public void setUser_id_for_email(Integer user_id_for_email) {
		this.user_id_for_email = user_id_for_email;
	}

	public String getPricipals_email() {
		return pricipals_email;
	}

	public void setPricipals_email(String pricipals_email) {
		this.pricipals_email = pricipals_email;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public Boolean getAcademic_status() {
		return academic_status;
	}

	public void setAcademic_status(Boolean academic_status) {
		this.academic_status = academic_status;
	}

	public String getPrincipal_sign() {
		return principal_sign;
	}

	public void setPrincipal_sign(String principal_sign) {
		this.principal_sign = principal_sign;
	}
	
	
}
