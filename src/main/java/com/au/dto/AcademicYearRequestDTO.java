package com.au.dto;

public class AcademicYearRequestDTO {

	private Integer academic_year_id;


	public AcademicYearRequestDTO() {
		
	}

	public AcademicYearRequestDTO(Integer academic_year_id) {
		this.academic_year_id = academic_year_id;
	}

	public Integer getAcademic_year_id() {
		return academic_year_id;
	}

	public void setAcademic_year_id(Integer academic_year_id) {
		this.academic_year_id = academic_year_id;
	}
}
