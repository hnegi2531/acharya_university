package com.au.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailsUpdateDto {

	
	private Integer emp_id ;
	private Character gender;
	private String blood_group;
	private String martial_status;
	private String employee_name ;
	private String dateofbirth;
	private String mobile;
	private String hometown;
	private String current_location;
	private String religion;
	private String alt_mobile_no;
	private String dlno;
	private String dlexpno;
	private String passportno;
	private String passportexpno;
	
	private String bank_branch;
	private Integer shift_category_id;
	private Integer leave_approver1_emp_id; 
	private Integer leave_approver2_emp_id; 
	private Integer store_indent_approver1; 
	private String bank_id;
	private String bank_account_holder_name;
	private String bank_account_no;
	private String uan_no;
	private String bank_ifsccode;
	
	private String pan_no;
	private String aadhar;
	private String personal_email;
	private Integer report_id;
	private Integer chief_proctor_id;
	private String caste_category;
}
