package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.EnvItemsInStores;
import com.au.model.ItemsCreation;
import com.au.repository.EnvItemsInStoresRepository;
import com.au.repository.GrnRepository;
import com.au.repository.ItemsCreationRepository;
import com.au.repository.LedgerRepository;
import com.au.repository.StockIssueRepository;
import com.au.response.ResponseHandler;

@Service
public class EnvItemsInStoresService {
		
		Logger log = LoggerFactory.getLogger(EnvItemsInStoresService.class);
		
		@Autowired
		private EnvItemsInStoresRepository envItemsInStoresRepository;
		
		@Autowired
		private ItemsCreationRepository itemsCreationRepository;
		
		@Autowired
		private LedgerRepository ledgerRepository;
		
		@Autowired
		private ItemsCreationService itemsCreationService;
		
		@Autowired
		private StockIssueRepository stockIssueRepository;

		@Autowired
		private GrnRepository grnRepository;


		
		public EnvItemsInStores createEnv(EnvItemsInStores es) {
			
			String number =envItemsInStoresRepository.getSerialNumber();
			String number1 ;
			ItemsCreation itemcration=itemsCreationService.get(es.getItem_id());
			String itemType = itemsCreationRepository.getItemType(es.getItem_id());
			Integer measureId = itemcration.getMeasure_id();
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " +measureId);
			if(ObjectUtils.isEmpty(itemType)) {
				throw new ResourceNotFoundException("Item Type is not found !!");
			}
			if(ObjectUtils.isEmpty(itemcration.getLedger_id())) {
				throw new ResourceNotFoundException("Ledger Id is not found in Item creation !!");
			}
			String ledgerShortName = ledgerRepository.getLedgerShortName(itemcration.getLedger_id());
			if(ObjectUtils.isEmpty(ledgerShortName)) {
				throw new ResourceNotFoundException("Ledger Short Name is not found !!");
			}

			if(number==null) {
			number1 = String.format("%05d",1);
			}
			else {
				String number2 = number;
				Integer number3 = Integer.valueOf(number2)+1;
				number1 = String.format("%05d",number3);
			}
			
			
			es.setItem_serial_no(itemType+""+ledgerShortName+""+number1);
			es.setMeasure_id(measureId);
			return envItemsInStoresRepository.save(es);
		}
		
		public List<EnvItemsInStores> getActiveList(){
				return envItemsInStoresRepository.findAll1();
		}
		
		public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
			Page<Object> envFilteredResponse = envItemsInStoresRepository.getAllDataFilteredByKeyword(pageable, keyword );
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, envFilteredResponse);
			}

		public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

			Page<Object> envSortedResponse = envItemsInStoresRepository.getAllSortedData(pageable);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, envSortedResponse);
		}
		
		public EnvItemsInStores get(Integer env_item_id) {
			return  envItemsInStoresRepository.findById(env_item_id).orElseThrow(() -> new ResourceNotFoundException("Env Items Not Found:" + env_item_id));
		}
		
		public EnvItemsInStores update(EnvItemsInStores ei) {
			
			return envItemsInStoresRepository.save(ei);
		}
		
		public void delete(Integer env_item_id) {
			envItemsInStoresRepository.findById(env_item_id)
			.orElseThrow(() -> new ResourceNotFoundException("Env Items Not Found:" + env_item_id));
			envItemsInStoresRepository.update(env_item_id);
		}
		
		public void delete1(Integer env_item_id) {
			envItemsInStoresRepository.findById(env_item_id)
			.orElseThrow(() -> new ResourceNotFoundException("Env Items Not Found:" + env_item_id));
			envItemsInStoresRepository.update1(env_item_id);
		}
		
		
		

		public List<Map<String,Object>> getItemNameConcatWithdescriptionAndMake() {	
			return envItemsInStoresRepository.getItemNameConcatWithdescriptionAndMake();
		}
		

}
