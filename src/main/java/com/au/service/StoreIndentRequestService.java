package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.amazonaws.services.resourcegroupstaggingapi.model.ThrottledException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.au.dto.JwtDetails;
import com.au.dto.storeIndentDto;
import com.au.model.EmployeeDetails;
import com.au.model.EnvItemsInStores;
import com.au.model.FinancialYear;
import com.au.model.StoreIndentRequest;
import com.au.repository.Academic_year_repository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.EnvItemsInStoresRepository;
import com.au.repository.FinancialYearRepository;
import com.au.repository.GrnRepository;
import com.au.repository.ItemsCreationRepository;
import com.au.repository.PurchaseOrderRepository;
import com.au.repository.StockIssueRepository;
import com.au.repository.StoreIndentRequestRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
@Service
public class StoreIndentRequestService {
	
	
	private Logger logger = LoggerFactory.getLogger(StoreIndentRequestService.class);

	@Autowired
	private StoreIndentRequestRepository sir_repo;

	@Autowired
	private Academic_year_repository ac_repo;

	@Autowired
	private ItemsCreationRepository item_repo;
	
	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private FinancialYearRepository financial_year_repo;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;
	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@Autowired
	private StockIssueRepository stockIssueRepository;

	@Autowired
	private EnvItemsInStoresRepository envItemsInStoresRepository;

	@Autowired
	private GrnRepository grnRepository;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	public static final String value = "StoreIndentRequest";
	
	private AmazonS3 s3client;
	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	@PostConstruct
	private void initializeAmazon1() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

	public List<StoreIndentRequest> saveStoreIndentRequest(List<StoreIndentRequest> sir) throws Exception {
		

		sir.stream().forEach(s -> {

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime now = LocalDateTime.now();
			String date = dtf.format(now);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			LocalDate date1 = null;
			date1 = LocalDate.parse(date);

			FinancialYear f_year = financial_year_repo.getFinancialYearDataForIndent(date1); // financial year details
																								// of one particular id.
			
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " +f_year.getFinancial_year_id() + f_year.getFinancial_year() );
			String year1 = f_year.getFinancial_year().substring(2, 5); // 24-
			String year2 = f_year.getFinancial_year().substring(7, 9); // 25
			StoreIndentRequest latest_indent = sir_repo.getLatestIndentRequest(); // based on latest indent_ticket , id
																					// will come

			String indent_ticket = null;
			String count_for_id = sir_repo.getMaxStoreIndentRequestCount();

			if (count_for_id == null) {
				indent_ticket = String.format("%04d", 1);
			} else if (f_year.getFinancial_year_id() == latest_indent.getFinancial_year_id()) {
				String abc = count_for_id.substring(0, 4);
				Integer count = Integer.valueOf(abc) + 1;
				indent_ticket = String.format("%04d", count);
			} else {
				indent_ticket = String.format("%04d", 1);

			}

			s.setIndent_ticket(indent_ticket + "/" + year1 + "" + year2);
			s.setIssued_status("Pending");
			s.setFinancial_year_id(f_year.getFinancial_year_id());
			s.setPurchase_status(0);
		});

		return sir_repo.saveAll(sir);
	}

	private Double getClosingStock(Integer env_item_id) {
		EnvItemsInStores itemAssignment = envItemsInStoresRepository.findById(env_item_id).get();
		Double stockQuantity = ObjectUtils.isNotEmpty(getStockQuantity(itemAssignment.getEnv_item_id()))
				? getStockQuantity(itemAssignment.getEnv_item_id())
				: 0;
		Double grnQuantity = ObjectUtils.isNotEmpty(getGrnQuantity(itemAssignment.getEnv_item_id()))
				? getGrnQuantity(itemAssignment.getEnv_item_id())
				: 0;
		Double openingBalance = ObjectUtils.isNotEmpty(itemAssignment.getOpening_balance())
				? itemAssignment.getOpening_balance()
				: 0;
		Double scrap = ObjectUtils.isNotEmpty(itemAssignment.getScrap()) ? itemAssignment.getScrap() : 0;
		Double closingStock = openingBalance + grnQuantity - stockQuantity - scrap;

		return closingStock;
	}

	private Double getStockQuantity(Integer envItemId) {
		return stockIssueRepository.sumQuantityByItemName(envItemId);
	}

	private Double getGrnQuantity(Integer envItemId) {
		return grnRepository.sumQuantityByItemName(envItemId);
	}

