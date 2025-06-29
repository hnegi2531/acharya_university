package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentFeedbackQuestionsDTO {

	private Long questionId;
	private String questions;

	private Integer instituteId;

	private Integer courseId;

}
