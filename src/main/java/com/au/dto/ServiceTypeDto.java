package com.au.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTypeDto {
	
		private Long id;
	    private String serviceTypeName;
	    private String serviceTypeShortName;
	    private Boolean showInEvent;
	    private Boolean hostelStatus;
	    
	    private String dept_id;

}
