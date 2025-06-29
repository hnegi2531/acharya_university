package com.au.event;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FrroPassportAndVisaExpiry {
	
//	private Integer schoolId;
	private String todaysDate;
	private String expectedExpiryDate20;
	private String expectedExpiryDate15;
	private String expectedExpiryDate07;
	private String expired;

}
