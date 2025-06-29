package com.au.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.dto.HolidayCalenderRequest;
import com.au.exception.ResourceNotFoundException;
import com.au.model.HolidayCalender;
import com.au.model.HolidayCalenderHistory;
import com.au.model.HolidayType;
import com.au.model.LeaveType;
import com.au.repository.DepartmentAssignmentRepository;
import com.au.repository.DepartmentRepository;
import com.au.repository.EventCreationRepository;
import com.au.repository.HolidayCalenderRepository;
import com.au.repository.HolidayTypeRepository;
import com.au.repository.LeaveTypeRepository;
import com.au.response.ResponseHandler;

@Service
public class HolidayCalenderService {

	@Autowired
	private HolidayCalenderRepository s_repo;
	
	@Autowired
	private LeaveTypeRepository l_repo;
	
	@Autowired
	private DepartmentAssignmentRepository deptAssign_repository;
	
	@Autowired
	private EventCreationRepository event_creation_repo;

//	@Autowired
//	private HolidayTypeRepository holiday_type_repo;;

	public List<HolidayCalender> listAll() {
		return s_repo.findAll1();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		List<Map<String, Object>> holiday_filtered_response = s_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponse(true, HttpStatus.OK, holiday_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		List<Map<String, Object>> holiday_sorted_response = s_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, holiday_sorted_response);
		}
	

	public List<HolidayCalender> saveHolidayCalender(HolidayCalenderRequest s) {
		HolidayCalender h2 = new HolidayCalender();
		List<HolidayCalender> h3 = new ArrayList<HolidayCalender>();
		List<HolidayCalender> h4 = new ArrayList<HolidayCalender>();
		LeaveType lt = l_repo.fetchLeaveType(s.getLeave_id());
		String lt_short = lt.getLeave_type_short();
		//if(s_repo.fetchCountByHolidayTypeSchoolJobType(s.getLeave_id(), s.getSchoolId(), s.getJobTypeId(), s.getDept_id())>=1) {
		if(s_repo.fetchCountByHolidayTypeSchoolJobType(s.getLeave_id(), s.getSchoolId(), s.getFromDate())>=1) {
			throw new RuntimeException("Holiday Calender Already exist with above Inputs!!!");
		}
		else if(lt.getLeave_type().equalsIgnoreCase("Declared Holiday")) {
			System.out.println("(((((((((((((((((())))))))))))))))))" +lt.getLeave_type());
			s.getSchoolId().stream().forEach(school -> {
					String department_ids = deptAssign_repository.fetchDepartmentIds(school);
					HolidayCalender h1 = new HolidayCalender();
					h1.setLeave_id(s.getLeave_id());
					h1.setHolidayName(s.getHolidayName());
					h1.setLeave_type_short(lt_short);
					h1.setLeave_type(lt.getLeave_type());
					h1.setCreatedBy(s.getCreatedBy());
					h1.setCreatedDate(s.getCreatedDate());
					h1.setCreatedUsername(s.getCreatedUsername());
					h1.setFromDate(s.getFromDate());
					h1.setActive(s.getActive());
					h1.setDaysCount(s.getDaysCount());
					h1.setSchoolId(school);
					h1.setJobTypeId(s.getJobTypeId());
					h1.setDept_id(department_ids);
					h1.setDay(s.getDay());
					h1.setHoliday_description(s.getHoliday_description());
					h3.add(h1);
					s_repo.save(h1);
				
			});
			return h3;
			
		} else {
			if(s_repo.countHolidayByDate(s.getFromDate()) >= 1)
			{
				throw new RuntimeException("Holiday Calender Already exist with above Date!!!");
			}
		    else if(s_repo.countHoliday(s.getHolidayName(),s.getFromDate()) >= 1)
			{
				throw new RuntimeException("Holiday Calender Already exist with combination with HolidayName and Date !!!");
			} 
			
			else {
			h2.setFromDate(s.getFromDate());
			h2.setLeave_id(s.getLeave_id());
			h2.setHolidayName(s.getHolidayName());
			h2.setLeave_type(lt.getLeave_type());
			h2.setLeave_type_short(lt.getLeave_type_short());
			h2.setCreatedBy(s.getCreatedBy());
			h2.setCreatedDate(s.getCreatedDate());
			h2.setCreatedUsername(s.getCreatedUsername());
			h2.setActive(s.getActive());
			h2.setDaysCount(s.getDaysCount());
			h2.setDay(s.getDay());
			h2.setHoliday_description(s.getHoliday_description());
			s_repo.save(h2);
			h4.add(h2);
			System.out.println("h4h4h4h4h4h4h4h4h4h4h4h4h4h4h4h4h4");
			return h4;
			}
		}
		
	}

