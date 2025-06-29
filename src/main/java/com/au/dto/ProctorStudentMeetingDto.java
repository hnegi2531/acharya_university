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
public class ProctorStudentMeetingDto {
	

	private Integer proctor_student_meeting_id;
	private Integer meeting_id;
	private Integer chief_proctor_id;
	private Integer proctor_id;
	private Integer emp_id;
	private List<Integer> student_ids;
	private String remarks;
	private String date_of_meeting;
	private String meeting_agenda;
	private String feedback;
	private String meeting_type;
	private Integer faq_id;
	private String feedback_date;
	private String mode_of_contact;
	private String parent_name;
	private Integer school_id;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
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
	private Integer user_id;

	private String modeof_connect;
	private String student_parent;
}
