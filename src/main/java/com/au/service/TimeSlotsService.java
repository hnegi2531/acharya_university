package com.au.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.JwtDetails;
import com.au.dto.TimeSlotsDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.TimeSlots;
import com.au.repository.TimeSlotsRepository;
import com.au.response.ResponseHandler;

@Service
public class TimeSlotsService {
	
	@Autowired
	private TimeSlotsRepository time_slots_repo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	public List<TimeSlots> saveTimeSlots(TimeSlotsDto tsd,String jwtToken) throws Exception {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		List<TimeSlots> ts_list = new ArrayList<TimeSlots>();
		List<TimeSlots> ts_list1=time_slots_repo.findAll1();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");

		// Parsing the Time Period
		Date date1 = simpleDateFormat.parse(tsd.getStarting_time());
		Date date2 = simpleDateFormat.parse(tsd.getEnding_time());

		
		Long diff = date2.getTime()-date1.getTime();
		long diffSeconds = diff / 1000 % 60;
	    long diffMinutes = diff / (60 * 1000) % 60;
	    long diffHours = diff / (60 * 60 * 1000) % 24;
	    Long total_minutes = (diffHours*60)+diffMinutes;
	    System.out.format("Difference : %d hours, %d minutes, %d seconds",
	            diffHours, diffMinutes, diffSeconds);
	    System.out.println("((((((((((((((((-----)))))))))))))))) "+total_minutes);
		
				
		tsd.getSchool_id().stream().forEach(sc -> {
			if (time_slots_repo.countTimeSlots(tsd.getStarting_time(), tsd.getEnding_time(), sc) >= 1) {
//			if(ts_list1.contains(tsd.getStarting_time(), tsd.getEnding_time(), sc)) {
				throw new RuntimeException("School With This Time Slots Already Exist");
			} else {
				TimeSlots ts = new TimeSlots();

				ts.setStarting_time(tsd.getStarting_time());
				ts.setEnding_time(tsd.getEnding_time());
				ts.setStarting_time_for_fornted(tsd.getStarting_time_for_fornted());
				ts.setEnding_time_for_fornted(tsd.getEnding_time_for_fornted());
				ts.setSchool_id(sc);
				ts.setCreated_by(jwtDetails.getUserId());
				ts.setCreated_username(jwtDetails.getUserName());
				ts.setActive(tsd.getActive());
				ts.setClass_time_table(tsd.getClass_time_table());
				ts.setDuration(total_minutes);
				time_slots_repo.save(ts);
				ts_list.add(ts);
			}
		});
		return ts_list;

	}
	
	public List<TimeSlots> listAll() {
		return time_slots_repo.findAll1();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = time_slots_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = time_slots_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public TimeSlots get(Integer id) {
		return time_slots_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Time Slots Not Found:" + id));
	}
	
	public TimeSlots updateTimeSlots(TimeSlots ts) throws ParseException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");

		// Parsing the Time Period
		Date date1 = simpleDateFormat.parse(ts.getStarting_time());
		Date date2 = simpleDateFormat.parse(ts.getEnding_time());

		
		Long diff = date2.getTime()-date1.getTime();
		long diffSeconds = diff / 1000 % 60;
	    long diffMinutes = diff / (60 * 1000) % 60;
	    long diffHours = diff / (60 * 60 * 1000) % 24;
	    Long total_minutes = (diffHours*60)+diffMinutes;
	    System.out.format("Difference : %d hours, %d minutes, %d seconds",
	            diffHours, diffMinutes, diffSeconds);
	    System.out.println("((((((((((((((((-----)))))))))))))))) "+total_minutes);
	    
		if (time_slots_repo.countTimeSlotsForUpdate(ts.getTime_slots_id(), ts.getStarting_time(), ts.getEnding_time(),
				ts.getSchool_id()) >= 1) {
			throw new RuntimeException("School With This Time Slots Already Exist");
		} else {

			ts.setDuration(total_minutes);
			return time_slots_repo.save(ts);
		}

	}
	
	public void delete(Integer id) {
		time_slots_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Time Slots Not Found:" + id));
		time_slots_repo.update(id);
	}
	
	public void delete1(Integer id) {
		time_slots_repo.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Time Slots Not Found:" + id));
		time_slots_repo.update1(id);

	}
	
	
	public List<TimeSlots> listAll1() {
		return time_slots_repo.findAll11();
	}
	
//	public List<TimeSlots> getTimeSlotMinAndMax() {
//		return time_slots_repo.getTimeSlotMinAndMax();
//	}

}
