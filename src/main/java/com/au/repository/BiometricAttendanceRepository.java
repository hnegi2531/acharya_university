package com.au.repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.BiometricAttendance;

@Repository
@Transactional
public interface BiometricAttendanceRepository extends JpaRepository<BiometricAttendance, Integer>{

	@Query(value = "Select ba.compoff_status from biometric_attendance ba where ba.emp_id=?1 and ba.compoff_date=?2",nativeQuery=true)
	public Boolean checkAvailabilityOfCompOff(Integer emp_id, Date date);
	
	@Query(value = "select ba.start_time from biometric_attendance ba where ba.emp_id=?1 and ba.date=?2",nativeQuery=true)
	public String getPunchInTime(Integer emp_id, Date date);
	
	@Query(value = "select ba.end_time from biometric_attendance ba where ba.emp_id=?1 and ba.date=?2",nativeQuery=true)
	public String getPunchOutTime(Integer emp_id, Date date);
	
	public BiometricAttendance findFirstByDateAndEmpCode(String formattedDate, String k);
	
	@Query(value=" select * from biometric_attendance b where b.date=:date and b.emp_id=:emp_id  limit 1 ",nativeQuery = true)
	public BiometricAttendance getByDateAndEmpId(@Param("date") String formattedDate,@Param("emp_id") Integer empId);
	
	public List<BiometricAttendance> findByEmpIdAndMonthAndYear(Integer emp_id, Integer month, Integer year);

	
	
	
	
	@Query(value=" select ba.start_time from biometric_attendance ba where ba.emp_id=?1 And ba.date=DATE_FORMAT(STR_TO_DATE(?2,'%d-%m-%Y'), '%Y-%m-%d') ",nativeQuery = true)
	public Time getPunchInTiming(Integer emp_id, String fromDate);

	@Query(value=" select ba.end_time from biometric_attendance ba where ba.emp_id=?1 And ba.date= DATE_FORMAT(STR_TO_DATE(?2,'%d-%m-%Y'), '%Y-%m-%d') ",nativeQuery = true)
	public Time getPunchOutTiming(Integer emp_id, String fromDate);

	public BiometricAttendance getAttendenceByDateAndEmpCode(String formattedDate, String empcode);

	@Query(value=" select * from biometric_attendance ba where ba.emp_id=?2 "
			+ "And ba.date= DATE_FORMAT(STR_TO_DATE(?1,'%d-%m-%Y'), '%Y-%m-%d') ",nativeQuery = true)
	public BiometricAttendance getAttendanceData(String date1, Integer emp_id);

	@Query(value=" select * from biometric_attendance ba where ba.emp_id=?2 "
			+ "And ba.date= DATE_FORMAT(STR_TO_DATE(?1,'%d-%m-%Y'), '%Y-%m-%d') ",nativeQuery = true)
	public BiometricAttendance getBiometricAttendanceDataByEmp_idAndDate(String date, Integer emp_id);
}
