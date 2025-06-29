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
@Table(name = "frro_history")
public class FrroHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer frroHistoryId;

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

	private Boolean isChangenameAsPerPassport = Boolean.FALSE;

	private Boolean isChangepassportNo = Boolean.FALSE;

	private Boolean isChangevisaNo = Boolean.FALSE;

	private Boolean isChangefsis = Boolean.FALSE;

	private Boolean isChangebirthPlace = Boolean.FALSE;

	private Boolean isChangepassportIssuePlace = Boolean.FALSE;

	private Boolean isChangevisaType = Boolean.FALSE;

	private Boolean isChangeimmigrationDate = Boolean.FALSE;

	private Boolean isChangeportOfArrival = Boolean.FALSE;

	private Boolean isChangepassportIssueDate = Boolean.FALSE;

	private Boolean isChangetypeOfEntry = Boolean.FALSE;

	private Boolean isChangeissueBy = Boolean.FALSE;

	private Boolean isChangeportOfDeparture = Boolean.FALSE;

	private Boolean isChangepassportExpiryDate = Boolean.FALSE;

	private Boolean isChangeplaceOfVisaIssue = Boolean.FALSE;

	private Boolean isChangerpNo = Boolean.FALSE;

	private Boolean isChangeisReportedToIndia = Boolean.FALSE;

	private Boolean isChangereportedOn = Boolean.FALSE;

	private Boolean isChangevisaIssueDate = Boolean.FALSE;

	private Boolean isChangerpIssueDate = Boolean.FALSE;

	private Boolean isChangeremarks = Boolean.FALSE;

	private Boolean isChangevisaExpiryDate = Boolean.FALSE;

	private Boolean isChangerpExpiryDate = Boolean.FALSE;

	private Boolean isChangealuEquivalenceDocument = Boolean.FALSE;

	private Boolean isChangepassportCopyDocument = Boolean.FALSE;

	private Boolean isChangevisaCopyDocument = Boolean.FALSE;

	private Boolean isChangeresidentialPermitCopyDocument = Boolean.FALSE;

	private Boolean isChangeAffiliation = Boolean.FALSE;

	private Boolean isChangeRecogination = Boolean.FALSE;

	private Boolean isChangevisaIssued = Boolean.FALSE;

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
