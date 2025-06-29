package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FrroDTO {
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

	private String visaIssued;

	private String recognition;

	private String affiliataion;

	private String aluEquivalenceDocument;
	private String passportCopyDocument;
	private String visaCopyDocument;
	private String residentialPermitCopyDocument;
	private Integer studentId;

}
