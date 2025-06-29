package com.au.model;

import java.io.Serializable;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private String userName;
	private String userType;
	private Integer userId;
	private String role;
	private Boolean adpStatus;
	private String  book_chapter_approver_designation;

	public JwtResponse(String jwttoken) {
		this.jwttoken = jwttoken;
	}

	public String getToken() {
		return this.jwttoken;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Boolean getAdpStatus() {
		return adpStatus;
	}

	public void setAdpStatus(Boolean adpStatus) {
		this.adpStatus = adpStatus;
	}

	public String getBook_chapter_approver_designation() {
		return book_chapter_approver_designation;
	}

	public void setBook_chapter_approver_designation(String book_chapter_approver_designation) {
		this.book_chapter_approver_designation = book_chapter_approver_designation;
	}
	
	
	
	

}