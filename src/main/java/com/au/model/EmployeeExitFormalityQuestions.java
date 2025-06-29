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
@Table(name = "employee_exit_formality_questions")
public class EmployeeExitFormalityQuestions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eefqid;
	private Integer category_details_id;
	private String question;
	private String type;
	
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
	
	
	public EmployeeExitFormalityQuestions() {
		super();
	}


	public Integer getEefqid() {
		return eefqid;
	}


	public void setEefqid(Integer eefqid) {
		this.eefqid = eefqid;
	}



	public Integer getCategory_details_id() {
		return category_details_id;
	}


	public void setCategory_details_id(Integer category_details_id) {
		this.category_details_id = category_details_id;
	}


	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
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


	
}
