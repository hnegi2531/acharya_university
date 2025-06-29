package com.au.dto;

public class CopyLeavePatternDto {

	private Integer prev_year;
	private Integer next_year;
	private Integer school_id;

	public CopyLeavePatternDto() {
		super();
	}

	public CopyLeavePatternDto(Integer prev_year, Integer next_year, Integer school_id) {
		super();
		this.prev_year = prev_year;
		this.next_year = next_year;
		this.school_id = school_id;
	}

	public Integer getPrev_year() {
		return prev_year;
	}

	public void setPrev_year(Integer prev_year) {
		this.prev_year = prev_year;
	}

	public Integer getNext_year() {
		return next_year;
	}

	public void setNext_year(Integer next_year) {
		this.next_year = next_year;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	@Override
	public String toString() {
		return "CopyLeavePatternDto [prev_year=" + prev_year + ", next_year=" + next_year + ", school_id=" + school_id
				+ "]";
	}

}
