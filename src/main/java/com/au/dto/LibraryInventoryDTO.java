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
public class LibraryInventoryDTO {

	private String category;
	private String accessionNumber;
	private String barcode;
	private String author;
	private String title;
	private String subtitle;
	private String edition;
	private String year;
	private String pages;
	private String publisher;
	private String place;
	private String sourceOfSupply;
	private String billNo;
	private Date billdate;
	private Integer foriegnCost;
	private Integer indianCost;
	private Integer discount;
	private Double netAmt;
	private String remarks;
	private String uid;

}
