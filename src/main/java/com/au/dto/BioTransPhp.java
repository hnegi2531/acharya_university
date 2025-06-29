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
public class BioTransPhp {

	private String empcode;
	private Date trn_date;
	private String trn_time;
	private String email;
	private String cardId;

}
