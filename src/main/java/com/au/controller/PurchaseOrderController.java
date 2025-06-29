package com.au.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.au.dto.DraftOrderRequestDTO;
import com.au.dto.EmployeeMedicalHistoryDto;
import com.au.dto.GrnDTO;
import com.au.dto.GrnDraftJournalVoucherDto;
import com.au.dto.PurchaseOrderDto;
import com.au.dto.PurchaseOrderFileRequest;
import com.au.dto.PurchaseOrderRequestDTOForUpdate;
import com.au.dto.ScrapDTO;
import com.au.dto.StockIssueDTO;
import com.au.dto.TemporaryPurchaseOrderDTO;
import com.au.dto.TemporaryPurchaseOrderDTOForUpdate;
import com.au.dto.purchaseOrderUpdateDto;
import com.au.model.EmployeeDetails;
import com.au.model.PurchaseOrder;
import com.au.response.ResponseHandler;
import com.au.scheduler.PurchaseOrderScheduler;
import com.au.service.PurchaseOrderService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey12}")
@CrossOrigin
public class PurchaseOrderController {

	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private PurchaseOrderScheduler purchaseOrderScheduler;
	
	@PostMapping("/draftPurchaseOrder")
	public ResponseEntity<Object> draftPurchaseOrder(@RequestBody TemporaryPurchaseOrderDTO temporaryPurchaseOrderDTO,@RequestHeader("Authorization") String jwtToken){
		return purchaseOrderService.draftPurchaseOrder(temporaryPurchaseOrderDTO,jwtToken);
	}
	
	@PostMapping("/getDraftPurchaseOrder")
	public ResponseEntity<Object> getDraftPurchaseOrder(@RequestBody DraftOrderRequestDTO draftOrderRequestDTO){
		return purchaseOrderService.getDraftPurchaseOrder(draftOrderRequestDTO);
	}
	
	@PostMapping("/getCancelledDraftPurchaseOrder")
	public ResponseEntity<Object> getCancelledDraftPurchaseOrder(@RequestBody DraftOrderRequestDTO draftOrderRequestDTO){
		return purchaseOrderService.getCancelledDraftPurchaseOrder(draftOrderRequestDTO);
	}
	
	@GetMapping("/getDraftPurchaseOrderById")
	public ResponseEntity<Object> getDraftPurchaseOrderById(@RequestParam("id") Integer id){
		return purchaseOrderService.getDraftPurchaseOrderById(id);
	}
	
	@PostMapping("/assignPurchaseApprover")
	public ResponseEntity<Object> assignPurchaseApprover(@RequestParam("purchaseApprover") String purchaseApprover,@RequestParam("id") Integer id,@RequestParam("temporaryPurchaseOrderId") Integer temporaryPurchaseOrderId){
		return purchaseOrderService.assignPurchaseApprover(purchaseApprover,id,temporaryPurchaseOrderId);
	}
	
	@DeleteMapping("/cancelDraft")
	public ResponseEntity<Object> cancelDraft(@RequestParam("temporaryPurchaseOrderId") Integer temporaryPurchaseOrderId,@RequestParam("cancelById") Integer cancelById){
		return purchaseOrderService.cancelDraft(temporaryPurchaseOrderId,cancelById);
	}
	
	@PutMapping("/updateDraft")
	public ResponseEntity<Object> updateDraft(@RequestParam("temporaryPurchaseOrderId") Integer temporaryPurchaseOrderId,@RequestBody TemporaryPurchaseOrderDTOForUpdate temporaryPurchaseOrderDTOForUpdate,@RequestHeader("Authorization") String jwtToken){
		return purchaseOrderService.updateDraft(temporaryPurchaseOrderId,temporaryPurchaseOrderDTOForUpdate,jwtToken);
	}
	
	@PutMapping("/approvedDraft")
	public ResponseEntity<Object> approvedDraft(@RequestParam("temporaryPurchaseOrderId") Integer temporaryPurchaseOrderId,@RequestParam("approverId") Integer approverId,@RequestHeader("Authorization") String jwtToken){
		return purchaseOrderService.approvedDraft(temporaryPurchaseOrderId,approverId,jwtToken);
	}
	
