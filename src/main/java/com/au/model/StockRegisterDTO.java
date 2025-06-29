package com.au.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockRegisterDTO {

	private Integer itemAssignmentId;
	
	private String itemAssigmentName;
	
	private Double openingStock;
	
	private Double closingStock;
	
	private Double grn;
	private Double stockIssue;
	
	private Double scrap;
	
	private Integer itemId;
	private Integer legderId;
	private String itemName;
	private String itemDescription;
	private String uom;

}
