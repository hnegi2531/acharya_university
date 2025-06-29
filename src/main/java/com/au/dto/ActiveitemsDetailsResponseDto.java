package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveitemsDetailsResponseDto {
	
	private Integer itemId;
	private String measure_name;
	private String measure_short_name;
	private String itemNamesWithDiscriprtionAndMake;
	private boolean active;
	private boolean libraryBookStatus;
	private String  itemNature;
	private boolean isAccession;
	private String itemType;
	private String itemNames;
	private Integer envItemId;
	

	
	

}
