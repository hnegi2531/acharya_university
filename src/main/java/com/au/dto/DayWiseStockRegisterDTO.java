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
public class DayWiseStockRegisterDTO {

	private Date date;
	private Double openingBalance;
	private Double grn;
	private Double stockIssue;
	private Double scrap;
	private Double closingStock;
	private String uom;
 	
}
