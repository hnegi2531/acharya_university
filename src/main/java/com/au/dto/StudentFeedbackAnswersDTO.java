package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentFeedbackAnswersDTO {

	private Integer questionId;

	private Integer courseId;

	private Integer instituteId;

	private Integer studentId;

	private Integer ratings;
}
