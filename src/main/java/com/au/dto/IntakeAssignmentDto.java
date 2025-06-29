package com.au.dto;

import java.util.HashMap;
import java.util.List;

import com.au.model.IntakeAssignment;

public class IntakeAssignmentDto {
	
	private List<IntakeAssignment> intake_assignment;
	private List<HashMap<Integer, Integer>> fee_admission_category_id;
	private Boolean active;
	
	public IntakeAssignmentDto() {
		super();
	}

	public List<IntakeAssignment> getIntake_assignment() {
		return intake_assignment;
	}

	public void setIntake_assignment(List<IntakeAssignment> intake_assignment) {
		this.intake_assignment = intake_assignment;
	}
	
	
	
	public List<HashMap<Integer, Integer>> getFee_admission_category_id() {
		return fee_admission_category_id;
	}

	public void setFee_admission_category_id(List<HashMap<Integer, Integer>> fee_admission_category_id) {
		this.fee_admission_category_id = fee_admission_category_id;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	

}