	public List<StoreIndentRequest> listAll1() {
		return sir_repo.findAll11();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer created_by) {
		Page<Object> exam_details_filtered_response = sir_repo.getAllDataFilteredByKeyword(pageable, keyword ,created_by);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable, Integer created_by) {
		Page<Object> exam_details_response = sir_repo.getAllSortedData(pageable ,created_by);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_response);
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword1(Pageable pageable, Object keyword) {
		Page<Object> exam_details_filtered_response = sir_repo.getAllDataFilteredByKeyword1(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData1(Pageable pageable) {
		Page<Object> exam_details_response = sir_repo.getAllSortedData1(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_response);
	}

	public StoreIndentRequest get(Integer id) {
		return sir_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Store Indent Request Not Found:" + id));
	}

	public List<StoreIndentRequest> updateAvailableItemInStore(List<StoreIndentRequest> sir) {
		List<StoreIndentRequest> list = sir_repo.saveAll(sir);

		list.stream().forEach(l -> {
//			l.getEnv_item_id();
			Integer available_item = sir_repo.getTotalQuantityAvailable(l.getEnv_item_id());
			sir_repo.updateTotalQuantity(available_item, l.getEnv_item_id());
		});

		return list;

	}

	public List<StoreIndentRequest> updateStoreIndentRequest(List<StoreIndentRequest> sir) {
		return sir_repo.saveAll(sir);
	}

	public void delete(Integer id) {
		StoreIndentRequest sir = sir_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Exam Details Not Found:" + id));
		sir_repo.updateExamDetail(id);
	}

	public void delete1(Integer id) {
		StoreIndentRequest sir = sir_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Exam Details Not Found:" + id));
		sir_repo.updateExamDetail1(id);
	}

	public List<Map<String, Object>> getEmpNameConcatWithDate() {
		return sir_repo.getEmpNameConcatWithDate();
	}

	public List<Map<String, Object>> getItemApproverdata(Integer store_indent_request_id) {
		return sir_repo.getItemApproverdata(store_indent_request_id);
	}

	public List<Map<String, Object>> getDataForDisplaying(Integer store_indent_request_id) {
		return sir_repo.getDataForDisplaying(store_indent_request_id);
	}

	public List<Map<String, Object>> getDataForDisplaying2(String indent_ticket) {
		return sir_repo.getDataForDisplaying2(indent_ticket);
	}

	public ResponseEntity<Object> getApprovedDataFilteredByKeyword1(Pageable pageable, Object keyword) {
		Page<Object> storeIndentRequest_filtered_response = sir_repo.getApprovedDataFilteredByKeyword1(pageable,
				keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, storeIndentRequest_filtered_response);
	}

	public ResponseEntity<Object> getApprovedDataSortedData1(Pageable pageable) {
		Page<Object> storeIndentRequest_response = sir_repo.getApprovedDataSortedData1(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, storeIndentRequest_response);
	}

	public ResponseEntity<Object> getItemApproverdataBasedOnUserIdByKeyword1(Pageable pageable, Integer user_id,
			Object keyword) {
		Page<Object> exam_details_filtered_response = sir_repo.getItemApproverdataBasedOnUserIdByKeyword1(pageable,user_id, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_filtered_response);
	}

	public ResponseEntity<Object> getItemApproverdataBasedOnUserId(Pageable pageable1, Integer user_id) {
		Page<Object> exam_details_response = sir_repo.getItemApproverdataBasedOnUserId(pageable1,user_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_response);

	}
	
	public List<Map<String, Object>> getApprovedData(Integer id) {
		Integer emp_id = uar_repo.getEmployee_id(id);
		
		return empDetail_repo.getItemApporoverData(emp_id);
		
	}
	
	public List<Map<String, Object>> getDataForDisplaying(String indent_ticket) {
		return sir_repo.getDataForDisplaying(indent_ticket);
	}
	
	
	public void uploadFile(MultipartFile multipartFile,String  indent_ticket) {
		StoreIndentRequest s = new StoreIndentRequest();
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String t2 = s.setAttachment_path(LocalDate.now() + "/" + indent_ticket + "/" + fileName);
			System.out.println("[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]] "+t2);
			uploadFileTos3bucket(fileName, file, indent_ticket);
			file.delete();
			List<Integer> ids = sir_repo.getStoreIndentRequest_ids(indent_ticket);

			
			sir_repo.updatePath(ids, t2);
		} catch (AmazonServiceException ase) {

			logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());

		} catch (AmazonClientException ace) {
			logger.info("Caught an AmazonClientException: ");
			logger.info("Error Message: " + ace.getMessage());
		} catch (IOException ioe) {
			logger.info("IOE Error Message: " + ioe.getMessage());

		}

	}

	
	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file, String indent_ticket) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + indent_ticket + "/" + fileName; // file.getName()
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));
	}

	
	public byte[] viewFiles(String fileName) throws NoSuchFileException {
		try {
			byte[] content;
			final S3Object s3Object = s3client.getObject(bucketName, value + "/" + fileName);
			final S3ObjectInputStream stream = s3Object.getObjectContent();
			content = IOUtils.toByteArray(stream);
			System.out.println(content);
			s3Object.close();
			return content;

		} catch (AmazonS3Exception e) {
			if (e.getStatusCode() == 404) {
				throw new NoSuchFileException("File Not Found");
			}
			throw new AmazonClientException("", e);
		} catch (IOException | AmazonClientException ex) {
			throw new AmazonClientException("", ex);
		}

	}
	
	
	public ResponseEntity<Object> storeIndentRequestApprovedDataByKeyword(Pageable pageable, Object keyword) {
		Page<Object> storeIndentRequestApprovedData = sir_repo.storeIndentRequestApprovedDataByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, storeIndentRequestApprovedData);
	}

	public ResponseEntity<Object> storeIndentRequestApprovedDataSortedData(Pageable pageable) {
		Page<Object> storeIndentRequestApprovedData = sir_repo.storeIndentRequestApprovedDataSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, storeIndentRequestApprovedData);

	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getApprovedStoreIndentRequestByIndentTicket(String indentTicket){
		List<Map<String,Object>> approvedStoreIndentRequest=sir_repo.getApprovedStoreIndentRequestByIndentTicket(indentTicket);
		List<Map<String,Object>> finalApprovedStoreIndentRequest=new ArrayList<Map<String,Object>>();
		approvedStoreIndentRequest.parallelStream().forEach(asir -> {
			ResponseEntity<Object> closingStock=purchaseOrderService.getClosingStock((Integer) asir.get("env_item_id"));
			Map<String, Object> map=(Map<String, Object>) closingStock.getBody();
			asir.put("closingStock", map.get("data"));
			finalApprovedStoreIndentRequest.add(asir);
		});
		return finalApprovedStoreIndentRequest;
	}

	public ResponseEntity<Object> getStoreIndentdetailsByStoreIndentId(Integer envItemId) {
		try {
			List<Map<String,Object>> storeDetails=sir_repo.getStoreIndentdetailsByStoreIndentId(envItemId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
					storeDetails);
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE",
					e.getMessage());

		}
	}

	public ResponseEntity<Object> fetchAllStoreIndentRequestBasedOnUserIdByKeyword(Pageable pageable, Object keyword,
			Integer created_by) {
		Page<Object> exam_details_filtered_response = sir_repo.fetchAllStoreIndentRequestBasedOnUserIdByKeyword(pageable, keyword,created_by);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_filtered_response);
	}

	public ResponseEntity<Object> fetchAllStoreIndentRequestBasedOnUserIdSortedData(Pageable pageable1,
			Integer created_by) {
		Page<Object> exam_details_response = sir_repo.fetchAllStoreIndentRequestBasedOnUserIdSortedData(pageable1,created_by);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_response);
	}

	
	
	public storeIndentDto updateReceiveStatus(storeIndentDto sir)
			throws JsonParseException, JsonMappingException, IOException {
		
    sir.getStore_indent_request_id().stream().forEach(s ->{
    	
    	StoreIndentRequest store = sir_repo.findById(s)
				.orElseThrow(()-> new ResourceNotFoundException("Store Indent not found"));
    	store.setReceived_status(1);
		sir_repo.save(store);
    	
    });
			
    return sir;
	}
	
	
	public List<Map<String, Object>> getItemApproverDataBasedOnIndentTicket(String indent_ticket) {
	    List<Map<String, Object>> get_item_approver_data = sir_repo.getItemApproverDataBasedOnIndentTicket(indent_ticket);

	    // Check the conditions for issuedUpdateStatus
	    String issuedUpdateStatus = determineIssuedUpdateStatus(get_item_approver_data);

	    // Add the issuedUpdateStatus to each item in the list
	    for (Map<String, Object> itemData : get_item_approver_data) {
	        itemData.put("issuedUpdateStatus", issuedUpdateStatus);
	    }

	    return get_item_approver_data;
	}

	private String determineIssuedUpdateStatus(List<Map<String, Object>> get_item_approver_data) {
	    boolean allApproved = true;
	    boolean allRejected = true;
	    boolean allCancelled = true;
	    boolean allPending = true;
	    boolean anyPending = false;
	    boolean hasApproved = false; // To track if there's at least one "Approved"
	    boolean hasRejected = false; // To track if there's at least one "Rejected"
	    boolean allApprovedWithDetails = true; // To track if all "Approved" have non-null issuedBy and issueDate

	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	    for (Map<String, Object> itemData : get_item_approver_data) {
	        String issuedStatus = (String) itemData.get("issued_status");

	        Integer issuedByInt = (Integer) itemData.get("issuedBy");
	        String issuedBy = (issuedByInt != null) ? String.valueOf(issuedByInt) : null;

	        Date issueDate = (Date) itemData.get("issueDate");
	        String issueDateStr = (issueDate != null) ? dateFormat.format(issueDate) : null;

	        // Track the issued status
	        if ("Approved".equalsIgnoreCase(issuedStatus)) {
	            hasApproved = true; // Track that there's at least one "Approved"
	            allRejected = false; // At least one is approved, so not all are rejected
	            allCancelled = false; // At least one is not cancelled, so not all are cancelled
	            allPending = false; // At least one is not pending, so not all are pending

	            // Check if issuedBy and issueDate are null for "Approved"
	            if (issuedBy == null || issueDateStr == null) {
	                allApprovedWithDetails = false; // At least one "Approved" has missing details
	            }

	        } else if ("Rejected".equalsIgnoreCase(issuedStatus)) {
	            hasRejected = true; // Track that there's at least one "Rejected"
	            allApproved = false; // At least one is rejected, so not all are approved
	            allCancelled = false; // At least one is not cancelled, so not all are cancelled
	            allPending = false; // At least one is not pending, so not all are pending
	        } else if ("Pending".equalsIgnoreCase(issuedStatus)) {
	            anyPending = true; // Found at least one pending
	            allApproved = false; // At least one is pending, so not all are approved
	            allRejected = false; // At least one is pending, so not all are rejected
	            allCancelled = false; // At least one is not cancelled, so not all are cancelled
	        } else if ("Cancelled".equalsIgnoreCase(issuedStatus)) {
	            allApproved = false; // At least one is cancelled, so not all are approved
	            allRejected = false; // At least one is cancelled, so not all are rejected
	            allPending = false; // At least one is not pending, so not all are pending
	        }
	    }

	    // Decision after the loop
	    if (hasApproved && hasRejected) {
	        return "Partial"; // Some approved, some rejected
	    } else if (allCancelled) {
	        return "Cancelled"; // All are cancelled
	    } else if (allApproved) {
	        if (allApprovedWithDetails) {
	            return "Issued"; // All are approved and issued_by/issue_date are not null
	        } else {
	            return "Approved"; // All are approved but issued_by/issue_date are null
	        }
	    } else if (allRejected) {
	        return "Cancelled"; // All are rejected
	    } else if (allPending) {
	        return "Pending"; // All are pending
	    } else if (anyPending) {
	        return "Partial"; // Some approved, some rejected, some pending
	    } else {
	        return "Partial"; // Default fallback, if mixed status
	    }
	}



}

