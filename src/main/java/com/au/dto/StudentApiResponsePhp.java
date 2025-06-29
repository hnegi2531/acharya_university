package com.au.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentApiResponsePhp {
	
	private boolean success;
	private List<StudentReportPhp> data;
	
	

}
