package com.au.dto;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
public class UserRoleRequestWithUserRole {
	private Integer id;
	private String username;
	private String password;
	private String usertype;
	@Email
	private String email;
	private String usercode;
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
	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;
	private String reset_password_token;
	private LocalDateTime reset_password_time;
	
	private Boolean is_lms_auditor=false;
	private Boolean guest_type;
	private String  book_chapter_approver_designation;
	
	private Integer user_role_id;
	private Integer role_id;
}
