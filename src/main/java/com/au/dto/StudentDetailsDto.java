package com.au.dto;

import java.util.Date;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
public class StudentDetailsDto {

	private String acharya_email;

	private String adhar_number;

	
	private String firstname;
	
	private String lastname;
	private String student_name;
	private String candidate_sex;
	private String nationality;
	private String photo;
	private String mobile;
	@Temporal(TemporalType.DATE)
	private Date dateofbirth;
	
	private String father_name;
	
	private String mother_name;
	@Column(length = 15)
	private String parents_mobile;
	@Column(length = 25)
	private String parents_email;
	@Column(length = 200)
	private String current_address;
	@Column(length = 200)
	private String permanent_address;
	@Column(length = 60)
	private String guardian_name;
	@Column(length = 15)
	private String guardian_phone;
	@Column(length = 10)
	private String blood_group;
	@Column(length = 20)
	private String religion;
	@Column(length = 20)
	private String caste;
	@Column(length = 20)
	private String surname;
	@Column(length = 50)
	private String permanant_country;
	@Column(length = 50)
	private String permanant_pincode;
	@Column(length = 50)
	private String permanant_city;
	/*
	 * @Column(length = 20) private String permant_adress2;
	 */
	@Column(length = 200)
	private String permanant_adress1;
	@Column(length = 50)
	private String local_email;
	@Column(length = 15)
	private String local_phone;
	@Column(length = 15)
	private String local_mobile;
	@Column(length = 30)
	private String local_state;
	@Column(length = 30)
	private String local_country;
	@Column(length = 20)
	private String local_pincode;
	@Column(length = 30)
	private String local_city;
	/*
	 * @Column(length = 20) private String local_adress2;
	 */
	@Column(length = 200)
	private String local_adress1;
	@Column(length = 30)
	private String current_city;
	@Column(length = 30)
	private String current_email;
	@Column(length = 15)
	private String current_phone;
	@Column(length = 15)
	private String current_mobile;
	@Column(length = 30)
	private String current_state;
	@Column(length = 30)
	private String current_country;
	@Column(length = 20)
	private String current_pincode;
	@Column(length = 200)
	private String current_adress1;
	@Column(length = 20)
	private String permanant_state;
	@Column(length = 20)
	private String permanant_mobile;
	@Column(length = 20)
	private String permanant_phone;
	@Column(length = 20)
	private String permanant_email;
	@Column(length = 30)
	private String account_holder_name;
	@Column(length = 50)
	private String bank_name;
	@Column(length = 30)
	private String account_number;
	@Column(length = 30)
	private String bank_branch;
	@Column(length = 20)
	private String ifsc_code;
	@Column(length = 20)
	private String p_city;
	@Column(length = 20)
	private String c_city;
	@Column(length = 20)
	private String l_city;
	@Column(length = 30)
	private String father_occupation;
	@Column(length = 20)
	private String mother_occupation;

	private Float father_income;

	private Float mother_income;
	@Column(length = 80)
	private String father_email;
	@Column(length = 80)
	private String mother_email;
	
	@Column(length = 15)
	private String father_mobile;
	@Column(length = 20)
	private String mother_mobile;
	@Column(unique = true)
	private String auid;
	@Column(length = 20)
	private String usn;
	@Column(unique = true)
	private Integer candidate_id;
	@Temporal(TemporalType.DATE)
	private Date dateofjoining;

	private Integer school_id;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	private Integer created_by;
	private Integer modified_by;
	private Boolean active;

	private Integer ac_year_id;
	private Integer fee_admission_category_id;
	private Integer program_id;
	private Integer program_specialization_id;
	private Integer fee_template_id;
	@Column(length = 20)
	private String allotment_number;
	private Integer visitor_id;
	private Integer lateral_sem;

	private Integer deassign_status;
	private Integer course_approver_status;

	@CreationTimestamp
	@Column(updatable = false)
	@Temporal(TemporalType.DATE)
	private Date date_of_admission;

	private Integer joining_year;
	private Integer joining_sem;
	private String student_email;
	private Integer scholarship_status;
	private String entrance_test_no;
	private String rank1;

	@CreationTimestamp
	@Column(updatable = false)
	@Temporal(TemporalType.DATE)
	private Date order_no_date;

	private Integer category_reserved;
	private Integer category_alloted;
	private Integer rural_urban;
	private Integer special_category;
	private Integer karnataka_medium;

	private Boolean board_admission_status;
	private String entranct_test_type;

	@Temporal(TemporalType.DATE)
	private Date board_admission_date;

	private Integer photo_upload_status;
	private Integer re_admission_status;

	private Integer proctor_assign_status;
	private Integer old_student_id;
	private Integer idcard_ac_status;
	private Integer passed_status;
	private Integer old_std_id_readmn;

	
	private String created_username;
	private String modified_username;
	private String student_master_code;
	private String passport_no;
	private String visa_no;
	private String student_image_path;
	private Integer program_assignment_id;
	
	
	private Integer board_university_id;
	private String mother_qualification;
	private String father_qualification;
	private String caste_category;
	private String old_auid_format;
	private String email_preferred_name;
	
	private Boolean id_barcode_generated;
	
	private Boolean laptop_issued_status;
	@Column(length = 20)
	private String laptop_issued_date;
	private Integer id_card_issued_year;
	
	private String pdfContent;
	private Boolean id_card_bucket_status;

	
	
	
}
