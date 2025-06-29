package com.au.service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.EventBlockedRooms;
import com.au.model.EventCreation;
import com.au.repository.EventBlockedRoomsRepository;
import com.au.repository.EventCreationRepository;
import com.au.response.ResponseHandler;

@Service
public class EventBlockedRoomsService {

	@Autowired
	private EventBlockedRoomsRepository event_blocked_rooms_repo;

	@Autowired
	private EventCreationRepository eventCreationRepo;

	public List<EventBlockedRooms> saveEventBlockedRooms(List<EventBlockedRooms> ebrList) throws Exception {
		for (EventBlockedRooms ebr : ebrList) {
			EventCreation event = eventCreationRepo.findById(ebr.getEvent_id())
					.orElseThrow(() -> new Exception("Event not found"));

			String eventStartTime = event.getEvent_start_time();
			String eventEndTime = event.getEvent_end_time();
			Integer roomId = ebr.getRoom_id();

			Integer count = event_blocked_rooms_repo.existsByRoomIdAndEventStartTimeAndEventEndTime(roomId,
					eventStartTime, eventEndTime);
			if (count >= 1) {
				throw new RuntimeException(
						"The room with ID " + roomId + " is already blocked for the same event time range.");
			}

		}

		return event_blocked_rooms_repo.saveAll(ebrList);
	}

	public List<EventBlockedRooms> listAll() {
		return event_blocked_rooms_repo.findAll1();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = event_blocked_rooms_repo.findAll2(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = event_blocked_rooms_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public EventBlockedRooms get(Integer id) {
		return event_blocked_rooms_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Event Blocked Rooms Is Not Found" + id));
	}

	public EventBlockedRooms updateEventBlockedRooms(EventBlockedRooms ebr) {
		return event_blocked_rooms_repo.save(ebr);
	}

	public void delete(Integer id) {
		event_blocked_rooms_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Event Blocked Rooms Not Found:" + id));
		event_blocked_rooms_repo.update(id);
	}

	public void delete1(Integer id) {
		event_blocked_rooms_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Event Blocked Rooms Not Found:" + id));
		event_blocked_rooms_repo.update1(id);
	}

	public List<Map<String, Object>> getAvailableBlockAndRooms(String eventStartTime, String eventEndTime) throws ParseException {
	    // Use ISO_LOCAL_DATE_TIME since input is "yyyy-MM-dd'T'HH:mm:ss" (without time zone)
	    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	    // Parse timestamps as LocalDateTime (which has NO timezone info)
	    LocalDateTime startTime = parseDate(eventStartTime, formatter);
	    LocalDateTime endTime = parseDate(eventEndTime, formatter);

	    // Convert LocalDateTime to Date in Asia/Kolkata time zone for database compatibility
	    Date startDate = convertToIST(startTime);
	    Date endDate = convertToIST(endTime);

	    return event_blocked_rooms_repo.getAvailableBlockAndRooms(startDate, endDate);
	}

	// Helper method to parse date
	private LocalDateTime parseDate(String dateStr, DateTimeFormatter formatter) throws ParseException {
	    try {
	        return LocalDateTime.parse(dateStr, formatter);
	    } catch (DateTimeParseException e) {
	        throw new ParseException("Unparseable date: " + dateStr, 0);
	    }
	}

	// Convert LocalDateTime to Date in IST (Asia/Kolkata)
	private Date convertToIST(LocalDateTime localDateTime) {
	    // Convert LocalDateTime to ZonedDateTime in Asia/Kolkata
	    ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Kolkata"));
	    return Date.from(zonedDateTime.toInstant());
	}

//	public String checkingAvailableBlockAndRooms(Integer room_id, String eventStartTime, String eventEndTime)
//			throws ParseException {
//		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
//
//		LocalDateTime startTime = parseDate(eventStartTime, formatter);
//		LocalDateTime endTime = parseDate(eventEndTime, formatter);
//
//		Date startDate = convertToIST(startTime);
//		Date endDate = convertToIST(endTime);
//
//		Integer checkingAvailableBlockAndRooms = event_blocked_rooms_repo.checkingAvailableBlockAndRooms(room_id,
//				startDate, endDate);
//
//		if (checkingAvailableBlockAndRooms < 1) {
//			return "eligible to create";
//		}
//
//		return "not eligible to create";
//	}
	
	public String checkingAvailableBlockAndRooms(Integer roomId, String eventStartTime, String eventEndTime) throws ParseException {
	    // Query the repository to check availability (no conversion needed)
	    Integer count = event_blocked_rooms_repo.checkingAvailableBlockAndRooms(roomId, eventStartTime, eventEndTime);

	    // Check the availability
	    if (count <= 0) {
	        return "eligible to create"; // No overlapping events, eligible to create
	    } else {
	        return "This room is already booked !!!"; // There are overlapping events
	    }
	}
	
	
}
