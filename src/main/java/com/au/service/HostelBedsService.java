package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.HostelBedAssignment;
import com.au.model.HostelBeds;
import com.au.model.HostelRoomAssignment;
import com.au.model.HostelRooms;
import com.au.repository.HostelBedAssignmentRepository;
import com.au.repository.HostelBedsRepository;
import com.au.repository.HostelBlocksRepository;
import com.au.repository.HostelFloorRepository;
import com.au.repository.HostelRoomAssignmentRepository;
import com.au.repository.HostelRoomsRepository;
import com.au.response.ResponseHandler;

@Service
public class HostelBedsService {

	@Autowired
	private HostelBedsRepository hostelBedsRepository;
	
	@Autowired
	private HostelBlocksRepository hostelBlocksRepository;

	@Autowired
	private HostelFloorRepository hostelFloorRepository;
	
	@Autowired
	private HostelRoomsRepository hostelRoomsRepository;
	
	@Autowired
	private HostelRoomAssignmentRepository hostelRoomAssignmentRepository;
	
	@Autowired
	private HostelBedAssignmentRepository hostelBedAssignmentRepository;

	
	public List<HostelBeds> listAll() {
		return hostelBedsRepository.findAll1();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = hostelBedsRepository.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = hostelBedsRepository.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public HostelBeds saveHostelBeds(HostelBeds s) {
		return hostelBedsRepository.save(s);

	}

	public HostelBeds get(Integer id) {
		return hostelBedsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("HostelBeds Not Found:" + id));
	}

	public void delete(Integer id) {
		HostelBeds cc = hostelBedsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelBeds Not Found:" + id));
		hostelBedsRepository.update(id);
	}

	public void delete1(Integer id) {
		HostelBeds cc = hostelBedsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelBeds Not Found:" + id));
		hostelBedsRepository.update1(id);

	}

	public List<Map<String, Object>> getHosteBedDetails(){
		return hostelBedsRepository.getHosteBedDetails();
	}
	
	public  List<Map<String, Object>> getCountOfFloorsRoomsBeds() {
		
		return hostelBedsRepository.getCountOfFloorsRoomsBeds();
	}
	
	public HashMap<Integer, Object> getAllBedsDetailsByHostelsBlockId(Integer hostelsBlockId) {
		HashMap<Integer, Object> m = new HashMap<Integer, Object>();
		HashMap<Integer, Object> m1 = new HashMap<Integer, Object>();
		
		List<Integer> floor_ids = hostelBedsRepository.getListOfFloorIds(hostelsBlockId);
		floor_ids.stream().forEach(f -> {
			List<Integer> room_ids = hostelBedsRepository.getListOfRoomIds(f);
			room_ids.stream().forEach(r -> {
				List<HashMap<String, Object>> bed_details = hostelBedsRepository.getAllBedDetails(r);
				m.put(r, bed_details);
			});
			m1.put(f, m);
		});
		return m1;
	}

	public Map<Object, List<HashMap<String, Object>>> hostelBedsByHostelBlockAndFloor(Integer hostelsBlockId,Integer hostelsFloorId,Integer roomTypeId) {
		Map<Object, List<HashMap<String, Object>>> bedsDetails= hostelBedsRepository.hostelBedsByHostelBlockAndFloor(hostelsBlockId,hostelsFloorId,roomTypeId).stream().collect(Collectors.groupingBy(e -> e.get("roomName")));
//		bedsDetails.values().forEach(list -> {
//			list.stream().forEach(map -> {
//				Optional<HostelRooms> hostelRoom=hostelRoomsRepository.findById((Integer)map.get("hostelRoomId"));
//				HostelRoomAssignment hostelRoomAssignment=hostelRoomAssignmentRepository.findByHostelRoomAndActiveTrue(hostelRoom.get());
//				if(ObjectUtils.isNotEmpty(hostelRoomAssignment)) {
//					map.put("BedStatus", hostelRoomAssignment.getStatus());
//				}else {
//					Optional<HostelBeds> hostelBeds=hostelBedsRepository.findById((Integer)map.get("hostelBedId"));
//					HostelBedAssignment hostelBedAssignment=hostelBedAssignmentRepository.findByHostelRoomAndHostelBedAndActiveTrue(hostelRoom.get(),hostelBeds.get());
//					if(ObjectUtils.isNotEmpty(hostelBedAssignment)) {
//						map.put("BedStatus", "Occupied");
//					}else {
//						map.put("BedStatus", "Vacant");
//					}
//				}
//			});
//		});
		
		return bedsDetails;
	}
	
	public Map<Object, List<HashMap<String, Object>>> unassignedBedDetails() {
		Map<Object, List<HashMap<String, Object>>> bedsDetails= hostelBedsRepository.unassignedBedDetails().stream().collect(Collectors.groupingBy(e -> e.get("roomName")));
		return bedsDetails;
	}

	public Map<Object, Map<Object, List<HashMap<String, Object>>>> hostelBedsByHostelBlockId(Integer hostelsBlockId, Integer roomTypeId) {
		
		return hostelBedsRepository.hostelBedsByHostelBlockId(hostelsBlockId,roomTypeId).stream().collect(Collectors.groupingBy(e -> e.get("floorName"),Collectors.groupingBy(e -> e.get("roomName"))));
	}
	

}
