package com.au.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.validation.Valid;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.LeavePatternRequest;
import com.au.exception.ResourceNotFoundException;
import com.au.model.LeavePattern;
import com.au.repository.LeavePatternRepository;
import com.au.response.ResponseHandler;

@Service
public class LeavePatternService {

	@Autowired
	private LeavePatternRepository leave_pattern_repo;

	public List<LeavePattern> listAll() {
		return leave_pattern_repo.findAll1();
	}

//	public List<LeavePattern> listAll1() {
//		return leave_pattern_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = leave_pattern_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		
		Page<Object> response = leave_pattern_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public List<LeavePattern> saveLeavePattern(LeavePatternRequest s) {

		LeavePattern lp1 = new LeavePattern();
		/*
		 if (leave_pattern_repo.validationLeavePattern(s.getYear(), s.getSchoolId(), s.getLeaveId(),
		  s.getEmpTypeId(), s.getJob_type_id())>=1) { throw new
		  RuntimeException("Detail's already exist with above data. Please change input"
		  ); } else {
		 */
		List<LeavePattern> list = new ArrayList();

		
		if (leave_pattern_repo.validationLeavePattern(s.getYear(), s.getSchool_id(), s.getLeave_id(),
				  s.getEmp_type_id(), s.getJob_type_id())>=1) { 
				System.out.println("true part");
				throw new  RuntimeException("Detail's already exist with above data. Please change input");
				  }
		 else {
		
		s.getSchool_id().stream().forEach(sid -> {
			s.getJob_type_id().stream().forEach(job -> {
				s.getEmp_type_id().stream().forEach(emptype -> {
					LeavePattern lp = new LeavePattern();
					lp.setActive(s.getActive());
					lp.setCreated_by(s.getCreated_by());
					lp.setCreated_username(s.getCreated_username());

					lp.setYear(s.getYear());
					lp.setLeave_id(s.getLeave_id());
					lp.setSchool_id(sid);
					lp.setEmp_type_id(emptype);

					lp.setJob_type_id(job);
					lp.setLeave_days_permit(s.getLeave_days_permit());
					lp.setSpecal_remarks(s.getSpecial_remarks());

					list.add(lp);
					leave_pattern_repo.save(lp);
				});
			});
		});
		}

		return list;

	}
/*
	public Boolean validationLeavePattern(Integer year, Integer school_id, Integer leave_id, Integer emp_type_id,
			Integer job_type_id) {
		if (leave_pattern_repo.validationLeavePattern(year, school_id, leave_id, emp_type_id, job_type_id) >= 1) {
			return true;
		} else {
			return false;
		}
		// return leave_pattern_repo.validationLeavePattern(year, school_id, leave_id,
		// emp_type_id, job_type_id);
	}
*/
	public LeavePattern get(Integer id) {
		return leave_pattern_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("LeavePattern Not Found:" + id));
	}

	public void delete(Integer id) {
		LeavePattern cc = leave_pattern_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("LeavePattern Not Found:" + id));
		leave_pattern_repo.updateLeavePattern(id);
	}

	public void delete1(Integer id) {
		LeavePattern cc = leave_pattern_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("LeavePattern Not Found:" + id));
		leave_pattern_repo.updateLeavePattern1(id);

	}

	public Map<String, Object> fetchLeavePatternByYearAndSchool(Integer year, Integer school_id) {
		List<String> key = leave_pattern_repo.fetchleavetype();
		Map<String, Object> m1 = new HashMap<String, Object>();

		key.stream().forEach(key1->{
			List<Map<String, Object>> value = leave_pattern_repo.fetchLeavePatternByYearAndSchool(year, school_id,Integer.parseInt(key1));
				m1.put(key1,value);
				
		//	key.stream().forEach(k -> {				
			//		m1.put(key1, value);				
		//	});					
		});
		return m1;
	}
	
	/*public Map<String, Object> fetchLeavePatternByYearAndSchool(Integer year, Integer school_id) {
		List<Integer> key = leave_pattern_repo.fetchleavetype();
		Map<String, Object> m1 = new HashMap<String, Object>();

		key.stream().forEach(key1->{
			List<Map<String, Object>> value = leave_pattern_repo.fetchLeavePatternByYearAndSchool(year, school_id,key1);
				m1.put(key1.toString(),value);
				
		//	key.stream().forEach(k -> {				
			//		m1.put(key1, value);				
		//	});					
		});
		return m1;
	}
	*/

	public void saveLeavePattern1(@Valid LeavePattern leave) {
		leave_pattern_repo.save(leave);
	}

	/*
	 * public List<Map<String, Object>> fetchLeavePatternByYearAndSchool(Integer
	 * year, Integer school_id) { return
	 * leave_pattern_repo.fetchLeavePatternByYearAndSchool(year, school_id); }
	 */
	public List<Map<String, Object>> fetchLeavePatternYearshool() {
		return leave_pattern_repo.fetchLeavePatternYearshool();
	}

	public List<Map<String, Object>> fetchLeavePatternByYear(Integer year, Integer school_id) {
		return leave_pattern_repo.fetchLeavePatternByYear(year, school_id);
	}

	public List<LeavePattern> copyLeavePattern(Integer prev_year, Integer next_year, Integer school_id) {
		List<LeavePattern> list = leave_pattern_repo.fetchLeavePatternByYears(prev_year, school_id);

		for (LeavePattern lp2 : list) {
			if (leave_pattern_repo.validationLeavePattern(next_year, school_id, lp2.getLeave_id(),
					  lp2.getEmp_type_id(), lp2.getJob_type_id())>=1) { 
					System.out.println("true part");
					throw new  RuntimeException("Detail's already exist with above data. Please change input");
					  }
			 else {
			LeavePattern lp1 = new LeavePattern();
			lp1.setActive(lp2.getActive());
			lp1.setCreated_by(lp2.getCreated_by());
			lp1.setCreated_date(lp2.getCreated_date());
			lp1.setCreated_username(lp2.getCreated_username());
			lp1.setEmp_type_id(lp2.getEmp_type_id());
			lp1.setLeave_id(lp2.getLeave_id());
			lp1.setJob_type_id(lp2.getJob_type_id());
			lp1.setLeave_days_permit(lp2.getLeave_days_permit());
			// lp1.setModifiedBy(lp2.getModifiedBy());
			// lp1.setModifiedDate(lp2.getModifiedDate());
			// lp1.setModifiedUsername(lp2.getModifiedUsername());
			lp1.setSchool_id(lp2.getSchool_id());
			lp1.setSpecal_remarks(lp2.getSpecal_remarks());
			lp1.setYear(next_year);
			leave_pattern_repo.save(lp1);
			 }
		}
		return list;
		// return leave_pattern_repo.copyLeavePattern(prev_year, next_year,school_id);
	}
	
	
	

}
