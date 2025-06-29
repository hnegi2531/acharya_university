package com.au.dto;

import java.util.List;

import com.au.model.SemesterWiseFeeData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentFeeDetailsDTO {

	private String auid;
	private Double feePaid;
	private Float grant;
	private Integer feeDue;

	private List<StudentFeeDetails> studentFeeDetails;
	private List<SemesterWiseFeeData> semesterWiseFeeDatas;
	
	public StudentFeeDetailsDTO(String auid, Double feePaid, Float grant, Integer feeDue) {
		
		this.auid = auid;
		this.feePaid = feePaid;
		this.grant = grant;
		this.feeDue = feeDue;
		
	}
}
