package com.au.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.au.dto.PurchaseIndentDTO;
import com.au.service.PurchaseIndentService;

@RestController
@RequestMapping("/api/purchaseIndent")
public class PurchaseIndentController {
	
	@Autowired
	private PurchaseIndentService purchaseIndentService;
	
	@PostMapping("/saveIndent")
	public ResponseEntity<Object> savePurchaseIndent(@RequestBody List<PurchaseIndentDTO> purchaseIndentDTO){
		return purchaseIndentService.savePurchaseIndent(purchaseIndentDTO);
	}
	
	@PostMapping(value = "/uploadPurchaseIndentFile",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Object> uploadPurchaseIndentFile(@RequestPart("file") MultipartFile file) {
		return purchaseIndentService.uploadPurchaseIndentFile(file);
		
	}

	@GetMapping("/getAllPurchaseIndent")
	public ResponseEntity<Object> getAllPurchaseIndent(){
		return purchaseIndentService.getAllPurchaseIndent();
	}
	
	@GetMapping("/getAllPurchaseIndentById")
	public ResponseEntity<Object> getAllPurchaseIndentById(@RequestParam("purchaseIndentId") Integer purchaseIndentId){
		return purchaseIndentService.getAllPurchaseIndentById(purchaseIndentId);
	}
	
	@DeleteMapping("/deactivatePurchaseIndentById")
	public ResponseEntity<Object> deactivatePurchaseIndentById(@RequestParam("purchaseIndentId") Integer purchaseIndentId){
		return purchaseIndentService.deactivatePurchaseIndentById(purchaseIndentId);
	}
	
	@PostMapping("/saveAllPurchaseIndentForApproval")
	public ResponseEntity<Object> saveAllPurchaseIndentForApproval(@RequestBody List<PurchaseIndentDTO> purchaseIndentDTO){
		return purchaseIndentService.saveAllPurchaseIndentForApproval(purchaseIndentDTO);
	}
	
	@GetMapping("/getAllPurchaseIndentForApproval")
	public ResponseEntity<Object> getAllPurchaseIndentForApproval(@RequestParam("approverId") Integer approverId){
		return purchaseIndentService.getAllPurchaseIndentForApproval(approverId);
	}
	
	@PostMapping("/approveOrRejectPurchaseIndent")
	public ResponseEntity<Object> approveOrRejectPurchaseIndent(@RequestBody List<PurchaseIndentDTO> purchaseIndentDTO){
		return purchaseIndentService.approveOrRejectPurchaseIndent(purchaseIndentDTO);
	}
	
	@GetMapping("/getAllPurchaseIndentbyStatus")
	public ResponseEntity<Object> getAllPurchaseIndentbyStatus(@RequestParam("status") String status){
		return purchaseIndentService.getAllPurchaseIndentbyStatus(status);
	}
	
	@GetMapping("/getAllPurchaseIndentByUserId")
	public ResponseEntity<Object> getAllPurchaseIndentByUserId(@RequestParam("userId") Integer userId){
		return purchaseIndentService.getAllPurchaseIndentByUserId(userId);
	}
	
	@GetMapping("/getApproverId")
	public ResponseEntity<Object> getApproverId(@RequestParam("userId") Integer userId){
		return purchaseIndentService.getApproverId(userId);
	}
	
	@GetMapping("/getPurchaseIndentHistory")
	public ResponseEntity<Object> getPurchaseIndentHistory(){
		return purchaseIndentService.getPurchaseIndentHistory();
	}
}
