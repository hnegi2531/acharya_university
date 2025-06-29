package com.au.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class GrnByItemAssigmentDTO {

	private String grnNumber;
	private Date grnDate;
	private Double quantity;
	private String createdBy;
	private String uom;
	private String itemDescription;
	private String make;
	private String itemName;
	private String itemAssignmentName;
	
	public GrnByItemAssigmentDTO(String grnNumber, Date grnDate, Double quantity, String createdBy, String uom,
			String itemDescription, String make, String itemName) {
	
		this.grnNumber = grnNumber;
		this.grnDate = grnDate;
		this.quantity = quantity;
		this.createdBy = createdBy;
		this.uom = uom;
		this.itemDescription = itemDescription;
		this.make = make;
		this.itemName = itemName;
	}
	
	
}
