package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import lombok.Data;


@Data
@Entity
@Table(name = "employee_details_history")
public class EmployeeDetailsHistory {
		
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer emp_history_id;
	private Integer emp_id;
	
	@Size(max=150)
	private String school_id;
	@Size(max=100)
	private String dept_id;
	@Size(max=70)
	private String job_id;
	@Size(max=150)
	private String email;
	@Size(max=200)
	private String employee_name;
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	private Integer created_by;
	@Size(max=200)
	private String created_username;
	private Boolean active;
	@Size(max=60)
	private String master_code;
	@Size(max=60)
	private String empcode;
	@Size(max=150)
	private String firstname;
	@Size(max=150)
	private String lastname;
	@Size(max=150)
	private String report_id;
	@Size(max=50)
	private String mobile;
	@Size(max=45)
	private String alt_mobile_no;
	@Size(max=50)
	private String dateofbirth;
	@Size(max=30)
	private String martial_status;
	@Size(max=35)
	private String blood_group;
	@Size(max=70)
	private String designation_id;
	@Size(max=150)
	private String father_name;
	@Size(max=150)
	private String spouse_name;
	@Size(max=255)
	private String current_location;
	@Size(max=80)
	private String salary_structure_id;
	@Size(max=60)
	private String ctc;
	@Size(max=70)
	private String pf_no;
	@Size(max=60)
	private String pan_no;
	@Size(max=70)
	private String emp_type_id;
	@Size(max=60)
	private String job_type_id;
//	@Size(max=200)
//	private String exp_in_years;
//	@Size(max=200)
//	private String exp_in_months;
	@Size(max=255)
	private String hometown;
	@Size(max=50)
	private String pincode;
	@Size(max=50)
	private String annual_salary;
	@Size(max=50)
	private String spl_pay;
	@Size(max=255)
	private String key_skills;
	@Size(max=100)
	private String bank_id;
	@Size(max=100)
	private String bank_account_no;
	@Size(max=75)
	private String bank_branch;
	@Size(max=45)
	private String bank_ifsccode;
	@Size(max=150)
	private String leave_approver1_emp_id;
	@Size(max=150)
	private String leave_approver2_emp_id;
	@Size(max=255)
	private String attach;
	@Size(max=70)
	private String dlno;
	@Size(max=70)
	private String dlexpno;
	@Size(max=100)
	private String passportno;
	@Size(max=100)
	private String passportexpno;
	@Size(max=100)
	private String bankacc2;
	@Size(max=70)
	private String to_date;
	@Size(max=50)
	private String prob;
	private Boolean nda;
	private Boolean nca;
	@Size(max=70)
	private String uan_no;
	@Size(max=70)
	private String punched_card_status;
	@Size(max=255)
	private String photo;
	@Size(max=40)
	private String shift_category_id;
	@Size(max=45)
	private String gender;
	@Size(max=30)
	private String maternity_status;
	@Size(max=30)
	private String marriage_status;
	@Size(max=30)
	private String paternity_status;
//	@Size(max=200)
//	private String transport_status;
	@Size(max=30)
	private String salary_approve_status;
//	@Size(max=200)
//	private String vehicle_route_id;
//	@Size(max=50)
//	private String salary_block_date;
	
//	@Size(max=50)
//	private String transport_assign_month;
//	@Size(max=50)
//	private String transport_assign_date;
//	@Size(max=50)
//	private String transport_deassign_date;
//	@Size(max=50)
//	private String transport_deassign_month;
	@Size(max=30)
	private String new_join_status;
	@Size(max=30)
	private String pf_status;
	@Size(max=30)
	private String pt_status;
	@Size(max=255)
	private String permanant_file;
	@Size(max = 45)
	private String aadhar;
	@Size(max = 30)
	private String photo_upload_status;
	@Size(max=30)
	private String proctor_assign_status;
//	@Size(max=200)
//	private String pick_up_point;
	@Size(max=150)
	private String bank_account_holder_name;
	@Size(max=50)
	private String grosspay_ctc;
	@Size(max=255)
	private String nda_nca_attach;
	@Size(max=150)
	private String store_indent_approver1;
	@Size(max=150)
	private String store_indent_approver2;
	@Size(max = 75)
	private String esi_no;
	@Size(max=45)
	private String hra;
	@Size(max=45)
	private String da;
	@Size(max=45)
	private String cca;
//	@Size(max=200)
//	private String ta;
//	@Size(max=200)
//	private String mr;
//	@Size(max=200)
//	private String fr;
//	@Size(max=200)
//	private String me;
	@Size(max=70)
	private String net_pay;
	@Size(max=60)
	private String other_allow;
	@Size(max=60)
	private String spl_1;
//	@Size(max=200)
//	private String fte_status;
//	@Size(max=200)
//	private String pick_id;
	@Size(max=45)
	private String title;
	@Size(max = 75)
	private String caste_category;
//	@Size(max = 50)
//	private String contract_emp_type;
	@Size(max=200)
	private String school;
	@Size(max=75)
	private String contract_empcode;
	@Size(max=75)
	private String consolidated_amount;
	@Size(max=50)
	private String from_date;
	@Size(max=50)
	private String date_of_joining;
	@Size(max=150)
	private String remarks;
	@Size(max=255)
	private String subject_skills;
	@Size(max=35)
	private String employee_status;
	@Size(max=50)
	private String proctor_type;
	@Size(max=150)
	private String chief_proctor_id;
	@Size(max=70)
	private String religion;
	@Size(max=100)
	private String preferred_name_for_email;
	@Size(max=255)
	private String emp_attachment_path;
	@Size(max=100)
	private String emp_attachment_file_name;
	@Size(max=200)
	private String emp_attachement_type;
	@Size(max=200)
	private String emp_image_attachment_path;
	@Size(max = 35)
	private String phd_status;
	@Size(max=60)
	private String salary_structure_email_content;
//	@Size(max = 100)
//	private String oked;
//	@Size(max = 100)
//	private String mfo;
//	@Size(max = 100)
//	private String pin_number;
	@Size(max=45)
	private String pt;

	@Size(max = 45)
	private String visa_expiry_date;
	@Size(max = 45)
	private String visa_document_number;
	@Size(max=45)
	private String nationality;
//	@Size(max = 50)
//	private String height;
	@Size(max = 100)
	private String place_of_birth;
	@Size(max=75)
	private String language_id;
	@Size(max=50)
	private String personal_medical_history;
	@Size(max=50)
	private String family_medical_history;
	@Size(max=100)
	private String emp_attachment_path2;
	@Size(max=100)
	private String emp_attachment_file_name2;
	@Size(max=200)
	private String emp_attachement_type2;
	@Size(max=45)
	private String id_barcode_generated;
	
	@Size(max=200)
	private String permanent_file;
	@Size(max = 150)
	private String permanent_done_by;
	@Size(max=60)
	private String permanent_status;
	@Size(max = 50)
	private String date_of_permanent;
	@Size(max=60)
	private String job_short_name;
	@Size(max=100)
	private String dept_name_short;
	@Size(max=45)
	private String school_name_short;
	@Size(max = 60)
	private String shift_name;
	
	@Size(max = 150)
	private String leave_approver1_name;
	@Size(max = 150)
	private String leave_approver2_name;
	@Size(max = 150)
	private String reportingOfficerName;
	@Size(max = 50)
	private String personal_email;
}
