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
@Table(name = "leave_apply")
public class LeaveApply {

//	1 = pending
//	2 = approved
//	3 = cancelled
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer leave_apply_id;
	private Integer employee_leave_id;
	private Integer emp_id;
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
	private String leave_apply_attachment_path2;
	
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

	public LeaveApply() {
		super();
	}

	public Integer getLeave_apply_id() {
		return leave_apply_id;
	}

	public void setLeave_apply_id(Integer leave_apply_id) {
		this.leave_apply_id = leave_apply_id;
	}

	public Integer getEmployee_leave_id() {
		return employee_leave_id;
	}

	public void setEmployee_leave_id(Integer employee_leave_id) {
		this.employee_leave_id = employee_leave_id;
	}

	public Integer getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(Integer emp_id) {
		this.emp_id = emp_id;
	}

	public Float getNo_of_days_applied() {
		return no_of_days_applied;
	}

	public void setNo_of_days_applied(Float no_of_days_applied) {
		this.no_of_days_applied = no_of_days_applied;
	}

	public Integer getTotal_leaves_applicable() {
		return total_leaves_applicable;
	}

	public void setTotal_leaves_applicable(Integer total_leaves_applicable) {
		this.total_leaves_applicable = total_leaves_applicable;
	}

	public Float getRemaining_days_left() {
		return remaining_days_left;
	}

	public void setRemaining_days_left(Float remaining_days_left) {
		this.remaining_days_left = remaining_days_left;
	}

	public String getLeave_comments() {
		return leave_comments;
	}

	public void setLeave_comments(String leave_comments) {
		this.leave_comments = leave_comments;
	}

	public String getReporting_approver_comment() {
		return reporting_approver_comment;
	}

	public void setReporting_approver_comment(String reporting_approver_comment) {
		this.reporting_approver_comment = reporting_approver_comment;
	}

	public String getReporting_approver1_comment() {
		return reporting_approver1_comment;
	}

	public void setReporting_approver1_comment(String reporting_approver1_comment) {
		this.reporting_approver1_comment = reporting_approver1_comment;
	}

	public Boolean getLeave_app1_status() {
		return leave_app1_status;
	}

	public void setLeave_app1_status(Boolean leave_app1_status) {
		this.leave_app1_status = leave_app1_status;
	}

	public Boolean getLeave_app2_status() {
		return leave_app2_status;
	}

	public void setLeave_app2_status(Boolean leave_app2_status) {
		this.leave_app2_status = leave_app2_status;
	}

	public String getAlternative_lecturer_name() {
		return alternative_lecturer_name;
	}

	public void setAlternative_lecturer_name(String alternative_lecturer_name) {
		this.alternative_lecturer_name = alternative_lecturer_name;
	}

	public String getContact_no() {
		return contact_no;
	}

	public void setContact_no(String contact_no) {
		this.contact_no = contact_no;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public Integer getSpecial_leave_id() {
		return special_leave_id;
	}

	public void setSpecial_leave_id(Integer special_leave_id) {
		this.special_leave_id = special_leave_id;
	}

	public Integer getCancel_by() {
		return cancel_by;
	}

	public void setCancel_by(Integer cancel_by) {
		this.cancel_by = cancel_by;
	}

	public Date getCancel_date() {
		return cancel_date;
	}

	public void setCancel_date(Date cancel_date) {
		this.cancel_date = cancel_date;
	}

	public Date getLeave_approved_date() {
		return leave_approved_date;
	}

	public void setLeave_approved_date(Date leave_approved_date) {
		this.leave_approved_date = leave_approved_date;
	}

	public String getCompoff_worked_date() {
		return compoff_worked_date;
	}

	public void setCompoff_worked_date(String compoff_worked_date) {
		this.compoff_worked_date = compoff_worked_date;
	}

	public Date getLeave_approved2_date() {
		return leave_approved2_date;
	}

	public void setLeave_approved2_date(Date leave_approved2_date) {
		this.leave_approved2_date = leave_approved2_date;
	}

	public Integer getLeave_approved_by1() {
		return leave_approved_by1;
	}

	public void setLeave_approved_by1(Integer leave_approved_by1) {
		this.leave_approved_by1 = leave_approved_by1;
	}

	public String getLeave_approved_by2() {
		return leave_approved_by2;
	}

	public void setLeave_approved_by2(String leave_approved_by2) {
		this.leave_approved_by2 = leave_approved_by2;
	}

	public String getDept_name_short() {
		return dept_name_short;
	}

	public void setDept_name_short(String dept_name_short) {
		this.dept_name_short = dept_name_short;
	}

	public String getWeb_status() {
		return web_status;
	}

	public void setWeb_status(String web_status) {
		this.web_status = web_status;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	public String getLeave_apply_attachment_path() {
		return leave_apply_attachment_path;
	}

	public String setLeave_apply_attachment_path(String leave_apply_attachment_path) {
		return this.leave_apply_attachment_path = leave_apply_attachment_path;
	}

	public String getAlternative_emails() {
		return alternative_emails;
	}

	public void setAlternative_emails(String alternative_emails) {
		this.alternative_emails = alternative_emails;
	}

	public String getCancel_comments() {
		return cancel_comments;
	}

	public void setCancel_comments(String cancel_comments) {
		this.cancel_comments = cancel_comments;
	}

	public Integer getLeave_id() {
		return leave_id;
	}

	public void setLeave_id(Integer leave_id) {
		this.leave_id = leave_id;
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

	public Integer getApproved_status() {
		return approved_status;
	}

	public void setApproved_status(Integer approved_status) {
		this.approved_status = approved_status;
	}

	public String getLeave_apply_attachment_path2() {
		return leave_apply_attachment_path2;
	}

	public String setLeave_apply_attachment_path2(String leave_apply_attachment_path2) {
		return this.leave_apply_attachment_path2 = leave_apply_attachment_path2;
	}

	
}