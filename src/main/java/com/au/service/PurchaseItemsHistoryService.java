package com.au.service;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.JwtDetails;
import com.au.dto.PurchaseItemsHistoryDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.PurchaseItemsHistory;
import com.au.model.PurchaseOrder;
import com.au.repository.PurchaseItemsHistoryRepository;
import com.au.repository.PurchaseOrderRepository;
import com.au.response.ResponseHandler;

@Service
public class PurchaseItemsHistoryService {
	
	
	@Autowired
	private PurchaseItemsHistoryRepository purchaseItemsHistoryRepository;
	
	@Autowired
	private EnvItemsInStoresService envItemsInStoresService;
	
	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;
	
	
	
	
	private final ModelMapper modelMapper=new ModelMapper(); 
	
	
	public ResponseEntity<Object> cratePurchaseItemHistory(List<PurchaseItemsHistoryDto> purchaseItemsHistoryDto,
			String jwtToken) {

		JwtDetails jwtDetails = modelMapper.map((ResponseHandler.getUserDetailsByToken(jwtToken)).getBody(),
				JwtDetails.class);
		System.out.println(jwtDetails.getUserId() +"dddddddddddddddd "+jwtDetails.getUserName());
		purchaseItemsHistoryDto.stream().forEach(pihd -> {
			modelMapper.getConfiguration().setAmbiguityIgnored(true);
			PurchaseItemsHistory purchaseItemsHistory = modelMapper.map(pihd, PurchaseItemsHistory.class);
			purchaseItemsHistory.setCreatedBy(jwtDetails.getUserId());
			purchaseItemsHistory.setCreatedUsername(jwtDetails.getUserName());
			purchaseItemsHistory.setActive(true);
			purchaseItemsHistory.setEnvItemsInStoresId(envItemsInStoresService.get(pihd.getEnvItemId()));
			PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(pihd.getPurchaseOrderId()).orElseThrow(
					() -> new ResourceNotFoundException("Purchase Order Not Found:" + pihd.getPurchaseOrderId()));
			purchaseItemsHistory.setPurchaseOrder(purchaseOrder);
			purchaseItemsHistoryRepository.save(purchaseItemsHistory);
		});

		return ResponseHandler.generateResponse(true, HttpStatus.OK, "Purchase Items History Successfully Created !!",
				null);
	}

}
