package com.au.model;

import java.io.Serializable;
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
@Table(name = "applicant_details")
public class ApplicantDetails implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer applicant_id;
	private Integer board_university_id;
	
	private String qualifying_exam_year;
	private Integer std_id;
	private String board_university;
	private String college_name;
	private String subjects_studied;
	private Integer marks_total;
	private String course;
	private String total_obtained;
	private float percentage_scored;
	private String entrance_exam_name;
	private String state;
	private Integer year_of_entrance;
	private float entrance_score;
	private String first_language;
	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;

	private String second_language;
	private String auid;
	private String optional_subject;
	private String optional_max_mark;
	private String optional_min_mark;
	private String optional_percentage;
	
	private String entrance_exam_date;
    private String rank_obtained;
	
	@Column(updatable = false)
	private Integer created_by;
	
	private Integer modified_by;

	@Column(name = "created_date",updatable = false)
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)	
	@UpdateTimestamp
	private Date modified_date;
	
	@Temporal(TemporalType.DATE) 
	private Date qualifying_year;

	private String remarks;
	private Integer passed_year;
	private Integer candidate_id;
	private String pdf_content;
	
	
}