package com.au.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.model.TimeTableEmployee;
import com.au.repository.TimeTableEmployeeRepository;


@Service
public class TimeTableEmployeeService {
	
	@Autowired
	private TimeTableEmployeeRepository tte_repo;
	@Autowired
	private TimeTableService timeTableService;
	
	public void delete(List<Integer> time_table_employee_ids) {
//		TimeTable cc = t_repo.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("TimeTable Not Found:" + id));
		List<TimeTableEmployee> timeTableEmployee=tte_repo.findAllById(time_table_employee_ids);
		List<Integer> timetableids=timeTableEmployee.stream().map(tt -> tt.getTime_table_id()).collect(Collectors.toList());
		
		timeTableService.delete(timetableids);
		tte_repo.deactivateTimeTableEmployeeByTimeTableId(timetableids);
		tte_repo.updateTimeTable(time_table_employee_ids);
	}

	public void delete1(List<Integer> time_table_employee_ids) {
		List<TimeTableEmployee> timeTableEmployee=tte_repo.findAllById(time_table_employee_ids);
		List<Integer> timetableids=timeTableEmployee.stream().map(tt -> tt.getTime_table_id()).collect(Collectors.toList());
		
		timeTableService.delete1(timetableids);
		tte_repo.deactivateTimeTableEmployeeByTimeTableId1(timetableids);
		tte_repo.updateTimeTable1(time_table_employee_ids);

	}
	
	public List<Map<String, Object>> timeTableEmployeeDetailsOnTimeTableId(Integer time_table_id){
		return tte_repo.timeTableEmployeeDetailsOnTimeTableId(time_table_id);
	}

}
