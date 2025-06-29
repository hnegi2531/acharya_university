package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LockDateRequestDTO {

	private Integer lock_month;

	private Integer lock_year;

	private String leave_lock_date;
	private String payroll_lock_date;
	private String created_by;
	private String modified_by;
	private String remark;
	
	private int pageNo;
	private int pageSize;
	
	private String searchtext;

	private Integer active;

}