	@PostMapping("/getPurchaseOrder")
	public ResponseEntity<Object> getPurchaseOrder(@RequestBody DraftOrderRequestDTO draftOrderRequestDTO){
		return purchaseOrderService.getPurchaseOrder(draftOrderRequestDTO);
	}
	
	@GetMapping("/getPurchaseOrderById")
	public ResponseEntity<Object> getPurchaseOrderById(@RequestParam("id") Integer id){
		return purchaseOrderService.getPurchaseOrderById(id);
	}
	
	@DeleteMapping("/rejectPurchaseOrder")
	public ResponseEntity<Object> rejectPurchaseOrder(@RequestParam("purchaseOrderId") Integer purchaseOrderId,@RequestParam("cancelById") Integer cancelById,
													  @RequestParam String cancelComments){
		return purchaseOrderService.rejectPurchaseOrder(purchaseOrderId,cancelById, cancelComments);
	}
	
	@PutMapping("/updatePurchaseOrder")
	public ResponseEntity<Object> updatePurchaseOrder(@RequestParam("purchaseOrderId") Integer purchaseOrderId,@RequestBody PurchaseOrderRequestDTOForUpdate purchaseOrderRequestDTO,@RequestHeader("Authorization") String jwtToken){
		return purchaseOrderService.updatePurchaseOrder(purchaseOrderId,purchaseOrderRequestDTO,jwtToken);
	}
	
	@GetMapping("/getListOfPOForGRN")
	public ResponseEntity<Object> getListOfPOForGRN(@RequestBody DraftOrderRequestDTO draftOrderRequestDTO){
		return purchaseOrderService.getListOfPOForGRN(draftOrderRequestDTO);
	}
	
	@GetMapping("/getPOForGRNByPoNo")
	public ResponseEntity<Object> getPOForGRNByPoNo(@RequestParam("poNo") String poNo){
		return purchaseOrderService.getPOForGRNByPoNo(poNo);
	}
	
	@PostMapping("/createGrn")
	public ResponseEntity<Object> createGrn(@RequestBody GrnDTO grnDTO,@RequestHeader("Authorization") String jwtToken){
		return purchaseOrderService.createGrn(grnDTO,jwtToken);
	}
	
	@PostMapping("/directCreateGrn")
	public ResponseEntity<Object> directCreateGrn(@RequestBody GrnDTO grnDTO,@RequestHeader("Authorization") String jwtToken){
		return purchaseOrderService.directCreateGrn(grnDTO,jwtToken);
	}
	
	@PostMapping("/getListofDirectGRN")
	public ResponseEntity<Object> getListofDirectGRN(@RequestBody DraftOrderRequestDTO draftOrderRequestDTO){
		return purchaseOrderService.getListofDirectGRN(draftOrderRequestDTO);
	}
	
	@GetMapping("/getListofDirectGRNById")
	public ResponseEntity<Object> getListofDirectGRNById(@RequestParam("grnNo") String grnNo){
		return purchaseOrderService.getListofDirectGRNById(grnNo);
	}
	
