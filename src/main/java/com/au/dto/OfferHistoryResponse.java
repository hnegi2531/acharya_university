package com.au.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
public class OfferHistoryResponse {
	
	private Integer offer_history_id;
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
	@Temporal(TemporalType.DATE)
	private Date from_date;
	@Temporal(TemporalType.DATE)
	private Date to_date;
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
	private String consolidated_amount;
	private Date date_of_joining;
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
	private Integer emp_id;

}
