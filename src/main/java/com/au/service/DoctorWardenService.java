package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.Department;
import com.au.model.DoctorWarden;
import com.au.repository.DoctorWardenRepository;
import com.au.response.ResponseHandler;


@Service
public class DoctorWardenService {

	@Autowired
	private DoctorWardenRepository s_repo;
	
	public List<DoctorWarden> listAll() {
		return s_repo.findAll1();
	}
	
//	public List<DoctorWarden> listAll1() {
//		return s_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = s_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll3(Pageable pageable) {
		
		Page<Object> response = s_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public DoctorWarden saveDoctor(DoctorWarden s) {
		if(s_repo.getDoctorWardenType(s.getUser_id()) == null) { 
			return s_repo.save(s);
		}else if(s_repo.getDoctorWardenType(s.getUser_id()).equals(s.getDoctorWardenType())){
			if(s_repo.getDoctorWardenType(s.getUser_id()).equals("Doctor")) {
				if(s_repo.countOfDoctor(s.getUser_id(),s.getHostel_block_id())>=1) {
					throw new RuntimeException("User Already Exist In the Block");
				} else {
					return s_repo.save(s);
				}
				
			}else {
				if(s_repo.countOfWarden(s.getUser_id(),s.getHostel_block_id(),s.getHostel_floor_id())>=1) {
					throw new RuntimeException("User Already Exist In The Floor");
				} else {
					return s_repo.save(s);
				}
			}
			
		}else{
			throw new RuntimeException("User Already Assigned With "+ s_repo.getDoctorWardenType(s.getUser_id()));
		}

	}
	
	public DoctorWarden saveDoctorWarden(DoctorWarden dw) {
//		if(dw.getDoctorWardenType().equals("Warden")) {	
//			if(s_repo.existsByUserId(dw.getUser_id())) 
//				throw new RuntimeException("Name already assigned!!!");
//			else if(s_repo.getWardenBlockAndFloorForUpdate(dw.getBlock_id(),dw.getFloor_id(),dw.getDoctorId(),dw.getDoctorWardenType()) >= 1) 
//				throw new RuntimeException("Warden already assigned for the given block And floor");	
//		}
//		else {
//			if(s_repo.existsByUserId(dw.getUser_id())) 
//				throw new RuntimeException("Name already assigned!!!");
//			else if(s_repo.getDoctorBlockAndFloorForUpdate(dw.getBlock_id(),dw.getDoctorId(),dw.getDoctorWardenType()) >= 1)
//				throw new RuntimeException("Doctor already assigned for the given block");
//		 }
		return s_repo.save(dw);
	}

	public DoctorWarden get(Integer id) {
		return s_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("DoctorWarden Not Found:" + id));
	}

	public void delete(Integer id) {
		DoctorWarden cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DoctorWarden Not Found:" + id));
		s_repo.update(id);
	}

	public void delete1(Integer id) {
		DoctorWarden cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DoctorWarden Not Found:" + id));
		s_repo.update1(id);

	}

	public List<DoctorWarden> fetchWardens(){
		return s_repo.fetchWardens();
	}

	public List<Map<String, Object>> fetchUnassignedUsers() {
		return s_repo.fetchUnassignedUsers();
	}
	
	public List<Map<String, Object>> fetchDoctorUnassignedBlocks() {
		return s_repo.fetchDoctorUnassignedBlocks();
	}
	
	public List<Map<String, Object>> UnassignedBlocksWithWarden() {
		return s_repo.UnassignedBlocksWithWarden();
	}
	
	public List<Map<String, Object>> UnassignedFloorsWithWarden(Integer block_id) {
		return s_repo.UnassignedFloorsWithWarden(block_id);
	}
	
	public List<HashMap<String, Object>> floorsAssignedToWarden(Integer user_id) {
		return s_repo.floorsAssignedToWarden(user_id);
	}

}
