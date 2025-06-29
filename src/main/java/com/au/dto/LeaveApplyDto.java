package com.au.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApplyDto {

	
	private Integer leave_apply_id;
	private Integer employee_leave_id;
	private Float no_of_days_applied;
	private Integer total_leaves_applicable;
	private Float remaining_days_left;
	private String leave_comments;
	private String reporting_approver_comment;
	private String reporting_approver1_comment;
	private Boolean leave_app1_status;
	private Boolean leave_app2_status;
	private String alternative_lecturer_name;
	private String contact_no;
	private String year;
	private String shift;
	private Integer special_leave_id;
	private Integer cancel_by;
	private Date cancel_date;
	private Date leave_approved_date;
	private String compoff_worked_date;
	private Date leave_approved2_date;
	private Integer leave_approved_by1;
	private String leave_approved_by2;
	private Integer leave_id;
	private String from_date;
	private String to_date;
	private String leave_apply_attachment_path;
	
	private List<Integer> emp_id;

	@Column(unique = true)
	private String dept_name_short;
	private String web_status;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	@Column(name = "created_by", updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	@Column(name = "created_username", updatable = false)
	private String created_username;
	private String modified_username;
	private String alternative_emails;
	private String cancel_comments;
	private Integer approved_status;
	
}
