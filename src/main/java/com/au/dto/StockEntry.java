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
public class StockEntry {

	private Date date;
	private GrnByItemAssigmentDTO grn;
	private StockIssueByItemAssigmentDTO stockIssue;
}
