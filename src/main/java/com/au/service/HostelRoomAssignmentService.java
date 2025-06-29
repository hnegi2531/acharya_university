package com.au.service;

import java.io.IOException;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.HostelRoomAssignmentDto;
import com.au.dto.JwtDetails;
import com.au.model.HostelRoomAssignment;
import com.au.model.HostelRooms;
import com.au.repository.HostelBedsRepository;
import com.au.repository.HostelRoomAssignmentRepository;
import com.au.repository.HostelRoomsRepository;
import com.au.response.ResponseHandler;

@Service
public class HostelRoomAssignmentService {
	
	Logger log=LoggerFactory.getLogger(HostelRoomAssignmentService.class);
	
	@Autowired
	private HostelRoomAssignmentRepository hostelRoomAssignmentRepository;
	
	@Autowired
	private HostelRoomsRepository hostelRoomsRepository;
	
	@Autowired
	private HostelBedsRepository hostelBedsRepository;
	
	@Autowired
	private JwtTokenService jwtService;
	
	private final ModelMapper modelMapper = new ModelMapper();

	public ResponseEntity<Object> hostelRoomAssignment(@Valid HostelRoomAssignmentDto hostelRoomAssignmentDto,
			String jwtToken) {

		try {
			HostelRooms hostelRoom=hostelRoomsRepository.findById(hostelRoomAssignmentDto.getHostelRoomId()).get();
			if(hostelRoomAssignmentRepository.vacantStatusOfRoom(hostelRoom) == null || hostelRoomAssignmentRepository.vacantStatusOfRoom(hostelRoom)) {
				JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
				HostelRoomAssignment hostelRoomAssignment=modelMapper.map(hostelRoomAssignmentDto,HostelRoomAssignment.class);
				hostelRoomAssignment.setCreatedBy(jwtDetails.getUserId());
				hostelRoomAssignment.setCreatedUsername(jwtDetails.getUserName());
				hostelRoomAssignment.setHostelRoom(hostelRoom);
				hostelRoomAssignmentRepository.save(hostelRoomAssignment);
				hostelBedsRepository.updateBedStatusByRoomId(hostelRoomAssignmentDto.getStatus(),hostelRoomAssignmentDto.getHostelRoomId());
				return	ResponseHandler.generateResponse(true, HttpStatus.CREATED,"Created Successfully");
			}else {
				return	ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR,"Room is already occupied!!");
			}
		} catch (IOException e) {
			return	ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
		}
	}
}

