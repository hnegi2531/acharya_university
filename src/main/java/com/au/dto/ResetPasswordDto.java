package com.au.dto;

import lombok.Data;

@Data
public class ResetPasswordDto {
	
	private String token;
	private String password;

}
