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
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;


@Entity
@Table(name = "employee_details")
public class EmployeeDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer emp_id;
	private Integer school_id;
	private Integer dept_id;
	@Column(unique=true)
	private Integer job_id;
	@Email
	@Size(max=200)
	private String email;
	@Size(max=200)
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
	@Size(max=200)
	private String created_username;
	@Size(max=200)
	private String modified_username;
	private Boolean active;
	private String master_code;
	private String empcode;
	@Size(max=150)
	private String firstname;
	@Size(max=150)
	private String lastname;
	private Integer report_id;
	@Size(max=50)
	private String mobile;
	@Size(max=50)
	private String alt_mobile_no;
	@Size(max=50)
	private String dateofbirth;
	@Size(max=50)
	private String martial_status;
	private String blood_group;
	private Integer designation_id;
	private String father_name;
	private String spouse_name;
	private String current_location;
	private Integer salary_structure_id;
	private Float ctc;
	@Size(max=150)
	private String pf_no;
	@Size(max=150)
	private String pan_no;
	private Integer emp_type_id;
	private Integer job_type_id;
	private Integer exp_in_years;
	private Integer exp_in_months;
	private String hometown;
	@Size(max=50)
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
	
	@Size(max=50)
	private String transport_assign_month;
	@Size(max=50)
	private String transport_assign_date;
	@Size(max=50)
	private String transport_deassign_date;
	@Size(max=50)
	private String transport_deassign_month;
	
	private Integer new_join_status;
	private Boolean pf_status;
	private Boolean pt_status;
	@Size(max=50)
	private String aadhar;
	@Size(max=50)
	private String photo_upload_status;
	private Integer proctor_assign_status;
	private String bank_account_holder_name;
	private Float grosspay_ctc;
	private String nda_nca_attach;
	private Integer store_indent_approver1;
	private Integer store_indent_approver2;
	@Size(max=50)
	private String esi_no;
	private Float hra;
	private Float da;
	private Float cca;
	private Float cea;
	private Float cha;
	private Float fr;
	private Float me;
	private Float mr;
	private Float ta;
	private Float net_pay;
	private Float other_allow;
	private Float spl_1;
	private Integer fte_status;
	private String title;
	@Size(max=50)
	private String caste_category;
	@Size(max=50)
	private String contract_emp_type;
	private String school;
	private String contract_empcode;
	private Float consolidated_amount;
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
	@Size(max=100)
	private String phd_status;
	private String salary_structure_email_content;//used only for getting html content from frontend to send in mail
	
	@Size(max = 200)
	private String cancel_remark;
	
	private String personal_email;
	private String mfo;
	private String pinfl;
	@Size(max = 50)
	private String height;
	
	private String personal_medical_history;
	private String family_medical_history;
	private Integer language_id;
	private String permanent_file;
	private String permanent_done_by;
	/**
	 * by Default Value=1 For Probationary of ORR Employee Type and 
	 * value=2 For Permanent Employee Of ORR Employee Type
	 */
	private Integer permanent_status;
	private String date_of_permanent;
	@Size(max = 100)
	private String permanent_remarks;
	
	private String emp_attachment_path2;
	private String emp_attachment_file_name2;
	private String emp_attachement_type2;
	
	private String job_short_name;
	private String dept_name_short;
	private String school_name_short;
	private String shift_name;
	
	private String nationality;
	private String plastic_card;

	public EmployeeDetails() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Integer getEmp_id() {
		return emp_id;
	}


	public void setEmp_id(Integer emp_id) {
		this.emp_id = emp_id;
	}


	public Integer getSchool_id() {
		return school_id;
	}


	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}


	public Integer getDept_id() {
		return dept_id;
	}


	public void setDept_id(Integer dept_id) {
		this.dept_id = dept_id;
	}


	public Integer getJob_id() {
		return job_id;
	}


	public void setJob_id(Integer job_id) {
		this.job_id = job_id;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getEmployee_name() {
		return employee_name;
	}


	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}


	public Date getCreated_date() {
		return created_date;
	}


	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}


	public Date getModified_date() {
		return modified_date;
	}


	public String getNationality() {
		return nationality;
	}


	public void setNationality(String nationality) {
		this.nationality = nationality;
	}


	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}


	public Integer getCreated_by() {
		return created_by;
	}


	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}


	public Integer getModified_by() {
		return modified_by;
	}


	public void setModified_by(Integer modified_by) {
		this.modified_by = modified_by;
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


	public Boolean getActive() {
		return active;
	}


	public void setActive(Boolean active) {
		this.active = active;
	}


	public String getMaster_code() {
		return master_code;
	}


	public void setMaster_code(String master_code) {
		this.master_code = master_code;
	}


	public String getEmpcode() {
		return empcode;
	}


	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}


	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public Integer getReport_id() {
		return report_id;
	}


	public void setReport_id(Integer report_id) {
		this.report_id = report_id;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getAlt_mobile_no() {
		return alt_mobile_no;
	}


	public void setAlt_mobile_no(String alt_mobile_no) {
		this.alt_mobile_no = alt_mobile_no;
	}


	public String getDateofbirth() {
		return dateofbirth;
	}


	public void setDateofbirth(String dateofbirth) {
		this.dateofbirth = dateofbirth;
	}


	public String getMartial_status() {
		return martial_status;
	}


	public void setMartial_status(String martial_status) {
		this.martial_status = martial_status;
	}


	public String getBlood_group() {
		return blood_group;
	}


	public void setBlood_group(String blood_group) {
		this.blood_group = blood_group;
	}


	public Integer getDesignation_id() {
		return designation_id;
	}


	public void setDesignation_id(Integer designation_id) {
		this.designation_id = designation_id;
	}


	public String getFather_name() {
		return father_name;
	}


	public void setFather_name(String father_name) {
		this.father_name = father_name;
	}


	public String getSpouse_name() {
		return spouse_name;
	}


	public void setSpouse_name(String spouse_name) {
		this.spouse_name = spouse_name;
	}


	public String getCurrent_location() {
		return current_location;
	}


	public void setCurrent_location(String current_location) {
		this.current_location = current_location;
	}


	public Integer getSalary_structure_id() {
		return salary_structure_id;
	}


	public void setSalary_structure_id(Integer salary_structure_id) {
		this.salary_structure_id = salary_structure_id;
	}


	public Float getCtc() {
		return ctc;
	}


	public void setCtc(Float ctc) {
		this.ctc = ctc;
	}


	public String getPf_no() {
		return pf_no;
	}


	public void setPf_no(String pf_no) {
		this.pf_no = pf_no;
	}


	public String getPan_no() {
		return pan_no;
	}


	public void setPan_no(String pan_no) {
		this.pan_no = pan_no;
	}


	public Integer getEmp_type_id() {
		return emp_type_id;
	}


	public void setEmp_type_id(Integer emp_type_id) {
		this.emp_type_id = emp_type_id;
	}


	public Integer getJob_type_id() {
		return job_type_id;
	}


	public void setJob_type_id(Integer job_type_id) {
		this.job_type_id = job_type_id;
	}


	public Integer getExp_in_years() {
		return exp_in_years;
	}


	public void setExp_in_years(Integer exp_in_years) {
		this.exp_in_years = exp_in_years;
	}


	public Integer getExp_in_months() {
		return exp_in_months;
	}


	public void setExp_in_months(Integer exp_in_months) {
		this.exp_in_months = exp_in_months;
	}


	public String getHometown() {
		return hometown;
	}


	public void setHometown(String hometown) {
		this.hometown = hometown;
	}


	public String getPincode() {
		return pincode;
	}


	public void setPincode(String pincode) {
		this.pincode = pincode;
	}


	public Float getAnnual_salary() {
		return annual_salary;
	}


	public void setAnnual_salary(Float annual_salary) {
		this.annual_salary = annual_salary;
	}


	public Float getSpl_pay() {
		return spl_pay;
	}


	public void setSpl_pay(Float spl_pay) {
		this.spl_pay = spl_pay;
	}


	public String getKey_skills() {
		return key_skills;
	}


	public void setKey_skills(String key_skills) {
		this.key_skills = key_skills;
	}


	public String getBank_id() {
		return bank_id;
	}


	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}


	public String getBank_account_no() {
		return bank_account_no;
	}


	public void setBank_account_no(String bank_account_no) {
		this.bank_account_no = bank_account_no;
	}


	public String getBank_branch() {
		return bank_branch;
	}


	public void setBank_branch(String bank_branch) {
		this.bank_branch = bank_branch;
	}


	public String getBank_ifsccode() {
		return bank_ifsccode;
	}


	public void setBank_ifsccode(String bank_ifsccode) {
		this.bank_ifsccode = bank_ifsccode;
	}


	public Integer getLeave_approver1_emp_id() {
		return leave_approver1_emp_id;
	}


	public void setLeave_approver1_emp_id(Integer leave_approver1_emp_id) {
		this.leave_approver1_emp_id = leave_approver1_emp_id;
	}


	public Integer getLeave_approver2_emp_id() {
		return leave_approver2_emp_id;
	}


	public void setLeave_approver2_emp_id(Integer leave_approver2_emp_id) {
		this.leave_approver2_emp_id = leave_approver2_emp_id;
	}


	public String getAttach() {
		return attach;
	}


	public void setAttach(String attach) {
		this.attach = attach;
	}


	public String getDlno() {
		return dlno;
	}


	public void setDlno(String dlno) {
		this.dlno = dlno;
	}


	public String getDlexpno() {
		return dlexpno;
	}


	public void setDlexpno(String dlexpno) {
		this.dlexpno = dlexpno;
	}


	public String getPassportno() {
		return passportno;
	}


	public void setPassportno(String passportno) {
		this.passportno = passportno;
	}


	public String getPassportexpno() {
		return passportexpno;
	}


	public void setPassportexpno(String passportexpno) {
		this.passportexpno = passportexpno;
	}


	public String getBankacc2() {
		return bankacc2;
	}


	public void setBankacc2(String bankacc2) {
		this.bankacc2 = bankacc2;
	}


	public String getTo_date() {
		return to_date;
	}


	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}


	public Integer getProb() {
		return prob;
	}


	public void setProb(Integer prob) {
		this.prob = prob;
	}


	public Boolean getNda() {
		return nda;
	}


	public void setNda(Boolean nda) {
		this.nda = nda;
	}


	public Boolean getNca() {
		return nca;
	}


	public void setNca(Boolean nca) {
		this.nca = nca;
	}


	public String getUan_no() {
		return uan_no;
	}


	public void setUan_no(String uan_no) {
		this.uan_no = uan_no;
	}


	public String getPunched_card_status() {
		return punched_card_status;
	}


	public void setPunched_card_status(String punched_card_status) {
		this.punched_card_status = punched_card_status;
	}


	public String getPhoto() {
		return photo;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}


	public Integer getShift_category_id() {
		return shift_category_id;
	}


	public void setShift_category_id(Integer shift_category_id) {
		this.shift_category_id = shift_category_id;
	}


	public Character getGender() {
		return gender;
	}


	public void setGender(Character gender) {
		this.gender = gender;
	}


	public Boolean getMaternity_status() {
		return maternity_status;
	}


	public void setMaternity_status(Boolean maternity_status) {
		this.maternity_status = maternity_status;
	}


	public Boolean getMarriage_status() {
		return marriage_status;
	}


	public void setMarriage_status(Boolean marriage_status) {
		this.marriage_status = marriage_status;
	}


	public Boolean getPaternity_status() {
		return paternity_status;
	}


	public void setPaternity_status(Boolean paternity_status) {
		this.paternity_status = paternity_status;
	}


	public Boolean getTransport_status() {
		return transport_status;
	}


	public void setTransport_status(Boolean transport_status) {
		this.transport_status = transport_status;
	}


	public Boolean getSalary_approve_status() {
		return salary_approve_status;
	}


	public void setSalary_approve_status(Boolean salary_approve_status) {
		this.salary_approve_status = salary_approve_status;
	}


	public Integer getVehicle_route_id() {
		return vehicle_route_id;
	}


	public void setVehicle_route_id(Integer vehicle_route_id) {
		this.vehicle_route_id = vehicle_route_id;
	}


	public String getTransport_assign_month() {
		return transport_assign_month;
	}


	public void setTransport_assign_month(String transport_assign_month) {
		this.transport_assign_month = transport_assign_month;
	}


	public String getTransport_assign_date() {
		return transport_assign_date;
	}


	public void setTransport_assign_date(String transport_assign_date) {
		this.transport_assign_date = transport_assign_date;
	}


	public String getTransport_deassign_date() {
		return transport_deassign_date;
	}


	public void setTransport_deassign_date(String transport_deassign_date) {
		this.transport_deassign_date = transport_deassign_date;
	}


	public String getTransport_deassign_month() {
		return transport_deassign_month;
	}


	public void setTransport_deassign_month(String transport_deassign_month) {
		this.transport_deassign_month = transport_deassign_month;
	}


	public Integer getNew_join_status() {
		return new_join_status;
	}


	public void setNew_join_status(Integer new_join_status) {
		this.new_join_status = new_join_status;
	}


	public Boolean getPf_status() {
		return pf_status;
	}


	public void setPf_status(Boolean pf_status) {
		this.pf_status = pf_status;
	}


	public Boolean getPt_status() {
		return pt_status;
	}


	public void setPt_status(Boolean pt_status) {
		this.pt_status = pt_status;
	}


	public String getAadhar() {
		return aadhar;
	}


	public void setAadhar(String aadhar) {
		this.aadhar = aadhar;
	}


	public String getPhoto_upload_status() {
		return photo_upload_status;
	}


	public void setPhoto_upload_status(String photo_upload_status) {
		this.photo_upload_status = photo_upload_status;
	}


	public Integer getProctor_assign_status() {
		return proctor_assign_status;
	}


	public void setProctor_assign_status(Integer proctor_assign_status) {
		this.proctor_assign_status = proctor_assign_status;
	}


	public String getBank_account_holder_name() {
		return bank_account_holder_name;
	}


	public void setBank_account_holder_name(String bank_account_holder_name) {
		this.bank_account_holder_name = bank_account_holder_name;
	}


	public Float getGrosspay_ctc() {
		return grosspay_ctc;
	}


	public void setGrosspay_ctc(Float grosspay_ctc) {
		this.grosspay_ctc = grosspay_ctc;
	}


	public String getNda_nca_attach() {
		return nda_nca_attach;
	}


	public void setNda_nca_attach(String nda_nca_attach) {
		this.nda_nca_attach = nda_nca_attach;
	}


	public Integer getStore_indent_approver1() {
		return store_indent_approver1;
	}


	public void setStore_indent_approver1(Integer store_indent_approver1) {
		this.store_indent_approver1 = store_indent_approver1;
	}


	public Integer getStore_indent_approver2() {
		return store_indent_approver2;
	}


	public void setStore_indent_approver2(Integer store_indent_approver2) {
		this.store_indent_approver2 = store_indent_approver2;
	}


	public String getEsi_no() {
		return esi_no;
	}


	public void setEsi_no(String esi_no) {
		this.esi_no = esi_no;
	}


	public Float getHra() {
		return hra;
	}


	public void setHra(Float hra) {
		this.hra = hra;
	}


	public Float getDa() {
		return da;
	}


	public void setDa(Float da) {
		this.da = da;
	}


	public Float getCca() {
		return cca;
	}


	public void setCca(Float cca) {
		this.cca = cca;
	}


	public Float getTa() {
		return ta;
	}


	public void setTa(Float ta) {
		this.ta = ta;
	}

	public Float getNet_pay() {
		return net_pay;
	}


	public void setNet_pay(Float net_pay) {
		this.net_pay = net_pay;
	}


	public Float getOther_allow() {
		return other_allow;
	}


	public void setOther_allow(Float other_allow) {
		this.other_allow = other_allow;
	}


	public Float getSpl_1() {
		return spl_1;
	}


	public void setSpl_1(Float spl_1) {
		this.spl_1 = spl_1;
	}


	public Integer getFte_status() {
		return fte_status;
	}


	public void setFte_status(Integer fte_status) {
		this.fte_status = fte_status;
	}

	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getCaste_category() {
		return caste_category;
	}


	public void setCaste_category(String caste_category) {
		this.caste_category = caste_category;
	}


	public String getContract_emp_type() {
		return contract_emp_type;
	}


	public void setContract_emp_type(String contract_emp_type) {
		this.contract_emp_type = contract_emp_type;
	}


	public String getSchool() {
		return school;
	}


	public void setSchool(String school) {
		this.school = school;
	}


	public String getContract_empcode() {
		return contract_empcode;
	}


	public void setContract_empcode(String contract_empcode) {
		this.contract_empcode = contract_empcode;
	}


	public Float getConsolidated_amount() {
		return consolidated_amount;
	}


	public void setConsolidated_amount(Float consolidated_amount) {
		this.consolidated_amount = consolidated_amount;
	}


	public String getFrom_date() {
		return from_date;
	}


	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}


	public String getDate_of_joining() {
		return date_of_joining;
	}


	public void setDate_of_joining(String date_of_joining) {
		this.date_of_joining = date_of_joining;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public String getSubject_skills() {
		return subject_skills;
	}


	public void setSubject_skills(String subject_skills) {
		this.subject_skills = subject_skills;
	}


	public Boolean getEmployee_status() {
		return employee_status;
	}


	public void setEmployee_status(Boolean employee_status) {
		this.employee_status = employee_status;
	}


	public Integer getProctor_type() {
		return proctor_type;
	}


	public void setProctor_type(Integer proctor_type) {
		this.proctor_type = proctor_type;
	}


	public Integer getChief_proctor_id() {
		return chief_proctor_id;
	}


	public void setChief_proctor_id(Integer chief_proctor_id) {
		this.chief_proctor_id = chief_proctor_id;
	}


	public String getReligion() {
		return religion;
	}


	public void setReligion(String religion) {
		this.religion = religion;
	}


	public String getPreferred_name_for_email() {
		return preferred_name_for_email;
	}


	public void setPreferred_name_for_email(String preferred_name_for_email) {
		this.preferred_name_for_email = preferred_name_for_email;
	}


	public String getEmp_attachment_path() {
		return emp_attachment_path;
	}


	public void setEmp_attachment_path(String emp_attachment_path) {
		this.emp_attachment_path = emp_attachment_path;
	}


	public String getEmp_attachment_file_name() {
		return emp_attachment_file_name;
	}


	public void setEmp_attachment_file_name(String emp_attachment_file_name) {
		this.emp_attachment_file_name = emp_attachment_file_name;
	}


	public String getEmp_attachement_type() {
		return emp_attachement_type;
	}


	public void setEmp_attachement_type(String emp_attachement_type) {
		this.emp_attachement_type = emp_attachement_type;
	}


	public String getEmp_image_attachment_path() {
		return emp_image_attachment_path;
	}


	public void setEmp_image_attachment_path(String emp_image_attachment_path) {
		this.emp_image_attachment_path = emp_image_attachment_path;
	}


	public String getPhd_status() {
		return phd_status;
	}


	public void setPhd_status(String phd_status) {
		this.phd_status = phd_status;
	}


	public String getSalary_structure_email_content() {
		return salary_structure_email_content;
	}


	public void setSalary_structure_email_content(String salary_structure_email_content) {
		this.salary_structure_email_content = salary_structure_email_content;
	}


	public String getCancel_remark() {
		return cancel_remark;
	}


	public void setCancel_remark(String cancel_remark) {
		this.cancel_remark = cancel_remark;
	}

	public String getMfo() {
		return mfo;
	}


	public void setMfo(String mfo) {
		this.mfo = mfo;
	}


	public String getPinfl() {
		return pinfl;
	}


	public void setPinfl(String pinfl) {
		this.pinfl = pinfl;
	}


	public String getHeight() {
		return height;
	}


	public void setHeight(String height) {
		this.height = height;
	}


	public String getPersonal_medical_history() {
		return personal_medical_history;
	}


	public void setPersonal_medical_history(String personal_medical_history) {
		this.personal_medical_history = personal_medical_history;
	}


	public String getFamily_medical_history() {
		return family_medical_history;
	}


	public void setFamily_medical_history(String family_medical_history) {
		this.family_medical_history = family_medical_history;
	}


	public String getPermanent_file() {
		return permanent_file;
	}


	public String setPermanent_file(String permanent_file) {
		return this.permanent_file = permanent_file;
	}


	public String getPermanent_done_by() {
		return permanent_done_by;
	}


	public void setPermanent_done_by(String permanent_done_by) {
		this.permanent_done_by = permanent_done_by;
	}


	public Integer getPermanent_status() {
		return permanent_status;
	}


	public void setPermanent_status(Integer permanent_status) {
		this.permanent_status = permanent_status;
	}


	public String getDate_of_permanent() {
		return date_of_permanent;
	}


	public void setDate_of_permanent(String date_of_permanent) {
		this.date_of_permanent = date_of_permanent;
	}


	public String getEmp_attachment_path2() {
		return emp_attachment_path2;
	}


	public void setEmp_attachment_path2(String emp_attachment_path2) {
		this.emp_attachment_path2 = emp_attachment_path2;
	}


	public String getEmp_attachment_file_name2() {
		return emp_attachment_file_name2;
	}


	public void setEmp_attachment_file_name2(String emp_attachment_file_name2) {
		this.emp_attachment_file_name2 = emp_attachment_file_name2;
	}


	public String getEmp_attachement_type2() {
		return emp_attachement_type2;
	}


	public void setEmp_attachement_type2(String emp_attachement_type2) {
		this.emp_attachement_type2 = emp_attachement_type2;
	}


	public String getPermanent_remarks() {
		return permanent_remarks;
	}


	public void setPermanent_remarks(String permanent_remarks) {
		this.permanent_remarks = permanent_remarks;
	}
	
	public String getJob_short_name() {
		return job_short_name;
	}


	public void setJob_short_name(String job_short_name) {
		this.job_short_name = job_short_name;
	}


	public String getDept_name_short() {
		return dept_name_short;
	}


	public void setDept_name_short(String dept_name_short) {
		this.dept_name_short = dept_name_short;
	}


	public String getSchool_name_short() {
		return school_name_short;
	}


	public void setSchool_name_short(String school_name_short) {
		this.school_name_short = school_name_short;
	}


	public String getShift_name() {
		return shift_name;
	}


	public void setShift_name(String shift_name) {
		this.shift_name = shift_name;
	}


	public Integer getLanguage_id() {
		return language_id;
	}


	public void setLanguage_id(Integer language_id) {
		this.language_id = language_id;
	}


	public String getPersonal_email() {
		return personal_email;
	}


	public void setPersonal_email(String personal_email) {
		this.personal_email = personal_email;
	}


	public Float getCea() {
		return cea;
	}


	public void setCea(Float cea) {
		this.cea = cea;
	}


	public Float getCha() {
		return cha;
	}


	public void setCha(Float cha) {
		this.cha = cha;
	}


	public Float getFr() {
		return fr;
	}


	public void setFr(Float fr) {
		this.fr = fr;
	}


	public Float getMe() {
		return me;
	}


	public void setMe(Float me) {
		this.me = me;
	}


	public Float getMr() {
		return mr;
	}


	public void setMr(Float mr) {
		this.mr = mr;
	}
	
	public String getPlastic_card() {
		return plastic_card;
	}


	public void setPlastic_card(String plastic_card) {
		this.plastic_card = plastic_card;
	}

	
	
}
