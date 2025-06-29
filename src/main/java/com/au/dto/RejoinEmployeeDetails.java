package com.au.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
public class RejoinEmployeeDetails {
	
	private Integer school_id;
	private Integer dept_id;
	@Column(unique = true)
	private Integer job_id;
	@Email
	@Size(max = 200)
	@Column(updatable = false)
	private String email;
	@Size(max = 200)
	private String employee_name;
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	@Column(updatable = false)
	@Size(max = 200)
	private String created_username;
	@Size(max = 200)
	private String modified_username;
	private Boolean active;
	@Column(unique=true ,updatable = false)
	private String master_code;
	@Column(unique = true)
	private String empcode;
	@Size(max = 150)
	private String firstname;
	@Size(max = 150)
	private String lastname;
	private Integer report_id;
	@Size(max = 50)
	private String mobile;
	@Size(max = 50)
	private String alt_mobile_no;
	@Size(max = 50)
	private String dateofbirth;
	@Size(max = 50)
	private String martial_status;
	private String blood_group;
	private Integer designation_id;
	private String father_name;
	private String spouse_name;
	private String current_location;
	private Integer salary_structure_id;
	private Float ctc;
	@Size(max = 150)
	private String pf_no;
	@Size(max = 150)
	private String pan_no;
	private Integer emp_type_id;
	private Integer job_type_id;
	private Integer exp_in_years;
	private Integer exp_in_months;
	private String hometown;
	@Size(max = 50)
	private String pincode;
	private Float annual_salary;
	private Float spl_pay;
	private String key_skills;
	private String bank_id;
	private String bank_account_no;
	private String bank_branch;
	private String bank_ifsccode;
	private Integer leave_approver1_emp_id;
	private Integer leave_approver2_emp_id;
	private String attach;
	private String dlno;
	private String dlexpno;
	private String passportno;
	private String passportIssuedDate;
	private String passportIssuedBy;
	private Boolean probation;
	private String partOrFullTime;

	private String passportexpno;
	private String bankacc2;
	private String to_date;
	private Integer prob;
	private Boolean nda;
	private Boolean nca;
	private String uan_no;
	private String punched_card_status;
	private String photo;
	private Integer shift_category_id;
	private Character gender;
	private Boolean maternity_status;
	private Boolean marriage_status;
	private Boolean paternity_status;
	private Boolean transport_status;
	private Boolean salary_approve_status;
	private Integer vehicle_route_id;
	private String salary_block_date;

	@Size(max = 50)
	private String transport_assign_month;
	@Size(max = 50)
	private String transport_assign_date;
	@Size(max = 50)
	private String transport_deassign_date;
	@Size(max = 50)
	private String transport_deassign_month;

	private Integer new_join_status;
	private Boolean pf_status;
	private Boolean pt_status;
	private String permanant_file;
	@Size(max = 50)
	private String aadhar;
	@Size(max = 50)
	private String photo_upload_status;
	private Integer proctor_assign_status;
	private String pick_up_point;
	private String bank_account_holder_name;
	private Float grosspay_ctc;
	private String nda_nca_attach;
	private Integer store_indent_approver1;
	private Integer store_indent_approver2;
	@Size(max = 50)
	private String esi_no;
	private Float hra;
	private Float da;
	private Float cca;
	private Float ta;
	private Float mr;
	private Float fr;
	private Float me;
	private Float net_pay;
	private Float other_allow;
	private Float spl_1;
	private Integer fte_status;
	private Integer pick_id;
	private String title;
	@Size(max = 50)
	private String caste_category;
	@Size(max = 50)
	private String contract_emp_type;
	private String school;
	private String contract_empcode;
	private String consolidated_amount;
	private String from_date;
	private String date_of_joining;
	private String remarks;
	private String subject_skills;
	private Boolean employee_status;
	private Integer proctor_type;
	private Integer chief_proctor_id;
	private String religion;
	private String preferred_name_for_email;
	private String emp_attachment_path;
	private String emp_attachment_file_name;
	private String emp_attachement_type;
	private String emp_image_attachment_path;
	@Size(max = 100)
	private String phd_status;
	private String salary_structure_email_content;// used only for getting html content from frontend to send in mail
	private String date_of_permanent;
	private String oked;
	private String mfo;
	private String pin_number;
	private Integer pt;

	private String visa_expiry_date;
	private String visa_document_number;

	private String nationality;
	private String height;
	private String place_of_birth;
	private String pinfl;
	private Integer language_id;

	private String personal_medical_history;
	private String family_medical_history;

	private String emp_attachment_path2;
	private String emp_attachment_file_name2;
	private String emp_attachement_type2;

	private String plastic_card;
	private String id_barcode_generated;

}
