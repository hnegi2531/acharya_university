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
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "lesson_plan_assignment")
public class LessonPlanAssignment {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer lesson_assignment_id;
	private Integer lesson_id;
	private String plan_date;//
	private String contents;//
	private String teaching_aid;//

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
	private Integer book_id;
	@Transient
	private MultipartFile file;
	
	private String ict_text;
	private String attachment_name;
 	private String attachment_path;
 	private String type;
 	private String teaching_mode;
 	private String learning_style;
	
	public LessonPlanAssignment(String plan_date, String contents, String teaching_aid,String type, String teaching_mode, String learning_style) {
		super();
		this.plan_date = plan_date;
		this.contents = contents;
		this.teaching_aid = teaching_aid;
		this.type = type;
		this.teaching_mode = teaching_mode;
		this.learning_style = learning_style;
	}

	public LessonPlanAssignment() {
		super();
	}

	public Integer getLesson_assignment_id() {
		return lesson_assignment_id;
	}

	public void setLesson_assignment_id(Integer lesson_assignment_id) {
		this.lesson_assignment_id = lesson_assignment_id;
	}

	public Integer getLesson_id() {
		return lesson_id;
	}

	public void setLesson_id(Integer lesson_id) {
		this.lesson_id = lesson_id;
	}

	public String getPlan_date() {
		return plan_date;
	}

	public void setPlan_date(String plan_date) {
		this.plan_date = plan_date;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getTeaching_aid() {
		return teaching_aid;
	}

	public void setTeaching_aid(String teaching_aid) {
		this.teaching_aid = teaching_aid;
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

	public Integer getBook_id() {
		return book_id;
	}

	public void setBook_id(Integer book_id) {
		this.book_id = book_id;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getIct_text() {
		return ict_text;
	}

	public void setIct_text(String ict_text) {
		this.ict_text = ict_text;
	}

	public String getAttachment_name() {
		return attachment_name;
	}

	public void setAttachment_name(String attachment_name) {
		this.attachment_name = attachment_name;
	}

	public String getAttachment_path() {
		return attachment_path;
	}

	public void setAttachment_path(String attachment_path) {
		this.attachment_path = attachment_path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTeaching_mode() {
		return teaching_mode;
	}

	public void setTeaching_mode(String teaching_mode) {
		this.teaching_mode = teaching_mode;
	}

	public String getLearning_style() {
		return learning_style;
	}

	public void setLearning_style(String learning_style) {
		this.learning_style = learning_style;
	}
	
	

	
}
