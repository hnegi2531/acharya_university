package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAccessTokenDetails {

	/*
	 * private String employee_name; private String branch_name; private String
	 * empcode;
	 */
	private Integer emp_id;
	private Integer role_id;
	private String lms_role;
	/* private Integer institute_id; */
	//private Integer branch_id;
	private Integer student_id;
	private Integer user_id;
	/*
	 * private String student_name; private String auid; private String
	 * institute_short_name; private String course_branch_short_name; private
	 * Integer current_year; private Integer current_sem; private String
	 * section_name;
	 */
	private Integer current_year; 
	private Integer current_sem;
	private String sub;
	private long exp;
	private long iat;

	

}
