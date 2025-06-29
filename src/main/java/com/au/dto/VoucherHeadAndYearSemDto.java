package com.au.dto;

import java.util.List;

import lombok.Data;

@Data
public class VoucherHeadAndYearSemDto {
	
	private Integer yearOrSem;
	private List<VoucherHeadWithAmountDto> voucherHeadWithAmountDtos;
}