//public List<StoreIndentRequest> saveStoreIndentRequest(List<StoreIndentRequest> sir) throws Exception {
//		
//		sir.stream().forEach(s ->{
//			
//			
//			String year1 = financial_year_repo.getFinancialYear1(s.getFinancial_year_id());
//			String year2 = financial_year_repo.getFinancialYear2(s.getFinancial_year_id());
//			
//			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + year1);
//			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + year2);
//			
//			String from_date = financial_year_repo.getFromDate(s.getFinancial_year_id());
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + from_date);
//			String to_date = financial_year_repo.getTODate(s.getFinancial_year_id());
//			System.out.println("=============================================================" + to_date);
//			
////			   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
////			   LocalDateTime now = LocalDateTime.now();
////			   String date  = dtf.format(now);
////			   
////				DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
////				Date date1=df.parse(date);
////				
////				System.out.println("asdfefzsdfzsdgasv " +date1);
//			
//			String indent_ticket = null;
////			if(financial_year_repo.checkDateValidity(date1)) {	
//			if (from_date.equals(to_date)) {
//				String count_for_id = sir_repo.getMaxStoreIndentRequestCount();  //.substring(0,5)
//				System.out.println("())))()()()1()()()( "+ count_for_id);
//			
//				if(count_for_id==null ) {
//
//					indent_ticket = String.format("%04d", 1);
//					System.out.println("())))()111111111111111111111111()()1()()()( "+ indent_ticket);
//				} else {
//				
//					String abc  = count_for_id.substring(0 , 4);
//					System.out.println("())))()()===============()1()()()( " +abc);
//					Integer count = Integer.valueOf(abc)+1;
//					System.out.println("())))()()*********************(3)()()()( " +count);
//					indent_ticket = String.format("%04d", count );
//				}
//			
//			}else {
//				
//				String count_for_id = sir_repo.getMaxStoreIndentRequestCount();  //.substring(0,5)
//				String abc  = count_for_id.substring(0 , 4);
//				System.out.println("())))()()===============()1()()()( " +abc);
//				Integer count = Integer.valueOf(abc)+1;
//				System.out.println("())))()()*********************(3)()()()( " +count);
//				indent_ticket = String.format("%04d", count );
//			}
//			s.setIndent_ticket(indent_ticket +"/" +year1+""+year2);
//			
//			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + s.getIndent_ticket());
//			
//		});	
//		
//		return	sir_repo.saveAll(sir);
//		}
