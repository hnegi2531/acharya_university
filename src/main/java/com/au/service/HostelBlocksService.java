package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.HostelBlocks;
import com.au.model.HostelFeeTemplate;
import com.au.model.HostelFloor;
import com.au.repository.HostelBlocksRepository;
import com.au.repository.HostelFeeTemplateRepository;
import com.au.repository.HostelFloorRepository;
import com.au.repository.HostelRoomsRepository;
import com.au.response.ResponseHandler;

@Service
public class HostelBlocksService {

	@Autowired
	private HostelBlocksRepository hostelBlocksRepository;

	@Autowired
	private HostelFloorRepository hostel_floor_repo;
	
	@Autowired
	private HostelRoomsRepository hostel_rooms_repo;
	
	@Autowired
	private HostelFeeTemplateRepository hostelFeeTemplateRepository;

	public List<HostelBlocks> listAll() {
		return hostelBlocksRepository.findAll1();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> hb_filtered_response = hostelBlocksRepository.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, hb_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> hb_sorted_response = hostelBlocksRepository.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, hb_sorted_response);
	}

	public HostelBlocks saveHostelBlocks(HostelBlocks s) {

		if (hostelBlocksRepository.existsByblockName(s.getBlockName())) {
			throw new RuntimeException("Block Name already Exist!!!");
		}
		if (hostelBlocksRepository.existsByblockShortName(s.getBlockShortName())) {
			throw new RuntimeException("Block Short Name already Exist!!!");
		}

		HostelBlocks h1 = hostelBlocksRepository.save(s);

		HostelFloor hf1 = new HostelFloor();
		hf1.setHostelsBlockId(h1.getHostelBlockId());
		hf1.setCreatedBy(h1.getCreatedBy());
		hf1.setCreatedUsername(h1.getCreatedUsername());
		hf1.setActive(h1.getActive());
		hf1.setFloorName(s.getBlockShortName() + "G" + "F");
		hf1.setWardensId(h1.getWardensId());
		hostel_floor_repo.save(hf1);

		int count = 0;
		for (int i = 0; i < s.getTotalFloors() - 1; i++) {
			HostelFloor hf = new HostelFloor();
			count++;
			hf.setActive(h1.getActive());
			hf.setCreatedBy(h1.getCreatedBy());
			hf.setCreatedUsername(h1.getCreatedUsername());
			hf.setHostelsBlockId(h1.getHostelBlockId());
			hf.setFloorName(s.getBlockShortName() + count + "F");
			hf.setWardensId(h1.getWardensId());
			hostel_floor_repo.save(hf);
		}

		return h1;

	}
	
	public HostelBlocks updateHostelBlocks(HostelBlocks hostelBlocks) throws Exception {
		HostelBlocks blocks=get(hostelBlocks.getHostelBlockId());
		if(blocks.getTotalFloors() < hostelBlocks.getTotalFloors()) {
			for (int i = blocks.getTotalFloors(); i < hostelBlocks.getTotalFloors(); i++) {
				HostelFloor hf = new HostelFloor();
				hf.setActive(hostelBlocks.getActive());
				hf.setCreatedBy(hostelBlocks.getModifiedBy());
				hf.setCreatedUsername(hostelBlocks.getModifiedUsername());
				hf.setHostelsBlockId(hostelBlocks.getHostelBlockId());
				hf.setFloorName(hostelBlocks.getBlockShortName() + i + "F");
				hf.setWardensId(hostelBlocks.getWardensId());
				hostel_floor_repo.save(hf);
			}
		}

		return hostelBlocksRepository.save(hostelBlocks);
	}

	public HostelBlocks get(Integer id) {
		return hostelBlocksRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("HostelBlocks Not Found:" + id));
	}

	public void delete(Integer id) {
		HostelBlocks cc = hostelBlocksRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelBlocks Not Found:" + id));
		hostelBlocksRepository.update(id);
	}

	public void delete1(Integer id) {
		HostelBlocks cc = hostelBlocksRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelBlocks Not Found:" + id));
		hostelBlocksRepository.update1(id);

	}

	public List<Map<String, Object>> getAllDoctorWardenDetails(String doctor_warden_type) {
		return hostelBlocksRepository.getAllDoctorWardenDetails(doctor_warden_type);
	}
		
	public Map<Integer, Object> getDetailsOfFloorsAndRooms(Integer block_id) {

		Map<Integer, Object> m1 = new HashMap<Integer, Object>();
		List<Integer> floor_ids = hostel_floor_repo.getListOfFloorIds(block_id);
		floor_ids.stream().forEach(id -> {
			List<Map<String, Object>> room_details = hostel_rooms_repo.getAllRoomsDetails(block_id, id);
			m1.put(id,room_details);
		});
		return m1;
	}

	public Map<Integer, Object> getHostelFloorDetails(Integer block_id) {
		Map<Integer, Object> m1 = new HashMap<Integer, Object>();
		List<Integer> floor_ids = hostel_floor_repo.getListOfFloorIds(block_id);
		floor_ids.stream().forEach(id -> {
			List<Map<String, Object>> room_details = hostel_rooms_repo.getHostelFloorDetails(block_id, id);
			m1.put(id,room_details);
		});
		return m1;
	}

	public ResponseEntity<Object> getHostelBlockDetailsByHostelFeeTemplate(Integer hostelFeeTemplateId) {
		Optional<HostelFeeTemplate> hostelFeeTemplate=hostelFeeTemplateRepository.findById(hostelFeeTemplateId);
		if(hostelFeeTemplate.isPresent() && ObjectUtils.isNotEmpty(hostelFeeTemplate.get().getHostels_block_id())) {
			List<HostelBlocks> hostelBlocks=hostelBlocksRepository.findAllByHostelBlockIdInAndActiveTrue(ResponseHandler.toConvertCommaSeperatedIdsAsList(hostelFeeTemplate.get().getHostels_block_id()));
			return  ResponseHandler.generateResponse(true, HttpStatus.OK, hostelBlocks);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "Hostel Fee Template Id Not Found");
		}
	}
}
