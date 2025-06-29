package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LibraryAccessionNumber {

	private String author;
	private String title;
	private String accessionNo;
	private String barcode;
}
