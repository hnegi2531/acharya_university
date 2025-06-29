package com.au.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class StudentTranscriptSubmissionRequest {
	private List<Integer> transcript_id;

	private Integer student_id;
	private  String is_collected;
	
	private HashMap<Integer, String> submitted_date;
	
	private  List<Integer> not_applicable;
	
	private String collected_by_institute;

	@Column(name = "created_date",updatable = false)
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)	
	@UpdateTimestamp
	private Date modified_date;
	
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	private String transcript_locker_number;
	private Integer candidate_id;
	
		
}
