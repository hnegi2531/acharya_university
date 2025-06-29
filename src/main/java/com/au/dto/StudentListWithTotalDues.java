package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentListWithTotalDues {

	private String auid;
	private String studentName;
	private String programmeName;
	private String phoneNo;
	private Float totalDue;
	private String categoryName;
	private String categoryShortName;
	
	
	
	
	public StudentListWithTotalDues(String auid, String studentName, String phoneNo, Float totalDue,
			String categoryName, String categoryShortName) {
	
		this.auid = auid;
		this.studentName = studentName;
		this.phoneNo = phoneNo;
		this.totalDue = totalDue;
		this.categoryName = categoryName;
		this.categoryShortName = categoryShortName;
	}




	public StudentListWithTotalDues(String auid, String studentName, String programmeName, String phoneNo,
			Float totalDue) {
		this.auid = auid;
		this.studentName = studentName;
		this.programmeName = programmeName;
		this.phoneNo = phoneNo;
		this.totalDue = totalDue;
	}
	
}
