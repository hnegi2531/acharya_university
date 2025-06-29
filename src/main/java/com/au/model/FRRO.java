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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "frro")
public class FRRO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer frroId;

	private String nameAsPerPassport;

	private String passportNo;

	private String visaNo;

	private String fsis;

	private String birthPlace;

	private String passportIssuePlace;

	private String visaType;

 
	 
	private String immigrationDate;
	
	private String portOfArrival;
	
	 
	 
	private String passportIssueDate;
	
	private String typeOfEntry;
	
	private String issueBy;
	
	private String portOfDeparture;
	
	 
	 
	private String passportExpiryDate;
	
	private String placeOfVisaIssue;
	
	private String rpNo;
	
	private Boolean isReportedToIndia;
	
	 
	 
	private String reportedOn;
	
	 
	 
	private String visaIssueDate;
	
	 
	 
	private String rpIssueDate;
	
	private String remarks;
	
	 
	 
	private String visaExpiryDate;
	
	 
	private String rpExpiryDate;
	
	private String aluEquivalenceDocument;
	
	private String passportCopyDocument;
	
	private String visaCopyDocument;
	
	private String residentialPermitCopyDocument;
	
	private Integer studentId;
	
	private String visaIssued;
	
	private String recognition;
	
	private String affiliataion;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)	
	@CreationTimestamp
	private Date created_date;

	@Temporal(TemporalType.TIMESTAMP)	
	@UpdateTimestamp
	private Date modified_date;
	@Column(updatable = false)
	private String created_by;
	private String modified_by;

}
