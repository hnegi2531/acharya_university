package com.au.dto;

import com.au.model.HostelBedAssignment;
import com.au.model.HostelBeds;
import com.au.model.HostelFeeTemplate;
import com.au.model.Student_Details;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class HostelBedAssignmentRequest {
	
	private HostelBedAssignment hostelBedAssignment;
	private HostelBeds hostelBeds;
	private HostelFeeTemplate hostelFeeTemplate;
	private Student_Details student;
	
	

}
