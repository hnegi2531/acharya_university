package com.au.dto;

import java.util.List;

import com.au.model.FeedbackQuestions;
import com.au.model.StudentFeedbackQuestions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetStudentForFeedback {
	
	private Integer studentId;
	
	private String studentName;
	
	private String auid;
	
	private Integer year;
	private Integer sem;
	
	private String section;
	private Integer schoolId;
	private String courseName;
	private String usn;
	private Integer acYearId;
	private Integer program_specialization_id;
	private String course_code;
	private String ac_year;
	private List<FeedbackQuestions> studentFeedbackQuestions;
	
	public GetStudentForFeedback(Integer studentId, String studentName, String auid, Integer year, Integer sem,
			String section, Integer schoolId,String courseName,String usn,Integer acYearId, Integer program_specialization_id,String course_code,String ac_year) {
		
		this.studentId = studentId;
		this.studentName = studentName;
		this.auid = auid;
		this.year = year;
		this.sem = sem;
		this.section = section;
		this.schoolId = schoolId;
		this.courseName=courseName;
		this.usn=usn;
		this.acYearId=acYearId;
		this.program_specialization_id=program_specialization_id;
		this.course_code=course_code;
		this.ac_year=ac_year;
	}
	
}
