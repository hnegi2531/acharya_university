package com.au.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.InfrastructureFloors;
import com.au.repository.InfrastructureFloorsRepository;
import com.au.response.ResponseHandler;

@Service
public class InfrastructureFloorsService {
	
	@Autowired
	private InfrastructureFloorsRepository floors_repo;
	
//	public InfrastructureFloors saveFloors(InfrastructureFloors floors) throws Exception {
//			return floors_repo.save(floors);
//	}
	
	
	
	public void createFloors(JwtDetails jwtDetails,Integer block_id, Integer basement,Integer total_no_of_floor,Integer school_id) throws Exception
	{
		if(basement==1) {
			for(int i=0;i<=total_no_of_floor;i++) {
				InfrastructureFloors floor = new InfrastructureFloors();
				if(i==0 || i==1) {
					floor.setFloor_name(i==0?"LF":"GF");
					floor.setFloorcode(i==0?"B":"G");
					floor.setBlock_id(block_id);
					floor.setActive(true);
					floor.setSchool_id(school_id);
					floor.setCreated_by(jwtDetails.getUserId());
					floor.setCreated_username(jwtDetails.getUserName());
				} else {
					floor.setFloor_name(i-1+"F");
					floor.setFloorcode("0"+(i-1));
					floor.setBlock_id(block_id);
					floor.setActive(true);
					floor.setSchool_id(school_id);
					floor.setCreated_by(jwtDetails.getUserId());
					floor.setCreated_username(jwtDetails.getUserName());
				}
				floors_repo.save(floor);
			}
		} else {
			for(int i=0;i<total_no_of_floor;i++) {
				InfrastructureFloors floor = new InfrastructureFloors();
				floor.setFloor_name(i==0?"GF":i+"F");
				floor.setFloorcode(i==0?"G":"0"+i);
				floor.setBlock_id(block_id);
				floor.setActive(true);
				floor.setSchool_id(school_id);
				floor.setCreated_by(jwtDetails.getUserId());
				floor.setCreated_username(jwtDetails.getUserName());
				
				floors_repo.save(floor);
			}
		} 
	  }
		
//		if(basement==1) {
//			InfrastructureFloors floor = new InfrastructureFloors();
//			floor.setFloor_name("LF");
//			floor.setFloorcode("B");
//			floor.setBlock_id(block_id);
//			floor.setActive(true);
//			floors_repo.save(floor);
//		}
//		
//		if(total_no_of_floor!=0){
//			InfrastructureFloors floor = new InfrastructureFloors();
//			if(total_no_of_floor>1){
//				
//				floor.setFloor_name("GF");
//				floor.setFloorcode("G");
//				floor.setBlock_id(block_id);
//				floor.setActive(true);
//				}else{
//
//					floor.setFloor_name("GF");
//					floor.setFloorcode("G");
//					floor.setBlock_id(block_id);
//					floor.setActive(true);
//				}
//			floors_repo.save(floor);
//			for(int i=1;i<=total_no_of_floor-1;i++) {
//				floor.setFloor_name(i+"F");
//				floor.setFloorcode("0"+i);
//				floor.setBlock_id(block_id);
//				floor.setActive(true);
//				floors_repo.save(floor);
//			}
//			
//		}
	
	public List<InfrastructureFloors> getFloors(Integer block_id) {
		return floors_repo.getFloorsByBlockId(block_id);
	}
		
	public void checkForExistingData(Integer block_id) throws Exception {
		if(floors_repo.checkForExistingData(block_id) >=1)
		{
			floors_repo.deleteExistingData(block_id);
		   }
		}
	
	public List<InfrastructureFloors> listAll() {
		return floors_repo.findAll1();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> floors_filtered_response = floors_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, floors_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> floors_sorted_response = floors_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, floors_sorted_response);
	}
	
	public InfrastructureFloors getFloor(Integer id) {
		return floors_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Floor Not Found:" + id));
	}
	
	public InfrastructureFloors saveUpdateFloor(@Valid InfrastructureFloors ib) {
		return floors_repo.save(ib);
	}
	
	public void deactivate(Integer id) {
		InfrastructureFloors ib = floors_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Floor Not Found:" + id));
		floors_repo.updateToDeactivate(id);
	}
	
	public void activate(Integer id) {
		InfrastructureFloors ib = floors_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Floor Not Found:" + id));
		floors_repo.updateToActivate(id);
	}

}
