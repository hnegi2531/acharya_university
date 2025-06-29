package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.au.model.Condonation.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="condonation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Condonation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long condonationId;
	
	private Integer studentId;
	
	private Integer courseId;
	
	private Integer totalClassTaken;
	
	private Integer totalClass;
	
	private Integer additionClass;
	
	private Float percentage;
	
	private String condonationType;
	
	private String remarks;
	
	private Boolean princpalStatus=Boolean.FALSE; 
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	private Integer created_by;
	private Integer modified_by;
	
	public enum Status{
		APPROVED,PENDING
	}
}
