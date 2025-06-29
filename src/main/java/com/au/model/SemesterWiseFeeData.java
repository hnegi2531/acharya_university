package com.au.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SemesterWiseFeeData {

	private Integer sem;
	private Float feePaid;
	private Float feeDue;
	private Float feeFixed;
}
