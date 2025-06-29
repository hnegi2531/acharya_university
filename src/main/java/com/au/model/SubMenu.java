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
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "sub_menu")
public class SubMenu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer submenu_id;
	
	@Lob
	@Column(unique = true)
	@NotBlank(message = "submenu_name should not be Empty OR Null")
	private String submenu_name;
	@Column(unique = true)
	@NotBlank(message = "submenu_url should not be Empty OR Null")
	private String submenu_url;
	private String submenu_desc;
	private String status;
	private Integer menu_id;
	private String user_ids;
	
	private Boolean mask;
	
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

	public SubMenu() {
		super();
	}

	public Integer getSubmenu_id() {
		return submenu_id;
	}

	public void setSubmenu_id(Integer submenu_id) {
		this.submenu_id = submenu_id;
	}

	public String getSubmenu_name() {
		return submenu_name;
	}

	public void setSubmenu_name(String submenu_name) {
		this.submenu_name = submenu_name;
	}

	public String getSubmenu_url() {
		return submenu_url;
	}

	public void setSubmenu_url(String submenu_url) {
		this.submenu_url = submenu_url;
	}

	public String getSubmenu_desc() {
		return submenu_desc;
	}

	public void setSubmenu_desc(String submenu_desc) {
		this.submenu_desc = submenu_desc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}

	public String getUser_ids() {
		return user_ids;
	}

	public void setUser_ids(String user_ids) {
		this.user_ids = user_ids;
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

	public Boolean getMask() {
		return mask;
	}

	public void setMask(Boolean mask) {
		this.mask = mask;
	}
	
	

}
