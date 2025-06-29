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
@Table(name = "sub_menu_assignment")
public class SubMenuAssignment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer menu_assignment_id;
	@Column(unique=true)
	private Integer role_id;
	@Lob
	@Column(columnDefinition="LONGTEXT")
	private String submenu_ids;
	@Column(columnDefinition="LONGTEXT")
	private String submenu_name;
	private Integer count;
	
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
	private Integer count_role;

	public SubMenuAssignment() {
		super();
	}

	public Integer getMenu_assignment_id() {
		return menu_assignment_id;
	}

	public void setMenu_assignment_id(Integer menu_assignment_id) {
		this.menu_assignment_id = menu_assignment_id;
	}

	public Integer getRole_id() {
		return role_id;
	}

	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}

	public String getSubmenu_ids() {
		return submenu_ids;
	}

	public void setSubmenu_ids(String submenu_ids) {
		this.submenu_ids = submenu_ids;
	}

	public String getSubmenu_name() {
		return submenu_name;
	}

	public void setSubmenu_name(String submenu_name) {
		this.submenu_name = submenu_name;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
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

	public Integer getCount_role() {
		return count_role;
	}

	public void setCount_role(Integer count_role) {
		this.count_role = count_role;
	}

}
