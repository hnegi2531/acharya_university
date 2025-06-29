package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LSQStatusDTO {

	private String SchemaName;
	private String Value;
	@Override
	public String toString() {
		return "LSQStatusDTO [SchemaName=" + SchemaName + ", Value=" + Value + "]";
	}
	
	
}
