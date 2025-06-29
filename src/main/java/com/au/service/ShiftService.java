package com.au.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.JwtDetails;
import com.au.dto.ShiftDTO;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Shift;
import com.au.repository.School_Repository;
import com.au.repository.ShiftRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class ShiftService {

	@Autowired
	private ShiftRepository shift_repo;
	
	
	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private School_Repository sc_repo;

	public List<Map<String, Object>> listAll() {
		return shift_repo.findAll1();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> shift_filtered_response = shift_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, shift_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> shift_sorted_response = shift_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, shift_sorted_response);
	}

	public List<Shift> saveShift(ShiftDTO s,String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		List<Shift> shiftList = new ArrayList<>();
		s.getSchool_id().forEach(sh -> {
			boolean shiftExistsByNameAndSchool = shift_repo.existsByShiftNameAndSchoolId(s.getShiftName(), sh);
			boolean shiftExistsByTimeAndSchool = shift_repo.existsByShiftStartTimeAndSchoolId(s.getShiftStartTime(),
					s.getShiftEndTime(), sh);

			if (!shiftExistsByNameAndSchool && !shiftExistsByTimeAndSchool) {
				Shift shift = new Shift();
				shift.setActive(Boolean.TRUE);
				shift.setGrace_time(s.getGrace_time());
				shift.setFrontend_use_end_time(s.getFrontend_use_end_time());
				shift.setFrontend_use_start_time(s.getFrontend_use_start_time());
				shift.setIs_saturday(s.getIs_saturday());
				shift.setShiftStartTime(s.getShiftStartTime());
				shift.setShiftEndTime(s.getShiftEndTime());
				shift.setShiftName(s.getShiftName());
				shift.setSchool_id(sh);
				shift.setActual_start_time(s.getActual_start_time());
				shift.setCreatedBy(jwtDetails.getUserId());
				shift.setCreatedUsername(jwtDetails.getUserName());
				shift.setFhPunchIn(s.getFhPunchIn());
				shift.setFhPunchOut(s.getFhPunchOut());
				shift.setShPunchIn(s.getShPunchIn());
				shift.setShPunchOut(s.getShPunchOut());
				shift_repo.save(shift);
				shiftList.add(shift);
			} else {
				System.out.println("Shift with same name or time already exists for school: " + sh);
				throw new RuntimeException("Check with Shift Time Or Shift Name is Already Exist!!!");
			}
		});

		return shiftList;
	
	}

	public Shift saveShifts(Shift shift) {
		if ((shift_repo.checkShiftNameWithSchoolId(shift.getShiftCategoryId(), shift.getSchool_id())) > 1) {
			throw new RuntimeException("ShiftName is Already Exist!!!");
			
			
//			throw new RuntimeException("ShiftName is Already Exist!!!");
//		} else if (shift_repo.checkshiftTimeForUpdate(shift.getShiftCategoryId(), shift.getShiftStartTime(),
//				shift.getShiftEndTime()) == true) {
//			throw new RuntimeException("Shift Time is Already Exist!!!");
		} else {
			return shift_repo.save(shift);
//		}
	}
	}

	public Shift get(Integer id) {
		if (id.equals(0)) {
			throw new RuntimeException("Opps Exception raised....");
		}
		return shift_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Shift id Not Found:" + id));
	}

	public void delete(Integer id) {
		Shift ay = shift_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Shift id Not Found:" + id));
		shift_repo.update(id);
	}

	public void delete1(Integer id) {
		Shift ay = shift_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Shift id Not Found:" + id));
		shift_repo.update1(id);

	}

//	public List<Shift> shiftDetailsData() {
//		
//		List<Shift> shi = new ArrayList<Shift>(); 
//	List<Shift> shift =	shift_repo.getShiftDetailData();
//	shift.stream().forEach(ss ->{
//		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " +shift);
//		List<Integer> school_ids = ResponseHandler.toConvertCommaSeperatedIdsAsList(ss.getSchool_id())
//				.parallelStream().distinct().collect(Collectors.toList());
//		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " +school_ids);
//		List<String> school_short_name= sc_repo.getAllReportedStudents(school_ids);
//		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " +school_short_name.size());
//		StringBuffer s = new StringBuffer();
//		for(int i=0;i<school_short_name.size();i++)
//		{
//			if(i == 0)
//				s.append(school_short_name.get(i));
//			else {
//				s.append("," + school_short_name.get(i));
//			}
//		}
//		ss.setSchool_id(s.toString());
//		shi.add(ss);
//	});
//
//		return shi;
//	}

	public List<Map<String, Object>> shiftDetailsData() {
		List<Map<String, Object>> shift = shift_repo.getShiftDetailData();
		return shift;
	}

	public List<Map<String, Object>> shiftDetailsBasedOnSchoolId(String school_id) {
		return shift_repo.shiftDetailsBasedOnSchoolId(school_id);
	}

}
