package com.au.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDTO {

	
	private String shiftName;

	private String shiftStartTime;

	private String shiftEndTime;
	private Integer createdBy;
	private Integer modifiedBy;


	private Boolean active;
	
	private String createdUsername;
	private String modifiedUsername;
	private String frontend_use_start_time;
	private String frontend_use_end_time;
	
	private Boolean is_saturday;
	private List<String> school_id;
	

	private String grace_time;
	
	private String actual_start_time;
	private String fhPunchIn;
	private String fhPunchOut;
	private String shPunchIn;
	private String shPunchOut;

}
