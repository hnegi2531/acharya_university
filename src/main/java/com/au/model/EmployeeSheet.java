package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee_sheets",uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"empId", "month", "year"})
	})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSheet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer employeeSheetId;

	
	private Integer empId;
	private String empCode;

	private String day1;
	private String day2;
	private String day3;
	private String day4;
	private String day5;

	private String day6;
	private String day7;
	private String day8;
	private String day9;
	private String day10;
	private String day11;
	private String day12;

	private String day13;
	private String day14;
	private String day15;
	private String day16;
	private String day17;
	private String day18;
	private String day19;
	private String day20;
	private String day21;
	private String day22;
	private String day23;
	private String day24;
	private String day25;
	private String day26;
	private String day27;
	private String day28;
	private String day29;
	private String day30;
	private String day31;

	private double paydays = 0;
	private double presentdays = 0;
	private double generalWO = 0;
	private double leavetaken = 0;
	private double absentdays = 0;
	private Integer dept_id;
	private Integer school_id;
	private Integer designation_id;
	private Boolean active;
	private Integer month;
	private Integer year;
	private Integer totalDays;
	private double workingDays = 0;
	private double declaredHoliday = 0;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;
	@Column(updatable = false)
	private Integer created_by;

}
