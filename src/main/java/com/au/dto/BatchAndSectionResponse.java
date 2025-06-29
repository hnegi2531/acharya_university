package com.au.dto;

import java.util.List;

public class BatchAndSectionResponse {

	

	private List<BatchByAcademicYear> batches;
	private List<SectionByAcademicYear> sections;
	
	public BatchAndSectionResponse() {
		
	}

	public BatchAndSectionResponse(List<BatchByAcademicYear> batches, List<SectionByAcademicYear> sections) {
		this.batches = batches;
		this.sections = sections;
	}

	public List<BatchByAcademicYear> getBatches() {
		return batches;
	}

	public void setBatches(List<BatchByAcademicYear> batches) {
		this.batches = batches;
	}

	public List<SectionByAcademicYear> getSections() {
		return sections;
	}

	public void setSections(List<SectionByAcademicYear> sections) {
		this.sections = sections;
	}
}
