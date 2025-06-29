package com.au.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.au.model.HostelRooms;
import com.au.repository.HostelBedAssignmentRepository;
import com.au.repository.HostelBedsRepository;
import com.au.repository.HostelBlocksRepository;
import com.au.repository.HostelFloorRepository;
import com.au.repository.HostelRoomsRepository;
import com.au.response.ResponseHandler;

@Service
public class HostelRoomsService {

	@Autowired
	private HostelRoomsRepository hostelRoomsRepository;

	@Autowired
	private HostelBedsRepository hostelBedsRepository;
	
	@Autowired
	private HostelBlocksRepository hostelBlocksRepository;

	@Autowired
	private HostelFloorRepository hostelFloorRepository;
	
	@Autowired
	private HostelBedAssignmentRepository hostelBedAssignmentRepository;
	
	
	public List<HostelRooms> listAll() {
		return hostelRoomsRepository.findAll1();
	}
	
//	public List<HostelRooms> listAll1() {
//		return hostelRoomsRepository.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = hostelRoomsRepository.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = hostelRoomsRepository.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public List<HostelRooms> saveHostelRooms(HostelRooms s) {
		Integer rooms_count=hostelRoomsRepository.countRoomsOnBlockFloor(s.getHostelsBlockId(),s.getHostelsFloorId());
		String block_short_name = hostelBlocksRepository.getBlockShortName(s.getHostelsBlockId()).substring(0, 3);
		String[] floor_short_name= hostelFloorRepository.getFloorName(s.getHostelsFloorId()).split(" ");
		List<HostelRooms> list_hostel_rooms=new ArrayList<>();
		if(rooms_count < 99 && s.getRoom_creation_number() <= 99 && rooms_count+s.getRoom_creation_number() <= 99) {
			for(Integer i=rooms_count;i< rooms_count+s.getRoom_creation_number() ;i++) {
				HostelRooms hr=new HostelRooms();
				hr.setRoomName(block_short_name + floor_short_name[0].charAt(floor_short_name[0].length()-2) + "F" +String.format("%02d", i+1) + "R");  /* bloGF01R  */
				hr.setStandardAccessories(s.getStandardAccessories());
				hr.setRoomTypeId(s.getRoomTypeId());
				hr.setTemplateId(s.getTemplateId());
				hr.setHostelsBlockId(s.getHostelsBlockId());
				hr.setHostelsFloorId(s.getHostelsFloorId());
				hr.setCreatedBy(s.getCreatedBy());
				hr.setCreatedUsername(s.getCreatedUsername());
				hr.setActive(s.getActive());
				hr.setRoom_creation_number(s.getRoom_creation_number());
				hr.setAc_year_id(s.getAc_year_id());
				list_hostel_rooms.add(hr);
				hostelRoomsRepository.save(hr);
				
			}
			Integer totalNoOfRooms = rooms_count+s.getRoom_creation_number();
			hostelBlocksRepository.updateTotalNoOfRooms(s.getHostelsBlockId(), totalNoOfRooms);
		}else {
			throw new RuntimeException("Maximum Rooms Already Created Or Room Creation Number is Exceeding Creation Limit");
		}
		
		Integer roomType = s.getRoomTypeId();
		
		list_hostel_rooms.stream().forEach(hr -> {
			int count=1;
			for(int i=0;i<roomType;i++) {
				HostelBeds hb = new HostelBeds();
				hb.setBedName(hr.getRoomName()+""+count+"B"+"-"+roomType);
				hb.setHostelsBlockId(s.getHostelsBlockId());
				hb.setHostelsFloorId(s.getHostelsFloorId());
				hb.setHostelsRoomId(hr.getHostelRoomId());
				hb.setCreatedBy(s.getCreatedBy());
				hb.setCreatedUsername(s.getCreatedUsername());
				hb.setActive(s.getActive());
				hb.setAc_year_id(s.getAc_year_id());
				count++;
				hostelBedsRepository.save(hb);
			}
		});
		
