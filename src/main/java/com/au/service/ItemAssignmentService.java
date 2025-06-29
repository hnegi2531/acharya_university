package com.au.service;

import com.au.model.ItemAssignment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.repository.ItemAssignmentRepository;
import com.au.response.ResponseHandler;

@Service
public class ItemAssignmentService {

	@Autowired
	private ItemAssignmentRepository item_assign_repo;
	
	
	public ItemAssignment saveItemAssignment(ItemAssignment ia) throws Exception {
		return	item_assign_repo.save(ia);
		}
	
	

	public List<ItemAssignment> listAllActiveItemAssignment() {
		List<ItemAssignment> ia=item_assign_repo.findAll1();
		return ia;
	}
	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> ic_filtered_response = item_assign_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, ic_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
        Page<Object> ic_sorted_response = item_assign_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, ic_sorted_response);
	}
	
	
	public ItemAssignment get(Integer id) {
		return item_assign_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item Assignment Id Not Found:" + id));
		}
	
	
	public ItemAssignment saveUpdateItemAssignment(ItemAssignment ia) {

		return item_assign_repo.save(ia);
	}
	
	
	public void deactivate(Integer id) {
		ItemAssignment ia = item_assign_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Roles Id Not Found:" + id));
		item_assign_repo.updateToDeactive(id);
	
		
	}

	public void activate(Integer id) {
		ItemAssignment ia = item_assign_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Roles Id Not Found:" + id));
		item_assign_repo.updateToActive(id);
		
	}
	
}
