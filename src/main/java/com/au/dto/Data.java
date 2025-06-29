package com.au.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Data {
	
	 @JsonProperty("faculty_id")
     private String facultyId;
     
     @JsonProperty("class_details")
     private ClassDetails classDetails;
     
     @JsonProperty("student_attendence")
    private List<String> studentAttendance;

}