		return list_hostel_rooms;
	}

	public ResponseEntity<Object> updateHostelRooms(HostelRooms hostelRooms) {
		HostelRooms rooms=get(hostelRooms.getHostelRoomId());
		if(rooms.getRoomTypeId() != hostelRooms.getRoomTypeId()) {
			List<HostelBedAssignment> studentBedAssignment=hostelBedAssignmentRepository.findByHostelRoomAndCancelledRemarksIsNullAndActiveTrue(hostelRooms);
			if(ObjectUtils.isEmpty(studentBedAssignment)) {
				if(rooms.getRoomTypeId() > hostelRooms.getRoomTypeId()) {
					List<HostelBeds> bedsDetails=hostelBedsRepository.findByHostelsRoomId(hostelRooms.getHostelRoomId());
					bedsDetails.stream().forEach(hb -> {
						String[] bedNameSplit=hb.getBedName().toString().split("B-");
						Integer bedNumber=Integer.parseInt(String.valueOf(bedNameSplit[0].charAt(bedNameSplit[0].length()-1)));
						if( bedNumber > hostelRooms.getRoomTypeId()) {
							hb.setActive(false);
						}else {
							String[] newBedNAme=hb.getBedName().split("-");
							hb.setBedName(newBedNAme[0]+"-"+hostelRooms.getRoomTypeId());
						}
					});
					hostelBedsRepository.saveAll(bedsDetails);
				} else {
					List<HostelBeds> bedsDetails=hostelBedsRepository.findByHostelsRoomId(hostelRooms.getHostelRoomId());
					bedsDetails.stream().forEach(hb -> {
						String[] newBedNAme=hb.getBedName().split("-");
						hb.setBedName(newBedNAme[0]+"-"+hostelRooms.getRoomTypeId());
					});
					hostelBedsRepository.saveAll(bedsDetails);
					for(int i=bedsDetails.size()+1 ;i <= bedsDetails.size() + (hostelRooms.getRoomTypeId() - rooms.getRoomTypeId());i++) {
						HostelBeds hb = new HostelBeds();
						hb.setBedName(hostelRooms.getRoomName()+""+i+"B"+"-"+hostelRooms.getRoomTypeId());
						hb.setHostelsBlockId(hostelRooms.getHostelsBlockId());
						hb.setHostelsFloorId(hostelRooms.getHostelsFloorId());
						hb.setHostelsRoomId(hostelRooms.getHostelRoomId());
						hb.setCreatedBy(hostelRooms.getModifiedBy());
						hb.setCreatedUsername(hostelRooms.getModifiedUsername());
						hb.setActive(hostelRooms.getActive());
						hostelBedsRepository.save(hb);
					}

				}
			}else {
				String studentAuids=studentBedAssignment.stream().map(e -> e.getStudent().getAuid()).collect(Collectors.joining(","));
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "Student "+studentAuids + " is Assigned to room, Occupency type can't be changed");
			}
		}
		 hostelRoomsRepository.save(hostelRooms);
		 return ResponseHandler.generateResponse(true, HttpStatus.OK, "Updated Successfully !!");

	}
	public HostelRooms get(Integer id) {
		return hostelRoomsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("HostelRooms Not Found:" + id));
	}

	public void delete(Integer id) {
		HostelRooms cc = hostelRoomsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelRooms Not Found:" + id));
		hostelRoomsRepository.updateAcademicWorkLoad(id);
	}

	public void delete1(Integer id) {
		HostelRooms cc = hostelRoomsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelRooms Not Found:" + id));
		hostelRoomsRepository.updateAcademicWorkLoad1(id);

	}


	public List<Map<String, Object>> getAllRoomDetails() {
		return hostelRoomsRepository.getAllRoomDetails();
	}

	
	public List<HashMap<String, Object>> allHostelOnBlockAndFloor(Integer hostelsBlockId,Integer hostelsFloorId) {
		return hostelRoomsRepository.allHostelOnBlockAndFloor(hostelsBlockId,hostelsFloorId);
	}


	public List<Map<String, Object>> getAllHostelBedDetails(Integer hostelRoomId) {
		return hostelRoomsRepository.getAllHostelBedDetails(hostelRoomId);
	}

	public List<HashMap<String, Object>> fetchAllUnassignedRoom() {
		
		return hostelRoomsRepository.fetchAllUnassignedRoom();
	}

}
