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
@Table(name = "foreign_regional_registration_offices_details")
public class ForeignRegionalRegistrationOfficesDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer frrod_id;
	private Integer student_id;
	private String surname;
	private String passport_no;
	private String place_of_birth;
	private String passport_issue_place;
	private String passport_issue_date;
	private String passport_expiry_date;
	private String visa_no;
	private String visa_type;
	private String type_of_entry;
	private String place_of_visa_issue;
	private String visa_issue_date;
	private String visa_expiry_date;
	private String port_of_departure;
	private String port_of_arrival;
	private String fsis_no;
	private String immigration_date;
	private String rp_no;
	private String rp_issue_date;
	private String rp_expiry_date;
	private String issue_by;
	private Boolean reported_to_india;
	private String reported_on;
	private String passport_copy_document_path;
	private String passport_copy_document_type;
	private String visa_copy_document_path;
	private String visa_copy_document_type;
	private String residential_permit_copy_document_path;
	private String residential_permit_copy_document_type;
	private String aiu_equivalence_document_path;
	private String aiu_equivalence_document_type;
	private String remarks;
	private Integer joining_year_id;
	private String attachment_path;
	private String p_v_remarks;
	private Boolean p_v_active;
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
	private String created_username;
	
	private String modified_username;
	private Boolean active;
	
	public ForeignRegionalRegistrationOfficesDetails() {
		super();
	}

	public Integer getFrrod_id() {
		return frrod_id;
	}



	public void setFrrod_id(Integer frrod_id) {
		this.frrod_id = frrod_id;
	}



	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassport_no() {
		return passport_no;
	}

	public void setPassport_no(String passport_no) {
		this.passport_no = passport_no;
	}

	public String getPlace_of_birth() {
		return place_of_birth;
	}

	public void setPlace_of_birth(String place_of_birth) {
		this.place_of_birth = place_of_birth;
	}

	public String getPassport_issue_place() {
		return passport_issue_place;
	}

	public void setPassport_issue_place(String passport_issue_place) {
		this.passport_issue_place = passport_issue_place;
	}

	public String getVisa_no() {
		return visa_no;
	}

	public void setVisa_no(String visa_no) {
		this.visa_no = visa_no;
	}

	public String getVisa_type() {
		return visa_type;
	}

	public void setVisa_type(String visa_type) {
		this.visa_type = visa_type;
	}

	public String getType_of_entry() {
		return type_of_entry;
	}

	public void setType_of_entry(String type_of_entry) {
		this.type_of_entry = type_of_entry;
	}

	public String getPlace_of_visa_issue() {
		return place_of_visa_issue;
	}

	public void setPlace_of_visa_issue(String place_of_visa_issue) {
		this.place_of_visa_issue = place_of_visa_issue;
	}

	public String getPort_of_departure() {
		return port_of_departure;
	}

	public void setPort_of_departure(String port_of_departure) {
		this.port_of_departure = port_of_departure;
	}

	public String getPort_of_arrival() {
		return port_of_arrival;
	}

	public void setPort_of_arrival(String port_of_arrival) {
		this.port_of_arrival = port_of_arrival;
	}

	public String getFsis_no() {
		return fsis_no;
	}

	public void setFsis_no(String fsis_no) {
		this.fsis_no = fsis_no;
	}

	public String getRp_no() {
		return rp_no;
	}

	public void setRp_no(String rp_no) {
		this.rp_no = rp_no;
	}

	public String getIssue_by() {
		return issue_by;
	}

	public void setIssue_by(String issue_by) {
		this.issue_by = issue_by;
	}

	public Boolean getReported_to_india() {
		return reported_to_india;
	}

	public void setReported_to_india(Boolean reported_to_india) {
		this.reported_to_india = reported_to_india;
	}

	public String getPassport_copy_document_path() {
		return passport_copy_document_path;
	}

	public void setPassport_copy_document_path(String passport_copy_document_path) {
		this.passport_copy_document_path = passport_copy_document_path;
	}

	public String getPassport_copy_document_type() {
		return passport_copy_document_type;
	}

	public void setPassport_copy_document_type(String passport_copy_document_type) {
		this.passport_copy_document_type = passport_copy_document_type;
	}

	public String getVisa_copy_document_path() {
		return visa_copy_document_path;
	}

	public void setVisa_copy_document_path(String visa_copy_document_path) {
		this.visa_copy_document_path = visa_copy_document_path;
	}

	public String getVisa_copy_document_type() {
		return visa_copy_document_type;
	}

	public void setVisa_copy_document_type(String visa_copy_document_type) {
		this.visa_copy_document_type = visa_copy_document_type;
	}

	public String getResidential_permit_copy_document_path() {
		return residential_permit_copy_document_path;
	}

	public void setResidential_permit_copy_document_path(String residential_permit_copy_document_path) {
		this.residential_permit_copy_document_path = residential_permit_copy_document_path;
	}

	public String getResidential_permit_copy_document_type() {
		return residential_permit_copy_document_type;
	}

	public void setResidential_permit_copy_document_type(String residential_permit_copy_document_type) {
		this.residential_permit_copy_document_type = residential_permit_copy_document_type;
	}

	public String getAiu_equivalence_document_path() {
		return aiu_equivalence_document_path;
	}

	public void setAiu_equivalence_document_path(String aiu_equivalence_document_path) {
		this.aiu_equivalence_document_path = aiu_equivalence_document_path;
	}

	public String getAiu_equivalence_document_type() {
		return aiu_equivalence_document_type;
	}

	public void setAiu_equivalence_document_type(String aiu_equivalence_document_type) {
		this.aiu_equivalence_document_type = aiu_equivalence_document_type;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getJoining_year_id() {
		return joining_year_id;
	}

	public void setJoining_year_id(Integer joining_year_id) {
		this.joining_year_id = joining_year_id;
	}

	public String getAttachment_path() {
		return attachment_path;
	}

	public void setAttachment_path(String attachment_path) {
		this.attachment_path = attachment_path;
	}

	public String getP_v_remarks() {
		return p_v_remarks;
	}

	public void setP_v_remarks(String p_v_remarks) {
		this.p_v_remarks = p_v_remarks;
	}

	public Boolean getP_v_active() {
		return p_v_active;
	}

	public void setP_v_active(Boolean p_v_active) {
		this.p_v_active = p_v_active;
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

	public String getPassport_issue_date() {
		return passport_issue_date;
	}

	public void setPassport_issue_date(String passport_issue_date) {
		this.passport_issue_date = passport_issue_date;
	}

	public String getPassport_expiry_date() {
		return passport_expiry_date;
	}

	public void setPassport_expiry_date(String passport_expiry_date) {
		this.passport_expiry_date = passport_expiry_date;
	}

	public String getVisa_issue_date() {
		return visa_issue_date;
	}

	public void setVisa_issue_date(String visa_issue_date) {
		this.visa_issue_date = visa_issue_date;
	}

	public String getVisa_expiry_date() {
		return visa_expiry_date;
	}

	public void setVisa_expiry_date(String visa_expiry_date) {
		this.visa_expiry_date = visa_expiry_date;
	}

	public String getRp_issue_date() {
		return rp_issue_date;
	}

	public void setRp_issue_date(String rp_issue_date) {
		this.rp_issue_date = rp_issue_date;
	}

	public String getRp_expiry_date() {
		return rp_expiry_date;
	}

	public void setRp_expiry_date(String rp_expiry_date) {
		this.rp_expiry_date = rp_expiry_date;
	}

	public String getImmigration_date() {
		return immigration_date;
	}

	public void setImmigration_date(String immigration_date) {
		this.immigration_date = immigration_date;
	}

	public String getReported_on() {
		return reported_on;
	}

	public void setReported_on(String reported_on) {
		this.reported_on = reported_on;
	}
	
	
	
}
