package com.au.dto;

public class StudentDetailsResponse {


	private String student_name;
	private String usn;
	private Integer current_year;
	private Integer current_sem;
	private Integer program_id;
	private Integer program_specialization_id;
	private Integer program_assignment_id;
	
	public StudentDetailsResponse() {
		
	}

	public StudentDetailsResponse(String student_name, String usn, Integer current_year, Integer current_sem) {
		this.student_name = student_name;
		this.usn = usn;
		this.current_year = current_year;
		this.current_sem = current_sem;
	}
	
	

	public StudentDetailsResponse(String student_name, String usn, Integer current_year, Integer current_sem,
			Integer program_id, Integer program_specialization_id, Integer program_assignment_id) {
		this.student_name = student_name;
		this.usn = usn;
		this.current_year = current_year;
		this.current_sem = current_sem;
		this.program_id = program_id;
		this.program_specialization_id = program_specialization_id;
		this.program_assignment_id = program_assignment_id;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public String getUsn() {
		return usn;
	}

	public void setUsn(String usn) {
		this.usn = usn;
	}

	public Integer getCurrent_year() {
		return current_year;
	}

	public void setCurrent_year(Integer current_year) {
		this.current_year = current_year;
	}

	public Integer getCurrent_sem() {
		return current_sem;
	}

	public void setCurrent_sem(Integer current_sem) {
		this.current_sem = current_sem;
	}

	public Integer getProgram_id() {
		return program_id;
	}

	public void setProgram_id(Integer program_id) {
		this.program_id = program_id;
	}

	public Integer getProgram_specialization_id() {
		return program_specialization_id;
	}

	public void setProgram_specialization_id(Integer program_specialization_id) {
		this.program_specialization_id = program_specialization_id;
	}

	public Integer getProgram_assignment_id() {
		return program_assignment_id;
	}

	public void setProgram_assignment_id(Integer program_assignment_id) {
		this.program_assignment_id = program_assignment_id;
	}
	
	
	
}
