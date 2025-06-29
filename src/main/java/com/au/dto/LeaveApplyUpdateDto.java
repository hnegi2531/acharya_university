package com.au.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApplyUpdateDto {
	
	private HashMap<Integer, Integer> leaveApprover1;
	private HashMap<Integer, Integer> leaveApprover2;
	
	

}