	public HolidayCalender get(Integer id) {
		return s_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("HolidayCalender Not Found:" + id));
	}

	public void delete(Integer id) {
		HolidayCalender cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HolidayCalender Not Found:" + id));
		s_repo.updateHolidayCalender(id);
	}

	public void delete1(Integer id) {
		HolidayCalender cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HolidayCalender Not Found:" + id));
		s_repo.updateHolidayCalender1(id);

	}

	public List<Map<String, Object>> fetchHolidayCalenderDetails() {
		return s_repo.fetchHolidayCalenderDetails();
	}

	public HolidayCalender saveHolidayCalender1(@Valid HolidayCalender r) {
		return s_repo.save(r);

	}

	public List<Map<String, Object>> fetchHolidayCalenderDetails1() {
		return s_repo.fetchHolidayCalenderDetails1();
	}
	
	public List<Map<String, Object>> fetchHolidayCalenderDetailsOnSchooId(Integer schoolId) {

		List<Map<String, Object>> final_response = new ArrayList<>();

		List<Map<String, Object>> declared_holiday_of_school = s_repo.fetchDeclaredHolidayDetailsOnSchooId(schoolId);

		declared_holiday_of_school.stream().forEach(dh -> {
			final_response.add(dh);
		});

		List<Map<String, Object>> general_holiday_for_all = s_repo.fetchGeneralHolidayDetails();

		general_holiday_for_all.stream().forEach(gh -> {
			final_response.add(gh);
		});

		return final_response;
	}
	
	public HashMap<String ,Object> fetchHolidayAndEventDetailsForAcademicCalendarOnSchoolId(Integer schoolId) {
		
		HashMap<String ,Object> final_response=new HashMap<>();
		
		List<Map<String, Object>> holiday_response = new ArrayList<>();

		List<Map<String, Object>> declared_holiday_of_school = s_repo.fetchDeclaredHolidayDetailsForAcademicCalendarOnSchooId(schoolId);

		declared_holiday_of_school.stream().forEach(dh -> {
			holiday_response.add(dh);
		});

		List<Map<String, Object>> general_holiday_for_all = s_repo.fetchGeneralHolidayDetailsForAcademicCalendar();

		general_holiday_for_all.stream().forEach(gh -> {
			holiday_response.add(gh);
		});
		List<Map<String, Object>> event_details=event_creation_repo.fetchHolidayAndEventDetailsForAcademicCalendarOnSchoolId(schoolId.toString());
		final_response.put("Holiday Details",holiday_response );
		final_response.put("Important Date Details",event_details);
		return final_response;
	}

	public List<Map<String, Object>> listAllHolidayCalenderData(Integer schoolId) {
		return s_repo.listAllHolidayCalenderData(schoolId);
	}
	
	
	public List<Map<String, Object>> listAllHolidayCalenderDataWith(Integer schoolId, Integer deptId, Integer jobTypeId) {
		return s_repo.listAllHolidayCalenderDataWith(schoolId,deptId,jobTypeId);
	}
	
	
	public List<Map<String, Object>> listAllHolidayCalenderDataWithOutSchoolId() {
		return s_repo.listAllHolidayCalenderDataWithOutSchoolId();
	}
	
	public List<Map<String, Object>> listAllHolidayCalenderDataForStudentWithOutSchoolId() {
		return s_repo.listAllHolidayCalenderDataForStudentWithOutSchoolId();
	}



}
