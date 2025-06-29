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

@Entity
@Table(name = "internal_time_table")
@Data
public class InternalTimeTable {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer internal_time_table_id;
	private Integer course_id;
	private String exam_time;
	private Integer time_slots_id;
	@Temporal(TemporalType.DATE)
	private Date date_of_exam;
	private Integer internal_id;
	private String week_day;
	
	private Integer min_marks;
	private Integer max_marks;
	
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
		private Integer course_assignment_id;
		
		
		public InternalTimeTable() {
			super();
		}


		public Integer getInternal_time_table_id() {
			return internal_time_table_id;
		}


		public void setInternal_time_table_id(Integer internal_time_table_id) {
			this.internal_time_table_id = internal_time_table_id;
		}


		public Integer getCourse_id() {
			return course_id;
		}


		public void setCourse_id(Integer course_id) {
			this.course_id = course_id;
		}


		public String getExam_time() {
			return exam_time;
		}


		public void setExam_time(String exam_time) {
			this.exam_time = exam_time;
		}



		public Date getDate_of_exam() {
			return date_of_exam;
		}


		public void setDate_of_exam(Date date_of_exam) {
			this.date_of_exam = date_of_exam;
		}


		public Integer getInternal_id() {
			return internal_id;
		}


		public void setInternal_id(Integer internal_id) {
			this.internal_id = internal_id;
		}


		public String getWeek_day() {
			return week_day;
		}


		public void setWeek_day(String week_day) {
			this.week_day = week_day;
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


		public Integer getTime_slots_id() {
			return time_slots_id;
		}


		public void setTime_slots_id(Integer time_slots_id) {
			this.time_slots_id = time_slots_id;
		}


		public Integer getMin_marks() {
			return min_marks;
		}


		public void setMin_marks(Integer min_marks) {
			this.min_marks = min_marks;
		}


		public Integer getMax_marks() {
			return max_marks;
		}


		public void setMax_marks(Integer max_marks) {
			this.max_marks = max_marks;
		}

		
}
