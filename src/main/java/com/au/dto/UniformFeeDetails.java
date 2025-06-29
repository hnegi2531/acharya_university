package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniformFeeDetails {

	private String itemName;
	private Float amount;
	private Integer quantity;
	private Double cgst_output;
	private Double cgst_input;
	private Double sgst_input;
	private Double sgst_output;	
	private Double gst;
	private Integer env_item_id;
	private Integer paidYear;
	private Integer studentId;
}
