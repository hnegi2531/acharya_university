package com.au.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ClassDetails {
	
	@JsonProperty("online")
    private String online;
    
    @JsonProperty("interval_type_id")
    private String intervalTypeId;
    
    @JsonProperty("time_table_id")
    private String timeTableId;
    
    @JsonProperty("sTime")
    private String sTime;
    
    @JsonProperty("eTime")
    private String eTime;
    
    @JsonProperty("empID")
    private String empID;
    
    @JsonProperty("selected_date")
    private String selectedDate;
    
    @JsonProperty("course_branch_assignment_id")
    private String courseBranchAssignmentId;
    
    @JsonProperty("current_year")
    private String currentYear;
    
    @JsonProperty("current_sem")
    private String currentSem;
    
    @JsonProperty("course_assignment_id")
    private String courseAssignmentId;
    
    @JsonProperty("start_time")
    private String startTime;
    
    @JsonProperty("end_time")
    private String endTime;
    
    @JsonProperty("section_assignment_id")
    private String sectionAssignmentId;
    
    @JsonProperty("batch_assignment_id")
    private String batchAssignmentId;
    
    @JsonProperty("subject_assignment_id")
    private String subjectAssignmentId;
    
    @JsonProperty("branch_name")
    private String branchName;
    
    @JsonProperty("branch_id")
    private String branchId;
    
    @JsonProperty("duration")
    private String duration;
    
    @JsonProperty("batch_short_name")
    private String batchShortName;
    
    @JsonProperty("section_short_name")
    private String sectionShortName;
    
    @JsonProperty("subject_name_short")
    private String subjectNameShort;
    
    @JsonProperty("course_branch_short_name")
    private String courseBranchShortName;
    
    @JsonProperty("course_short_name")
    private String courseShortName;
    
    @JsonProperty("interval")
    private String interval;
    
    @JsonProperty("institute_id")
    private String instituteId;
    
    @JsonProperty("institute_name_short")
    private String instituteNameShort;
    
    @JsonProperty("subject_id")
    private String subjectId;
    
    @JsonProperty("quizzes")
    private List<Object> quizzes;
    
    @JsonProperty("date")
    private DateInfo date;
    
    @JsonProperty("does_exists")
    private int doesExists;
    
    @JsonProperty("record")
    private boolean record;
    
    @JsonProperty("presenter_name")
    private String presenterName;


}
