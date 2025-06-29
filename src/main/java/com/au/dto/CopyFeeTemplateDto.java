package com.au.dto;

import java.util.Date;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CopyFeeTemplateDto {
	
	private Integer fee_template_id;
	private Integer old_ac_year_id;
	private Integer new_ac_year_id;
	private Integer current_year;
	

}
