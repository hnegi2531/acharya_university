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
@Table(name = "phd_details")
public class PhD_Details {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer phd_id;
	private Integer job_id;
	private String stream;
	private String registered_year;
	private String passedout_year;
	private String university;
	private Integer no_of_journals_published;
	private Integer no_of_paper_presentations;
	private Integer book_chapters;
	private Integer book_publications;
	private String patents_owned;
	private Integer no_of_patents;
	private String description;
	private String phd;
	private String mphil;
//	
//	private Integer phd_attachment_id;
//	private String phd_attachment_path;
//	private String phd_attachment_file_name;
//	private String phd_attachement_type;
	
	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_Date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_Date;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	
	public PhD_Details() {
		super();
	}

	public Integer getPhd_id() {
		return phd_id;
	}

	public void setPhd_id(Integer phd_id) {
		this.phd_id = phd_id;
	}
	
	public Integer getJob_id() {
		return job_id;
	}

	public void setJob_id(Integer job_id) {
		this.job_id = job_id;
	}

	public String getStream() {
		return stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}

	public String getRegistered_year() {
		return registered_year;
	}

	public void setRegistered_year(String registered_year) {
		this.registered_year = registered_year;
	}

	public String getPassedout_year() {
		return passedout_year;
	}

	public void setPassedout_year(String passedout_year) {
		this.passedout_year = passedout_year;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public Integer getNo_of_journals_published() {
		return no_of_journals_published;
	}

	public void setNo_of_journals_published(Integer no_of_journals_published) {
		this.no_of_journals_published = no_of_journals_published;
	}

	public Integer getNo_of_paper_presentations() {
		return no_of_paper_presentations;
	}

	public void setNo_of_paper_presentations(Integer no_of_paper_presentations) {
		this.no_of_paper_presentations = no_of_paper_presentations;
	}

	public Integer getBook_chapters() {
		return book_chapters;
	}

	public void setBook_chapters(Integer book_chapters) {
		this.book_chapters = book_chapters;
	}

	public Integer getBook_publications() {
		return book_publications;
	}

	public void setBook_publications(Integer book_publications) {
		this.book_publications = book_publications;
	}

	public String getPatents_owned() {
		return patents_owned;
	}

	public void setPatents_owned(String patents_owned) {
		this.patents_owned = patents_owned;
	}

	public Integer getNo_of_patents() {
		return no_of_patents;
	}

	public void setNo_of_patents(Integer no_of_patents) {
		this.no_of_patents = no_of_patents;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhd() {
		return phd;
	}

	public void setPhd(String phd) {
		this.phd = phd;
	}

	public String getMphil() {
		return mphil;
	}

	public void setMphil(String mphil) {
		this.mphil = mphil;
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

	public Date getCreated_Date() {
		return created_Date;
	}

	public void setCreated_Date(Date created_Date) {
		this.created_Date = created_Date;
	}

	public Date getModified_Date() {
		return modified_Date;
	}

	public void setModified_Date(Date modified_Date) {
		this.modified_Date = modified_Date;
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
