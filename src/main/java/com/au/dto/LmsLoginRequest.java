package com.au.dto;

public class LmsLoginRequest {

	private String token;

	public LmsLoginRequest() {
	
	}
	
	public LmsLoginRequest(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
