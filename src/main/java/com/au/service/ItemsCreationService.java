package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.EnvItemsInStores;
import com.au.model.ItemsCreation;
import com.au.repository.EnvItemsInStoresRepository;
import com.au.repository.ItemsCreationRepository;
import com.au.response.ResponseHandler;

import com.au.dto.ActiveitemsDetailsResponseDto;

@Service
public class ItemsCreationService {

	@Autowired
	private ItemsCreationRepository itemsCreationRepository;
	
	@Autowired
	private EnvItemsInStoresRepository envItemsInStoresRepository;

	public ItemsCreation saveItemsCreation(ItemsCreation item) throws Exception {
		if (itemsCreationRepository.getCountItemName(item.getItem_names()) >= 1) {
			throw new Exception("Item Name Already Exist");
		} else {
			return itemsCreationRepository.save(item);
		}
	}


	public List<ItemsCreation> listAll1() {
		return itemsCreationRepository.findAll1();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> itemsCreationFilteredResponse = itemsCreationRepository.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, itemsCreationFilteredResponse);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> itemsCreationSortedResponse = itemsCreationRepository.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, itemsCreationSortedResponse);
	}

	public ItemsCreation get(Integer itemId) {
		return itemsCreationRepository.findById(itemId)
				.orElseThrow(() -> new ResourceNotFoundException("Item Not Found:" + itemId));
	}

	public ItemsCreation saveUpdateItemCreation(ItemsCreation item) {
       Integer itemId = item.getItem_id();
       Integer measureId = item.getMeasure_id();
       
       List<EnvItemsInStores> data = envItemsInStoresRepository.getData(itemId);
       
       data.stream().forEach(env ->{
    	   env.setMeasure_id(measureId);
       });
       
//       envItemsInStoresRepository.updateMeasureId(itemId);
		return itemsCreationRepository.save(item);
	}

	public void delete(Integer itemId) {
		itemsCreationRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item Not Found:" + itemId));
		itemsCreationRepository.update(itemId);
	}

	public void delete1(Integer itemId) {

		itemsCreationRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item Not Found:" + itemId));
		itemsCreationRepository.update1(itemId);
	}
	
	public List<ActiveitemsDetailsResponseDto> allActiveitemsDetails() {
		return itemsCreationRepository.allActiveitemsDetails();
	}

}
