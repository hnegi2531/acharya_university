package com.au.dto;

import java.util.HashMap;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassFeedbackAnswersWebDTO {

	private Integer class_feedback_answers_id;
	private Integer class_feedback_questions_id;
	private Integer student_id;
	private Integer user_id;
	private Integer program_specialization_id;
	private String remarks;
	private Boolean active;
	private HashMap<Integer, Integer> ratings;
	private Integer created_by;
	
	private Integer modified_by;
	
	private String created_username;
	private String modified_username;
	private Integer sectionId;
	private Integer acYearId;
	private Integer year;
	private Integer sem;
	private String courseAndBranch;
	private Integer course_id;
	private Integer feedback_window_id;
	private Integer window_count;
	
	private List<AnswersDTO> answers;

}