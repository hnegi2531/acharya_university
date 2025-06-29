package com.au.service;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.InternalRoomDto;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.InternalRoom;
import com.au.repository.InfrastructureRoomsRepository;
import com.au.repository.InternalRoomRepository;
import com.au.response.ResponseHandler;

@Service
public class InternalRoomService {
	
	@Autowired
	private InternalRoomRepository ir_repo;
	
	@Autowired
	public InfrastructureRoomsRepository rooms_repo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	private final ModelMapper modelMapper = new ModelMapper();

	public InternalRoom saveInternalRoom(InternalRoomDto internalRoomDto, String jwtToken) throws Exception {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		InternalRoom newInternalRoom = modelMapper.map(internalRoomDto, InternalRoom.class);
		
		newInternalRoom.setCreated_by(jwtDetails.getUserId());
		newInternalRoom.setCreated_username(jwtDetails.getUserName());
		
		newInternalRoom.setRooms(rooms_repo.findById(internalRoomDto.getRoom_id()).get());
		
			return ir_repo.save(newInternalRoom);
			
		}
	
	
	 public List<InternalRoom> listAllActiveInternalRoom() {
			List<InternalRoom> itts=ir_repo.findAll();
			return itts;
		}
	    
	 
	 public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
			Page<Map<String, Object>> oc_filtered_response = ir_repo.getAllDataFilteredByKeyword(pageable, keyword);
			return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, oc_filtered_response);
		}

		public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
			Page<Map<String, Object>> oc_sorted_response = ir_repo.getAllSortedData(pageable);
			return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, oc_sorted_response);
		}
	    
		
	public InternalRoom get(Integer id) {
			return ir_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("InternalRoom Not Found:" + id));
		}
		
	public InternalRoom updateInternalRoom(InternalRoomDto internalRoomDto, String jwtToken) throws Exception {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		InternalRoom newInternalRoom = modelMapper.map(internalRoomDto, InternalRoom.class);

		newInternalRoom.setModified_by(jwtDetails.getUserId());
		newInternalRoom.setModified_username(jwtDetails.getUserName());

		newInternalRoom.setRooms(rooms_repo.findById(internalRoomDto.getRoom_id()).get());
		return ir_repo.save(newInternalRoom);
	}

	
	public void delete(Integer id) {
		InternalRoom itt = ir_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("InternalRoom Not Found:" + id));
		ir_repo.updateInternalRoom(id);
	}

	public void delete1(Integer id) {
		InternalRoom itt = ir_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("InternalRoom Not Found:" + id));
		ir_repo.updateInternalRoom1(id);
	}
	
}
