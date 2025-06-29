package com.au.dto;

import java.util.List;

public class CurrentYearBatchAndSectionofStudent {

	private SectionDetailsResponse section;
	private List<BatchDetailsByReportingStudent> batch;

	public CurrentYearBatchAndSectionofStudent() {
		
	}

	public CurrentYearBatchAndSectionofStudent(SectionDetailsResponse section,
			List<BatchDetailsByReportingStudent> batch) {
		
		this.section = section;
		this.batch = batch;
	}

	public SectionDetailsResponse getSection() {
		return section;
	}

	public void setSection(SectionDetailsResponse section) {
		this.section = section;
	}

	public List<BatchDetailsByReportingStudent> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchDetailsByReportingStudent> batch) {
		this.batch = batch;
	}
}
