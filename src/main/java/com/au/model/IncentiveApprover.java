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

import lombok.Data;

@Data
@Entity
@Table(name="incentive_approver")
public class IncentiveApprover {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer incentive_approver_id;
	
	private Integer emp_id;
	
	private Integer hod_id;
	private Integer hoi_id;
	private Integer asst_dir_id;
	private Integer qa_id;
	private Integer hr_id;
	private Integer finance_id;
	private Integer ipr_id;
	private String ipr_name; 
	
	private Integer publications_id;
	private Integer conferences_id;
	private Integer book_chapter_id;
	private Integer membership_id;
	private Integer grant_id;
	private Integer patent_id;
	
	private String remark;
	private String date;
	private String amount; 
	

	private String hod_remark;

	private String hoi_remark;

	private String asst_dir_remark;

	private String qa_remark;

	private String hr_remark;

	private String finance_remark;
	
	private String ipr_remark;
	
	
	private String hod_date;
	private String hoi_date;
	private String asst_dir_date;
	private String qa_date;
	private String hr_date;
	private String finance_date;
	private String ipr_date;
	
	private Boolean status;
	private Boolean hod_status;
	private Boolean hoi_status;
	private Boolean asst_dir_status;
	private Boolean qa_status;
	private Boolean hr_status;
	private Boolean finance_status;
	private Boolean ipr_status;

	private Boolean approver_status;
	private String approved_status;
	
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
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Integer credited_month;
	private Integer credited_year;
	private String type;
	private String ip_address;
	private String hod_ip_address;
	private String hoi_ip_address;
	private String asst_ip_address;
	private String qa_ip_address;
	private String hr_ip_address;
	private String finance_ip_address;
	private String ipr_ip_address;
}
