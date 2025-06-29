package com.au.scheduler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.StoreIndentRequest;
import com.au.repository.StoreIndentRequestHistoryRepository;
import com.au.repository.StoreIndentRequestRepository;
import com.au.model.StoreIndentRequestHistory;

@Service
public class StoreIndentRequestScheduler {

	private final ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	private StoreIndentRequestRepository sir_repo;
	
	@Autowired
	private StoreIndentRequestHistoryRepository sir_history_repo;
	
	@Scheduled(cron = "0 * 23 * * ?")
	public void deactivateApprovedPurchaseOrder() {
		try {
			LocalDateTime currentDate = LocalDateTime.now(ZoneId.systemDefault());
			System.out.println(currentDate);
			List<StoreIndentRequest> activeStoreIndent = sir_repo.getStoreIndentData();
			if (ObjectUtils.isNotEmpty(activeStoreIndent)) {
				activeStoreIndent.stream()
						.filter(e -> (Duration.between(
								e.getCreated_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
								currentDate).toDays()) > 7)
						.forEach(po -> {
							po.setActive(false);
							po.setIssued_status("Cancelled");
							sir_repo.save(po);
							StoreIndentRequest indent = sir_repo.storeIndentDetails(po.getStore_indent_request_id());
							
							StoreIndentRequest oldStoreIndentRequest=sir_repo.findById(indent.getStore_indent_request_id()).orElseThrow(() -> new ResourceNotFoundException("Store Indent Not Found:" + indent.getStore_indent_request_id()));
							StoreIndentRequestHistory storeIndentHistory=modelMapper.map(oldStoreIndentRequest,StoreIndentRequestHistory.class);
							sir_history_repo.save(storeIndentHistory);
							
						});
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}
} 
