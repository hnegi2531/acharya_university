package com.au.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniformTransactionDTO {

	private Integer studentId;
	private Integer schoolId;
	private Integer acYearId;
	private Integer currentYear;
	private Integer currentSem;
	private String mobile;
	private Integer amount;
	private Double cgst_input;

	private Double cgst_output;

	private Double sgst_input;

	private Double sgst_output;
	
	private Double gst;
	
	private Integer env_item_id;

}
