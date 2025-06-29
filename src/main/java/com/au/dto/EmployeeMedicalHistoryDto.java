package com.au.dto;

public class EmployeeMedicalHistoryDto {
	
private Integer employeeId;
	
	private String personal_medical_history;
	
	private String family_medical_history;

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getPersonal_medical_history() {
		return personal_medical_history;
	}

	public void setPersonal_medical_history(String personal_medical_history) {
		this.personal_medical_history = personal_medical_history;
	}

	public String getFamily_medical_history() {
		return family_medical_history;
	}

	public void setFamily_medical_history(String family_medical_history) {
		this.family_medical_history = family_medical_history;
	}
	
	

}
