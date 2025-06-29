package com.au.dto;

import java.util.Date;
import java.util.List;

import com.au.model.Budget;

import lombok.Data;

@Data
public class BudgetStatusDto {

	private List<Integer> budget_id;
	private Boolean lock_status; 
	private String lock_date;
}
