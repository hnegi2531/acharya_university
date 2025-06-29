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
@Table(name = "family_structure")
public class FamilyStructure {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer family_structure_id;
	private Integer emp_id;
	private Integer age;
	private String dob;
	private String relationship;
	private String occupation;
	private String dependent;
	private String name;
	private String contact_number;
	
	@Column(updatable = false)
	private Integer created_by;
	
	private Integer modified_by;

	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	
	private String modified_username;

	public FamilyStructure() {
		super();
		
	}



	public Integer getFamily_structure_id() {
		return family_structure_id;
	}



	public void setFamily_structure_id(Integer family_structure_id) {
		this.family_structure_id = family_structure_id;
	}



	public String getDob() {
		return dob;
	}



	public void setDob(String dob) {
		this.dob = dob;
	}



	public Integer getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(Integer emp_id) {
		this.emp_id = emp_id;
	}



	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getDependent() {
		return dependent;
	}

	public void setDependent(String dependent) {
		this.dependent = dependent;
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
	
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getContact_number() {
		return contact_number;
	}

	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}
	
	


}
