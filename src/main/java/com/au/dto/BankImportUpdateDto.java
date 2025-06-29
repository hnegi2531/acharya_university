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
public class BankImportUpdateDto {

	private Integer bank_import_transaction_id;
	 private String total_usd;
	   private String exachange_rate;
}
