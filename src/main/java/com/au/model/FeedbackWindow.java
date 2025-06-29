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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="feedback_window")
public class FeedbackWindow {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long feedbackWindowId;
	
	private String academicYear;
	private String institute;
	private Integer course;
	private String courseAndBranch;
	private String branch;
	private Integer year;
	private Integer sem;
	private String semester;
	
	private Date fromDate;
	private Date toDate;
	
	private Boolean active;
	
	private Integer instituteId;
	private Integer program_specialization_id;
	
	@Column(name="created_by",updatable = false)
	private Integer createdBy;
	@Column(name="modified_by")
	private Integer modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	
	
	public FeedbackWindow(Long feedbackWindowId, String academicYear, String institute, String courseAndBranch,
			String semester, Date fromDate, Date toDate,  Integer instituteId, 
			Date created_date ,Boolean active,Integer program_specialization_id) {
	
		this.feedbackWindowId = feedbackWindowId;
		this.academicYear = academicYear;
		this.institute = institute;
		this.courseAndBranch = courseAndBranch;
		this.semester = semester;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.instituteId = instituteId;
		this.createdBy = createdBy;
		this.created_date = created_date;
		this.active = active;
		this.program_specialization_id = program_specialization_id;
	}
	
	
	

}
