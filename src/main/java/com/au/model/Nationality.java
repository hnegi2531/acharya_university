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
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "nationality")
public class Nationality {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nationality_id;
	private String country_name;
	private Integer num_code;
	private String alpha_2_code;
	private String alpha_3_code;
	private String en_short_name;
	private String nationality;
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;
	private Integer created_by;
	private String created_username;
	
	
	public Nationality() {
		super();
		
	}


	public Integer getNum_code() {
		return num_code;
	}


	public void setNum_code(Integer num_code) {
		this.num_code = num_code;
	}


	public String getAlpha_2_code() {
		return alpha_2_code;
	}


	public void setAlpha_2_code(String alpha_2_code) {
		this.alpha_2_code = alpha_2_code;
	}


	public String getAlpha_3_code() {
		return alpha_3_code;
	}


	public void setAlpha_3_code(String alpha_3_code) {
		this.alpha_3_code = alpha_3_code;
	}


	public String getEn_short_name() {
		return en_short_name;
	}


	public void setEn_short_name(String en_short_name) {
		this.en_short_name = en_short_name;
	}

	public String getCountry_name() {
		return country_name;
	}


	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}


	public String getNationality() {
		return nationality;
	}


	public void setNationality(String nationality) {
		this.nationality = nationality;
	}


	public Date getCreated_date() {
		return created_date;
	}


	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}


	public Integer getCreated_by() {
		return created_by;
	}


	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}


	public String getCreated_username() {
		return created_username;
	}


	public void setCreated_username(String created_username) {
		this.created_username = created_username;
	}


	public Integer getNationality_id() {
		return nationality_id;
	}


	public void setNationality_id(Integer nationality_id) {
		this.nationality_id = nationality_id;
	}
	
	
	
}
