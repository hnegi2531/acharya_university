package com.au.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BioMetricAttendanceEvent {
	private Integer month;
	private Integer year;
	private Integer empId;
}
