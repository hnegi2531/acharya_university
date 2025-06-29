package com.au.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.EmployeeMedicalHistoryDto;
import com.au.dto.EventCreationDto;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.EmployeeDetails;
import com.au.model.EventCreation;
import com.au.repository.EventCreationRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class EventCreationService {
	
	
	@Autowired
	private EventCreationRepository event_creation_repo;
	
	@Autowired
	private JwtTokenService jwt_service;


	public EventCreation saveEventCreation(EventCreation ec, Date eventStartTime, Date eventEndTime) throws Exception {
		
		Date currentDate = new Date();

        long diffInHours = getTimeDifferenceInHours(currentDate, eventStartTime);
        if (diffInHours < 24) {
            throw new Exception("Event start time should be at least 24 hours from the current time.");
        }

        long diffInDays = getTimeDifferenceInDays(eventStartTime, eventEndTime);
        if (diffInDays > 11) {
            throw new Exception("Event duration should not exceed 10 days.");
        }

        long maxEventDurationFromToday = getTimeDifferenceInDays(currentDate, eventStartTime);
        if (maxEventDurationFromToday > 10) {
            throw new Exception("Event start time should be within the next 10 days from the current date.");
        }

        return event_creation_repo.save(ec);
    }

    private long getTimeDifferenceInHours(Date date1, Date date2) {
        long diffInMillis = date2.getTime() - date1.getTime();
        return TimeUnit.MILLISECONDS.toHours(diffInMillis);
    }

    private long getTimeDifferenceInDays(Date date1, Date date2) {
        long diffInMillis = date2.getTime() - date1.getTime();
        return TimeUnit.MILLISECONDS.toDays(diffInMillis);
    }
	
	public List<EventCreation> listAll() {
		return event_creation_repo.findAll1();
	}
	
	public ResponseEntity<Object> fetchAllEventCreationDetails(Pageable pageable, Object keyword, Integer school_id, String date, Integer room_id, Integer facility_type_id, Integer created_by) {
		Page<Map<String, Object>> response1 = event_creation_repo.fetchAllEventCreationDetails(pageable, keyword,school_id,date,room_id,facility_type_id, created_by);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllEventCreationDetailsWOKeyword(Pageable pageable, Integer school_id, String date, Integer room_id, Integer facility_type_id, Integer created_by) {
		Page<Map<String, Object>> response = event_creation_repo.fetchAllEventCreationDetailsWOKeyword(pageable,school_id,date,room_id,facility_type_id, created_by);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}
	
	
	public ResponseEntity<Object> fetchAllEventCreationForApproverlistAll1(Pageable pageable, Integer userId,
			Object keyword) {
		List<Map<String,Object>> response1 = event_creation_repo.fetchAllEventCreationForApproverlistAll1(pageable,userId, keyword );
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllEventCreationForApproverlistAll2(Pageable pageable1, Integer userId) {
		List<Map<String,Object>> response = event_creation_repo.fetchAllEventCreationForApproverlistAll2(pageable1,userId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}
	
	public EventCreation get(Integer id) {
		return event_creation_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event Creation Is Not Found" + id));
	}
	
	public EventCreation updateEventCreation(EventCreation ec) {
			return event_creation_repo.save(ec);
	}
	
	public void delete(Integer id) {
		event_creation_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Event Creation Not Found:" + id));
		event_creation_repo.update(id);
	}
	
	public void delete1(Integer id) {
		event_creation_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Event Creation Not Found:" + id));
		event_creation_repo.update1(id);
	}
	
	public List<Map<String, Object>> fetchEventDetailsOnSchoolId(Integer school_id) {
		return event_creation_repo.fetchEventDetailsOnSchoolId(school_id.toString(),school_id);
	}

	public List<Map<String, Object>> getEventReportDetails(Integer room_id, LocalDate localDateTime) {
		return event_creation_repo.getEventReportDetails(room_id,localDateTime);
	}


	public EventCreation updateEventCreation(EventCreationDto dto, String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

		EventCreation employee = event_creation_repo.findById(dto.getEvent_id())
		.orElseThrow(()-> new ResourceNotFoundException("Event Creation not found"));
		employee.setSummarize(dto.getSummarize());
		employee.setSummarize_status(dto.getSummarize_status());
		employee.setModified_by(jwtDetails.getUserId());
		employee.setModified_username(jwtDetails.getUserName());
		return event_creation_repo.save(employee);
	}


}
