package com.au.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LockDateResponsseDTO {

	private Integer lock_month;

	private Integer lock_year;

	private Date leave_lock_date;
	private Date payroll_lock_date;
	private String created_by;
	private Date created_date;
	private String remarks;
	private String modified_by;

	
}
