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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name = "candidate_walkin_dump")
public class CandidateWalkinDump {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer candidate_id;

//	private Date date_of_visit;
	 private String username;
	private Integer visitor_id;	
	private String candidate_name;
	private String candidate_sex;
	
	//@DateTimeFormat(pattern = "yyyy-mm-dd")
	//@Basic
	@Temporal(TemporalType.DATE)
	private Date date_of_birth;
	private String candidate_last_name;
	private String mobile_number;
//	private Integer purpose_of_visit_id;
//	private Integer visitor_type_id;
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
	
//	@Column(updatable = false)
//	@Temporal(TemporalType.TIMESTAMP)
//	@CreationTimestamp
//	private Date date_of_admission;

	private String candidate_email;
	private String nationality;
	
	private String category;
	private String caste;
	private String religion;
	private String place_of_birth;
	private String blood_group;
	private String present_address;
	private Integer present_pincode;
	private String present_country;
	private String present_state;
	private String permanent_address;
	private Integer permanent_pincode;
	private String permanent_country;
	private String permanent_state;
	private String permanent_city;
//	private Integer alternate_contact_number_permanent;
	private String father_occupation;
	private String father_email;
	private String father_qualification;
	private Float father_annual_income;
	private String father_mobile;
	private String mother_name;
	private String mother_occupation;
	private String mother_email;
	private String mother_qualification;
	private String mother_mobile;
	private String guardian_name;
	private String guardian_city;
	private String guardian_relation_to_student;
	private String guardian_email;
	private String guardian_address;
	private String guardian_pincode;
	private String guardian_mobile;
	private String guardian_occupation;
	
	private String sslc_school_name;
	private Integer sslc_year_of_passing;
	private String sslc_board;
	private Integer sslc_registration_number;
	private float sslc_percentage_grade;

	private String puc_school_name;
	private String puc_board;
	private String is_puc_result;
	private String puc_mode_of_study;
	private Integer puc_registration_number;
	private Integer puc_year_of_passing;
	private float puc_percentage_grade;
	
	private String evaluation_type;
	
	private String puc_subjects;
	private float puc_subject_max_marks;
	private float puc_subject_marks_obtain;
	private float puc_percentage_obtain;

	private String entrance_exam_name;
	private float marks_rank_obtain;
	
	private String degree_diploma;
	private String pg_diploma;
	private String photo;
	private Integer other_candidate_id;
//	private Integer alternate_contact_number_present;
	private String school_npf;
//	private String program_npf;
//	private String program_specilization_npf;
	private String application_no_npf;
//	private String visitor_type_npf;
//	private String course_branch_subtype_npf;
	private String counselor_remarks;

	private Integer counselor_status;
	private Integer paid_status;
	private String voucher_code;
//	private Integer form_id;
	private Integer npf_status;
	private Date link_exp;
	private String ip_address;
	private Integer program_assignment_id;
	private Date mail_sent_date;
	
	public CandidateWalkinDump() {
		super();
	}

	public Integer getCandidate_id() {
		return candidate_id;
	}

	public void setCandidate_id(Integer candidate_id) {
		this.candidate_id = candidate_id;
	}

//	public Date getDate_of_visit() {
//		return date_of_visit;
//	}
//
//	public void setDate_of_visit(Date date_of_visit) {
//		this.date_of_visit = date_of_visit;
//	}

	public Integer getVisitor_id() {
		return visitor_id;
	}

	public void setVisitor_id(Integer visitor_id) {
		this.visitor_id = visitor_id;
	}

	public String getCandidate_name() {
		return candidate_name;
	}

	public void setCandidate_name(String candidate_name) {
		this.candidate_name = candidate_name;
	}

	public String getCandidate_sex() {
		return candidate_sex;
	}

	public void setCandidate_sex(String candidate_sex) {
		this.candidate_sex = candidate_sex;
	}

	public Date getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

//	public Integer getPurpose_of_visit_id() {
//		return purpose_of_visit_id;
//	}
//
//	public void setPurpose_of_visit_id(Integer purpose_of_visit_id) {
//		this.purpose_of_visit_id = purpose_of_visit_id;
//	}

//	public Integer getVisitor_type_id() {
//		return visitor_type_id;
//	}
//
//	public void setVisitor_type_id(Integer visitor_type_id) {
//		this.visitor_type_id = visitor_type_id;
//	}

	public Integer getAc_year_id() {
		return ac_year_id;
	}

	public String getCandidate_last_name() {
		return candidate_last_name;
	}

	public void setCandidate_last_name(String candidate_last_name) {
		this.candidate_last_name = candidate_last_name;
	}

	public void setAc_year_id(Integer ac_year_id) {
		this.ac_year_id = ac_year_id;
	}

	public String getFather_name() {
		return father_name;
	}

