package com.au.dto;

import java.util.Date;
import java.util.List;

import com.au.model.AdvanceMonthlyEmiDeduction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoDuesAssignmentDto {

	
	private Integer no_dues_assignment_id;
	private Boolean no_due_status;
	private String comments;
	private String approver_date;
	private String ip_address;
}
