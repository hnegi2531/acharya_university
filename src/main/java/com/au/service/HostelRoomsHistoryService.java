package com.au.service;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.JwtDetails;
import com.au.model.HostelRooms;
import com.au.model.HostelRoomsHistory;
import com.au.repository.HostelRoomsHistoryRepository;
import com.au.repository.HostelRoomsRepository;
import com.au.response.ResponseHandler;

@Service
public class HostelRoomsHistoryService {
	
	@Autowired
	private HostelRoomsHistoryRepository hostelRoomsHistoryRepository;
	
	@Autowired
	private JwtTokenService jwtService;
	
	private final ModelMapper modelMapper=new ModelMapper();

	public ResponseEntity<Object> saveHostelRoomsHistory(@Valid HostelRooms hostelRooms, String jwtToken) {
		try {
			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
			HostelRoomsHistory hostelRoomsHistory=modelMapper.map(hostelRooms, HostelRoomsHistory.class);
			hostelRoomsHistory.setCreatedBy(jwtDetails.getUserId());
			hostelRoomsHistory.setCreatedUsername(jwtDetails.getUserName());
			hostelRoomsHistoryRepository.save(hostelRoomsHistory);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "History Created Successfully !!");
		}catch (Exception e){
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	

}
