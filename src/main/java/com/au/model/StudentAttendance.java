package com.au.model;

import java.util.Date;
import java.util.Objects;

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

@Entity
@Table(name = "student_attendance")
@Data
public class StudentAttendance {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer student_attendance_id;
	private Integer school_id;
	private Integer ac_year_id;
	private Integer course_id;
	private String remarks;
	private String description;
	private Integer student_id;
	private Boolean present_status;
	private Boolean offline_status;
	private Integer batch_id;
	private Integer section_id;
	private Date date_of_class;
	private Integer time_slots_id;
	private Integer lesson_id;
	private Integer time_table_id;
	private Integer year_or_sem;
	private Boolean active;

	@Column(updatable = false)
	private String created_username;
	private String modified_username;

	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	
	private Integer rating;
	private Integer lesson_assignment_id;
	private Integer course_assignment_id;
	private Integer emp_id;
	
	private Boolean isEligibleForFeedback;
	private Integer syllabus_id;
	/*1 - due exist
	* 0 - No due*/
	private Integer attendace_on_fee_due;
	
	public StudentAttendance() {
		super();

	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		StudentAttendance sta = (StudentAttendance) o;
		return Objects.equals(student_id, sta.student_id) &&
				       Objects.equals(time_table_id, sta.time_table_id) ;
	}

	@Override
	public int hashCode() {
		return Objects.hash(student_id, time_table_id);
	}
	
	

}
