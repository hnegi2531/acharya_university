package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScrapDTO {

	private Integer itemAssigmentId;
	private Double availableQunatity;
	private Double enterQuantity;
	private String remarks;
	private String uom;
}