	public void setFather_name(String father_name) {
		this.father_name = father_name;
	}

	public Integer getCity_id() {
		return city_id;
	}

	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}

	public Integer getState_id() {
		return state_id;
	}

	public void setState_id(Integer state_id) {
		this.state_id = state_id;
	}

	public Integer getCountry_id() {
		return country_id;
	}

	public void setCountry_id(Integer country_id) {
		this.country_id = country_id;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getProgram_id() {
		return program_id;
	}

	public void setProgram_id(Integer program_id) {
		this.program_id = program_id;
	}

	public Integer getProgram_specilaization_id() {
		return program_specilaization_id;
	}

	public void setProgram_specilaization_id(Integer program_specilaization_id) {
		this.program_specilaization_id = program_specilaization_id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRep_name() {
		return rep_name;
	}

	public void setRep_name(String rep_name) {
		this.rep_name = rep_name;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Integer getModified_by() {
		return modified_by;
	}

	public void setModified_by(Integer modified_by) {
		this.modified_by = modified_by;
	}

	public Date getModified_date() {
		return modified_date;
	}

	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

//	public Date getDate_of_admission() {
//		return date_of_admission;
//	}
//
//	public void setDate_of_admission(Date date_of_admission) {
//		this.date_of_admission = date_of_admission;
//	}

	public String getCandidate_email() {
		return candidate_email;
	}

	public void setCandidate_email(String candidate_email) {
		this.candidate_email = candidate_email;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCaste() {
		return caste;
	}

	public void setCaste(String caste) {
		this.caste = caste;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getPlace_of_birth() {
		return place_of_birth;
	}

	public void setPlace_of_birth(String place_of_birth) {
		this.place_of_birth = place_of_birth;
	}

	public String getBlood_group() {
		return blood_group;
	}

	public void setBlood_group(String blood_group) {
		this.blood_group = blood_group;
	}

	public String getPresent_address() {
		return present_address;
	}

	public void setPresent_address(String present_address) {
		this.present_address = present_address;
	}

	public Integer getPresent_pincode() {
		return present_pincode;
	}

	public void setPresent_pincode(Integer present_pincode) {
		this.present_pincode = present_pincode;
	}

	public String getPresent_country() {
		return present_country;
	}

	public void setPresent_country(String present_country) {
		this.present_country = present_country;
	}

	public String getPresent_state() {
		return present_state;
	}

	public void setPresent_state(String present_state) {
		this.present_state = present_state;
	}

	public String getPermanent_address() {
		return permanent_address;
	}

	public void setPermanent_address(String permanent_address) {
		this.permanent_address = permanent_address;
	}

	public Integer getPermanent_pincode() {
		return permanent_pincode;
	}

	public void setPermanent_pincode(Integer permanent_pincode) {
		this.permanent_pincode = permanent_pincode;
	}

	public String getPermanent_country() {
		return permanent_country;
	}

	public void setPermanent_country(String permanent_country) {
		this.permanent_country = permanent_country;
	}

	public String getPermanent_state() {
		return permanent_state;
	}

	public void setPermanent_state(String permanent_state) {
		this.permanent_state = permanent_state;
	}

	public String getPermanent_city() {
		return permanent_city;
	}

	public void setPermanent_city(String permanent_city) {
		this.permanent_city = permanent_city;
	}

//	public Integer getAlternate_contact_number_permanent() {
//		return alternate_contact_number_permanent;
//	}
//
//	public void setAlternate_contact_number_permanent(Integer alternate_contact_number_permanent) {
//		this.alternate_contact_number_permanent = alternate_contact_number_permanent;
//	}

	public String getFather_occupation() {
		return father_occupation;
	}

	public void setFather_occupation(String father_occupation) {
		this.father_occupation = father_occupation;
	}

	public String getFather_email() {
		return father_email;
	}

	public void setFather_email(String father_email) {
		this.father_email = father_email;
	}
	
	

	public String getCreated_username() {
		return created_username;
	}

	public void setCreated_username(String created_username) {
		this.created_username = created_username;
	}

	public String getModified_username() {
		return modified_username;
	}

	public void setModified_username(String modified_username) {
		this.modified_username = modified_username;
	}

	public String getFather_qualification() {
		return father_qualification;
	}

	public void setFather_qualification(String father_qualification) {
		this.father_qualification = father_qualification;
	}

	public Float getFather_annual_income() {
		return father_annual_income;
	}

	public void setFather_annual_income(Float father_annual_income) {
		this.father_annual_income = father_annual_income;
	}

	public String getFather_mobile() {
		return father_mobile;
	}

	public void setFather_mobile(String father_mobile) {
		this.father_mobile = father_mobile;
	}

	public String getMother_name() {
		return mother_name;
	}

	public void setMother_name(String mother_name) {
		this.mother_name = mother_name;
	}

	public String getMother_occupation() {
		return mother_occupation;
	}

	public void setMother_occupation(String mother_occupation) {
		this.mother_occupation = mother_occupation;
	}

	public String getMother_email() {
		return mother_email;
	}

	public void setMother_email(String mother_email) {
		this.mother_email = mother_email;
	}

	public String getMother_qualification() {
		return mother_qualification;
	}

	public void setMother_qualification(String mother_qualification) {
		this.mother_qualification = mother_qualification;
	}

	public String getMother_mobile() {
		return mother_mobile;
	}

	public void setMother_mobile(String mother_mobile) {
		this.mother_mobile = mother_mobile;
	}

	public String getGuardian_name() {
		return guardian_name;
	}

	public void setGuardian_name(String guardian_name) {
		this.guardian_name = guardian_name;
	}

	public String getGuardian_city() {
		return guardian_city;
	}

	public void setGuardian_city(String guardian_city) {
		this.guardian_city = guardian_city;
	}

	public String getGuardian_relation_to_student() {
		return guardian_relation_to_student;
	}

	public void setGuardian_relation_to_student(String guardian_relation_to_student) {
		this.guardian_relation_to_student = guardian_relation_to_student;
	}

	public String getGuardian_email() {
		return guardian_email;
	}

	public void setGuardian_email(String guardian_email) {
		this.guardian_email = guardian_email;
	}

	public String getGuardian_address() {
		return guardian_address;
	}

	public void setGuardian_address(String guardian_address) {
		this.guardian_address = guardian_address;
	}

	public String getGuardian_pincode() {
		return guardian_pincode;
	}

	public void setGuardian_pincode(String guardian_pincode) {
		this.guardian_pincode = guardian_pincode;
	}

	public String getGuardian_mobile() {
		return guardian_mobile;
	}

	public void setGuardian_mobile(String guardian_mobile) {
		this.guardian_mobile = guardian_mobile;
	}

	public String getGuardian_occupation() {
		return guardian_occupation;
	}

	public void setGuardian_occupation(String guardian_occupation) {
		this.guardian_occupation = guardian_occupation;
	}

	public String getSslc_school_name() {
		return sslc_school_name;
	}

	public void setSslc_school_name(String sslc_school_name) {
		this.sslc_school_name = sslc_school_name;
	}

	public Integer getSslc_year_of_passing() {
		return sslc_year_of_passing;
	}

	public void setSslc_year_of_passing(Integer sslc_year_of_passing) {
		this.sslc_year_of_passing = sslc_year_of_passing;
	}

	public String getSslc_board() {
		return sslc_board;
	}

	public void setSslc_board(String sslc_board) {
		this.sslc_board = sslc_board;
	}

	public Integer getSslc_registration_number() {
		return sslc_registration_number;
	}

	public void setSslc_registration_number(Integer sslc_registration_number) {
		this.sslc_registration_number = sslc_registration_number;
	}

	public float getSslc_percentage_grade() {
		return sslc_percentage_grade;
	}

	public void setSslc_percentage_grade(float sslc_percentage_grade) {
		this.sslc_percentage_grade = sslc_percentage_grade;
	}

	public String getPuc_school_name() {
		return puc_school_name;
	}

	public void setPuc_school_name(String puc_school_name) {
		this.puc_school_name = puc_school_name;
	}

	public String getPuc_board() {
		return puc_board;
	}

	public void setPuc_board(String puc_board) {
		this.puc_board = puc_board;
	}

	public String getIs_puc_result() {
		return is_puc_result;
	}

	public void setIs_puc_result(String is_puc_result) {
		this.is_puc_result = is_puc_result;
	}

	public String getPuc_mode_of_study() {
		return puc_mode_of_study;
	}

	public void setPuc_mode_of_study(String puc_mode_of_study) {
		this.puc_mode_of_study = puc_mode_of_study;
	}

	public Integer getPuc_registration_number() {
		return puc_registration_number;
	}

	public void setPuc_registration_number(Integer puc_registration_number) {
		this.puc_registration_number = puc_registration_number;
	}

	public Integer getPuc_year_of_passing() {
		return puc_year_of_passing;
	}

	public void setPuc_year_of_passing(Integer puc_year_of_passing) {
		this.puc_year_of_passing = puc_year_of_passing;
	}

	public float getPuc_percentage_grade() {
		return puc_percentage_grade;
	}

	public void setPuc_percentage_grade(float puc_percentage_grade) {
		this.puc_percentage_grade = puc_percentage_grade;
	}

	public String getEvaluation_type() {
		return evaluation_type;
	}

	public void setEvaluation_type(String evaluation_type) {
		this.evaluation_type = evaluation_type;
	}

	public String getPuc_subjects() {
		return puc_subjects;
	}

	public void setPuc_subjects(String puc_subjects) {
		this.puc_subjects = puc_subjects;
	}

	public float getPuc_subject_max_marks() {
		return puc_subject_max_marks;
	}

	public void setPuc_subject_max_marks(float puc_subject_max_marks) {
		this.puc_subject_max_marks = puc_subject_max_marks;
	}

	public float getPuc_subject_marks_obtain() {
		return puc_subject_marks_obtain;
	}

	public void setPuc_subject_marks_obtain(float puc_subject_marks_obtain) {
		this.puc_subject_marks_obtain = puc_subject_marks_obtain;
	}

	public float getPuc_percentage_obtain() {
		return puc_percentage_obtain;
	}

	public void setPuc_percentage_obtain(float puc_percentage_obtain) {
		this.puc_percentage_obtain = puc_percentage_obtain;
	}

	public String getEntrance_exam_name() {
		return entrance_exam_name;
	}

	public void setEntrance_exam_name(String entrance_exam_name) {
		this.entrance_exam_name = entrance_exam_name;
	}

	public float getMarks_rank_obtain() {
		return marks_rank_obtain;
	}

	public void setMarks_rank_obtain(float marks_rank_obtain) {
		this.marks_rank_obtain = marks_rank_obtain;
	}

	public String getDegree_diploma() {
		return degree_diploma;
	}

	public void setDegree_diploma(String degree_diploma) {
		this.degree_diploma = degree_diploma;
	}

	public String getPg_diploma() {
		return pg_diploma;
	}

	public void setPg_diploma(String pg_diploma) {
		this.pg_diploma = pg_diploma;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Integer getOther_candidate_id() {
		return other_candidate_id;
	}

	public void setOther_candidate_id(Integer other_candidate_id) {
		this.other_candidate_id = other_candidate_id;
	}

//	public Integer getAlternate_contact_number_present() {
//		return alternate_contact_number_present;
//	}
//
//	public void setAlternate_contact_number_present(Integer alternate_contact_number_present) {
//		this.alternate_contact_number_present = alternate_contact_number_present;
//	}

	public String getSchool_npf() {
		return school_npf;
	}

	public void setSchool_npf(String school_npf) {
		this.school_npf = school_npf;
	}

//	public String getProgram_npf() {
//		return program_npf;
//	}
//
//	public void setProgram_npf(String program_npf) {
//		this.program_npf = program_npf;
//	}

//	public String getProgram_specilization_npf() {
//		return program_specilization_npf;
//	}
//
//	public void setProgram_specilization_npf(String program_specilization_npf) {
//		this.program_specilization_npf = program_specilization_npf;
//	}

	public String getApplication_no_npf() {
		return application_no_npf;
	}

	public void setApplication_no_npf(String application_no_npf) {
		this.application_no_npf = application_no_npf;
	}

//	public String getVisitor_type_npf() {
//		return visitor_type_npf;
//	}
//
//	public void setVisitor_type_npf(String visitor_type_npf) {
//		this.visitor_type_npf = visitor_type_npf;
//	}

//	public String getCourse_branch_subtype_npf() {
//		return course_branch_subtype_npf;
//	}
//
//	public void setCourse_branch_subtype_npf(String course_branch_subtype_npf) {
//		this.course_branch_subtype_npf = course_branch_subtype_npf;
//	}

	public String getCounselor_remarks() {
		return counselor_remarks;
	}

	public void setCounselor_remarks(String counselor_remarks) {
		this.counselor_remarks = counselor_remarks;
	}

	public Integer getCounselor_status() {
		return counselor_status;
	}

	public void setCounselor_status(Integer counselor_status) {
		this.counselor_status = counselor_status;
	}

	public Integer getPaid_status() {
		return paid_status;
	}

	public void setPaid_status(Integer paid_status) {
		this.paid_status = paid_status;
	}

	public String getVoucher_code() {
		return voucher_code;
	}

	public void setVoucher_code(String voucher_code) {
		this.voucher_code = voucher_code;
	}

//	public Integer getForm_id() {
//		return form_id;
//	}
//
//	public void setForm_id(Integer form_id) {
//		this.form_id = form_id;
//	}

	public Integer getNpf_status() {
		return npf_status;
	}

	public void setNpf_status(Integer npf_status) {
		this.npf_status = npf_status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getLink_exp() {
		return link_exp;
	}

	public void setLink_exp(Date link_exp) {
		this.link_exp = link_exp;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public Integer getProgram_assignment_id() {
		return program_assignment_id;
	}

	public void setProgram_assignment_id(Integer program_assignment_id) {
		this.program_assignment_id = program_assignment_id;
	}

	public Date getMail_sent_date() {
		return mail_sent_date;
	}

	public void setMail_sent_date(Date mail_sent_date) {
		this.mail_sent_date = mail_sent_date;
	}
}
