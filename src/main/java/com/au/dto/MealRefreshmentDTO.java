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
public class MealRefreshmentDTO {

	private Integer refreshmentId;
	private String institute;
	private String department;
	private String endUser;
	private String end_user_feedback_remarks;
	private String approver;
	private Integer approvedStatus;
	private String mealDate;
	private String menuType;
	private Integer qty;
	private Double rate;
	private String total;
	private String remarks;
	private Integer approved_count;  
	private String school_name_short;	
	private Integer dept_id;
	private Integer school_id;
}