	@PostMapping(value = "/uploadGrnFile",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Object> uploadGrnFile(@RequestPart("file") MultipartFile file,@RequestParam("grn_no") String grnNO) {
		return purchaseOrderService.uploadGrnFile(file,grnNO);
		
	}
	

	@GetMapping(path = "/grnFileDownload")
	public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("fileName") final String pathName) {
		try {
			final byte[] data = purchaseOrderService.downloadFile(pathName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type","application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + pathName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (Exception e) {
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
	@PostMapping("/saveStockIssue")
	public ResponseEntity<Object> saveStockIssue(@RequestBody List<StockIssueDTO> stockIssues){
		return purchaseOrderService.saveStockIssue(stockIssues);
	}
	
	@PostMapping("/getListOfStockIssue")
	public ResponseEntity<Object> getListOfStockIssue(@RequestBody DraftOrderRequestDTO draftOrderRequestDTO){
		return purchaseOrderService.getListOfStockIssue(draftOrderRequestDTO);
	}
	
	@PostMapping("/getListOfStockRegister")
	public ResponseEntity<Object> getListOfStockRegister(@RequestBody DraftOrderRequestDTO draftOrderRequestDTO){
		return purchaseOrderService.getListOfStockRegister(draftOrderRequestDTO);
	}
	
	@PostMapping("/saveScrap")
	public ResponseEntity<Object> saveScrap(@RequestBody ScrapDTO scrapDTO){
		return purchaseOrderService.saveScrap(scrapDTO);
	}
	
	@GetMapping("/getClosingStock")
	public ResponseEntity<Object> getClosingStock(@RequestParam("item_assignment_id") Integer item_assigment_id){
		return purchaseOrderService.getClosingStock(item_assigment_id);
	}
	
	@GetMapping("/getGrnByItemAssigmentId")
	public ResponseEntity<Object> getGrnByItemAssigmentId(@RequestParam("item_assignment_id") Integer item_assigment_id){
		return purchaseOrderService.getGrnByItemAssigmentId(item_assigment_id);
	}
	
	@GetMapping("/getStockIssueByItemAssigmentId")
	public ResponseEntity<Object> getStockIssueByItemAssigmentId(@RequestParam("item_assignment_id") Integer item_assigment_id){
		return purchaseOrderService.getStockIssueByItemAssigmentId(item_assigment_id);
	}
	
	@GetMapping("/getStockIssueByStockNumber")
	public ResponseEntity<Object> getStockIssueByStockNumber(@RequestParam("stock_no") String stockNumber){
		return purchaseOrderService.getStockIssueByStockNumber(stockNumber);
	}
	
	@GetMapping("/getScrapDetails")
	public ResponseEntity<Object> getScrapDetails(@RequestParam("item_assignment_id") Integer itemAssignmentId){
		return purchaseOrderService.getScrapDetails(itemAssignmentId);
	}
	
	@GetMapping("/getStores")
	public ResponseEntity<Object> getStores(){
		return purchaseOrderService.getStores();
	}
	
	@GetMapping("/getListOfStockRegisterByStoreId")
	public ResponseEntity<Object> getListOfStockRegisterByStoreId(@RequestParam("store_id") Integer storeId){
		return purchaseOrderService.getListOfStockRegisterByStoreId(storeId);
	}
	
	@GetMapping("/getDayWiseSummary")
	public ResponseEntity<Object> getDayWiseSummary(@RequestParam("item_assignment_id") Integer itemAssignmentId){
		return purchaseOrderService.getDayWiseSummary(itemAssignmentId);
	}
	
	@PostMapping("/directCreateGrnForLibrary")
	public ResponseEntity<Object> directCreateGrnForLibrary(@RequestBody GrnDTO grnDTO){
		return purchaseOrderService.directCreateGrnForLibrary(grnDTO);
	}
	
	@GetMapping("/getApprovers")
	public ResponseEntity<Object> getApprovers(){
		return purchaseOrderService.getApprovers();
	}
	
	@PostMapping(value="/purchaseOrderUploadFile")
	public ResponseEntity<Object> purchaseOrderUploadFile(@ModelAttribute PurchaseOrderFileRequest purchaseOrderFileRequest) throws IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			SecurityContextHolder.getContext().getAuthentication();
			purchaseOrderService.purchaseOrderUploadFile(purchaseOrderFileRequest.getFile() , purchaseOrderFileRequest.getPurchaseOrderId());
			return ResponseHandler.generateResponse(true, HttpStatus.OK,"File Uploaded Successfully !!",null);
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			}
		
	}
	
	@GetMapping(path = "/purchaseOrderFileDownload")
	public ResponseEntity<ByteArrayResource> purchaseOrderFileDownload(@RequestParam("pathName") final String pathName) {
		try {
			final byte[] data = purchaseOrderService.downloadFile(pathName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type","application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + pathName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (Exception e) {
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
	@PostMapping("/getDeactivateApprovedPurchaseOrder")
	public ResponseEntity<Object> getDeactivateApprovedPurchaseOrder(@RequestBody DraftOrderRequestDTO draftOrderRequestDTO) {
		if (RateLimitController.bucket.tryConsume(1)) {
			return purchaseOrderService.getDeactivateApprovedPurchaseOrder(draftOrderRequestDTO);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	@DeleteMapping("/deactivateApprovedPurchaseOrder")
	public void deactivateApprovedPurchaseOrder() {
		purchaseOrderScheduler.deactivateApprovedPurchaseOrder();
	}
	
	@GetMapping("/getListofGRNForPdf")
	public ResponseEntity<Object> getListofGRNForPdf(@RequestParam("grnNo") String grnNo){
		return purchaseOrderService.getListofGRNForPdf(grnNo);
	}
	
	@PostMapping(value="/temporaryPurchaseOrderUploadFile")
	public ResponseEntity<Object> temporaryPurchaseOrderUploadFile(@ModelAttribute PurchaseOrderFileRequest purchaseOrderFileRequest) throws IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			SecurityContextHolder.getContext().getAuthentication();
			purchaseOrderService.temporaryPurchaseOrderUploadFile(purchaseOrderFileRequest.getFile() , purchaseOrderFileRequest.getTemporary_purchase_order_id());
			return ResponseHandler.generateResponse(true, HttpStatus.OK,"File Uploaded Successfully !!",null);
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			}
		
	}
	
	@GetMapping(path = "/temporaryPurchaseOrderFileDownload")
	public ResponseEntity<ByteArrayResource> temporaryPurchaseOrderFileDownload(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = purchaseOrderService.temporaryPurchaseOrderFileDownload(fileName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type","application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (Exception e) {
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
	@GetMapping("/getListOfStockRegisterByLegderId")
	public ResponseEntity<Object> getListOfStockRegisterByLegderId(@RequestParam(value = "ledgerId", required = false) Integer ledgerId){
		return purchaseOrderService.getListOfStockRegisterByLegderId(ledgerId);
	}
	
	@GetMapping("/getLegderbyGroupId")
	public ResponseEntity<Object> getLegderbyGroupId(@RequestParam("groupId") Integer groupId){
		return purchaseOrderService.getLegderbyGroupId(groupId);
	}
	
	@GetMapping("/getGroupsForStockRegister")
	public ResponseEntity<Object> getGroupsForStockRegister(){
		return purchaseOrderService.getGroupsForStockRegister();
	}
	

	
	
	@PutMapping("/updateBillApprover/{id}")
	public ResponseEntity<Object> updateBillApprover(@RequestBody purchaseOrderUpdateDto dto,
			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			purchaseOrderService.updateBillApprover(dto, jwtToken);
			ResponseEntity<Object> emp_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return emp_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getPurchaseOrderOnVoucherHeadNewIdAndSchoolId/{voucher_head_new_id}/{school_id}")
	public ResponseEntity<Object> getPurchaseOrderOnVoucherHeadNewIdAndSchoolId(@PathVariable Integer voucher_head_new_id, @PathVariable Integer school_id){
		return purchaseOrderService.getPurchaseOrderOnVoucherHeadNewIdAndSchoolId(voucher_head_new_id,school_id);

	}
	
	@GetMapping("/getJournalVoucher")
	public ResponseEntity<Object> getJournalVoucher(@RequestParam("grnNo") String grnNo){
		return purchaseOrderService.getJournalVoucher(grnNo);

	}
	
	@GetMapping("/indexPageForGrn")
	public ResponseEntity<Object> indexPageForGrn(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword,
			 @RequestParam(value = "date_range", required = false) String dateRange,
		        @RequestParam(value = "start_date", required = false) String startDate,
		        @RequestParam(value = "end_date", required = false) String endDate) {
//		if(RateLimitController.bucket.tryConsume(1)) {
//				Sort sorted = Sort.by(Direction.DESC, sort );
//				if(keyword != null) {	
//						Pageable pageable = PageRequest.of(page, page_size,sorted);
//						System.out.println("page, page_size, sorted, keyword");
//						ResponseEntity<Object> abc =  purchaseOrderService.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
//						return abc;
//				}
//				else {
//						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
//						System.out.println("page, page_size, sorted");
//						ResponseEntity<Object> xyz = purchaseOrderService.getAllSortedData(pageable1);
//						return xyz;
//				}
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}		
//	}
		
		 Sort sorted = Sort.by(Direction.DESC, sort);
		    Pageable pageable = PageRequest.of(page, page_size, sorted);

		    LocalDate start = null;
		    LocalDate end = null;
		    LocalDate minDate = purchaseOrderService.getMinimumPaymentVoucherDate(); // dynamic minDate

		    if (dateRange != null) {
		        switch (dateRange) {
		            case "week":
		                start = LocalDate.now().minusWeeks(1);
		                if (start.isBefore(minDate)) start = minDate;
		                end = LocalDate.now();
		                break;
		            case "month":
		                start = LocalDate.now().minusMonths(1);
		                if (start.isBefore(minDate)) start = minDate;
		                end = LocalDate.now();
		                break;
		            case "custom":
		                try {
		                    if (startDate != null && endDate != null) {
		                        start = LocalDate.parse(startDate);
		                        if (start.isBefore(minDate)) start = minDate;
		                        end = LocalDate.parse(endDate);
		                    }
		                } catch (DateTimeParseException e) {
		                    return ResponseEntity.badRequest().body("Invalid date format. Use yyyy-MM-dd");
		                }
		                break;
		            case "today":
		                start = LocalDate.now();
		                end = LocalDate.now();
		                break;
		        }
		    }

		    return (keyword != null)
		            ? purchaseOrderService.getAllDataFilteredByKeyword(pageable, keyword, start, end, minDate)
		            : purchaseOrderService.getAllSortedData(pageable, start, end, minDate);
		}	
	
	@GetMapping("/getDraftJournalVoucher")
	public List<Map<String, Object>> getDraftJournalVoucher(@RequestParam("draft_journal_voucher_id") Integer draft_journal_voucher_id){
		return purchaseOrderService.getDraftJournalVoucher(draft_journal_voucher_id);

	}
	
	
	@GetMapping("/getJournalVoucherData")
	public List<Map<String, Object>> getJournalVoucher(@RequestParam("journal_voucher_id") Integer journal_voucher_id){
		return purchaseOrderService.getJournalVoucher(journal_voucher_id);
	}
	
	@GetMapping("/getPaymentVoucherDetails")
	public List<Map<String, Object>> getPaymentVoucherDetails(@RequestParam("payment_voucher_id") Integer payment_voucher_id){
		return purchaseOrderService.getPaymentVoucherDetails(payment_voucher_id);
	}
	
	@PostMapping("/mailSendToEmployee")
	public Object mailSendToEmployee(@RequestParam("empId") Integer empId,
			@RequestParam("vendorId") Integer vendorId,
			@RequestParam MultipartFile attachment)
			throws Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				purchaseOrderService.mailSendToEmployee(empId,vendorId, attachment);
				ResponseEntity<Object> service = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return service;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/sendMailToVendor")
	public Object sendMailToVendor(@RequestParam Integer vendorId,@RequestParam String poNumber, @RequestParam MultipartFile attachment) throws Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				purchaseOrderService.sendMailToVendor(vendorId, poNumber,attachment);
                return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (NoSuchElementException e) {
                return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
            return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/getPOIdAndRefNum")
	public ResponseEntity<Object> getPOIdAndRefNum(){
		return purchaseOrderService.getPOIdAndRefNum();
	}
	

	@GetMapping("/getPurchaseOrderData")
	public ResponseEntity<Object> getPurchaseOrderData() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<PurchaseOrder> emp = purchaseOrderService.getPurchaseOrderData();
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateGrnDraftJournalVoucher")
	public ResponseEntity<Object> updateDraftJournalVoucher(@RequestBody GrnDraftJournalVoucherDto dto, @RequestHeader("Authorization") String jwtToken)
	        throws JsonParseException, JsonMappingException, IOException {
	    if(RateLimitController.bucket.tryConsume(1)) {
	        try {
	            purchaseOrderService.updateBillApprover(dto, jwtToken);
	            ResponseEntity<Object> emp_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
	            return emp_response;
	        } catch (NoSuchElementException e) {
	            ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
	            return response;
	        }
	    } else {
	        ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	        return rs;
	    }
	}
	
	@PutMapping("/updatePurchaseOrder/{id}")
	public ResponseEntity<Object> updatePurchaseOrder(@RequestBody PurchaseOrderDto dto,
			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			purchaseOrderService.updatePurchaseOrder(dto, jwtToken);
			ResponseEntity<Object> emp_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return emp_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}


	@GetMapping("/getGRNByPOId/{poId}")
	public ResponseEntity<Object> getGRNByPOId(@PathVariable Integer poId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return purchaseOrderService.getGRNByPOId(poId);
		} else {
            return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
}
