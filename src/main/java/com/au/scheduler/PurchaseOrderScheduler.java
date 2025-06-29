package com.au.scheduler;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.au.model.PurchaseOrder;
import com.au.repository.PurchaseOrderRepository;

@Service
public class PurchaseOrderScheduler {
	
	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;
	

	@Scheduled(cron = "0 * 23 * * ?")
	public void deactivateApprovedPurchaseOrder() {
		try {
			LocalDateTime currentDate = LocalDateTime.now(ZoneId.systemDefault());
			System.out.println(currentDate);
			List<PurchaseOrder> activePurchaseOrder = purchaseOrderRepository.getApprovedPurchaseOrder();
			if (ObjectUtils.isNotEmpty(activePurchaseOrder)) {
				activePurchaseOrder.stream()
						.filter(e -> (Duration.between(
								e.getApprovedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
								currentDate).toDays()) > 60)
						.forEach(po -> {
							po.setActive(false);
							po.setRemarks("Purchase Order exceeded the 60 days !!");
							if (ObjectUtils.isNotEmpty(po.getPurchaseItems())) {
								po.getPurchaseItems().stream().forEach(pi -> {
									pi.setActive(false);
								});
								purchaseOrderRepository.save(po);
							}
						});
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}
	

}
