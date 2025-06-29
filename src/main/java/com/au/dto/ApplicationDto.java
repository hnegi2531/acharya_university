package com.au.dto;

import java.util.Date;
import java.util.List;

import com.au.model.ApplicantDetails;
import com.au.model.Candidate_Walkin;
import com.au.model.PGApplicable;
import com.au.model.ReportingStudents;
import com.au.model.StdEntranceExam;
import com.au.model.StdReportingStudentsHistory;
import com.au.model.StudentTranscriptSubmission;
import com.au.model.Student_Details;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {

	private List<ApplicantDetails> ap;

	private PGApplicable pgapp;
	private Student_Details sd;

	private StdEntranceExam see;

	private StudentTranscriptSubmission sts;

	private StudentTranscriptSubmissionRequest streq;

	private StdReportingStudentsHistory srsh;

	private ReportingStudents rs;
	
	private Candidate_Walkin cw;


}
