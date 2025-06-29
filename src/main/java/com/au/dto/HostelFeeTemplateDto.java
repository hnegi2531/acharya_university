package com.au.dto;


import java.util.List;

import com.au.model.HostelFeeTemplate;
import com.au.model.HostelFeeTemplateSlots;
import com.au.model.HostelHeadWiseAmt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HostelFeeTemplateDto {

	private List<HostelFeeTemplate> hft;
	
	private List<HostelHeadWiseAmt> hhwa;
	
	private List<HostelFeeTemplateSlots> hfts;

}
