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
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;


@Data
@Entity
@Table(name = "candidate_walkin")
public class Candidate_Walkin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer candidate_id;

	 private String username;
	private String visitor_id;	
	private String candidate_name;
	@Size(max=30)
	private String candidate_sex;
	@Size(max=30)
	private String date_of_birth;
	private String candidate_last_name;
	@Size(max=30)
	private String mobile_number;
	private Integer ac_year_id;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;

	private String father_name;
	private Integer city_id;
	private Integer state_id; 
	private Integer country_id;
	private Integer school_id;
	private Integer program_id;                     //FK
	private Integer program_specilaization_id;      //FK
	private String remarks;
	private String rep_name;
	
	@Column(updatable = false)
	private Integer created_by;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	
	private Integer modified_by;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	private Boolean active;
	
	private String candidate_email;
	private Integer nationality;
	@Size(max=30)
	private String category;
	@Size(max=75)
	private String caste;
	@Size(max=75)
	private String religion;
	@Size(max=200)
	private String place_of_birth;
	@Size(max=30)
	private String blood_group;
	private String present_address;
	private Integer present_pincode;
	private Integer present_country;
	private Integer present_state;
	private String permanent_address;
	private Integer permanent_pincode;
	private Integer permanent_country;
	private Integer permanent_state;
	private Integer permanent_city;
	@Size(max=255)
	private String father_occupation;
	@Size(max=150)
	private String father_email;
	@Size(max=150)
	private String father_qualification;
	private Float father_annual_income;
	@Size(max=30)
	private String father_mobile;
	@Size(max=250)
	private String mother_name;
	@Size(max=230)
	private String mother_occupation;
	@Size(max=100)
	private String mother_email;
	@Size(max=150)
	private String mother_qualification;
	@Size(max=30)
	private String mother_mobile;
	@Size(max=250)
	private String guardian_name;
	@Size(max=150)
	private String guardian_city;
	@Size(max=50)
	private String guardian_relation_to_student;
	@Size(max=200)
	private String guardian_email;
	private String guardian_address;
	@Size(max=30)
	private String guardian_pincode;
	@Size(max=30)
	private String guardian_mobile;
	@Size(max=255)
	private String guardian_occupation;
	@Size(max=200)
	private String sslc_school_name;
	private Integer sslc_year_of_passing;
	private String sslc_board;
	private String sslc_registration_number;
	private String sslc_percentage_grade;
	private float sslc_subject_max_marks;
	private float sslc_subject_marks_obtain;
	
	
	@Size(max=200)
	private String puc_school_name;
	private String puc_board;
	@Size(max=30)
	private String is_puc_result;
	@Size(max=30)
	private String puc_mode_of_study;
	private String puc_registration_number;
	private Integer puc_year_of_passing;
	private String puc_percentage_grade;
	@Size(max=30)
	private String evaluation_type;
	@Size(max=200)
	private String puc_subjects;
	private float puc_subject_max_marks;
	private float puc_subject_marks_obtain;
	private float puc_percentage_obtain;
	@Size(max=250)
	private String ug_board; 
	@Size(max=150)
	private String ug_registration_number;
	@Size(max=200)
	private String ug_school_name; 
	private Integer ug_year_of_passing;
	private Float ug_subject_marks_obtain; 
	private Float ug_subject_max_marks;
	private Float ug_percentage_grade;
	
	@Size(max=16)
	private String aadhar;

	private String entrance_exam_name;
	private float marks_rank_obtain;
	private Integer other_candidate_id;
	@Size(max=200)
	private String school_npf;
	@Column(unique=true)
	private String application_no_npf;
	private String counselor_remarks;

	private Integer counselor_status;
	private Integer paid_status;
	@Size(max=50)
	private String voucher_code;
	private Integer npf_status;
	private String link_exp;
	private String ip_address;
	private Integer program_assignment_id;
	private Date mail_sent_date;
	@Size(max=20)
	private String form_filled_percentage;
	@Size(max=255)
	private String permanant_adress1;
	@Size(max=255)
	private String present_address1;
	private Integer present_city_id;
	private Float mother_annual_income;
	private Integer graduation_id;
	@Size(max=30)
	private String application_status;
	
	@Size(max=15)
	private String result_status;
	@Size(max=30)
	private String exam_date;
	private Integer exam_details_id;
	@Size(max=50)
	private String lead_status;
	
//	private Integer interview_id;
	
	/**
	 * counselor_id and counselor_name is user_id and user name of user details table
	 */
	private Integer counselor_id;
	@Size(max=250)
	private String counselor_name;
	
	@Size(max=150)
	private String counsellor_email;
	
	@Size(max=100)
	private String result_score;

	@Size(max=50)
	private String passport_expiry_date;

	private String location;

	private Float gpa_or_percentage;
	@Size(max=200)
	private String course_completed;

	@Size(max=100)
	private String other_sources;
	
	
	@Size(max=255)
	private String lead_id;
	
	@Size(max=255)
	private String opportunity_id;
	
	private String marital_status;
	private String whatsapp_number ;
	private String entrance_exam_date;
	private String rank_obtained;
	private String alternate_number;
	
	private Boolean is_nri;
	private String entrance_exam_result; 
	private String rural_urban;
	private String applicant_status;
	
	private String entrance_exam_score;
	private String aet_result;
	
	private String passport_number; 
	private String passport_issued_by;
	private String aet_date;
	private String source;
	private String campaign_name;
	private String utm_value;
	
}