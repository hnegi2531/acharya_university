package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtherFeeDetailsDTO {

	private Integer voucherHeadId;

	private Float sem1;

	private Float sem2;

	private Float sem3;

	private Float sem4;

	private Float sem5;

	private Float sem6;

	private Float sem7;

	private Float sem8;
	
	private Float sem9;
	private Float sem10;
	private Float sem11;
	private Float sem12;

	private Float year1;
	private Float year2;
	private Float year3;
	private Float year4;

	private Float total;
	
	private Integer otherFeeDetailsId;
	private Integer fee_admission_category_id;
	private Integer currency_type_id;
	private Integer fee_template_id;
}
