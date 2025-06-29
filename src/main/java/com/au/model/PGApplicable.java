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
@Table(name = "pg_applicable")
public class PGApplicable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pg_id;
	private String pg_exam_passed_name;
	private String university;

	private String ug_degree;
	private String ug_board;

	private Integer total_obtained;
	private Integer marks_total;
	private String college_name;
	private String year_of_passing;
	private Integer std_id;
	private String subject_studied_lang;
	private Integer year_1;
	private Integer year_2;
	private Integer year_3;
	private Integer year_4;
	private float pg_total_percentage;
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	@Column(name = "created_Date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_Date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_Date;

	private String remarks;
	private String auid;
	private Integer candidate_id;

	private String created_username;
	private String modified_username;

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

	public String getCollege_name() {
		return college_name;
	}

	public void setCollege_name(String college_name) {
		this.college_name = college_name;
	}

	public PGApplicable() {
		super();
	}

	public Integer getPg_id() {
		return pg_id;
	}

	public void setPg_id(Integer pg_id) {
		this.pg_id = pg_id;
	}

	public String getPg_exam_passed_name() {
		return pg_exam_passed_name;
	}

	public void setPg_exam_passed_name(String pg_exam_passed_name) {
		this.pg_exam_passed_name = pg_exam_passed_name;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getYear_of_passing() {
		return year_of_passing;
	}

	public void setYear_of_passing(String year_of_passing) {
		this.year_of_passing = year_of_passing;
	}

	public Integer getStd_id() {
		return std_id;
	}

	public void setStd_id(Integer std_id) {
		this.std_id = std_id;
	}

	public String getSubject_studied_lang() {
		return subject_studied_lang;
	}

	public void setSubject_studied_lang(String subject_studied_lang) {
		this.subject_studied_lang = subject_studied_lang;
	}

	public Integer getYear_1() {
		return year_1;
	}

	public void setYear_1(Integer year_1) {
		this.year_1 = year_1;
	}

	public Integer getYear_2() {
		return year_2;
	}

	public void setYear_2(Integer year_2) {
		this.year_2 = year_2;
	}

	public Integer getYear_3() {
		return year_3;
	}

	public void setYear_3(Integer year_3) {
		this.year_3 = year_3;
	}

	public Integer getTotal_obtained() {
		return total_obtained;
	}

	public void setTotal_obtained(Integer total_obtained) {
		this.total_obtained = total_obtained;
	}

	public Integer getMarks_total() {
		return marks_total;
	}

	public void setMarks_total(Integer marks_total) {
		this.marks_total = marks_total;
	}

	public Integer getYear_4() {
		return year_4;
	}

	public void setYear_4(Integer year_4) {
		this.year_4 = year_4;
	}

	public float getPg_total_percentage() {
		return pg_total_percentage;
	}

	public void setPg_total_percentage(float pg_total_percentage) {
		this.pg_total_percentage = pg_total_percentage;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAuid() {
		return auid;
	}

	public void setAuid(String auid) {
		this.auid = auid;
	}

	public Integer getCandidate_id() {
		return candidate_id;
	}

	public void setCandidate_id(Integer candidate_id) {
		this.candidate_id = candidate_id;
	}

	public String getUg_degree() {
		return ug_degree;
	}

	public void setUg_degree(String ug_degree) {
		this.ug_degree = ug_degree;
	}

	public String getUg_board() {
		return ug_board;
	}

	public void setUg_board(String ug_board) {
		this.ug_board = ug_board;
	}

	@Override
	public String toString() {
		return "PGApplicable [pg_id=" + pg_id + ", pg_exam_passed_name=" + pg_exam_passed_name + ", university="
				+ university + ", year_of_passing=" + year_of_passing + ", std_id=" + std_id + ", subject_studied_lang="
				+ subject_studied_lang + ", year_1=" + year_1 + ", year_2=" + year_2 + ", year_3=" + year_3
				+ ", year_4=" + year_4 + ", pg_total_percentage=" + pg_total_percentage + ", created_by=" + created_by
				+ ", modified_by=" + modified_by + ", created_Date=" + created_Date + ", modified_Date=" + modified_Date
				+ ", remarks=" + remarks + ", auid=" + auid + ", candidate_id=" + candidate_id + "]";
	}

}