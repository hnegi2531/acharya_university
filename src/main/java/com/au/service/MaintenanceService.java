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
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.au.dto.JwtDetails;
import com.au.dto.MaintenanceDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.FinancialYear;
import com.au.model.Notifications;
import com.au.model.ServiceTicketMaintenance;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.FinancialYearRepository;
import com.au.repository.MeintenanceRepo;
import com.au.repository.ServiceTicketRepo;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class MaintenanceService {

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private MeintenanceRepo repo;

	@Autowired
	private FinancialYearRepository financial_year_repo;

	@Autowired
	private ServiceTicketRepo serviceTicketRepo;

	@Autowired
	private UserAuthenticationRepository uar_repo;

	@Autowired
	private EmployeeDetailsRepository empDetail_repo;

	@Autowired
	private ResponseHandler response_handler;

	private final String CRITICAL = "CRITICAL";
	private final String NONCRITICAL = "NONCRITICAL";
	private final String UNDERPROCESS = "UNDERPROCESS";
	private final String COMPLETED = "COMPLETED";
	private final String PENDING = "PENDING";

	private Logger logger = LoggerFactory.getLogger(FeeTemplateService.class);

	public static final String value = "MaintenanceBucket";
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
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

	public List<ServiceTicketMaintenance> createMaintenance(List<MaintenanceDto> dtoList, String jwtToken) throws Exception {
	    JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    List<ServiceTicketMaintenance> serviceTicketList = new ArrayList<>();

	    // Iterate over each MaintenanceDto and process
	    for (MaintenanceDto dto : dtoList) {
	        // Check if the user has more than 3 pending complaints in the same department
	        long count = repo.countByUserIdAndServiceTypeIdAndComplaintStatus(dto.getUserId(), dto.getServiceTypeId(), "PENDING");
	        if (count >= 1) {
	            throw new Exception("You can't request a new service for this Service type as you have awaiting !!!");
	        }

	        // Prepare the ServiceTicketMaintenance entity
	        ServiceTicketMaintenance entity = new ServiceTicketMaintenance();
	        
	        Integer serviceticket_id = repo.getLatestData();
	        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        LocalDateTime now = LocalDateTime.now();
	        String date = dtf.format(now);
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        Date date1 = null;
	        try {
	            date1 = df.parse(date);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }

	        // Fetch the latest service ticket ID and financial year info
	        ServiceTicketMaintenance iid = repo.getLatestServiceTicketId();
	        FinancialYear f_year = financial_year_repo.getFinancialYearData(date1);

	        // Determine the ServiceTicketId for the new service ticket
	        if (serviceticket_id == null) {
	            dto.setServiceTicketId(1);
	        } else if (f_year.getFinancial_year_id() == iid.getFinancial_year_id()) {
	            dto.setServiceTicketId(serviceticket_id + 1);
	        } else {
	            dto.setServiceTicketId(1);
	        }

	        // Set fields for ServiceTicketMaintenance entity
	        entity.setCreated_username(jwtDetails.getUserName());
	        entity.setCreated_by(jwtDetails.getUserId());
	        entity.setServiceTicketId(dto.getServiceTicketId());
	        entity.setComplaintAttendedBy(dto.getComplaintAttendedBy());
	        entity.setComplaintStage(dto.getComplaintStage());
	        entity.setComplaintStatus(dto.getComplaintStatus());
	        entity.setRemarks(dto.getRemarks());
	        entity.setAttendedByUserName(jwtDetails.getUserName());
	        entity.setPurchaseNeed(dto.getPurchaseNeed());
	        entity.setBranchId(dto.getBranchId());
	        entity.setDateOfAttended(dto.getDateOfAttended());
	        entity.setDateOfClosed(dto.getDateOfClosed());
	        entity.setActive(dto.getActive());
	        entity.setInstituteId(dto.getInstituteId());
	        entity.setAttendedBy(dto.getAttendedBy());
	        entity.setServiceTypeId(dto.getServiceTypeId());
	        entity.setUserId(dto.getUserId());
	        entity.setFloorAndExtension(dto.getFloorAndExtension());
	        entity.setComplaintDetails(dto.getComplaintDetails());
	        entity.setFinancial_year_id(f_year.getFinancial_year_id());
	        entity.setBlockId(dto.getBlockId());
	        entity.setDate(dto.getDate());
	        entity.setDeptId(dto.getDeptId());
	        entity.setEvent_status(dto.getEvent_status());
	        entity.setFrom_date(dto.getFrom_date());
	        entity.setTo_date(dto.getTo_date());
	        entity.setProgram_id(dto.getProgram_id());
	        entity.setYear_sem(dto.getYear_sem());
	        entity.setProgram_specialization_id(dto.getProgram_specialization_id());
	        entity.setEvent_id(dto.getEvent_id());

	        // Save the entity
	        repo.save(entity);

	        // Add the created entity to the result list
	        serviceTicketList.add(entity);
	    }

	    // Return the list of created service tickets
	    return serviceTicketList;
	}



	public ServiceTicketMaintenance updateMaintenance(MaintenanceDto dto, Long id, String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
//		if (dto.getComplaintStage() != null) {
//			if (!(dto.getComplaintStage() == CRITICAL || dto.getComplaintStage() == NONCRITICAL)) {
//				throw new ResourceNotFoundException("Enter CRITICAL or NONCRITICAL for ComplaintStage");
//				
//			}
//		}
//		if(dto.getComplaintStatus()!=null) {
//			if (dto.getComplaintStatus() != UNDERPROCESS || dto.getComplaintStatus() != COMPLETED
//					|| dto.getComplaintStatus()!= PENDING) {
//				throw new ResourceNotFoundException("Enter UNDERPROCESS ,PENDING or COMPLETED for ComplaintStatus");
//			}
//		}

		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		ServiceTicketMaintenance entity = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Maintenance not found"));
		entity.setComplaintAttendedBy(dto.getComplaintAttendedBy());
		entity.setComplaintStage(dto.getComplaintStage());
		entity.setComplaintStatus(dto.getComplaintStatus());
		entity.setRemarks(dto.getRemarks());
		entity.setDateOfClosed(dto.getDateOfClosed());
		entity.setModified_by(jwtDetails.getUserId());
		entity.setModified_username(jwtDetails.getUserName());
		repo.save(entity);

		if (entity.getComplaintStatus().equalsIgnoreCase("COMPLETED")) {
			entity.setActive(false);
			repo.save(entity);
		}
//			
//			ServiceTypeTicket ticket = serviceTicketRepo.findById(entity.getServiceTicketId()).
//			orElseThrow(()-> new ResourceNotFoundException("Ticket by this id not found"));
//			ticket.setTicketStatus(true);
//			serviceTicketRepo.save(ticket);
//		}}
		return entity;
	}

	public List<Map<String, Object>> getAllServiceByUserIdAndDeptId(Integer user_id, Integer dept_id) {
		return repo.getAllServiceByUserIdAndDeptId(user_id, dept_id);

	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer dept_id) {
		Page<Object> roles_filtered_response = repo.getAllDataFilteredByKeyword(pageable, keyword, dept_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable, Integer dept_id) {
		Page<Object> roles_sorted_response = repo.getAllSortedData(pageable, dept_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public ServiceTicketMaintenance get(Long id) {
		return repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Service Maintenance Not Found:" + id));
	}

	public ResponseEntity<Object> getAllDataFilteredByKeywordWithDate(Pageable pageable, Object keyword,
			String fromDate, String toDate, Integer dept_id) throws ParseException {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTimeFrom = dateTimeFormat.parse(fromDate);
		Date dateTimeTo = dateTimeFormat.parse(toDate);

		Page<Object> roles_sorted_response = repo.findAll1(pageable, keyword, dateTimeFrom, dateTimeTo, dept_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public ResponseEntity<Object> getAllDataFilteredWithDate(Pageable pageable, String fromDate, String toDate,
			Integer dept_id) throws ParseException {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTimeFrom = dateTimeFormat.parse(fromDate);
		Date dateTimeTo = dateTimeFormat.parse(toDate);

		Page<Object> roles_sorted_response = repo.findAll2(pageable, dateTimeFrom, dateTimeTo, dept_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public ResponseEntity<Object> fetchAllServiceTypeHistory(Pageable pageable, Object keyword) {
		Page<Object> employee_filtered_response = repo.fetchAllServiceTypeHistory(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_filtered_response);
	}

	public ResponseEntity<Object> fetchAllServiceTypeHistory12(Pageable pageable) {
		Page<Object> employee_sorted_response = repo.fetchAllServiceTypeHistory12(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_sorted_response);
	}

	public ResponseEntity<Object> getAllDataFilteredByKeywordWithStatusDept(Pageable pageable, Object keyword,
			String fromDate, String toDate, Integer dept_id, String complaintStatus) throws ParseException {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTimeFrom = dateTimeFormat.parse(fromDate);
		Date dateTimeTo = dateTimeFormat.parse(toDate);

		Page<Object> roles_sorted_response = repo.getAllDataFilteredByKeywordWithStatus(pageable, keyword, dateTimeFrom,
				dateTimeTo, dept_id, complaintStatus);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public ResponseEntity<Object> getAllDataFilteredWithstatusDept(Pageable pageable, String fromDate, String toDate,
			String complaintStatus) throws ParseException {
		Page<Object> roles_sorted_response = repo.getAllDataFilteredWithstatus(pageable, fromDate, toDate,
				complaintStatus);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public ResponseEntity<Object> getAllDataFilteredByKeywordWithStatus(Pageable pageable, Object keyword,
			String complaintStatus, String fromDate, String toDate) {
		Page<Object> roles_sorted_response = repo.getAllDataFilteredByKeywordWithStatus(pageable, keyword,
				complaintStatus, fromDate, toDate);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public ResponseEntity<Object> getAllDataFilteredWithstatus(Pageable pageable, String fromDate, String toDate,
			Integer dept_id, String complaintStatus) throws ParseException {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTimeFrom = dateTimeFormat.parse(fromDate);
		Date dateTimeTo = dateTimeFormat.parse(toDate);
		Page<Object> roles_sorted_response = repo.getAllDataFilteredWithstatus111(pageable, fromDate, toDate, dept_id,
				complaintStatus);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public ResponseEntity<Object> fetchAllServiceTypeHistory(Pageable pageable, Object keyword2, Integer dept_id) {
		Page<Object> employee_filtered_response = repo.fetchAllServiceTypeHistory(pageable, keyword2, dept_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_filtered_response);
	}

	public ResponseEntity<Object> fetchAllServiceTypeHistory1(Pageable pageable1, Integer dept_id) {
		Page<Object> employee_sorted_response = repo.fetchAllServiceTypeHistory1(pageable1, dept_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_sorted_response);
	}

	public HashMap<String, Object> getServicStatusDetailsForGraph(Integer dept_id) throws Exception {
		HashMap<String, Object> hs1 = new HashMap<String, Object>();

		double TotalCountOfServiceType = repo.getTotalCountOfServiceType(dept_id);
		double pendingStatusCount = repo.getPendingStatusCount(dept_id);
		double completedStatusCount = repo.getCompletedStatusCount(dept_id);
		double underProcessStatusCount = repo.getUnderProcessStatusCount(dept_id);

		hs1.put("totalCont", TotalCountOfServiceType);
		hs1.put("pending_count", pendingStatusCount);
		hs1.put("completed_count", completedStatusCount);
		hs1.put("underprocess_count", underProcessStatusCount);

		try {
			double pendingPercentage = pendingStatusCount / TotalCountOfServiceType;
			pendingPercentage = pendingPercentage * 100;
			hs1.put("pending_percentage", pendingPercentage);
		} catch (ArithmeticException ae) {
			System.out.println("0");
			hs1.put("pendingPercentage", 0);
		}

		try {
			double completePercentage = completedStatusCount / TotalCountOfServiceType;
			completePercentage = completePercentage * 100;
			hs1.put("complete_percentage", completePercentage);
		} catch (ArithmeticException ae) {
			System.out.println("0");
			hs1.put("completePercentage", 0);
		}

		try {
			double underProcessPercentage = underProcessStatusCount / TotalCountOfServiceType;
			underProcessPercentage = underProcessPercentage * 100;
			hs1.put("underProcess_percentage", underProcessPercentage);
		} catch (ArithmeticException ae) {
			System.out.println("0");
			hs1.put("underProcessPercentage", 0);
		}

		return hs1;
	}

	public HashMap<String, Object> getServicStatusDetailswithStatusForGraph(Integer dept_id, String complaintStatus) {

		HashMap<String, Object> hs1 = new HashMap<String, Object>();

		double TotalCountOfServiceType = repo.getTotalCountOfServiceType(dept_id);
		double CountOfServiceTypeWithStatus = repo.getCountOfServiceTypeWithStatus(dept_id, complaintStatus);
		hs1.put("TotalCount", TotalCountOfServiceType);
		hs1.put("countOfStatus", CountOfServiceTypeWithStatus);
		try {
			double PercentageOfStatus = CountOfServiceTypeWithStatus / TotalCountOfServiceType;
			PercentageOfStatus = PercentageOfStatus * 100;

			hs1.put("percentageOfComplaintStatus", PercentageOfStatus);
		} catch (ArithmeticException ae) {
			System.out.println("0");
			hs1.put("percentage_of_complaintStatus", 0);
		}

		return hs1;
	}

	public ResponseEntity<Object> getAllDataFiltered(Pageable pageable, Object keyword, String fromDate, String toDate,
			Integer dept_id, String complaintStatus) throws ParseException {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTimeFrom = dateTimeFormat.parse(fromDate);
		Date dateTimeTo = dateTimeFormat.parse(toDate);

		Page<Object> roles_sorted_response = repo.getAllDataFiltered(pageable, keyword, dateTimeFrom, dateTimeTo,
				dept_id, complaintStatus);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public ResponseEntity<Object> getAllData(Pageable pageable, Object keyword, Integer dept_id,
			String complaintStatus) {
		Page<Object> roles_filtered_response = repo.getAllData(pageable, keyword, dept_id, complaintStatus);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
	}

	public ResponseEntity<Object> getAllDataFiltered12(Pageable pageable, String fromDate, String toDate,
			Integer dept_id, String complaintStatus) throws ParseException {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTimeFrom = dateTimeFormat.parse(fromDate);
		Date dateTimeTo = dateTimeFormat.parse(toDate);

		Page<Object> roles_sorted_response = repo.getAllDataFiltered12(pageable, dateTimeFrom, dateTimeTo, dept_id,
				complaintStatus);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public ResponseEntity<Object> getAllData12(Pageable pageable, Integer dept_id, String complaintStatus) {
		Page<Object> roles_sorted_response = repo.getAllData12(pageable, dept_id, complaintStatus);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public Object serviceRequestEmailToStaffByDeptId(Long id) {
		HashMap<String, Object> data = repo.getDataForMailing(id);
		Integer deptId = (Integer) data.get("dept_id");
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + data.get("dept_id").toString());
		List<String> employeeEmails = repo.getEmployeeData(deptId);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + employeeEmails);
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& " + data.get("floorAndExtension").toString());
		System.out
				.println("********************************************* " + (String) data.get("serviceTypeShortName"));
		String content = "Dear Staff," + "<br/>" + "<br/>" + "You have maintenance request pending." + "<br/>" + "<br/>"
				+ "Maintenance type : " + data.get("serviceTypeName") + "-" + data.get("serviceTypeShortName") + "<br/>"
				+ " Maintenance details : " + data.get("complaintDetails") + " , " + data.get("floorAndExtension")
				+ " in " + data.get("dept_name") + "<br/>" + "<br/>" + "Please Check the maintenance request." + "<br/>"
				+ "<br/>" + "Regards<br/>" + "Team Acharya"
				+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br> "
				+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> "
				+ "</body></html>";
		System.out.println("############################ " + content);
		String[] strarray = employeeEmails.toArray(new String[0]);
		System.out.println("############################ " + strarray[0]);
		for (int i = 0; i < strarray.length; i++) {
			System.out.println(strarray[i]);
		}
		response_handler.sendMultipleSimpleEmail(strarray, content, "Pending maintenance request");
		return null;

	}

	public ResponseEntity<Object> getAllDataFilteredWODept(Pageable pageable, Object keyword, String fromDate,
			String toDate, String complaintStatus) throws ParseException {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTimeFrom = dateTimeFormat.parse(fromDate);
		Date dateTimeTo = dateTimeFormat.parse(toDate);

		Page<Object> roles_sorted_response = repo.getAllDataFilteredWODept(pageable, keyword, dateTimeFrom, dateTimeTo,
				complaintStatus);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public ResponseEntity<Object> getAllDataWODept(Pageable pageable, Object keyword, String complaintStatus) {
		Page<Object> roles_filtered_response = repo.getAllDataWODept(pageable, keyword, complaintStatus);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
	}

	public ResponseEntity<Object> getAllDataFiltered12WODept(Pageable pageable, String fromDate, String toDate,
			String complaintStatus) throws ParseException {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTimeFrom = dateTimeFormat.parse(fromDate);
		Date dateTimeTo = dateTimeFormat.parse(toDate);

		Page<Object> roles_sorted_response = repo.getAllDataFiltered12WODept(pageable, dateTimeFrom, dateTimeTo,
				complaintStatus);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public ResponseEntity<Object> getAllData12WODept(Pageable pageable, String complaintStatus) {
		Page<Object> roles_sorted_response = repo.getAllData12WODept(pageable, complaintStatus);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public void uploadFile(MultipartFile multipartFile, Long id) {
		Notifications noti = new Notifications();
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String t2 = noti.setNotification_attach_path(LocalDate.now() + "/" + id + "/" + fileName);
			System.out.println("[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]] " + t2);
			uploadFileTos3bucket(fileName, file, id);
			file.delete();
			repo.updatePath(id, t2);
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

	private void uploadFileTos3bucket(String fileName, File file, Long id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + id + "/" + fileName; // file.getName()
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

	public ResponseEntity<Object> partialUpdateServiceTicket(Long ticketId, MaintenanceDto maintenanceDto,
			String jwtToken) {
		Optional<ServiceTicketMaintenance> optionalServiceTicketMaintenance = repo.findById(ticketId);
		if (!optionalServiceTicketMaintenance.isPresent())
			throw new RuntimeException("Service Ticket not present with id " + ticketId);

		optionalServiceTicketMaintenance.get().setComplaintDetails(maintenanceDto.getComplaintDetails());
		if (maintenanceDto.getAttachment_path() != null) {
			optionalServiceTicketMaintenance.get().setAttachment_path(maintenanceDto.getAttachment_path());
		}
		ServiceTicketMaintenance savedTicket = repo.save(optionalServiceTicketMaintenance.get());
		return ResponseHandler.generateResponse(true, HttpStatus.OK, savedTicket);
	}

	public ServiceTicketMaintenance attendMaintenance(MaintenanceDto dto, String jwtToken) throws Exception {
	    JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    ServiceTicketMaintenance response = new ServiceTicketMaintenance();

	    // Check if the user has more than 3 pending complaints in the same department
	    long count = repo.countByUserIdAndDeptIdAndComplaintStatus(dto.getUserId(), dto.getDeptId(), "PENDING");
	    if (count >= 3) {
	        throw new Exception("You can't request a new service for the selected department as you have three awaiting !!!");
	    }

	    // Prepare the ServiceTicketMaintenance entity
	    Integer serviceticket_id = repo.getLatestData();
	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDateTime now = LocalDateTime.now();
	    String date = dtf.format(now);
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    Date date1 = null;
	    try {
	        date1 = df.parse(date);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    // Fetch the latest service ticket ID and financial year info
	    ServiceTicketMaintenance iid = repo.getLatestServiceTicketId();
	    FinancialYear f_year = financial_year_repo.getFinancialYearData(date1);

	    // Determine the ServiceTicketId for the new service ticket
	    if (serviceticket_id == null) {
	        dto.setServiceTicketId(1);
	    } else if (f_year.getFinancial_year_id() == iid.getFinancial_year_id()) {
	        dto.setServiceTicketId(serviceticket_id + 1);
	    } else {
	        dto.setServiceTicketId(1);
	    }

	    // Set fields for ServiceTicketMaintenance entity
	    response.setCreated_username(jwtDetails.getUserName());
	    response.setCreated_by(jwtDetails.getUserId());
	    response.setServiceTicketId(dto.getServiceTicketId());
	    response.setComplaintAttendedBy(dto.getComplaintAttendedBy());
	    response.setComplaintStage(dto.getComplaintStage());
	    response.setComplaintStatus(dto.getComplaintStatus());
	    response.setRemarks(dto.getRemarks());
	    response.setAttendedByUserName(jwtDetails.getUserName());
	    response.setPurchaseNeed(dto.getPurchaseNeed());
	    response.setBranchId(dto.getBranchId());
	    response.setDateOfAttended(dto.getDateOfAttended());
	    response.setDateOfClosed(dto.getDateOfClosed());
	    response.setActive(dto.getActive());
	    response.setInstituteId(dto.getInstituteId());
	    response.setAttendedBy(dto.getAttendedBy());
	    response.setServiceTypeId(dto.getServiceTypeId());
	    response.setUserId(dto.getUserId());
	    response.setFloorAndExtension(dto.getFloorAndExtension());
	    response.setComplaintDetails(dto.getComplaintDetails());
	    response.setFinancial_year_id(f_year.getFinancial_year_id());
	    response.setBlockId(dto.getBlockId());
	    response.setDate(dto.getDate());
	    response.setDeptId(dto.getDeptId());
	    response.setEvent_status(dto.getEvent_status());
	    response.setFrom_date(dto.getFrom_date());
	    response.setTo_date(dto.getTo_date());
	    response.setProgram_id(dto.getProgram_id());
	    response.setYear_sem(dto.getYear_sem());
	    response.setProgram_specialization_id(dto.getProgram_specialization_id());

	    // Save the entity
	    repo.save(response);

	    // Return the created service ticket
	    return response;
	}

	public ResponseEntity<Object> fetchServiceThroughEvent(Pageable pageable, Integer userId, Integer event_id,
			Object keyword) {
		Page<Map<String, Object>> response1 = repo.fetchServiceThroughEvent(pageable,userId,event_id, keyword );
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> fetchServiceThroughEventWOKeyword(Pageable pageable1, Integer event_id,
			Integer userId) {
		 Page<Map<String, Object>> response = repo.fetchServiceThroughEventWOKeyword(pageable1,userId,event_id);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}


}
