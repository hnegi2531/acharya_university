package com.au.model;

import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "biometric_attendance",uniqueConstraints = {
		@UniqueConstraint(columnNames = {"empId", "date", "empCode"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BiometricAttendance {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bioAttendanceId;
	@Column(name = "start_time")
	private String startTime;
	@Column(name = "end_time")
	private String endTime;
	private Integer empId;
	private String empCode;

	private String date;
	private String bio_date;
	
	private Double attendanceStatus;
	private Boolean compoffStatus;
	private Integer shiftId;
	private String compoffDate;
	private String presentStatus;
    private Date modifiedDate;
    private String remarks;
    private Date updateDate;
    private Integer user_id;
    private Integer month;
    private Integer year;
    private Integer deptId;
    private String employeeName;
    private String deptName;
    private String shiftStartTime;
    private String shiftEndTime;

	private String grace_start_time;
    private String grace_end_time;
    private Integer instituteId;
	
    private String duration;
}
