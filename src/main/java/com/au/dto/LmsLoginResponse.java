package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LmsLoginResponse {

	private String accessToken;
	private String username;
	private String email;
	private String lms_role;
	private Integer lms_role_id;
	private String branch_name;
	private Integer branch_id;
	private String empcode;
	private Boolean is_auditor;
	private String employee_name;
	private String master_code;
	private String contract_empcode;
	private Integer emp_id;
	
	private String auid;
	private String student_name;
	private String institute_name_short;
	private String course_branch_short_name;
	private Integer current_year;
	private Integer current_sem;
	private String section_short_name;
	private String image_path;
	private String course_name;
	private String course_short_name;
	private String course_branch_name;
	private String acerp_email;
	private String usn;
	private String ac_year;
	private Boolean due_status;
	private Boolean eligible_status;
	private Integer course_branch_assignment_id;
	private Boolean section_status;
	private Integer institute_id;
	private String token;
	


}
