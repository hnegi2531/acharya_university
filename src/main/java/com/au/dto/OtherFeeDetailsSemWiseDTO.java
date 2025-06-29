package com.au.dto;

import java.util.List;

import com.au.model.OtherFeeDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtherFeeDetailsSemWiseDTO {

	private Float grantTotalSem1;
	private Float grantTotalSem2;
	private Float grantTotalSem3;
	private Float grantTotalSem4;
	private Float grantTotalSem5;
	private Float grantTotalSem6;
	private Float grantTotalSem7;
	private Float grantTotalSem8;
	private Float grantTotalSem9;

	private Float grantTotalSem10;
	private Float grantTotalSem11;
	private Float grantTotalSem12;
	private List<OtherFeeDetails> otherFeeDetails;
}
