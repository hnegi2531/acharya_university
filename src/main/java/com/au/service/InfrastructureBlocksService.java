package com.au.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.InfrastructureBlocks;
import com.au.model.InfrastructureRooms;
import com.au.repository.InfrastructureBlocksRepository;
import com.au.response.ResponseHandler;

@Service
public class InfrastructureBlocksService {
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private InfrastructureBlocksRepository blocks_repo;
	
	@Autowired
	private InfrastructureFloorsService floors_ser;
	
	public InfrastructureBlocks saveBlocks(JwtDetails jwtDetails,InfrastructureBlocks blocks) throws Exception {
		if (blocks_repo.getCountBlockName(blocks.getBlock_name()) >= 1)
			throw new Exception("Block Name Already Exist");
		else if (blocks_repo.getCountBlockShortName(blocks.getBlock_short_name()) >= 1)
			throw new Exception("Short Name Already Exist");
		else if (blocks_repo.getCountBlockCode(blocks.getBlockcode()) >= 1)
			throw new Exception("Block Code Already Exist");
		else if(blocks.getTotal_no_of_floor() > 9){
			throw new Exception("Maximum Value for Total No. Of Floors is 9");
			
		}else { 
			
		
		InfrastructureBlocks blockssss = blocks_repo.save(blocks);
			floors_ser.createFloors(jwtDetails, blocks.getBlock_id(),blocks.getBasement(),blocks.getTotal_no_of_floor(),blocks.getSchool_id());
			return blockssss;
		}
	}
	
	public List<InfrastructureBlocks> listAll() {
		return blocks_repo.findAll1();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> blocks_filtered_response = blocks_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, blocks_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> blocks_sorted_response = blocks_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, blocks_sorted_response);
	}
	
	
	
	public InfrastructureBlocks getBlock(Integer id) {
		return blocks_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Block Not Found:" + id));
	}
	
	public InfrastructureBlocks saveUpdateBlock(@Valid InfrastructureBlocks ib) {
		return blocks_repo.save(ib);
	}
	
	public void deactivate(Integer id) {
		InfrastructureBlocks ib = blocks_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Block Not Found:" + id));
		blocks_repo.updateToDeactivate(id);
	}
	
	public void activate(Integer id) {
		InfrastructureBlocks ib = blocks_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Block Not Found:" + id));
		blocks_repo.updateToActivate(id);
	}

}
