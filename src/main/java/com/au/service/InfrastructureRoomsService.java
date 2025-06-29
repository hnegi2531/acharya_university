package com.au.service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.InfrastructureRooms;
import com.au.repository.InfrastructureBlocksRepository;
import com.au.repository.InfrastructureFacilityTypeRepository;
import com.au.repository.InfrastructureFloorsRepository;
import com.au.repository.InfrastructureRoomsRepository;
import com.au.response.ResponseHandler;

@Service
public class InfrastructureRoomsService {
	
	@Autowired
	public InfrastructureRoomsRepository rooms_repo;
	
	@Autowired
	private InfrastructureFloorsRepository floors_repo;
	
	@Autowired
	private InfrastructureBlocksRepository blocks_repo;
	
	@Autowired
	private InfrastructureFacilityTypeRepository facility_repo;
	
	public void checkForExistingData(@Valid @RequestBody InfrastructureRooms rooms) throws Exception {
		if(rooms.getNo_of_rooms() < rooms_repo.checkForExistingData(rooms.getBlock_id(),rooms.getSchool_id(),rooms.getFloor_id())) 
		{
			System.out.println(":::::::::::Repost Not Possible:::::::::   "+ rooms_repo.checkForExistingData(rooms.getBlock_id(),rooms.getSchool_id(),rooms.getFloor_id()));
		}
	}

	public List<InfrastructureRooms> saveRooms(JwtDetails jwtDetails,InfrastructureRooms rooms) throws Exception {
		Integer room  = rooms.getNo_of_rooms();
		Integer checkForExistingData = rooms_repo.checkForExistingData(rooms.getBlock_id(),rooms.getSchool_id(),rooms.getFloor_id());
		if(room >= checkForExistingData) 
			
		{
		if(rooms.getNo_of_rooms()<=99)
		{
		List<InfrastructureRooms> list_rooms = new ArrayList<InfrastructureRooms>();
		String bc = blocks_repo.getBlockCode(rooms.getBlock_id());  /* ---------BLOCK CODE -------- */
		String fn = floors_repo.getFloorName(rooms.getFloor_id());  /* ---------FLOOR NAME -------- */
		
		Integer latestRoomCode = rooms_repo.getLatestRoomCode(rooms.getBlock_id(),rooms.getFloor_id(),rooms.getSchool_id());
		if (latestRoomCode == null) {
			latestRoomCode = 0;
		}
		for(int i=latestRoomCode+1;i<=room;i++) {
            InfrastructureRooms rooms2 = new InfrastructureRooms();
            
            rooms2.setNo_of_rooms(i);
            if(i<=9) {                    
                rooms2.setRoomcode(bc+fn+"0"+i);
                }else{
                rooms2.setRoomcode(bc+fn+i);
                }
                rooms2.setActive(rooms.getActive());
                rooms2.setArea(rooms.getArea());
                rooms2.setBlock_id(rooms.getBlock_id());
                rooms2.setDescription(rooms.getDescription());
                rooms2.setFacility_type_id(rooms.getFacility_type_id());
                rooms2.setFloor_id(rooms.getFloor_id());
                rooms2.setSchool_id(rooms.getSchool_id());
                rooms2.setManual_room_no(rooms.getManual_room_no());
                rooms2.setRemarks(rooms.getRemarks());
                rooms2.setShow_in_event(rooms.getShow_in_event());
                rooms2.setStrength(rooms.getStrength());
                rooms2.setCreated_by(jwtDetails.getUserId());
                rooms2.setCreated_username(jwtDetails.getUserName());
                rooms2.setRoom_status(rooms.getRoom_status());
            
            rooms_repo.save(rooms2);
            list_rooms.add(rooms2);
    }
		
		return list_rooms;
	} else {
		throw new Exception("Maximum value for No. Of Rooms is 99 ");
	}
		} else {
			throw new Exception("No. of Rooms provided is less than existing rooms");
		}
	}
	
	public List<InfrastructureRooms> listAll() {
		return rooms_repo.findAll1();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> rooms_filtered_response = rooms_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, rooms_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> rooms_sorted_response = rooms_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, rooms_sorted_response);
	}
	
	public InfrastructureRooms get(Integer id) {
		return rooms_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room Not Found:" + id));
	}
	
	public InfrastructureRooms saveUpdateRoom(@Valid InfrastructureRooms ir) {
	    
	    String f_code = facility_repo.getFacilityCode(ir.getFacility_type_id());


	    String[] roomCodeParts = ir.getRoomcode().split("-");

	   
	    if (roomCodeParts.length > 1) {
	       
	        ir.setRoomcode(roomCodeParts[0] + "-" + f_code); 
	    } else {
	       
	        ir.setRoomcode(ir.getRoomcode() + "-" + f_code);
	    }

	   
	    return rooms_repo.save(ir);
	}

	
	public void deactivate(Integer id) {
		InfrastructureRooms ir = rooms_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room Not Found:" + id));
		rooms_repo.updateToDeactivate(id);
	}
	
	public void activate(Integer id) {
		InfrastructureRooms ir = rooms_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room Not Found:" + id));
		rooms_repo.updateToActivate(id);
	}
	
	
	public List<InfrastructureRooms> getAllActiveRoomsForTimeTable() {
		return rooms_repo.getAllActiveRoomsForTimeTable();
	}

	
	public List<Map<String,Object>> getAllActiveRoomsForTimeTableBsn(Integer time_slot_id,Date from_date,Date to_date, String day) {
		List<Date> dates=daywiseDates(from_date,to_date,day);
	       return rooms_repo.getAllActiveRoomsForTimeTableBsn(time_slot_id,dates,day);
	}
	
	public List<Date> daywiseDates(Date fromDate, Date toDate, String weekDay) {
		List<Date> dates=new ArrayList<Date>(); 
		LocalDate fromLocalDate = LocalDate.fromDateFields(fromDate);
		LocalDate toLocalDate = LocalDate.fromDateFields(toDate);
		long daysDiff = Days.daysBetween(fromLocalDate, toLocalDate).getDays();
		for(int i = 0 ; i<=daysDiff ; i++) {
			//get DayName by date
			Format f = new SimpleDateFormat("EEEE");  
			String dayName = f.format(fromDate); 
			System.out.println("________________________________________________________________ "+dayName);
			if(weekDay.equalsIgnoreCase(dayName)) {
				System.out.println("________________________________________________________________ "+fromDate);
				dates.add(fromDate);
			}
			Calendar c = Calendar.getInstance();
			c.setTime(fromDate);
			c.add(Calendar.DATE, 1);
			fromDate = c.getTime();
		}
		return dates;
	}

	
	public List<Map<String,Object>> getAllActiveRoomsForTimeTableBsn(Integer time_slot_id,Date from_date,Date to_date) {
	       return rooms_repo.getAllActiveRoomsForTimeTableBsn(time_slot_id,from_date,to_date);
	}

	public List<Map<String, Object>> roomsForTimeTableRoomSwapping(Integer time_slot_id, Date date1) {
		return rooms_repo.roomsForTimeTableRoomSwapping(time_slot_id,date1);
	}

	public List<Map<String, Object>> getEventRoomAvailabilityForTimeTable(Integer block_id, Integer floor_id,
			Integer month, Integer year) {
		return rooms_repo.getEventRoomAvailabilityForTimeTable(block_id,floor_id,month,year);
	}


}
