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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "offer")
public class Offer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer offer_id;
	private Integer report_id;
	private String offer_name;
	private String comments;
	private Date interview_date;
	private Integer annual_salary;
	private String salary_structure;
	private Integer salary_structure_id;
	private Integer dept_id;
	private Integer job_type_id;
	private Integer job_id;
	private String employee_type;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	private Boolean active;
	private Boolean mail;
	@Email
	private String email;
	private Float ctc;
	private Float net_pay;
	private String offercode;
	private Boolean offerstatus;
	private Integer ctc_status;
	private Integer school_id;
	private Integer program_specialization_id;
	private Integer designation_id;
	private String designation;
	private String from_date;
	private String to_date;
	private Float basic;
	private Float hra;
	private Float cca;
	private Float ta;
	private Float mr;
	private Float fr;
	private Float me;
	private Float other_allow;
	private Float spl_1;
	private Float pf;
	private Float pt;
	private Float esi;
	private Float pfc;
	private Float da;
	private String job_type;
	private Integer ref_no;
	private Float ma;
	private Float gross;
	private Float esic;
	private Float management_pf;
	private String employeement_type;
	private Integer consolidated_amount;
	private String date_of_joining;
	private String consultant_emp_type;
	private Date end_date;
	private String remarks;
	private String description;
	@Column(updatable = false)
	private String createdUsername;
	private String modifiedUsername;
	private Integer emp_type_id;
	private Float epf;
	private String ip_address;
    private  Boolean isPf=Boolean.FALSE;
    private Boolean isPt=Boolean.FALSE;
    
    
	public Offer() {
		super();
	}

	public Integer getOffer_id() {
		return offer_id;
	}

	public void setOffer_id(Integer offer_id) {
		this.offer_id = offer_id;
	}

	public Integer getReport_id() {
		return report_id;
	}

	public void setReport_id(Integer report_id) {
		this.report_id = report_id;
	}

	public String getOffer_name() {
		return offer_name;
	}

	public void setOffer_name(String offer_name) {
		this.offer_name = offer_name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getInterview_date() {
		return interview_date;
	}

	public void setInterview_date(Date interview_date) {
		this.interview_date = interview_date;
	}

	public Integer getAnnual_salary() {
		return annual_salary;
	}

	public void setAnnual_salary(Integer annual_salary) {
		this.annual_salary = annual_salary;
	}

	public String getSalary_structure() {
		return salary_structure;
	}

	public void setSalary_structure(String salary_structure) {
		this.salary_structure = salary_structure;
	}

	public Integer getSalary_structure_id() {
		return salary_structure_id;
	}

	public void setSalary_structure_id(Integer salary_structure_id) {
		this.salary_structure_id = salary_structure_id;
	}

	public Integer getDept_id() {
		return dept_id;
	}

	public void setDept_id(Integer dept_id) {
		this.dept_id = dept_id;
	}

	public Integer getJob_type_id() {
		return job_type_id;
	}

	public void setJob_type_id(Integer job_type_id) {
		this.job_type_id = job_type_id;
	}

	public Integer getJob_id() {
		return job_id;
	}

	public void setJob_id(Integer job_id) {
		this.job_id = job_id;
	}

	public String getEmployee_type() {
		return employee_type;
	}

	public void setEmployee_type(String employee_type) {
		this.employee_type = employee_type;
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

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
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

	public Boolean getMail() {
		return mail;
	}

	public void setMail(Boolean mail) {
		this.mail = mail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Float getCtc() {
		return ctc;
	}

	public void setCtc(Float ctc) {
		this.ctc = ctc;
	}

	public Float getNet_pay() {
		return net_pay;
	}

	public void setNet_pay(Float net_pay) {
		this.net_pay = net_pay;
	}

	public String getOffercode() {
		return offercode;
	}

	public void setOffercode(String offercode) {
		this.offercode = offercode;
	}

	public Boolean getOfferstatus() {
		return offerstatus;
	}

	public void setOfferstatus(Boolean offerstatus) {
		this.offerstatus = offerstatus;
	}

	public Integer getCtc_status() {
		return ctc_status;
	}

	public void setCtc_status(Integer ctc_status) {
		this.ctc_status = ctc_status;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public Integer getProgram_specialization_id() {
		return program_specialization_id;
	}

	public void setProgram_specialization_id(Integer program_specialization_id) {
		this.program_specialization_id = program_specialization_id;
	}

	public Integer getDesignation_id() {
		return designation_id;
	}

	public void setDesignation_id(Integer designation_id) {
		this.designation_id = designation_id;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	

	public String getFrom_date() {
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public String getTo_date() {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

	public Float getBasic() {
		return basic;
	}

	public void setBasic(Float basic) {
		this.basic = basic;
	}

	public Float getHra() {
		return hra;
	}

	public void setHra(Float hra) {
		this.hra = hra;
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

	public Float getMr() {
		return mr;
	}

	public void setMr(Float mr) {
		this.mr = mr;
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

	public Float getPf() {
		return pf;
	}

	public void setPf(Float pf) {
		this.pf = pf;
	}

	public Float getPt() {
		return pt;
	}

	public void setPt(Float pt) {
		this.pt = pt;
	}

	public Float getEsi() {
		return esi;
	}

	public void setEsi(Float esi) {
		this.esi = esi;
	}

	public Float getPfc() {
		return pfc;
	}

	public void setPfc(Float pfc) {
		this.pfc = pfc;
	}

	public Float getDa() {
		return da;
	}

	public void setDa(Float da) {
		this.da = da;
	}

	public String getJob_type() {
		return job_type;
	}

	public void setJob_type(String job_type) {
		this.job_type = job_type;
	}

	public Integer getRef_no() {
		return ref_no;
	}

	public void setRef_no(Integer ref_no) {
		this.ref_no = ref_no;
	}

	public Float getMa() {
		return ma;
	}

	public void setMa(Float ma) {
		this.ma = ma;
	}

	public Float getGross() {
		return gross;
	}

	public void setGross(Float gross) {
		this.gross = gross;
	}

	public Float getEsic() {
		return esic;
	}

	public void setEsic(Float esic) {
		this.esic = esic;
	}

	public Float getManagement_pf() {
		return management_pf;
	}

	public void setManagement_pf(Float management_pf) {
		this.management_pf = management_pf;
	}

	public String getEmployeement_type() {
		return employeement_type;
	}

	public void setEmployeement_type(String employeement_type) {
		this.employeement_type = employeement_type;
	}

	public Integer getConsolidated_amount() {
		return consolidated_amount;
	}

	public void setConsolidated_amount(Integer consolidated_amount) {
		this.consolidated_amount = consolidated_amount;
	}


	public String getDate_of_joining() {
		return date_of_joining;
	}

	public void setDate_of_joining(String date_of_joining) {
		this.date_of_joining = date_of_joining;
	}

	public String getConsultant_emp_type() {
		return consultant_emp_type;
	}

	public void setConsultant_emp_type(String consultant_emp_type) {
		this.consultant_emp_type = consultant_emp_type;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedUsername() {
		return createdUsername;
	}

	public void setCreatedUsername(String createdUsername) {
		this.createdUsername = createdUsername;
	}

	public String getModifiedUsername() {
		return modifiedUsername;
	}

	public void setModifiedUsername(String modifiedUsername) {
		this.modifiedUsername = modifiedUsername;
	}

	public Integer getEmp_type_id() {
		return emp_type_id;
	}

	public void setEmp_type_id(Integer emp_type_id) {
		this.emp_type_id = emp_type_id;
	}

	public Float getEpf() {
		return epf;
	}

	public void setEpf(Float epf) {
		this.epf = epf;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public Boolean getIsPf() {
		return isPf;
	}

	public void setIsPf(Boolean isPf) {
		this.isPf = isPf;
	}

	public Boolean getIsPt() {
		return isPt;
	}

	public void setIsPt(Boolean isPt) {
		this.isPt = isPt;
	}
	
	

}
