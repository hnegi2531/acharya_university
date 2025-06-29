package com.au.dto;

import java.util.List;

public class UserRoleRequest {

	private int id;
	private String username;
	private String password;
	private String usertype;
	private String email;
	private String usercode;
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	public List<Integer> role_id;

	private String created_username;
	private String modified_username;
	
	private Boolean guest_type;

	public UserRoleRequest() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
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

	public List<Integer> getRole_id() {
		return role_id;
	}

	public void setRole_id(List<Integer> role_id) {
		this.role_id = role_id;
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

	public Boolean getGuest_type() {
		return guest_type;
	}

	public void setGuest_type(Boolean guest_type) {
		this.guest_type = guest_type;
	}
	

}
