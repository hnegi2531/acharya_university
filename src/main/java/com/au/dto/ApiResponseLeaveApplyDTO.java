package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseLeaveApplyDTO {

	private String leave_short_name;
	private String contract_empcode;
	private String emp_id;
	private String from_date;
	private String to_date;
	private String no_of_days_applied;
}
