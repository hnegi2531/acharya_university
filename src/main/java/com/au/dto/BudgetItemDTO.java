package com.au.dto;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class BudgetItemDTO {
	
	private Integer ledger_id;
	private Integer voucher_head_new_id;
	private Double proposed_amount;
	private Double recommended_amount;
	private Double approved_amount;
}
