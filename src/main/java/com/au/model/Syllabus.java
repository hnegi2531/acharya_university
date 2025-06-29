package com.au.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;


@Entity
@Table(name = "syllabus")
public class Syllabus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer syllabus_id;

	
//  private String syllabus_name;
//	private Integer ac_year_id;
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
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Boolean active;

	private String syllabus_path;

	@Transient
	private MultipartFile file;
	private String syllabus_code; 
	
    private Integer course_id;

    @Lob
	@NotBlank(message = "Syllabus_objective should not be Empty OR Null")
	private String syllabus_objective;
	private Integer course_assignment_id;
	
	private String duration;
	
	@Lob
	private String learning;
	@Lob
	private String topic_name;
	private String module;
	
	public Syllabus() {
		super();
	}

	public Integer getSyllabus_id() {
		return syllabus_id;
	}

	public void setSyllabus_id(Integer syllabus_id) {
		this.syllabus_id = syllabus_id;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}


	public String getSyllabus_path() {
		return syllabus_path;
	}

	public String setSyllabus_path(String syllabus_path) {
		return this.syllabus_path = syllabus_path;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getSyllabus_code() {
		return syllabus_code;
	}

	public void setSyllabus_code(String syllabus_code) {
		this.syllabus_code = syllabus_code;
	}

	public Integer getCourse_id() {
		return course_id;
	}

	public void setCourse_id(Integer course_id) {
		this.course_id = course_id;
	}

	public String getSyllabus_objective() {
		return syllabus_objective;
	}

	public void setSyllabus_objective(String syllabus_objective) {
		this.syllabus_objective = syllabus_objective;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Integer getCourse_assignment_id() {
		return course_assignment_id;
	}

	public void setCourse_assignment_id(Integer course_assignment_id) {
		this.course_assignment_id = course_assignment_id;
	}

	public String getLearning() {
		return learning;
	}

	public void setLearning(String learning) {
		this.learning = learning;
	}

	public String getTopic_name() {
		return topic_name;
	}

	public void setTopic_name(String topic_name) {
		this.topic_name = topic_name;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

}
