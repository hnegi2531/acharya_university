package com.au.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.SlabStructure;
import com.au.repository.SlabStructureRepository;
import com.au.response.ResponseHandler;

@Service
public class SlabStructureService {

	@Autowired
	private SlabStructureRepository ssr_repo;

	public List<SlabStructure> listAll() {
		return ssr_repo.findAll1();
	}
	
//	public List<SlabStructure> listAll1() {
//		return ssr_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = ssr_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		
		Page<Object> response = ssr_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public SlabStructure saveSlabStructure(SlabStructure slabstructure) {
		if(slabstructure.getMin_value() == 0 && ssr_repo.countSlabDetailsId(slabstructure.getSlab_details_id()) == 0) {
			return ssr_repo.save(slabstructure);
		}else {
			if(ssr_repo.getLastDataOfHeadValue(slabstructure.getSlab_details_id()) > slabstructure.getHead_value()) {
				throw new RuntimeException("Give Amount Value More than Already Existing Amount Value With this Slab Name");
			}
			else {
				return ssr_repo.save(slabstructure);
			}
		}
	}
	
	public SlabStructure updateSlabStructure(SlabStructure slabstructure) {
		if(slabstructure.getMin_value() == 0 && ssr_repo.countSlabDetailsId(slabstructure.getSlab_details_id()) == 0) {
			return ssr_repo.save(slabstructure);
		}else {
			if(ssr_repo.getLastDataOfHeadValueForUpdate(slabstructure.getSlab_structure_id(),slabstructure.getSlab_details_id()) > slabstructure.getHead_value()) {
				throw new RuntimeException("Give Amount Value More than Already Existing Amount Value With this Slab Name");
			}
			else {
				return ssr_repo.save(slabstructure);
			}
		}
	}

	public SlabStructure get(Integer id) {
		if (id.equals(0)) {
			throw new RuntimeException("Opps Exception raised....");
		}
		return ssr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SlabStructure id Not Found:" + id));
	}

	public void delete(Integer id) {
		SlabStructure ay = ssr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SlabStructure id Not Found:" + id));
		ssr_repo.update(id);
	}
	
	public void deletes(Integer id) {
		SlabStructure ay = ssr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SlabStructure id Not Found:" + id));
		ssr_repo.delete(ay);
	}
	
	public void delete1(Integer id) {
		SlabStructure ay = ssr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SlabStructure id Not Found:" + id));
		ssr_repo.update1(id);
	}
	
	public List<Map<String, Object>> getAllValues() {
		return ssr_repo.getAllValue();
	}

}
