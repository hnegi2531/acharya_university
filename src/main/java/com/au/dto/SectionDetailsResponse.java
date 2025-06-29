package com.au.dto;

public class SectionDetailsResponse {
	
	private Integer section_assignment_id;
	private Integer section_id;
	private String section_short_name;
	
	public SectionDetailsResponse() {
		
	}

	public SectionDetailsResponse(Integer section_assignment_id, Integer section_id, String section_short_name) {
		this.section_assignment_id = section_assignment_id;
		this.section_id = section_id;
		this.section_short_name = section_short_name;
	}

	public Integer getSection_assignment_id() {
		return section_assignment_id;
	}

	public void setSection_assignment_id(Integer section_assignment_id) {
		this.section_assignment_id = section_assignment_id;
	}

	public Integer getSection_id() {
		return section_id;
	}

	public void setSection_id(Integer section_id) {
		this.section_id = section_id;
	}

	public String getSection_short_name() {
		return section_short_name;
	}

	public void setSection_short_name(String section_short_name) {
		this.section_short_name = section_short_name;
	}
	
	
}
