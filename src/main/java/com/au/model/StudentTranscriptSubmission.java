package com.au.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "student_transcript_submission")
public class StudentTranscriptSubmission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer stu_transcript_id;
	private Integer transcript_id;
	private Integer student_id;
	private String is_collected;

	private String will_submit_by;
	private String not_applicable;

	private String collected_by_institute;

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

	private String created_username;
	private String modified_username;
	private String transcript_locker_number;
	
	private String submitted_date;
	private Integer collected_by;


}
