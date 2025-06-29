package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllLibraryBookWithAccessionNumberDTO {

	private String bookName;
	private String accessionNumber;
	private Integer libraryAssigmentId;
}
