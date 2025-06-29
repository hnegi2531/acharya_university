package com.au.dto;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.au.model.BatchAssignment;

import lombok.Data;

@Data
public class BatchAssignmentDto {
	
	private Integer batch_id;
	private Integer school_id;  // institute_id
	private Integer ac_year_id;
//	private Integer section_id; //Not required
	private Integer current_year;
	private Integer current_sem;
	private String student_ids;
	private Integer batch_type; // Not required
	private String remarks;
	private Integer batch_master_id;

	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	private Boolean active;
	private String guest_uesr_ids;
	private Integer interval_type_id;
	private List<Integer> program_assignment_id;
	private List<Integer> program_specialization_id;
	private List<Integer> program_id;           // course_assignment_id;
	
}
