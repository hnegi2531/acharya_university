package com.au.dto;

public class UserDetailsDto {
private Integer empOrStdId;
	
	private String name;

	private String preferredName;
	
	private Integer userId;
	
	private String firstName;
	
	private String lastName;
	
	private String photoAttachmentPath;
	
	private String email;
	
	private String mobileNumber;
	
	private String usertype;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhotoAttachmentPath() {
		return photoAttachmentPath;
	}

	public void setPhotoAttachmentPath(String photoAttachmentPath) {
		this.photoAttachmentPath = photoAttachmentPath;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPreferredName() {
		return preferredName;
	}

	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getEmpOrStdId() {
		return empOrStdId;
	}

	public void setEmpOrStdId(Integer empOrStdId) {
		this.empOrStdId = empOrStdId;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	
}
