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
@Table(name = "course_branch")
public class CourseBranch {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
			private Integer course_branch_id;	
			private String course_branch_name;
			private String course_branch_short_name;	
			
			@Column(updatable = false)
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
			private boolean active;
			
			public CourseBranch() {
				super();
			}

			public CourseBranch(Integer course_branch_id, String course_branch_name, String course_branch_short_name,
					Date created_date, Date modified_date, Integer created_by, Integer modified_by, boolean active) {
				super();
				this.course_branch_id = course_branch_id;
				this.course_branch_name = course_branch_name;
				this.course_branch_short_name = course_branch_short_name;
				this.created_date = created_date;
				this.modified_date = modified_date;
				this.created_by = created_by;
				this.modified_by = modified_by;
				this.active = active;
			}

			public Integer getCourse_branch_id() {
				return course_branch_id;
			}

			public void setCourse_branch_id(Integer course_branch_id) {
				this.course_branch_id = course_branch_id;
			}

			public String getCourse_branch_name() {
				return course_branch_name;
			}

			public void setCourse_branch_name(String course_branch_name) {
				this.course_branch_name = course_branch_name;
			}

			public String getCourse_branch_short_name() {
				return course_branch_short_name;
			}

			public void setCourse_branch_short_name(String course_branch_short_name) {
				this.course_branch_short_name = course_branch_short_name;
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

			public boolean isActive() {
				return active;
			}

			public void setActive(boolean active) {
				this.active = active;
			}

			
}
