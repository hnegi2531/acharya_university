package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignUserResponseDTO {

	private String userName;
	private Integer userId;
	private Boolean food_approver;
	private Boolean travel_approver;
	private Boolean bill_approver;
	private Boolean purchase_approver;
}
