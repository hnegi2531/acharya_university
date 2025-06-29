package com.au.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.InfrastructureFacilityType;
import com.au.model.JobType;
import com.au.repository.InfrastructureFacilityTypeRepository;
import com.au.response.ResponseHandler;

@Service
public class InfrastructureFacilityTypeService {
	
	@Autowired
	private InfrastructureFacilityTypeRepository facility_repo;
	
	public InfrastructureFacilityType saveFacilityType(InfrastructureFacilityType facility) throws Exception {
		if (facility_repo.getCountFacilityType(facility.getFacility_type_name()) >= 1)
			throw new Exception("Facility Name Already Exist");
		else if (facility_repo.getCountFacilityShortName(facility.getFacility_short_name()) >= 1)
			throw new Exception("Short Name Already Exist");
		else if (facility_repo.getCountFacilityCode(facility.getFacility_code()) >= 1)
			throw new Exception("Facility Code Already Exist");
		else {
			facility_repo.save(facility);
		}
		return facility;
	}
	
	public List<InfrastructureFacilityType> listAll() {
		return facility_repo.findAll1();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> facility_filtered_response = facility_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, facility_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> facility_sorted_response = facility_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, facility_sorted_response);
	}
	
	public InfrastructureFacilityType get(Integer id) {
		return facility_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Facilty Type Not Found:" + id));
	}
	
	public InfrastructureFacilityType saveUpdatefacilitytype(@Valid InfrastructureFacilityType ift) {
		return facility_repo.save(ift);
	}
	
	public void deactivate(Integer id) {
		InfrastructureFacilityType ift = facility_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Facilty Type Not Found:" + id));
		facility_repo.updateToDeactivate(id);
	}
	
	public void activate(Integer id) {
		InfrastructureFacilityType ift = facility_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Facilty Type Not Found:" + id));
		facility_repo.updateToActivate(id);
	}

	public List<Map<String, Object>> getFacilityTypeBasedOnEvent() {
		return facility_repo.getFacilityTypeBasedOnEvent();
	}
	
	
	public List<Map<String, Object>> getFacilityTypeForTimeTable() {
		return facility_repo.getFacilityTypeForTimeTable();
	}

	
	
//	public Map<String, Object> getEventRoomAvailability(Integer facility_type_id, LocalDate startDate, LocalDate endDate) {
//	    // Fetch raw data from the database
//	    List<Map<String, Object>> rawData = facility_repo.getEventRoomAvailability(facility_type_id, startDate, endDate);
//	    
//	    List<Map<String, Object>> rawData1 = facility_repo.getEventRoomAvailability1(facility_type_id);
//
//	    // Prepare the final output
//	    Map<String, Object> response = new HashMap<>();
//
//	    
//	    // Create a data map to hold the final data
//	    Map<String, Object> dataMap = new HashMap<>();
//	    
//	    if (!rawData.isEmpty()) {
//	    	System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " +rawData);
//	        // Group the raw data by block_name, facility_short_name, block_short_name, roomcode, facility_type_name
//	        Map<Map<String, Object>, List<Map<String, Object>>> groupedData = rawData.stream()
//	                .collect(Collectors.groupingBy(data -> {
//	                    Map<String, Object> roomAttributes = new HashMap<>();
//	                    roomAttributes.put("block_name", data.get("block_name"));
//	                    roomAttributes.put("facility_short_name", data.get("facility_short_name"));
//	                    roomAttributes.put("block_short_name", data.get("block_short_name"));
//	                    roomAttributes.put("roomcode", data.get("roomcode"));
//	                    roomAttributes.put("facility_type_name", data.get("facility_type_name"));
//	                    return roomAttributes;
//	                }));
//
//	        List<Map<String, Object>> result = new ArrayList<>();
//
//	        for (Map.Entry<Map<String, Object>, List<Map<String, Object>>> entry : groupedData.entrySet()) {
//	            // Create a new map for each block/group
//	            Map<String, Object> groupedRoomData = new HashMap<>(entry.getKey());  // copy room attributes
//	            groupedRoomData.put("roomId", entry.getValue());  // add the list of room events
//	            result.add(groupedRoomData);  // add to the result list
//	        }
//	        
//	        dataMap.put("data", result);  // Add grouped results to data map
//	    } else {
//	        // If rawData is empty, return rawData1 instead
//	    	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +rawData1);
//	        dataMap.put("data", rawData1);  // Just put rawData1 as is
//	    }
//
//	    // Set the constructed data map in the response
//	    response.put("data", dataMap);  // Assign the data map to the response
//
//	    return response;  // Return the final response
//	}
	
	public Map<String, Object> getEventRoomAvailability(Integer facility_type_id, LocalDate startDate, LocalDate endDate) {
	    // Fetch all rooms (occupied + unoccupied)
	    List<Map<String, Object>> allRooms = facility_repo.getEventRoomAvailability1(facility_type_id);

	    // Fetch only occupied rooms (those with events)
	    List<Map<String, Object>> occupiedRooms = facility_repo.getEventRoomAvailability(facility_type_id, startDate, endDate);

	    // Group occupied rooms by room code
	    Map<String, List<Map<String, Object>>> occupiedRoomsMap = occupiedRooms.stream()
	        .collect(Collectors.groupingBy(room -> (String) room.get("roomcode")));

	    List<Map<String, Object>> finalRoomList = new ArrayList<>();

	    // Iterate over all rooms and attach event data if available
	    for (Map<String, Object> room : allRooms) {
	        // Create a new modifiable HashMap to avoid the TupleBackedMap error
	        Map<String, Object> roomCopy = new HashMap<>(room);

	        String roomCode = (String) roomCopy.get("roomcode");

	        // Attach events if available, otherwise an empty list
	        roomCopy.put("roomId", occupiedRoomsMap.getOrDefault(roomCode, new ArrayList<>()));

	        finalRoomList.add(roomCopy);
	    }

	    // Prepare response
	    Map<String, Object> response = new HashMap<>();
	    response.put("data", finalRoomList);

	    return response;
	}



}
