package com.au.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FrroResponseDTO {

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

	private boolean isReportedToIndia;

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

	private String studentName;

	private Date dateOfBirth;
	private String genderString;
	private String fatherName;
	private String currentAddress;
	private String permanentAddress;
	private String usn;
	private String auid;
	private Integer currentYear;
	private Integer currentSem;
	private String programName;
	private String programSpecializationName;
	private String visaIssued;

	private String recognition;

	private String affiliataion;

	private String nationality;
}
