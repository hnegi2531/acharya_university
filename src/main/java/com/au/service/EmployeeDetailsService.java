package com.au.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.au.constant.LeaveKittyEnum;
import com.au.dto.*;
import com.au.event.*;
import com.au.exception.ResourceNotFoundException;
import com.au.model.*;
import com.au.repository.*;
import com.au.response.ResponseHandler;
import com.au.util.AmzonS3PresignedUrlGenerator;
import com.au.util.CsvUtil;
import com.au.util.EncryptionUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.NoSuchFileException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.*;

@Service
public class EmployeeDetailsService {

	private final String PAN_CARD = "PAN_CARD";
	private final String PASSPORT = "PASSPORT";
	private final String DRIVING_LICENCE = "DRIVING_LICENCE";
	private final String AADHAR_CARD = "AADHAR_CARD";
	private final String VOTER_ID = "VOTER_ID";
	private final String OTHERS = "OTHERS";
	private final String EMPLOYMENT_CONTRACT = "EMPLOYMENT_CONTRACT";
	private final String WORK_CONTRACTS = "WORK_CONTRACTS";
	private final String NDA_NCA = "NDA_NCA";
	private final String PHOTO ="PHOTO";
	private static 	final String EMPLOYMENT_OFFER="EMPLOYMENT_OFFER";
	private final String CONTRACT = "CONTRACT";
	private final String DECLARATION = "DECLARATION";
	private final String RESUME = "RESUME"; 
	private final String IMAGEPATH = "IMAGEPATH";
	private final String PROFESSIONAL_EXPERIENCE = "PROFESSIONAL_EXPERIENCE";
	
	Logger log = LoggerFactory.getLogger(EmployeeDetailsService.class);

	public static final String value = "HrBucket";

	public static final String value1 = "ContractBucket";
	
	public static final String value2 = "EmployeeIdsBucket";
	
	public static final String value3 = "EmployeePermanentFileBucket";
	
	
	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;
	
	@Autowired
	private EmployeeIDsAttachmentRepo employeeIDsAttachmentRepo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;

	@Autowired
	private AttachmentsRepository ar_repo;

//	@Autowired
//	private JavaMailSender mailSender;
	
	  @Autowired
	    @Qualifier("mailSenderPrimary")  // ✅ Explicitly use the primary mail sender
	    private JavaMailSender mailSender;

	@Autowired
	private CourseAssignmentEmployeeRepository courseAssignmentEmployeeRepository;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private LeaveApplyRepository leaveapplyrepo;
	
	@Autowired
	private HolidayCalenderRepository holidayCalenderRepository;
	
	@Autowired
	private ResponseHandler response_handler;
	
	@Autowired
	private DepartmentRepository deptrepo;
	
	@Autowired
	private ProctorHeadRepository phr_repo;
	
	@Autowired
	private JobProfileRepository jpr_repo;
	
	@Autowired
	private JobTypeRepository j_repo;
	
	@Autowired
	private School_Repository sc_repo;	
	
	@Autowired
	private ShiftRepository shift_repo;
	
	@Autowired
	private DesignationRepository dr_repo;
	
	@Autowired
	private OfferRepository or_repo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	@Autowired
	private EmployeeSheetRepository employeeSheetRepository;
	
	@Autowired
	private EmployeePayHistoryRepository employeePayHistoryRepository;
	
	@Autowired
	private ProctorStudentAssignmentRepository psar_repo;
	
	
	@Autowired
	private EmployeeDetailsHistoryRepository emp_history_repo;
	
	private final ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	private VendorAttachmentRepository ven_repo;
	
	@Autowired
	private LeavePatternRepository leave_pattern_repo;
	
	@Autowired
	private EmployeeLeavesService empLeave_ser;
	
	@Autowired
	private TimeTableEmployeeRepository tter_ser;
	
	@Autowired
	private LeaveKittyRepository empLeaveRepo;
	
	@Autowired
	private EmployeeContractsRepo employeeContractsAttachmentRepo;
	
	@Autowired
	private EmployeeDetailsHistoryRepository employeeDetailsHistoryRepository;
	
	@Autowired
	private InvPayRepository invPayRepository;
	
	@Autowired
	private InvPayBatchRepository invPayBatchRepository;
	
	@Autowired
	private TemporaryInvPayRepository temporaryInvPayRepository;
	
	@Autowired
	private AmzonS3PresignedUrlGenerator amzonS3PresignedUrlGenerator;
	
	@Autowired
	private FamilyStructureRepository familyStructureRepository;
	
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private MasterSalaryRepository masterSalaryRepository;

	@Autowired
	private LeaveKittyRepository leaveKittyRepository;

	@Autowired
	private EmployeeTypeRepository employeeTypeRepository;

	@Autowired
	private JobTypeRepository jobTypeRepository;

	@Autowired
	private LeaveTypeRepository leaveTypeRepository;

	@Autowired
	private LeaveKittyEventPublisher leaveKittyEventPublisher;

	@Autowired
	private LeaveKittyForYearEventPublisher leaveKittyForYearEventPublisher;
	
	private static final Integer EXPIRE_TIME_FOR_PRESIGNED_URL=10;

	@Autowired
	private PaySlipLockDateRepository paySlipLockDateRepository;

	private final Integer  DEFAULT_RESTRICTED_ID = 0;

	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;

	@Autowired
	private LeavePatternRepository leavePatternRepository;

	@Autowired
	private LeaveApplyRepository leaveApplyRepository;

	@Autowired
	private ConsoliatedPayHistoryService consoliatedPayHistoryService;
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}
	
	public EmployeeDetails updateEmployeeMedicalHistory(EmployeeMedicalHistoryDto dto, String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

		EmployeeDetails employee = empDetail_repo.findById(dto.getEmployeeId())
		.orElseThrow(()-> new ResourceNotFoundException("Employee not found"));
		employee.setFamily_medical_history(dto.getFamily_medical_history());
		employee.setPersonal_medical_history(dto.getPersonal_medical_history());
		employee.setModified_by(jwtDetails.getUserId());
		employee.setModified_username(jwtDetails.getUserName());
		return empDetail_repo.save(employee);
	}
	

	public List<EmployeeIDsAttachment> getEmpIdsByEmpId(Integer empId) {
		EmployeeDetails emp=	empDetail_repo.findById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("employee with id not present"));
		List<EmployeeIDsAttachment> findByEmployeeId = employeeIDsAttachmentRepo.getEmployeeIDsAttachmentDetails(emp.getEmp_id());
		return findByEmployeeId;
	}
	
	
	public void deactivate(Long id) {
		EmployeeIDsAttachment co = employeeIDsAttachmentRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee Attachment Not Found:" + id));
		employeeIDsAttachmentRepo.updateEmployeeIDsAttachment(id);
	}

	public List<EmployeeContractsAttachment> getEmpContractsByEmpId(Integer empId) {
		EmployeeDetails emp=	empDetail_repo.findById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("employee with id not present"));
		List<EmployeeContractsAttachment> findByEmployeeId = employeeContractsAttachmentRepo.getEmpContractsByEmpId(emp.getEmp_id());
		return findByEmployeeId;
	}


	public EmployeeIDsAttachment uploadFile(EmployeeIDsAttachmentRequestDTO dto){
		EmployeeIDsAttachment attachment1;
		attachment1	=	employeeIDsAttachmentRepo.findByEmployeeIdAndDocumentType(dto.getEmpId(), dto.getDocumentType());
		if(attachment1 == null) {
			attachment1=new EmployeeIDsAttachment();
		}
	EmployeeDetails emp=	empDetail_repo.findById(dto.getEmpId())
			.orElseThrow(() -> new ResourceNotFoundException("employee with id not present"));
//		EmployeeIDsAttachment attachment= new EmployeeIDsAttachment();
		if(dto.getDocument() !=null) {
		if (dto.getDocumentType().equals(PAN_CARD) ||
				dto.getDocumentType().equals(VOTER_ID) ||
				dto.getDocumentType().equals(AADHAR_CARD) ||
				dto.getDocumentType().equals(PASSPORT) ||
				dto.getDocumentType().equals(DRIVING_LICENCE) ||
				dto.getDocumentType().equals(OTHERS)|| dto.getDocumentType().equals(PHOTO) || dto.getDocumentType().equals(RESUME) ) {
		try {
			File file = convertMultiPartToFile(dto.getDocument());
			String fileName = generateFileName(dto.getDocument());
			attachment1.setEmployeeId(emp.getEmp_id());
			attachment1.setActive(true);
			attachment1.setDocumentType(dto.getDocumentType());
			attachment1.setFileName(fileName);
			attachment1.setFilePath(LocalDate.now() + "/" + dto.getEmpId()+ "/" + fileName);
			attachment1.setFileType(
					endpointUrl + "/" + bucketName + "/" + value2 + "/" + LocalDate.now() + "/" + dto.getEmpId() + "/" + fileName);
			uploadFileToS3Bucket(fileName, file, dto.getEmpId());	
			file.delete();
			} catch (AmazonServiceException e) {
			    
			    e.printStackTrace();
			} catch (SdkClientException e) {
			    // Handle the AWS SDK client exception
			    e.printStackTrace();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			employeeIDsAttachmentRepo.save(attachment1);
		}
		else {
			throw new IllegalArgumentException("please enter proper document type");
		}	}
		else {
			throw new IllegalArgumentException("please enter document");

		}
		return attachment1;
	}
	
	public List<EmployeeDetails> listAll() {
		return empDetail_repo.findAll1();
	}
	
	public List<EmployeeDetails> activeEmployeeDetailsForProctor() {
		List<EmployeeDetails> emp_details = new ArrayList<>();
		System.out.println("((((((((((((((((())))))))))))))))) "+phr_repo.getEmpIds());
		List<EmployeeDetails> list_emp = empDetail_repo.activeEmployeeDetailsForProctor(phr_repo.getEmpIds());
		System.out.println("((((((((((((((((())))))))))))))))) "+phr_repo.getEmpIds());
		list_emp.stream().forEach(ed -> {
			String departmentname = deptrepo.getDepartment(ed.getDept_id());
			EmployeeDetails emp = new EmployeeDetails();
			emp.setEmployee_name(ed.getEmployee_name() + "-" + ed.getEmpcode() + "-" + departmentname);
			emp.setEmp_id(ed.getEmp_id());
			
			emp_details.add(emp);
		});
		
		
		return emp_details;
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, String jwtToken, 
			Integer dept_id, Integer designation_id, Integer job_type_id, Integer school_id) throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		String roleName = uar_repo.checkIfUserIsPrincipal(jwtDetails.getUserId());
		if(roleName.equalsIgnoreCase("principal")) {
			Integer schoolId = sc_repo.getSchoolIdByEmail(jwtDetails.getUserId());
			Page<Object> employee_filtered_response = empDetail_repo.getAllDataFilteredByKeywordBySchool(pageable, keyword, schoolId,dept_id,designation_id,job_type_id );
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_filtered_response);
		} else {
			Page<Object> employee_filtered_response = empDetail_repo.getAllDataFilteredByKeyword(pageable, keyword,dept_id,school_id,designation_id,job_type_id );
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_filtered_response);
			}
		}
	

	public ResponseEntity<Object> getAllSortedData(Pageable pageable, String jwtToken, Integer dept_id, Integer school_id, Integer designation_id, Integer job_type_id)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		String roleName = uar_repo.checkIfUserIsPrincipal(jwtDetails.getUserId());
		if(roleName.equalsIgnoreCase("principal")) {
			Integer schoolId = sc_repo.getSchoolIdByEmail(jwtDetails.getUserId());
			Page<Object> employee_sorted_response = empDetail_repo.getAllSortedDataBySchool(pageable, schoolId,dept_id,designation_id,job_type_id);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_sorted_response);
		}else {
			Page<Object> employee_sorted_response = empDetail_repo.getAllSortedData(pageable ,dept_id,school_id,designation_id,job_type_id);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_sorted_response);
		}
	}

	public EmployeeDetails saveEmployeeDetails(EmployeeDetails ed) {
		String val = getMasterCode();
		String master = null;
		int count_for_email;
		// String val1 = getContractMasterCode();
		// String[] exe = { val, val1 };
		// List<String> ints = Arrays.asList(exe);
		// String max = Collections.max(ints);
		if (val == null) {
			int num1 = 0;
			int add1 = 1;
			num1 = num1 + add1;
			count_for_email=num1;
			String formattedStrr = String.format("%05d", num1);
			ed.setMaster_code("MC" + formattedStrr);
			master = formattedStrr;
		} else {
			String[] arrOfStrr = val.split("C");
			String str2 = arrOfStrr[1];
			int num1 = Integer.parseInt(str2);
			int add1 = 1;
			num1 = num1 + add1;
			count_for_email=num1;
			String formattedStr1 = String.format("%05d", num1);
			ed.setMaster_code("MC" + formattedStr1);
			master = formattedStr1;
		}

		String str = getEmpCode();
		if (str == null) {
			int num = 0;
			int add = 1;
			num = num + add;
			String formattedStr = String.format("%05d", num);
			ed.setEmpcode("AI" + formattedStr);
		} else {
			String[] arrOfStr = str.split("I");
			String str1 = arrOfStr[1];
			int num = Integer.parseInt(str1);
			int add = 1;
			num = num + add;
			String formattedStr = String.format("%05d", num);
			ed.setEmpcode("AI" + formattedStr);

		}
		ed.setPermanent_status(1);
		ed.setPersonal_email(jpr_repo.getEmail(ed.getJob_id()));

		String preferrred_name = ResponseHandler.removeSpaceBetweenWords(ed.getPreferred_name_for_email());
	//	empDetail_repo.updateEmail(preferrred_name+""+count_for_email+"@acharya.ac.in" , ed.getEmp_id());
		String email=preferrred_name.toLowerCase()+"@acharya.ac.in";
		if(empDetail_repo.emailCount(email) > 0){
			throw new RuntimeException("Acharya Email is already present");
		}
		EmployeeDetails savedEmployee = empDetail_repo.save(ed);
		empDetail_repo.updateEmail(email , ed.getEmp_id()); //for testing
		jpr_repo.updateEmpCodeStatus(ed.getJob_id());
//		empLeave_ser.updateEmployeeLeave(ed); Kitty will generate on 20th of the month
		if(savedEmployee.getEmp_type_id() == 3){
			if(!savedEmployee.getEmployee_name().toUpperCase().startsWith("GUEST")) {
				ConsoliatedAmountDTO consoliatedAmountDTO = getConsoliatedAmountDTO(savedEmployee);
				consoliatedPayHistoryService.saveAdditionAmount(consoliatedAmountDTO);
			}
		}
		return ed;
	}

	private static ConsoliatedAmountDTO getConsoliatedAmountDTO(EmployeeDetails savedEmployee) {
		LocalDate currentDate = LocalDate.now();
		String fromDate = savedEmployee.getDate_of_joining();
		String toDate = savedEmployee.getTo_date();
		fromDate = convertFormat(fromDate);
		toDate = convertFormat(toDate);
		ConsoliatedAmountDTO consoliatedAmountDTO = new ConsoliatedAmountDTO();
		consoliatedAmountDTO.setAmount(savedEmployee.getConsolidated_amount());
		consoliatedAmountDTO.setEmpId(savedEmployee.getEmp_id());
		consoliatedAmountDTO.setRemarks(savedEmployee.getRemarks());
		consoliatedAmountDTO.setMonth(currentDate.getMonthOfYear());
		consoliatedAmountDTO.setYear(currentDate.getYear());
		consoliatedAmountDTO.setSubject(null);
		consoliatedAmountDTO.setFromDate(fromDate);
		consoliatedAmountDTO.setToDate(toDate);
		return consoliatedAmountDTO;
	}

	private static String convertFormat(String givenFormat) {
		SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

		String requiredFormat = "";
		try {
			// Parse the input date strings
			Date from = inputFormat.parse(givenFormat);

			// Format the dates into the required format
			requiredFormat = outputFormat.format(from);

			// Print the results
			System.out.println("Formatted fromDate: " + requiredFormat);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return requiredFormat;
	}


	/*
	 * private String getContractMasterCode() { return
	 * conEmp_repo.fetchgetMaxMasterCode(); }
	 */
	private String getMasterCode() {
		return empDetail_repo.fetchgetMaxMasterCode();
	}

	private String getEmpCode() {
		return empDetail_repo.fetchEmployeeCodes();
	}

	public List<Map<String, Object>> get(Integer id) {
		List<Map<String, Object>> data = empDetail_repo.getEmployeeDetailById(id);

		return data;
	}

	public void delete(Integer id) {
		EmployeeDetails ay = empDetail_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("EmployeeDetails Not Found:" + id));
		empDetail_repo.delete(ay);
	}

//	public void sendSimpleEmail(String[] toEmail, String body, String subject) {
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"));
//		message.setTo(toEmail);
//		message.setText(body);
//		message.setSubject(subject);
//		// message.setCc("vikashkumar@acharya.ac.in");
//		mailSender.send(message);
//		System.out.println("Mail Send...");
//	}

//	public String fetchAttachmentPath(Integer job_id) {
//		String attachmentPath = ar_repo.getAllRecipients(job_id);
//		System.out.println(attachmentPath);
//		return attachmentPath;
//	}

//	public void sendMail(String[] toEmail, String body, String subject, String path) throws IOException {
//		try {
//			MimeMessage message = mailSender.createMimeMessage();
//			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//			helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"),"ERP-Info");
//			helper.setTo(toEmail);
//			helper.setText(body);
//			helper.setSubject(subject);
//			// attach the file
//			FileSystemResource file = new FileSystemResource(new File(path));
//			helper.addAttachment("Invoice", file);// image will be sent by this name
//			mailSender.send(message);
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}
//	}

	public String[] getEmailId(Integer school_id, Integer dept_id) {
		return empDetail_repo.getEmailList(school_id, dept_id);
	}

	public List<HashMap<String, Object>> getEmailIds(Integer school_id, Integer dept_id) {
		return empDetail_repo.getEmailLists(school_id, dept_id);
	}

	public void sendMail(EmailRequest emails, String content, String subject) {
		String[] strarray = emails.getEmails().toArray(new String[0]);
		for (int i = 0; i < strarray.length; i++) {
			System.out.println(strarray[i]);
		}
		System.out.println(strarray);
//		String cc = null;
//		Integer i=emails.getHc().get(0).getJob_id();
//		String s=fetchAttachmentPath(emails.getHc().get(0).getJob_id());
//		System.out.println("+++++++++++++++++++ " + s);
//		try {
//			sendMail(strarray,content,subject,fetchAttachmentPath(emails.getHc().get(0).getJob_id()));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		response_handler.sendMultipleSimpleEmail(strarray,content,subject);
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"));
//		message.setTo(strarray);
//		message.setText(content);
//		message.setSubject(subject);
//		mailSender.send(message);
		System.out.println("Mail Send...");

	}

	public EmployeeDetails saveEmployeeDetail(@Valid EmployeeDetails employeeDetails) {

		EmployeeDetails emp_details = empDetail_repo.findById(employeeDetails.getEmp_id()).orElseThrow(
				() -> new ResourceNotFoundException("EmployeeDetails Not Found:" + employeeDetails.getEmp_id()));

		if (emp_details.getEmp_type_id() != employeeDetails.getEmp_type_id()) {
			String str = getEmpCode();
			if (str == null) {
				int num = 0;
				int add = 1;
				num = num + add;
				String formattedStr = String.format("%04d", num);
				employeeDetails.setEmpcode("AUB" + formattedStr);
			} else {
				String[] arrOfStr = str.split("B");
				String str1 = arrOfStr[1];
				int num = Integer.parseInt(str1);
				int add = 1;
				num = num + add;
				String formattedStr = String.format("%04d", num);
				employeeDetails.setEmpcode("AUB" + formattedStr);

			}
		}
		return empDetail_repo.save(employeeDetails);

	}

	public List<HashMap<String, Object>> getEmailDetails() {
		return empDetail_repo.getEmailForCourseAssign();
	}

	public List<HashMap<String, Object>> getProctorDetails() {
		return empDetail_repo.getProctorDeatil();
	}

	public List<HashMap<String, Object>> getEmployeeNames(Integer school_id) {
		return empDetail_repo.getEmployeeName(school_id);
	}

	public List<Map<String, Object>> getEmployeeName() {
		return empDetail_repo.getEmployeeNames();
	}

	public void updateProctor(Integer chief_proctor_id, List<Integer> emp_id) {
		empDetail_repo.updateProctorHead(chief_proctor_id, emp_id);

	}
	
	public List<HashMap<String , Object>> EmployeeDetailsForBudgetExpense(String email){
		return empDetail_repo.EmployeeDetailsForBudgetExpense(email);
	}
	
	public HashMap<String, Object> EmployeeDetailsForMobileApp(String email){
		HashMap<String, Object> employeeDetails=empDetail_repo.EmployeeDetailsForMobileApp(email);
		String key=StringUtils.isBlank((CharSequence) employeeDetails.get("emp_image_attachment_path"))? "":(String)employeeDetails.get("emp_image_attachment_path");
		String key1=value + "/" + key;
		String keyForPresignedUrl=amzonS3PresignedUrlGenerator.generatePresignedUrl(bucketName, key1, EXPIRE_TIME_FOR_PRESIGNED_URL);
		employeeDetails.put("emp_image_attachment_path", keyForPresignedUrl);
		List<HashMap<String, Object>> familyDetails=familyStructureRepository.getFamilyDetailsByEmployeeId(ObjectUtils.isNotEmpty(employeeDetails.get("emp_id"))?(Integer)employeeDetails.get("emp_id"):0);
		employeeDetails.put("family_details", familyDetails);
		return employeeDetails;
	}
	
	public void uploadFile(MultipartFile multipartFile, Integer emp_id,MultipartFile imageFile) throws IOException{

		EmployeeDetails emp_detail = empDetail_repo.findById(emp_id)
				.orElseThrow(() -> new ResourceNotFoundException("EmployeeDetails Not Found:" + emp_id));;
				try
				{
					log.debug("Message For Employee Details Attachment --------------");
					File file = convertMultiPartToFile(multipartFile);
					File image_file = convertMultiPartToFile(imageFile);
					String fileName = generateFileName(multipartFile);
					String imageFileName = generateFileName(imageFile);
					log.debug("Employee Details Attachment", file);
					System.out.println(emp_id);
					System.out.println("(((((((((((((((((()))))))))))))))))) " +emp_detail.getEmp_attachment_path());
					emp_detail.setEmp_attachment_file_name(fileName);
					emp_detail.setEmp_attachment_path(LocalDate.now() + "/" + emp_id + "/" + fileName);
					emp_detail.setEmp_image_attachment_path(LocalDate.now() + "/" + emp_id + "/" + imageFileName);
					emp_detail.setEmp_attachement_type(
							endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + emp_id + "/" + fileName);
					uploadFileToS3Bucket(fileName, file, emp_id);
					uploadFileToS3Bucket(imageFileName, image_file, emp_id);
					log.debug("Message For Attachment", file);
					System.out.println("(((((((((((((((((()))))))))))))))))) " +emp_detail.getEmp_image_attachment_path());
					file.delete();
					image_file.delete();
				}catch (AmazonServiceException ase) {

					log.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
					log.info("Error Message:    " + ase.getMessage());
					log.info("HTTP Status Code: " + ase.getStatusCode());
					log.info("AWS Error Code:   " + ase.getErrorCode());
					log.info("Error Type:       " + ase.getErrorType());
					log.info("Request ID:       " + ase.getRequestId());

				} catch (AmazonClientException ace) {
					log.info("Caught an AmazonClientException: ");
					log.info("Error Message: " + ace.getMessage());
				} catch (IOException ioe) {
					log.info("IOE Error Message: " + ioe.getMessage());

				}
				empDetail_repo.save(emp_detail);
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convertFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convertFile);
		fos.write(file.getBytes());
		fos.close();
		return convertFile;

	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileToS3Bucket(String fileName, File file, Integer emp_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + emp_id + "/" + fileName; 
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));

	}
	
	
//	public byte[] downloadFile(final String keyName) throws NoSuchFileException {
//		try {
//			byte[] content;
//			final S3Object s3Object = s3client.getObject(bucketName, value + "/" + keyName);
//			final S3ObjectInputStream stream = s3Object.getObjectContent();
//			content = IOUtils.toByteArray(stream);
//			System.out.println(content);
//			s3Object.close();
//			return content;
//
//		} catch (AmazonS3Exception e) {
//			if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
//				throw new NoSuchFileException("File Not Found");
//			}
//			throw new AmazonClientException("", e);
//		} catch (IOException | AmazonClientException ex) {
//			throw new AmazonClientException("", ex);
//		}
//	}
	public byte[] downloadFile(final String keyName) throws NoSuchFileException {
	    try {
	        String fullKey = value + "/" + keyName; // 'value' should be the folder/prefix like 'profile_photo'
	        System.out.println("📂 Attempting to download from S3");
	        System.out.println("➡ Bucket: " + bucketName);
	        System.out.println("➡ Full S3 Key: " + fullKey);

	        S3Object s3Object = s3client.getObject(bucketName, fullKey);
	        S3ObjectInputStream inputStream = s3Object.getObjectContent();
	        byte[] content = IOUtils.toByteArray(inputStream);

	        System.out.println("✅ Download successful. Size: " + content.length + " bytes");
	        s3Object.close();
	        return content;

	    } catch (AmazonS3Exception e) {
	        System.err.println("❗ AmazonS3Exception occurred");
	        System.err.println("Message     : " + e.getMessage());
	        System.err.println("Error Code  : " + e.getErrorCode());
	        System.err.println("Status Code : " + e.getStatusCode());
	        System.err.println("Request ID  : " + e.getRequestId());
	        System.err.println("AWS Error Type: " + e.getErrorType());

	        // File not found or access denied
	        if (e.getStatusCode() == 404 || e.getStatusCode() == 403) {
	            throw new NoSuchFileException("S3 file not found or permission denied: " + keyName);
	        }

	        throw new AmazonClientException("Amazon S3 Exception occurred", e);

	    } catch (IOException | AmazonClientException ex) {
	        System.err.println("❗ IOException / ClientException: " + ex.getMessage());
	        ex.printStackTrace();
	        throw new AmazonClientException("Failed to download file from S3", ex);
	    }
	}
	
	public List<Map<String, Object>> getRolesByEmployeeEmail(String email) {
		return empDetail_repo.getRolesByEmployeeEmail(email);
	}
	
	public Object emailToStaffsRegardingNewRecruit(String salary_structure_email_content, Integer job_id, Integer emp_id) throws Exception {
		EmployeeDetails emp_details = empDetail_repo.getEmployeeDetailsByEmployeeId(emp_id);
		
		String employeeName = null;
		if(emp_details.getGender() == 'M')
			employeeName = "Mr. " + emp_details.getEmployee_name();
		else
			employeeName = "Ms. " + emp_details.getEmployee_name();
		
		String schoolName = sc_repo.getSchoolName(emp_details.getSchool_id());
		String emp_Code = emp_details.getEmpcode();
		String email = emp_details.getEmail();
		String mobile_no = emp_details.getMobile();
		String jobType = j_repo.getJobTypeName(emp_details.getJob_type_id());
		String department = deptrepo.getDepartment(emp_details.getDept_id());
		String dateOfJoining = emp_details.getDate_of_joining();
		Shift shiftData = shift_repo.getShiftData(emp_details.getShift_category_id());
		String shift = shiftData.getShiftName() + " (" + shiftData.getShiftStartTime() + " - " + shiftData.getShiftEndTime() + ")";
		System.out.println("sssssssssssssssssssssssssssssssss "+shift);
		String designation = dr_repo.getDesignation(emp_details.getDesignation_id());
		EmployeeDetails reportingOfficerDetails = empDetail_repo.getEmployeeDetailsByEmployeeId(emp_details.getReport_id());
		EmployeeDetails leaveApprover1Details =  empDetail_repo.getEmployeeDetailsByEmployeeId(emp_details.getLeave_approver1_emp_id());
		EmployeeDetails leaveApprover2Details =  empDetail_repo.getEmployeeDetailsByEmployeeId(emp_details.getLeave_approver2_emp_id());
		String reportingOfficerName = reportingOfficerDetails.getEmployee_name();
		String leaveApprover1Name = leaveApprover1Details.getEmployee_name();
		String leaveApprover2Name = leaveApprover2Details.getEmployee_name();
		String message = or_repo.getRemarks(emp_details.getJob_id());
		String reportingOfficerEmail = reportingOfficerDetails.getEmail();
		String leaveApprover1Email = leaveApprover1Details.getEmail();
		String leaveApprover2Email = leaveApprover2Details.getEmail();
		String headHr = "headhr@acharya.ac.in";
		List<String> list_emails = null;
		if(leaveApprover1Email.equals(leaveApprover2Email)) 
			list_emails = Arrays.asList(leaveApprover1Email,headHr);		
		else 
			list_emails = Arrays.asList(leaveApprover1Email,leaveApprover2Email,headHr);

		String[] cc_emails = list_emails.stream().toArray(String[] ::new);
			
		String contents = "<p style='text-align: justify;'>Dear Sir/Madam," + "<br><br>"
				+ "Please find below the particulars of the new employee recruited @ Acharya."
				+ "The information is provided to create new Email ID, New Staff ID, entry in Statutory records, "
				+ "entry in the website and other formalities as applicable..</p>" + "<br>"
				+ "<html><head>" + "<style>" + "table {" + "  font-family: arial, sans-serif;"
				+ "  border-collapse: collapse;" + "  width: 40%;" + "  font-size:12px;" + "}" + "th {\r\n" + "  border: 1px solid #808080;"
				+ "  text-align: left;" + "  padding: 5px;" + "}" + "td{"
				+ "border: 1px solid #808080;text-align:left;padding:5px" + "}" + "</style>"
				+ "</head><body><br><table>" 
				+ "  <tr>" + "    <th>Institute</th>" + "    <td>" + schoolName + "</td></tr>"
				+ "  <tr>" + "    <th>Name</th>" + "     <td>" + employeeName + "</td></tr>"
				+ "  <tr>" + "    <th>Employee Id</th>" + "     <td>" + emp_Code + "</td></tr>"
				+ "  <tr>" + "    <th>Email</th>" + "     <td>" + email + "</td></tr>"
				+ "  <tr>" + "    <th>Mobile</th>" + "     <td>" + mobile_no + "</td></tr>"
				+ "  <tr>" + "    <th>Job Type</th>" + "     <td>" + jobType + "</td></tr>"
				+ "  <tr>" + "    <th>Department</th>" + "     <td>" + department + "</td></tr>"
				+ "  <tr>" + "    <th>DOJ</th>" + "     <td>" + dateOfJoining + "</td></tr>"
				+ "  <tr>" + "    <th>Shift</th>" + "     <td>" + shift + "</td></tr>"
				+ "  <tr>" + "    <th>Designation</th>" + "     <td>" + designation + "</td></tr>"
				+ "  <tr>" + "    <th>Reporting Officer</th>" + "     <td>" + reportingOfficerName + "</td></tr>"
				+ "  <tr>" + "    <th>Leave Approver 1</th>" + "     <td>" + leaveApprover1Name + "</td></tr>"
				+ "  <tr>" + "    <th>Leave Approver 2</th>" + "     <td>" + leaveApprover2Name + "</td></tr>"
				+ "  <tr>" + "    <th>Message</th>" + "     <td>" + message + "</td></tr>"
				+ "</table>"+ "<br><br>" + salary_structure_email_content + 	"</table></body></html>" + "<br><br>"
				+ "Regards <br>"
				+ "Team Acharya";

//		String contents = "Dear Sir/Madam," + "<br><br>"
//				+ "This is to bring to your notice that the below candidate shall report to you on&nbsp;"
//				+ reportingDateTime + "<html><head>" + "<style>" + "table {" + "  font-family: arial, sans-serif;"
//				+ "  border-collapse: collapse;" + "  width: 60%;" + "}" + "th {\r\n" + "  border: 1px solid #dddddd;"
//				+ "  text-align: center;" + "  padding: 5px;" + "background-color : #eee;color:black" + "}" + "td{"
//				+ "border: 1px solid #dddddd;text-align:center;padding:5px" + "}" + "</style>"
//				+ "</head><body><br><table>" + "  <tr>" + "    <th>Candidate Name</th>" + "    <th>Designation</th>"
//				+ "    <th>Job type</th>" + "     <th>Offer Status</th>" + "  </tr>" + " <tr>\r\n" + "    <td>"
//				+ firstName + "&nbsp;" + "</td>" + "    <td>" + designation + "</td>" + "    <td>" + jobType
//				+ "</td>" + "      <td>" + offerName + "</td>" + "  </tr>" + "</table></body></html>";
		
		String subject = null;
		if(emp_details.getGender() == 'M') 
			subject = "Employee Recruited - " + employeeName + " , " + designation;
		else 
			subject = "Employee Recruited - " + employeeName + " , " + designation;
		
		response_handler.sendMailWithTableFormatWithCC(reportingOfficerEmail,cc_emails,contents,subject);
		System.out.println("Mail Send...");
		return null;
	}
	
	public Object emailToReportingOfficerAndNewEmployee(Integer job_id, Integer emp_id) throws Exception {
		EmployeeDetails emp_details = empDetail_repo.getEmployeeDetailsByEmployeeId(emp_id);
		
		String employeeName = null;
		if(emp_details.getGender() == 'M')
			employeeName = "Mr. " + emp_details.getEmployee_name();
		else
			employeeName = "Ms. " + emp_details.getEmployee_name();
		
		String schoolName = sc_repo.getSchoolName(emp_details.getSchool_id());
		String emp_Code = emp_details.getEmpcode();
		String email = emp_details.getEmail();
		String username = uar_repo.getUsername(email);
		String mobile_no = emp_details.getMobile();
		String jobType = j_repo.getJobTypeName(emp_details.getJob_type_id());
		String department = deptrepo.getDepartment(emp_details.getDept_id());
		String dateOfJoining = emp_details.getDate_of_joining();
		Shift shiftData = shift_repo.getShiftData(emp_details.getShift_category_id());
		String shift = shiftData.getShiftName() + " (" + shiftData.getShiftStartTime() + " - " + shiftData.getShiftEndTime() + ")";
		System.out.println("sssssssssssssssssssssssssssssssss "+shift);
		String designation = dr_repo.getDesignation(emp_details.getDesignation_id());
		EmployeeDetails reportingOfficerDetails = empDetail_repo.getEmployeeDetailsByEmployeeId(emp_details.getReport_id());
		EmployeeDetails leaveApprover1Details =  empDetail_repo.getEmployeeDetailsByEmployeeId(emp_details.getLeave_approver1_emp_id());
		EmployeeDetails leaveApprover2Details =  empDetail_repo.getEmployeeDetailsByEmployeeId(emp_details.getLeave_approver2_emp_id());
		String reportingOfficerName = reportingOfficerDetails.getEmployee_name();
		String leaveApprover1Name = leaveApprover1Details.getEmployee_name();
		String leaveApprover2Name = leaveApprover2Details.getEmployee_name();
		String reportingOfficerEmail = reportingOfficerDetails.getEmail();
		String message = or_repo.getRemarks(emp_details.getJob_id());
		String[] emails = {reportingOfficerEmail,email};
		

//		if(leaveApprover1Email.equals(leaveApprover2Email))
//			cc_emails = ;
//		else
//			cc_emails = Arrays.asList(leaveApprover1Email,leaveApprover2Email);
			
		String contents = "<p style='text-align: justify;'>"
				+ "Dear " + employeeName + ","+"<br><br>"
				+ "Welcome aboard ! We are pleased to have you working with us. You are selected for employment due to the attributes you "
				+ "displayed that appear to match the qualities Acharya looks for, in an employee.<br>"
				+ "We look forward to seeing you grow and develop into an outstanding colleague who exhibits a high level of care, "
				+ "concern and compassion for others. We hope that you will find your work rewarding, challenging and meaningful.<br>"
				+ "In addition to your Teaching Position, you are also encouraged to join Research team by sending an email to "
				+ "research@acharya.ac.in .<br>"
				+ "You are requested to upload your fields of interest in Research and also your current "
				+ "Research credentials (if available) in the Research profile in your ERP login ID under >My Menu>My Profile.<br> "
				+ "This shall enable you to get in touch with your peers in similar fields of interests and also showcase your "
				+ "credentials to your colleagues for a collaborative and constructive teamwork.<br>"
				+ "The keys to your success will be being dependable, reliable, showing openness, follow-through, attentiveness, "
				+ "supervision, documentation and following the policies and procedures. While doing these things, you will be successful "
				+ "and so will Acharya.</p><br>"
				+ "<b><p style='font-size:120%;text-align: justify;'>Please make note of the below details</b></p>"
				+ "<html><head>" + "<style>" + "table {" + "  font-family: arial, sans-serif;"
				+ "  border-collapse: collapse;" + "  width: 40%;" + " font-size:12px;" + "}" + "th {\r\n" + "  border: 1px solid #808080;"
				+ "  text-align: left;" + "  padding: 5px;" + "}" + "td{"
				+ "border: 1px solid #808080;text-align:left;padding:5px" + "}" + "</style>"
				+ "</head><body><br><table>" 
				+ "  <tr>" + "    <th>Institute</th>" + "    <td>" + schoolName + "</td></tr>"
				+ "  <tr>" + "    <th>Name</th>" + "     <td>" + employeeName + "</td></tr>"
				+ "  <tr>" + "    <th>Employee Id</th>" + "     <td>" + emp_Code + "</td></tr>"
				+ "  <tr>" + "    <th>Email</th>" + "     <td>" + email + "</td></tr>"
								+ "  <tr>" + "    <th>ERP Login Id</th>" + "     <td>" + username + "</td></tr>"
				+ "  <tr>" + "    <th>Password</th>" + "     <td>" + "acharya1234" + "</td></tr>"
				+ "  <tr>" + "    <th>Mobile</th>" + "     <td>" + mobile_no + "</td></tr>"
				+ "  <tr>" + "    <th>Job Type</th>" + "     <td>" + jobType + "</td></tr>"
				+ "  <tr>" + "    <th>Department</th>" + "     <td>" + department + "</td></tr>"
				+ "  <tr>" + "    <th>DOJ</th>" + "     <td>" + dateOfJoining + "</td></tr>"
				+ "  <tr>" + "    <th>Shift</th>" + "     <td>" + shift + "</td></tr>"
				+ "  <tr>" + "    <th>Designation</th>" + "     <td>" + designation + "</td></tr>"
				+ "  <tr>" + "    <th>Reporting Officer</th>" + "     <td>" + reportingOfficerName + "</td></tr>"
				+ "  <tr>" + "    <th>Leave Approver 1</th>" + "     <td>" + leaveApprover1Name + "</td></tr>"
				+ "  <tr>" + "    <th>Leave Approver 2</th>" + "     <td>" + leaveApprover2Name + "</td></tr>"
				+ "  <tr>" + "    <th>Message</th>" + "     <td>" + message + "</td></tr>"
				+ "</table>"+ "<br><br>" + "</table></body></html>"
				+ "<b><p style='font-size:120%;text-align: justify;'>General Guidelines:</b></p>" 
				+ "	<p style='text-align: justify;'>"
				+ " 1. Default Email, LoginId and Password is mentioned above ( Acharya E-mail shall be operational within 24 hours from DOJ ).<br>"
				+ "	2. Go to https://acharyainstitutes.in for ERP login and reset your password once login using the above credentials.<br>"
				+ "	3. Upon reporting to duty, kindly ensure that you collect your Bio-metric Card from HR Department on the same day.\n Please note Attendance is recorded as per Punching.<br>"
				+ "	4. During your probationary period, you shall be entitled to one Casual/sick leave every month... same shall credit on 20th of every month. Leave to be availed only on approval in ERP. However, in extreme circumstances and unavoidable situations where in leave could not be applied in advance, your leave can be initiated by your first approver within two days of the leave date.<br>"
				+ "	5. You can view your attendance report in your ERP login. Your attendance is your responsibility and ensure your leaves are approved well in advance and the attendance status is to your satisfaction at any given date. Payroll is automatically calculated as per the attendance status in ERP.<br>"
				+ "	6. You can lodge complaints against services like Maintenance, Housekeeping, System/IT support, HR attendance and Security related support in ERP.</p><br>"
				+ "<b><p style='color:blue;font-family: Rockwell Extra Bold;font-size:160%;'>Once again Welcome to Acharya...</p></b>"
				+ "__________________________________________________________________________________________________________________________________________________________________<br><br>"
				+ "Sincerely,<br><br>"
				+ "Regards<br>"
				+ "Principal, " + schoolName + "<br>"
				+ "HOD - " + department + "<br>"
				+ "Human Resource Department";
		
		String subject = "General Guidelines";
		response_handler.sendMailWithTableFormat(emails,contents,subject);
		System.out.println("Mail Send...");
		return null;
	}
	
	//add principal email also before going live
	public Object emailToPrincipalAndTeam(Integer job_id, Integer emp_id) throws Exception {
		EmployeeDetails emp_details = empDetail_repo.getEmployeeDetailsByEmployeeId(emp_id);
		
		String employeeName = null;
		if(emp_details.getGender() == 'M')
			employeeName = "Mr. " + emp_details.getEmployee_name();
		else
			employeeName = "Ms. " + emp_details.getEmployee_name();
		
		String schoolName = sc_repo.getSchoolName(emp_details.getSchool_id());
		String principal_email = sc_repo.getSchoolPrincipalEmail(emp_details.getSchool_id());
		String[] cc_email = empDetail_repo.getAllEmailsOfTeam(emp_details.getDept_id());
		String department = deptrepo.getDepartment(emp_details.getDept_id());
		String dateOfJoining = emp_details.getDate_of_joining();
		String designation = dr_repo.getDesignation(emp_details.getDesignation_id());
					
		String contents = "<p style='font-size:120%;font-family: Rockwell Extra Bold;text-align: justify;'>Dear All,<br><br>"
				+ "We are very pleased to announce that our team is growing. " + employeeName + " , " + designation + " has joined the "
				+ "Department of " + department + " on " + dateOfJoining + ".<br>"
				+ "Please join us in welcoming " + employeeName + " and make sure to drop by their workspace to introduce yourselves.</p>"
				+ "_________________________________________________________________________________________________________________________________<br><br>"
				+ "Sincerely,<br><br>"
				+ "Regards<br>"
				+ "Principal, " + schoolName + "<br>"
				+ "HOD - " + department + "<br>";
		
		String subject = "New Addition to Our Team";
		response_handler.sendMailWithTableFormatWithCC(principal_email,cc_email,contents,subject);
		System.out.println("Mail Send...");
		return null;
	}
	
	public EmployeeDetails getEmployeeDataByUserID(Integer user_id) {
		return empDetail_repo.getEmployeeDataByUserID(user_id);
	}
	
//	public List<Map<String, Object>> getEmployeesUnderDepartment(Integer emp_id) {
//		return empDetail_repo.getEmployeesUnderDepartment(emp_id);
//	}
	
	public List<Map<String, Object>> getEmployeesUnderDepartment(Integer emp_id,Date selected_date,Integer time_slots_id) {
		List<Integer> emp_ids_from_time_table_employee = tter_ser.getEmplIdsFromTimeTableEmployee(selected_date,time_slots_id);
		System.out.println("(((((((((((((((((((---1----)))))))))))))))))))))))))) "+emp_ids_from_time_table_employee);
		List<Integer> emp_ids_under_specific_department = empDetail_repo.getEmployeesUnderDepartment(emp_id);
		System.out.println("(((((((((((((((((((----2---)))))))))))))))))))))))))) "+emp_ids_under_specific_department);
		ArrayList<Integer> duplicateList = new ArrayList<Integer>();
		 ArrayList<Integer> uniqueList = new ArrayList<Integer>();
		 
		 for (Integer item : emp_ids_under_specific_department) {
			    if (emp_ids_from_time_table_employee.contains(item)) {
			    	 System.out.println("(((((((((((((((((((---3----)))))))))))))))))))))))))) "+item);
			        duplicateList.add(item);
			    } else {
			    	 System.out.println("(((((((((((((((((((=========4=============)))))))))))))))))))))))))) "+item);
			        uniqueList.add(item);
			    }
			}
		 System.out.println("(((((((((((((((((((5)))))))))))))))))))))))))) "+uniqueList);
		 List<Map<String, Object>> data = empDetail_repo.getStudentData1(uniqueList);
		 return data;
	}

	public String makeEmployeePermanent(Integer emp_id, JwtDetails jwtDetails,String permanent_remarks) throws ParseException {
		
		EmployeeDetails emp_details = empDetail_repo.getEmployeeDetailsByEmployeeId(emp_id);
		LocalDate currentDate=LocalDate.now();
		String[] endDateSplit=emp_details.getTo_date().substring(0, 10).split("-");
		System.out.println(" endDateSplit[0] " + Integer.parseInt(endDateSplit[0]) + " endDateSplit[1]" + Integer.parseInt(endDateSplit[1]) +" endDateSplit[2] "+Integer.parseInt(endDateSplit[2]));
		LocalDate endDate=new LocalDate(Integer.parseInt(endDateSplit[2]),Integer.parseInt(endDateSplit[1]),Integer.parseInt(endDateSplit[0]));
		if(currentDate.isAfter(endDate)) {
			empDetail_repo.makeEmployeePermanent(emp_id, currentDate.toString(), empDetail_repo.getEmployeeDataByUserID(jwtDetails.getUserId()).getEmployee_name(),permanent_remarks);

			
			ModelMapper modelMapper = new ModelMapper();
		    modelMapper.addMappings(new PropertyMap<EmployeeDetails, EmployeeDetailsHistory>() {
		        @Override
		        protected void configure() {
		            map().setEmployee_status(source.getMartial_status()); // Explicitly map the correct source property
		            // Add mappings for other fields if needed
		        }
		    });
		    EmployeeDetailsHistory employeeDetailsHistory=modelMapper.map(emp_details, EmployeeDetailsHistory.class);
			employeeDetailsHistoryRepository.save(employeeDetailsHistory);
		} else {
			throw new RuntimeException("Probationary period not yet completed! we can't make the employee permanent!");
		}
		assignNewLeaveKitty(emp_id, "permanent");
		handleCLForNewPermanentStaff(emp_id);
		return "Employee updated to permanent staff successfully ";

	}
	
//	LocalDate date1 = LocalDate.fromDateFields(date_of_permanent_DateFormat);
//	Integer leave_pattern_id = leave_pattern_repo.fetchLeavePatternId(emp_details.getEmp_type_id(),emp_details.getJob_type_id(),
//			emp_details.getSchool_id(),date1.getYear());
//
//	Integer available_leave = empLeaveRepo.getUpdatedDaysCount(emp_id,leave_pattern_id);
//	
//	if(date1.getMonthOfYear() <=6) {
//		if(date1.getDayOfMonth()<20) {
//			Integer leaved_to_be_added = (6 - date1.getMonthOfYear()) + available_leave + 1;
//			System.out.println("(((((((((((((()))))))))))))) "+leaved_to_be_added);
//			empLeaveRepo.updateAvailabeLeaves(emp_id,leave_pattern_id,leaved_to_be_added);
//		} else {
//			Integer leaved_to_be_added = (6 - date1.getMonthOfYear()) + available_leave;
//			System.out.println("(((((((((((((()))))))))))))) "+leaved_to_be_added);
//			empLeaveRepo.updateAvailabeLeaves(emp_id,leave_pattern_id,leaved_to_be_added);
//		}
//	} else {
//		if(date1.getDayOfMonth()<20) {
//			Integer leaved_to_be_added = (12 - date1.getMonthOfYear()) + available_leave + 1;
//			System.out.println("(((((((((((((()))))))))))))) "+leaved_to_be_added);
//			empLeaveRepo.updateAvailabeLeaves(emp_id,leave_pattern_id,leaved_to_be_added);
//		} else {
//			Integer leaved_to_be_added = (12 - date1.getMonthOfYear()) + available_leave;
//			System.out.println("(((((((((((((()))))))))))))) "+leaved_to_be_added);
//			empLeaveRepo.updateAvailabeLeaves(emp_id,leave_pattern_id,leaved_to_be_added);
//		}
//	}
	
//	public void updateEmployeeLeave(Integer school_id, Integer dept_id) {
//		empDetail_repo.getEmailList(school_id, dept_id);
//	}
	
	public List<Map<String,Object>> employeeDetailsForTimeTableView(Integer time_table_id,Date selected_date){
		return empDetail_repo.employeeDetailsForTimeTableView(time_table_id,selected_date);
	}

	public EmployeeContractsAttachment uploadContractsFile(EmployeeIDsAttachmentRequestDTO dto) {
		EmployeeContractsAttachment attachment1= new EmployeeContractsAttachment();
		EmployeeDetails emp=	empDetail_repo.findById(dto.getEmpId())
				.orElseThrow(() -> new ResourceNotFoundException("employee with id not present"));
		if(dto.getDocument() ==null ||  dto.getDocument().isEmpty()) {
			throw new ResourceNotFoundException("please add document");
		}
		if (dto.getDocumentType().equals(EMPLOYMENT_CONTRACT) ||
				dto.getDocumentType().equals(WORK_CONTRACTS) ||
				dto.getDocumentType().equals(NDA_NCA) || 
				dto.getDocumentType().equals(EMPLOYMENT_OFFER)|| dto.getDocumentType().equals(PROFESSIONAL_EXPERIENCE)
				|| dto.getDocumentType().equals(DECLARATION) || dto.getDocumentType().equals(OTHERS)) {
			try {
				File file = convertMultiPartToFile(dto.getDocument());
				String fileName = generateFileName(dto.getDocument());
				attachment1.setEmployeeId(emp.getEmp_id());
				attachment1.setActive(true);
				attachment1.setDocumentType(dto.getDocumentType());
				attachment1.setFileName(fileName);
				attachment1.setFilePath(LocalDate.now() + "/" + dto.getEmpId()+ "/" + fileName);
				attachment1.setFileType(
						endpointUrl + "/" + bucketName + "/" + value1 + "/" + LocalDate.now() + "/" + dto.getEmpId() + "/" + fileName);
				uploadFileToS3Bucket(fileName, file, dto.getEmpId());	
				file.delete();
				} catch (AmazonServiceException e) {
				    
				    e.printStackTrace();
				} catch (SdkClientException e) {
				    e.printStackTrace();
				
				} catch (IOException e) {
					e.printStackTrace();
				}
				employeeContractsAttachmentRepo.save(attachment1);
			}
			else {
				throw new IllegalArgumentException("please enter proper document type");

				}

			return attachment1;
		}
	
	public EmployeeDetails updateEmployeeDetailsData(EmployeeDetails edo) {
		return empDetail_repo.save(edo);
	}

	
	public EmployeeDetails updateEmployeeById(@Valid EmployeeDetails ed) {
		return empDetail_repo.save(ed);
		}
	
	public ResponseEntity<Object> getAllInActiveDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> employee_filtered_response = empDetail_repo.getAllInActiveDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_filtered_response);
	}

	public ResponseEntity<Object> getAllInActiveSortedData(Pageable pageable) {
		Page<Object> employee_sorted_response = empDetail_repo.getAllInActiveSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_sorted_response);
	}
	
	public List<Map<String, Object>> getEmployeeDetailsData() {
		return empDetail_repo.getEmployeeDetailsData();
	}
	
	public List<Map<String, Object>> getStaffDetailsData() {
		return empDetail_repo.getStaffDetailsData();
	}
	
	public EmployeeDetails getEmployeeDetailsData(Integer id) {
		return empDetail_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee Details Not Found:" + id));
	}	

//	public List<Map<String, Object>> getEmployeeDataForIdCard(Integer schoolId, Integer departmentId) {
//		return empDetail_repo.getEmployeeDataForIdCard(schoolId,departmentId);
//	}

	public List<Map<String, Object>> getEmployeeDataForIdCard(Integer deptId, Integer schoolId) {
		return empDetail_repo.getEmployeeDataForIdCardNew(deptId,schoolId);
	}
	
	public List<Map<String, Object>> getEmployeeDetailsForIdCardWoHistory(Integer deptId, Integer schoolId) {
		return empDetail_repo.getEmployeeDetailsForIdCardWoHistory(deptId,schoolId);
	}
	
	public EmployeeDetails updateEmployeeKeySkills(EmployeeDetails edo, String jwtToken) 
			throws JsonParseException, JsonMappingException, IOException {

		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		EmployeeDetails employee = empDetail_repo.findById(edo.getEmp_id())
				.orElseThrow(()-> new ResourceNotFoundException("Employee not found"));

		employee.setKey_skills(edo.getKey_skills());
		employee.setModified_by(jwtDetails.getUserId());
		employee.setModified_username(jwtDetails.getUserName());
		return empDetail_repo.save(employee);
	}	
	
	public void deactivateEmployeeDetails(Integer emp_id) {
		empDetail_repo.deactivateEmployeeDetails(emp_id);
	}
	
	public HashMap<String, Object> getEmployeeDetailsByUserID(Integer user_id) {
		return empDetail_repo.getEmployeeDetailsByUserID(user_id);
	}
	
	public Map<String, Object> getEmployeeDetailsBasedOnUserID(Integer user_id) {
		return empDetail_repo.getEmployeeDetailsBasedOnUserID(user_id);
	}
	
	public EmployeeDetails getEmployeeDetailsByJobId(Integer job_id) {
		return empDetail_repo.getEmployeeDetailsByJobId(job_id);
			}
	
	public void updateEmployeeDetailsAfterRejoin(EmployeeDetails employeeDetails){
		  empDetail_repo.save(employeeDetails);
			}
	
	public List<Map<String,Object>> getEmployeeNameConcateWithEmployeeCodeAndDept() {	
		return empDetail_repo.getEmployeeNameConcateWithEmployeeCodeAndDept();
	}
	
	public ResponseEntity<Object> getAllNewJoineeDetailsDataKeyword(Pageable pageable, Object keyword) {
		Page<Object> employee_filtered_response = empDetail_repo.getAllNewJoineeDetailsDataKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_filtered_response);
	}

	public ResponseEntity<Object> getAllNewJoineeDetailsDataSortedData(Pageable pageable) {
		Page<Object> employee_sorted_response = empDetail_repo.getAllNewJoineeDetailsDataSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_sorted_response);
	}
	
	public HashMap<String, Object> getNewJoineeDetailsByEmpID(Integer emp_id) {
		return empDetail_repo.getNewJoineeDetailsByEmpID(emp_id);
	}	
	
	public EmployeeDetails getEmployeeById(Integer id) {
		return empDetail_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Not Found:" + id));
	}

//	public ResponseEntity<Object> getListOfEmployeeAttendence(EmployeeSheetRequestDTO employeeSheetRequestDTO) {
//		try {
//			List<EmployeeSheetResponseDTO> employeeSheetResponseDTO =null;
//			if(employeeSheetRequestDTO.getIsConsultant().equals(Boolean.FALSE)) {
//			 employeeSheetResponseDTO = employeeSheetRepository
//						.getListOfEmployeeAttendence(employeeSheetRequestDTO);
//			}else {
//				employeeSheetResponseDTO = employeeSheetRepository
//						.getListOfEmployeeAttendenceConsultant(employeeSheetRequestDTO);
//			
//			}
//			
//			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", employeeSheetResponseDTO);
//
//		} catch (Exception e) {
//			System.out.println("Exception Occur " + e.getMessage());
//			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
//
//		}
//	}
	
	public ResponseEntity<?> getWorkingDays(Integer month, Integer year) {
		try {
			
			int sundayCount = countSundays(month, year);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, sundayCount);
				
		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, null);
			
		}
	}	
	
	private int countSundays(int month, int year) {
		int sundayCount = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1); // Setting the calendar to the first day of the specified month
        int monthlyHolidays=holidayCalenderRepository.countTotalHolidayInAMonthAndYear(month, year); 
		int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		for (int day = 1; day <= daysInMonth; day++) {
			calendar.set(year, month - 1, day);
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek == Calendar.SUNDAY) {
				sundayCount++;
			}
		}

		return daysInMonth-sundayCount-monthlyHolidays;
	}	
	
	public List<Map<String,Object>> getAttendanceOfEmployeeByEmployeeId(Integer emp_id, Date fDate, Date tDate) {
		return empDetail_repo.getAttendanceOfEmployeeByEmployeeId(emp_id,fDate,tDate);
	}

	public PaginationDTO getEmployeePayHistory(Integer school_id, Integer dept_id, Integer month, Integer year,
			Integer page, Integer page_size, Object keyword) {
		try {
			Pageable pageable=PageRequest.of(page, page_size);
			Page<EmployeePayHistoryDTO> listPage=employeePayHistoryRepository.getEmployeePayHistoryData(school_id,dept_id, month,year,keyword,pageable);	
			PaginationDTO paginationDTO=new PaginationDTO();
		    paginationDTO.setContent(listPage.getContent());
		    paginationDTO.setPageNo(page);
		    paginationDTO.setPageSize(page_size);
		    paginationDTO.setTotalElement(listPage.getTotalElements());
		    paginationDTO.setTotalPage(listPage.getTotalPages());
		    paginationDTO.setIslast(listPage.isLast());
		    
			return paginationDTO;	
		}catch(Exception e) {
		 System.out.print(e.getMessage());
		 return null;
		}

	}

	public PaySlipDetails getPaySlipDetails(Integer emp_pay_history_id) {
		try{
			PaySlipDetails paySlipDetail=employeePayHistoryRepository.getPaySlipDetails(emp_pay_history_id);
			
			List<InvPay> invPays =invPayRepository.getByEmpCodeAndMonthAndYear(paySlipDetail.getEmpCode(), paySlipDetail.getMonth(), paySlipDetail.getYear());
			List<InvPayPaySlipDTO> invPayPaySlipDTOs=new ArrayList<>();
			invPays.stream().forEach(i->{
				InvPayPaySlipDTO invPayPaySlipDTO=new InvPayPaySlipDTO();
				invPayPaySlipDTO.setInvPay(ObjectUtils.isNotEmpty(i.getInvPay())?i.getInvPay():0.0 );
				invPayPaySlipDTO.setType(ObjectUtils.isNotEmpty(i.getType())?i.getType():null);
				invPayPaySlipDTOs.add(invPayPaySlipDTO);
			});
			paySlipDetail.setInvPayPaySlipDTOs(invPayPaySlipDTOs);
			
			return paySlipDetail;
		  }catch(Exception e) {
			System.out.print(" "+ e.getMessage());
			return null;
		}
	}

	public List<Map<String, Object>> getEmployeesUnderDepartment(Integer emp_id, Date selected_date,
			Integer time_slots_id, String date) {
		List<Integer> emp_ids_from_time_table_employee = tter_ser.getEmplIdsFromTimeTableEmployee(selected_date,time_slots_id);
		System.out.println("(((((((((((((((((((---1----)))))))))))))))))))))))))) " + emp_ids_from_time_table_employee);
		
		List<Integer> employees_on_leave = leaveapplyrepo.getEmplIdsOnLeave(date);
		System.out.println("(((((((((((((((((((-------)))))))))))))))))))))))))) " + employees_on_leave);
		
		List<Integer> unavailable_employees_list = new ArrayList<Integer>();
		unavailable_employees_list.addAll(emp_ids_from_time_table_employee);
		unavailable_employees_list.addAll(employees_on_leave);
		
		List<Integer> all_emp_ids = empDetail_repo.getAllEmployees1();
		System.out.println("(((((((((((((((((((----2---)))))))))))))))))))))))))) " + all_emp_ids);
		
		ArrayList<Integer> duplicateList = new ArrayList<Integer>();
		ArrayList<Integer> uniqueList = new ArrayList<Integer>();

		for (Integer item : all_emp_ids) {
			if (unavailable_employees_list.contains(item)) {
				System.out.println("(((((((((((((((((((---3----)))))))))))))))))))))))))) " + item);
				duplicateList.add(item);
			} else {
				System.out.println("(((((((((((((((((((=========4=============)))))))))))))))))))))))))) " + item);
				uniqueList.add(item);
			}
		}
		System.out.println("(((((((((((((((((((5)))))))))))))))))))))))))) " + uniqueList);
		List<Map<String, Object>> data = empDetail_repo.getEmployeeData1(uniqueList);
		return data;
		// return
		// empDetail_repo.getEmployeesUnderDepartment(emp_id,selected_date,time_slots_id);
	}


	public EmployeeDetails updateNewJoineeDetails(EmployeeDetails edo, String jwtToken) 
			throws JsonParseException, JsonMappingException, IOException {
		
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		EmployeeDetails employee = empDetail_repo.findById(edo.getEmp_id())
		.orElseThrow(()-> new ResourceNotFoundException("Employee not found"));
	
		employee.setNew_join_status(edo.getNew_join_status());
		employee.setCancel_remark(edo.getCancel_remark());
		employee.setModified_by(jwtDetails.getUserId());
		employee.setModified_username(jwtDetails.getUserName());
		if(edo.getNew_join_status().equals(2) == true) {
			employee.setActive(false);
			employeePayHistoryRepository.updateDataByEmpId(edo.getEmp_id());
		}
		
		return empDetail_repo.save(employee);
		
	}


	public EmployeeDetails updateEmployeeDetailsUpdatableData(EmployeeDetailsUpdateDto edDto, String jwtToken) 
				throws JsonParseException, JsonMappingException, IOException{
		
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			EmployeeDetails employee = empDetail_repo.findById(edDto.getEmp_id())
			.orElseThrow(()-> new ResourceNotFoundException("Employee not found"));
			employee.setModified_by(jwtDetails.getUserId());
			employee.setModified_username(jwtDetails.getUserName());
			
			employee.setGender(ObjectUtils.isNotEmpty(edDto.getGender()) ? edDto.getGender() : employee.getGender());
			employee.setBlood_group(ObjectUtils.isNotEmpty(edDto.getBlood_group())?edDto.getBlood_group():employee.getBlood_group());
			employee.setMartial_status(ObjectUtils.isNotEmpty(edDto.getMartial_status())?edDto.getMartial_status():employee.getMartial_status());

			employee.setEmployee_name(ObjectUtils.isNotEmpty(edDto.getEmployee_name()) ? edDto.getEmployee_name() : employee.getEmployee_name());
			employee.setDateofbirth(ObjectUtils.isNotEmpty(edDto.getDateofbirth())?edDto.getDateofbirth():employee.getDateofbirth());
			employee.setMobile(ObjectUtils.isNotEmpty(edDto.getMobile())?edDto.getMobile():employee.getMobile());
			
			employee.setHometown(ObjectUtils.isNotEmpty(edDto.getHometown()) ? edDto.getHometown() : employee.getHometown());
			employee.setCurrent_location(ObjectUtils.isNotEmpty(edDto.getCurrent_location())?edDto.getCurrent_location():employee.getCurrent_location());
			employee.setReligion(ObjectUtils.isNotEmpty(edDto.getReligion())?edDto.getReligion():employee.getReligion());
			
			employee.setAlt_mobile_no(ObjectUtils.isNotEmpty(edDto.getAlt_mobile_no()) ? edDto.getAlt_mobile_no() : employee.getAlt_mobile_no());
			employee.setDlno(ObjectUtils.isNotEmpty(edDto.getDlno())?edDto.getDlno():employee.getDlno());
			employee.setDlexpno(ObjectUtils.isNotEmpty(edDto.getDlexpno())?edDto.getDlexpno():employee.getDlexpno());
			
			
			employee.setPassportexpno(ObjectUtils.isNotEmpty(edDto.getPassportexpno()) ? edDto.getPassportexpno() : employee.getPassportexpno());
			employee.setPassportno(ObjectUtils.isNotEmpty(edDto.getPassportno())?edDto.getPassportno():employee.getPassportno());
			employee.setBank_account_holder_name(ObjectUtils.isNotEmpty(edDto.getBank_account_holder_name())?edDto.getBank_account_holder_name():employee.getBank_account_holder_name());
			
			employee.setBank_account_no(ObjectUtils.isNotEmpty(edDto.getBank_account_no()) ? edDto.getBank_account_no() : employee.getBank_account_no());
			employee.setBank_branch(ObjectUtils.isNotEmpty(edDto.getBank_branch())?edDto.getBank_branch():employee.getBank_branch());
			employee.setUan_no(ObjectUtils.isNotEmpty(edDto.getUan_no())?edDto.getUan_no():employee.getUan_no());
			
			employee.setBank_ifsccode(ObjectUtils.isNotEmpty(edDto.getBank_ifsccode()) ? edDto.getBank_ifsccode() : employee.getBank_ifsccode());
			employee.setBank_id(ObjectUtils.isNotEmpty(edDto.getBank_id())?edDto.getBank_id():employee.getBank_id());
			employee.setShift_category_id(ObjectUtils.isNotEmpty(edDto.getShift_category_id())?edDto.getShift_category_id():employee.getShift_category_id());
			
			employee.setStore_indent_approver1(ObjectUtils.isNotEmpty(edDto.getStore_indent_approver1()) ? edDto.getStore_indent_approver1() : employee.getStore_indent_approver1());
			employee.setLeave_approver1_emp_id(ObjectUtils.isNotEmpty(edDto.getLeave_approver1_emp_id())?edDto.getLeave_approver1_emp_id():employee.getLeave_approver1_emp_id());
			employee.setLeave_approver2_emp_id(ObjectUtils.isNotEmpty(edDto.getLeave_approver2_emp_id())?edDto.getLeave_approver2_emp_id():employee.getLeave_approver2_emp_id());
			
			employee.setAadhar(ObjectUtils.isNotEmpty(edDto.getAadhar())?edDto.getAadhar():employee.getAadhar());
			employee.setPan_no(ObjectUtils.isNotEmpty(edDto.getPan_no())?edDto.getPan_no():employee.getPan_no());
			employee.setPersonal_email(ObjectUtils.isNotEmpty(edDto.getPersonal_email())?edDto.getPersonal_email():employee.getPersonal_email());
			
			employee.setCaste_category(ObjectUtils.isNotEmpty(edDto.getCaste_category())?edDto.getCaste_category():employee.getCaste_category());

			employee.setReport_id(ObjectUtils.isNotEmpty(edDto.getReport_id())?edDto.getReport_id():employee.getReport_id());
			employee.setChief_proctor_id(ObjectUtils.isNotEmpty(edDto.getChief_proctor_id())?edDto.getChief_proctor_id():employee.getChief_proctor_id());		
			return empDetail_repo.save(employee);
	
	}
	
	public List<HashMap<String, Object>> fetchAllLeaveApplyDetailByApproverId(Integer user_id) {
		
		Integer emp_id = uar_repo.getEmployee_id(user_id);
		List<HashMap<String, Object>> data = empDetail_repo.fetchAllLeaveApplyDetailByApproverId(emp_id);
		
		return data;

	}
	
	public void uploadFile(MultipartFile multipartFile, Integer emp_id) {

		EmployeeDetails emp_detail = empDetail_repo.findById(emp_id)
				.orElseThrow(() -> new ResourceNotFoundException("EmployeeDetails Not Found:" + emp_id));
				
			try {
				File file = convertMultiPartToFile(multipartFile);
				String fileName = generateFileName(multipartFile);
				String t2 = emp_detail.setPermanent_file(LocalDate.now() + "/" + emp_id + "/" + fileName);
				uploadFileTos3bucket(fileName, file, emp_id);
				file.delete();
				empDetail_repo.updatePath(emp_id, t2);
			} catch (AmazonServiceException ase) {

				log.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
				log.info("Error Message:    " + ase.getMessage());
				log.info("HTTP Status Code: " + ase.getStatusCode());
				log.info("AWS Error Code:   " + ase.getErrorCode());
				log.info("Error Type:       " + ase.getErrorType());
				log.info("Request ID:       " + ase.getRequestId());

			} catch (AmazonClientException ace) {
				log.info("Caught an AmazonClientException: ");
				log.info("Error Message: " + ace.getMessage());
			} catch (IOException ioe) {
				log.info("IOE Error Message: " + ioe.getMessage());

			}


	}

	private void uploadFileTos3bucket(String fileName, File file, Integer emp_id) {
		final String uniqueFileName = value3 + "/" + LocalDate.now() + "/" + emp_id + "/" + fileName; // file.getName()
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));
		
	}
	
	
	public byte[] viewFiles(String fileName) throws NoSuchFileException {
		try {
			byte[] content;
			final S3Object s3Object = s3client.getObject(bucketName, value3 + "/" + fileName);
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


	public List<Map<String, Object>> getAllActiveEmployeeDetails() {
		return empDetail_repo.getAllActiveEmployeeDetails();
	}

	public List<Map<String, Object>> getAllActiveEmployeeDetailsWithUserId() {
		return empDetail_repo.getAllActiveEmployeeDetailsWithUserId();
	}

		public void uploadImageFile(MultipartFile image_file1,Integer emp_id) throws IOException {

		EmployeeDetails emp_detail = empDetail_repo.findById(emp_id)
				.orElseThrow(() -> new ResourceNotFoundException("EmployeeDetails Not Found:" + emp_id));
		;
		try {
			log.debug("Message For Employee Details Attachment --------------");
			
			File image_file = convertMultiPartToFile1(image_file1);
			String imageFileName = generateFileName1(image_file1);
		
			log.debug("Employee Details Attachment", image_file1);
			emp_detail.setEmp_attachment_file_name(imageFileName);
			emp_detail.setEmp_attachment_path(LocalDate.now() + "/" + emp_id + "/" + image_file);
			emp_detail.setEmp_image_attachment_path(LocalDate.now() + "/" + emp_id + "/" + imageFileName);
			emp_detail.setEmp_attachement_type(endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now()
					+ "/" + emp_id + "/" + image_file);
			uploadFileToS3Bucket1(imageFileName, image_file, emp_id);
			log.debug("Message For Attachment", image_file);
			image_file.delete();
			image_file.delete();
		} catch (AmazonServiceException ase) {

			log.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
			log.info("Error Message:    " + ase.getMessage());
			log.info("HTTP Status Code: " + ase.getStatusCode());
			log.info("AWS Error Code:   " + ase.getErrorCode());
			log.info("Error Type:       " + ase.getErrorType());
			log.info("Request ID:       " + ase.getRequestId());

		} catch (AmazonClientException ace) {
			log.info("Caught an AmazonClientException: ");
			log.info("Error Message: " + ace.getMessage());
		} catch (IOException ioe) {
			log.info("IOE Error Message: " + ioe.getMessage());

		}
		empDetail_repo.save(emp_detail);
	}
	
	

		private File convertMultiPartToFile1(MultipartFile file) throws IOException {
			File convertFile = new File(file.getOriginalFilename());
			FileOutputStream fos = new FileOutputStream(convertFile);
			fos.write(file.getBytes());
			fos.close();
			return convertFile;

		}

		private String generateFileName1(MultipartFile multiPart) {
			return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
		}

		private void uploadFileToS3Bucket1(String fileName, File file, Integer emp_id) {
			final String uniqueFileName = value + "/" + LocalDate.now() + "/" + emp_id + "/" + fileName;
			s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));

		}
		
		public void uploadFile2(MultipartFile multipartFile, Integer emp_id) throws IOException {

			EmployeeDetails emp_detail = empDetail_repo.findById(emp_id)
					.orElseThrow(() -> new ResourceNotFoundException("EmployeeDetails Not Found:" + emp_id));
			;
			try {
				log.debug("Message For Employee Details Attachment --------------");
				File file = convertMultiPartToFile2(multipartFile);

				String fileName = generateFileName2(multipartFile);

				log.debug("Employee Details Attachment", file);
				emp_detail.setEmp_attachment_file_name2(fileName);
				emp_detail.setEmp_attachment_file_name2(LocalDate.now() + "/" + emp_id + "/" + fileName);
				emp_detail.setEmp_attachement_type2(endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now()
						+ "/" + emp_id + "/" + fileName);
				uploadFileToS3Bucket2(fileName, file, emp_id);
				log.debug("Message For Attachment", file);
				file.delete();

			} catch (AmazonServiceException ase) {

				log.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
				log.info("Error Message:    " + ase.getMessage());
				log.info("HTTP Status Code: " + ase.getStatusCode());
				log.info("AWS Error Code:   " + ase.getErrorCode());
				log.info("Error Type:       " + ase.getErrorType());
				log.info("Request ID:       " + ase.getRequestId());

			} catch (AmazonClientException ace) {
				log.info("Caught an AmazonClientException: ");
				log.info("Error Message: " + ace.getMessage());
			} catch (IOException ioe) {
				log.info("IOE Error Message: " + ioe.getMessage());

			}
			empDetail_repo.save(emp_detail);
		}

		private File convertMultiPartToFile2(MultipartFile file) throws IOException {
			File convertFile = new File(file.getOriginalFilename());
			FileOutputStream fos = new FileOutputStream(convertFile);
			fos.write(file.getBytes());
			fos.close();
			return convertFile;

		}

		private String generateFileName2(MultipartFile multiPart) {
			return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
		}

		private void uploadFileToS3Bucket2(String fileName, File file2, Integer emp_id) {
			final String uniqueFileName = value + "/" + LocalDate.now() + "/" + emp_id + "/" + fileName;
			s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file2));

		}

		
//		public EmployeeDetails rejoinEmployeeDetails(Integer emp_id,JwtDetails jwtDetails) {
//			
//			EmployeeDetails oldEmplyeeDetails=empDetail_repo.findById(emp_id).orElseThrow(() -> new ResourceNotFoundException("Employee Details Not Found:" + emp_id));
//			RejoinEmployeeDetails rejoinEmployeeDetails=modelMapper.map(oldEmplyeeDetails,RejoinEmployeeDetails.class);
//			EmployeeDetails rejoinEmployee=modelMapper.map(rejoinEmployeeDetails,EmployeeDetails.class);
//			String str = getEmpCode();
//			if (str.isEmpty()) {
//				int num = 0;
//				int add = 1;
//				num = num + add;
//				String formattedStr = String.format("%05d", num);
//				rejoinEmployee.setEmpcode("AI" + formattedStr);
//			} else {
//				String[] arrOfStr = str.split("I");
//				String str1 = arrOfStr[1];
//				int num = Integer.parseInt(str1);
//				int add = 1;
//				num = num + add;
//				String formattedStr = String.format("%05d", num);
//				rejoinEmployee.setEmpcode("AI" + formattedStr);
//
//			}
//			
//			rejoinEmployee.setCreated_by(jwtDetails.getUserId());
//			rejoinEmployee.setCreated_username(jwtDetails.getUserName());
//			rejoinEmployee.setEmp_id(null);
//			rejoinEmployee.setActive(true);
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//			String dateOfjoining = dateFormat.format(new Date());
//			rejoinEmployee.setDate_of_joining(dateOfjoining.toString());
//			empDetail_repo.save(rejoinEmployee);
//			Integer rejoin_emp_id= ObjectUtils.isNotEmpty( rejoinEmployee.getEmp_id())?rejoinEmployee.getEmp_id():null;
//			EmployeeDetailsHistory employeeDetailsHistory=modelMapper.map(oldEmplyeeDetails,EmployeeDetailsHistory.class);
//			employeeDetailsHistoryRepository.save(employeeDetailsHistory);
//			oldEmplyeeDetails.setActive(false);
//			empDetail_repo.save(oldEmplyeeDetails);
//			empLeave_ser.updateRejoinEmployeeLeave(rejoinEmployee,rejoin_emp_id);
//			
//			return rejoinEmployee;
//		}
		
		
		public EmployeeDetails rejoinEmployeeDetails(Integer emp_id,JwtDetails jwtDetails) {
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			EmployeeDetails oldEmplyeeDetails=empDetail_repo.findById(emp_id).orElseThrow(() -> new ResourceNotFoundException("Employee Details Not Found:" + emp_id));
			RejoinEmployeeDetails rejoinEmployeeDetails=modelMapper.map(oldEmplyeeDetails,RejoinEmployeeDetails.class);
			EmployeeDetails rejoinEmployee=modelMapper.map(rejoinEmployeeDetails,EmployeeDetails.class);
			String str = getEmpCode();
			if (str == null) {
				int num = 0;
				int add = 1;
				num = num + add;
				String formattedStr = String.format("%05d", num);
				rejoinEmployee.setEmpcode("AI" + formattedStr);
			} else {
				String[] arrOfStr = str.split("I");
				String str1 = arrOfStr[1];
				int num = Integer.parseInt(str1);
				int add = 1;
				num = num + add;
				String formattedStr = String.format("%05d", num);
				rejoinEmployee.setEmpcode("AI" + formattedStr);

			}
			
			rejoinEmployee.setCreated_by(jwtDetails.getUserId());
			rejoinEmployee.setCreated_username(jwtDetails.getUserName());
			rejoinEmployee.setEmp_id(null);
			rejoinEmployee.setActive(true);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String dateOfjoining = dateFormat.format(new Date());
			rejoinEmployee.setDate_of_joining(dateOfjoining.toString());
			empDetail_repo.save(rejoinEmployee);
			Integer rejoin_emp_id= ObjectUtils.isNotEmpty( rejoinEmployee.getEmp_id())?rejoinEmployee.getEmp_id():null;
			EmployeeDetailsHistory employeeDetailsHistory=modelMapper.map(oldEmplyeeDetails,EmployeeDetailsHistory.class);
			employeeDetailsHistoryRepository.save(employeeDetailsHistory);
			oldEmplyeeDetails.setActive(false);
			empDetail_repo.save(oldEmplyeeDetails);
			empLeave_ser.updateRejoinEmployeeLeave(rejoinEmployee,rejoin_emp_id);
			
			return rejoinEmployee;
		}



		public ResponseEntity<Object> getEmployeeDetailsForReporting() {
			try {
			List<Map<String, Object>> employeesData=empDetail_repo.getEmployeeDetailsForReporting();
			return  ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
					employeesData);
			}catch(Exception e) {
			return	ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE",
						e.getMessage());
			}
			
		}
		

	


		public EmployeeDetails updateJobTypeOfEmployee(@Valid EmployeeJobTypeDto dto, String jwtToken) 
		        throws JsonParseException, JsonMappingException, IOException {
		    JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		    EmployeeDetails empDataDetail = empDetail_repo.employeDetails(dto.getEmp_id());

		    empDataDetail.setModified_username(jwtDetails.getUserName());
		    empDataDetail.setModified_by(jwtDetails.getUserId());
		    empDataDetail.setActive(true);
		    empDataDetail.setJob_type_id(dto.getJob_type_id());
		    empDetail_repo.save(empDataDetail);

		    EmployeeDetails oldEmployeeDetails = empDetail_repo.findById(empDataDetail.getEmp_id())
		            .orElseThrow(() -> new ResourceNotFoundException("Employee Details Not Found:" + empDataDetail.getEmp_id()));

		    // Configure ModelMapper
		    ModelMapper modelMapper = new ModelMapper();
		    modelMapper.addMappings(new PropertyMap<EmployeeDetails, EmployeeDetailsHistory>() {
		        @Override
		        protected void configure() {
		            map().setEmployee_status(source.getMartial_status()); // Explicitly map the correct source property
		            // Add mappings for other fields if needed
		        }
		    });

		    // Map and save history
		    EmployeeDetailsHistory employeeDetailsHistory = modelMapper.map(oldEmployeeDetails, EmployeeDetailsHistory.class);
		    employeeDetailsHistory.setJob_short_name(dto.getJob_short_name());
		    emp_history_repo.save(employeeDetailsHistory);
			assignNewLeaveKitty(dto.getEmp_id(), "Job Change");
		    return empDataDetail;
		}


		public ResponseEntity<Object> getEmployeeDetailsForReportingById(Integer empId) {
			try {
				Map<String, Object> employeesData=empDetail_repo.getEmployeeDetailsForReportingById(empId);
				return  ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
						employeesData);
				}catch(Exception e) {
				return	ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE",
							e.getMessage());
				}
		}


		public ResponseEntity<Object> getEmployeeDetailsForFeedbackReporting() {
			try {
				List<Map<String, Object>> employeesData=empDetail_repo.getEmployeeDetailsForFeedbackReporting();
				return  ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
						employeesData);
				}catch(Exception e) {
				return	ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE",
							e.getMessage());
				}
		}

		
		
		public ResponseEntity<Object> saveInvPayDetails(InvPayDTO invPayDto, String jwtToken) {
			try {
			JwtDetails jwtDetails=jwt_service.callJwtToken(jwtToken);
			Boolean isExists=invPayRepository.existsByEmpCodeAndMonthAndYearAndType(invPayDto.getEmpCode() , invPayDto.getMonth(), invPayDto.getYear(), invPayDto.getType());
			
			if(isExists) {
				return ResponseHandler.generateResponse(true, HttpStatus.OK, "Record Already exists",
						null);
			}
			
			InvPay invPay=new InvPay();
			invPay.setEmpCode(invPayDto.getEmpCode());
			invPay.setEmployeeName(invPay.getEmployeeName());
			invPay.setInvPay(invPayDto.getInvPay());
			invPay.setMonth(invPayDto.getMonth());
			invPay.setYear(invPayDto.getYear());
			invPay.setType(invPayDto.getType());
			invPay.setRemarks(invPayDto.getRemarks());
			invPay.setCreatedBy(jwtDetails.getUserId());
			invPayRepository.save(invPay);
			
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
					null);
			}catch(Exception e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());	
			}
		}

		public ResponseEntity<Object> uploadInvPayFile(MultipartFile file, Integer month, Integer year, String jwtToken) {
			System.out.println("file "+ file.getOriginalFilename());
			try {
				 if (!CsvUtil.hasCSVFormat(file)) {
					 return ResponseHandler.generateResponse(true, HttpStatus.OK,"File is not in CSV Format",null );	 
				 }
				 JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					
				List<TemporaryInvPay> temporaryInvPays=CsvUtil.getDateFromCSV(file.getInputStream());
				InvPayBatch invPayBatch=new InvPayBatch();
				invPayBatch.setFileName(file.getOriginalFilename());
				invPayBatch.setTotalEntries(Long.valueOf(temporaryInvPays.size()));
			
				invPayBatchRepository.save(invPayBatch);
				invPayBatch.setBatchCode("BATCH-"+invPayBatch.getInvPayBatchId());
				invPayBatchRepository.save(invPayBatch);
				temporaryInvPays.stream().forEach(t->{
					t.setInvPayBatch(invPayBatch);
					t.setMonth(month); 
					t.setYear(year);
					t.setCreatedBy(jwtDetails.getUserId());
					t.setCreatedByName(jwtDetails.getUserName());
					  });
				temporaryInvPayRepository.saveAll(temporaryInvPays);
				mapTemporaryInvPayToInvPay(temporaryInvPays);
				 return ResponseHandler.generateResponse(true, HttpStatus.OK,"File saved successfully",null );
					
			}catch(Exception e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),null);	
			}
			
		}
		
		private void mapTemporaryInvPayToInvPay(List<TemporaryInvPay> temporaryInvPays) {
			List<InvPay> invPays=new ArrayList<>();
			
			temporaryInvPays.stream().forEach(t->{
				InvPay invPay=new InvPay();
				invPay.setEmpCode(t.getEmpCode());
				invPay.setEmployeeName(t.getEmployeeName());
				invPay.setInvPay(t.getInvPay());
				invPay.setMonth(t.getMonth());
				invPay.setYear(t.getYear());
				invPay.setInvPayBatch(t.getInvPayBatch());
				invPay.setCreatedBy(t.getCreatedBy());
				invPay.setCreatedByName(t.getCreatedByName());
				invPay.setType(t.getType());
				invPay.setRemarks(t.getRemarks());
				invPays.add(invPay);
			});
			
			invPayRepository.saveAll(invPays);
		}
		
		
		public ResponseEntity<Object> getAllInvPayDataFilteredByKeyword(Pageable pageable, Object keyword) {
			Page<Object> employee_filtered_response = invPayRepository.getAllInvPayDataFilteredByKeyword(pageable, keyword);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_filtered_response);
		}

		public ResponseEntity<Object> getAllInvPaySortedData(Pageable pageable) {
			Page<Object> employee_filtered_response1 = invPayRepository.getAllInvPaySortedData(pageable);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, employee_filtered_response1);
		}

		public ResponseEntity<Object> getAllDataFilteredByKeywordUserId(Pageable pageable, Object keyword, String jwtToken) throws JsonParseException, JsonMappingException, IOException {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				Integer EmpId = empDetail_repo.getEmployeeId(jwtDetails.getUserId());
				Page<Object> employee_filtered_response = empDetail_repo.getAllDataFilteredByKeywordUserId(pageable, keyword, EmpId );
				return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_filtered_response);
			
			}

		public ResponseEntity<Object> getAllSortedDataUserId(Pageable pageable, String jwtToken) throws JsonParseException, JsonMappingException, IOException {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				Integer EmpId = empDetail_repo.getEmployeeId(jwtDetails.getUserId());
				Page<Object> employee_sorted_response = empDetail_repo.getAllSortedDataUserId(pageable, EmpId);
				return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, employee_sorted_response);
			
		}

		public PaginationDTO getEmployeeMasterSalary(Integer school_id, Integer dept_id, Integer month, Integer year,
				Integer page, Integer page_size, Object keyword) {
			try {
				Pageable pageable=PageRequest.of(page, page_size);
				Page<EmployeePayHistoryDTO> listPage=masterSalaryRepository.getEmployeePayHistoryData(school_id,dept_id, month,year,keyword,pageable);	
				PaginationDTO paginationDTO=new PaginationDTO();
			    paginationDTO.setContent(listPage.getContent());
			    paginationDTO.setPageNo(page);
			    paginationDTO.setPageSize(page_size);
			    paginationDTO.setTotalElement(listPage.getTotalElements());
			    paginationDTO.setTotalPage(listPage.getTotalPages());
			    paginationDTO.setIslast(listPage.isLast());
			    
				return paginationDTO;	
			}catch(Exception e) {
			 System.out.print(e.getMessage());
			 return null;
			}

		}

		public ResponseEntity<?> employeeLeaveTriggerForRejoinedEmployees() {
			EmployeeLeaveForRejoinEmployeesEvent employeeLeaveForRejoinEmployeesEvent=new EmployeeLeaveForRejoinEmployeesEvent("Employee leave");
			applicationEventPublisher.publishEvent(employeeLeaveForRejoinEmployeesEvent);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "Employee Leaves for Rejoin Employee Started Running",null);			
			
		}

		public ResponseEntity<?> employeeLeavePatternTrigger() {
			EmployeeLeavePatternEvent employeeLeavePatternEvent=new EmployeeLeavePatternEvent("Employee Leave Pattern");
			applicationEventPublisher.publishEvent(employeeLeavePatternEvent);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "Employee Leaves Pattern Started Running",null);			
			
		}

		public ResponseEntity<Object> getAllDataFilteredByKeywordWoCon(Pageable pageable, Object keyword, Integer month,
				Integer year, Integer school_id, Integer dept_id) {
			System.out.println("########### with out But sclId And deptId ############## " );
			Page<Object> oc_filtered_response = employeeSheetRepository.getAllDataFilteredByKeywordWoCon(pageable, keyword, month, year, school_id, dept_id);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
		}
		
		public ResponseEntity<Object> getAllDataFilteredByKeywordWoConSclIdDeptId(Pageable pageable, Object keyword, Integer month,
				Integer year) {
			System.out.println("########### without con And WO sclId And deptId ############## " );
			Page<Object> oc_filtered_response = employeeSheetRepository.getAllDataFilteredByKeywordWoConSclIdDeptId(pageable, keyword, month, year);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
		}

		public ResponseEntity<Object> getAllDataFilteredByKeywordWithCon(Pageable pageable, Object keyword,
				Integer month, Integer year, Integer school_id, Integer dept_id, String empTypeShortName) {
			System.out.println("########### with con And sclId And deptId ############## " +empTypeShortName);
			Page<Object> oc_filtered_response = employeeSheetRepository.getAllDataFilteredByKeywordWithCon(pageable, keyword, month, year, school_id, dept_id, empTypeShortName);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
		}
		
		
		public ResponseEntity<Object> getAllDataFilteredByKeywordWithConWoSclIdDeptId(Pageable pageable, Object keyword,
				Integer month, Integer year, String empTypeShortName) {
			System.out.println("########### with con But Wo sclId And deptId ############## " +empTypeShortName);
			Page<Object> oc_filtered_response = employeeSheetRepository.getAllDataFilteredByKeywordWithConWoSclIdDeptId(pageable, keyword, month, year, empTypeShortName);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
		}
		
		
		public ResponseEntity<Object> getAllSortedDataWithCon(Pageable pageable1, Integer month, Integer year,
				Integer school_id, Integer dept_id, String empTypeShortName) {
			System.out.println("*************** with con(no key) And with sclId And deptId ********************** " +empTypeShortName);
			Page<Object> oc_sorted_response = employeeSheetRepository.getAllSortedDataWithCon(pageable1, month, year, school_id, dept_id, empTypeShortName);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
		}
		
		public ResponseEntity<Object> getAllSortedKeywordWithConWoSclIdDeptId(Pageable pageable1, Integer month, Integer year, String empTypeShortName) {
			System.out.println("*************** with con(no key) And without sclId And deptId ********************** " +empTypeShortName);
			Page<Object> oc_sorted_response = employeeSheetRepository.getAllSortedKeywordWithConWoSclIdDeptId(pageable1, month, year, empTypeShortName);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
		}
		
		
		public ResponseEntity<Object> getAllSortedDataWoCon(Pageable pageable1, Integer month, Integer year,
				Integer school_id, Integer dept_id) {
			System.out.println("**************** without con(no key) And with sclId And deptId ********************** " );
			Page<Object> oc_sorted_response = employeeSheetRepository.getAllSortedDataWoCon(pageable1, month, year, school_id, dept_id);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
		}
		
		public ResponseEntity<Object> getAllSortedDataWoConSclIdDeptId(Pageable pageable1, Integer month, Integer year) {
			System.out.println("**************** without con (no key)And without sclId And deptId ********************** " );
			Page<Object> oc_sorted_response = employeeSheetRepository.getAllSortedDataWoConSclIdDeptId(pageable1, month, year);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
		}

		public ResponseEntity<Object> getAllDataFilteredByKeywordWithConAndWithSclId(Pageable pageable, Object keyword,
				Integer month, Integer year, Integer school_id, String empTypeShortName) {
			System.out.println("**************** without con (no key)And without deptId ********************** " );
			Page<Object> oc_filtered_response = employeeSheetRepository.getAllDataFilteredByKeywordWithConAndWithSclId(pageable, keyword, month, year, school_id, empTypeShortName);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
		}

		public ResponseEntity<Object> getAllDataFilteredByKeywordWithConAndWithDeptId(Pageable pageable, Object keyword,
				Integer month, Integer year, Integer dept_id, String empTypeShortName) {
			System.out.println("**************** without con (no key)And without SclId ********************** " );
			Page<Object> oc_filtered_response = employeeSheetRepository.getAllDataFilteredByKeywordWithConAndWithDeptId(pageable, keyword, month, year,dept_id, empTypeShortName);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
		}

		public ResponseEntity<Object> getAllDataFilteredByKeywordWoConAndWithSclId(Pageable pageable, Object keyword,
				Integer month, Integer year, Integer school_id) {
			System.out.println("########### with out But with DeptId ############## " );
			Page<Object> oc_filtered_response = employeeSheetRepository.getAllDataFilteredByKeywordWoConAndWithSclId(pageable, keyword, month, year, school_id);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
		}

		public ResponseEntity<Object> getAllDataFilteredByKeywordWoAndWithDeptId(Pageable pageable, Object keyword,
				Integer month, Integer year, Integer dept_id) {
			System.out.println("########### with out But with sclId ############## " );
			Page<Object> oc_filtered_response = employeeSheetRepository.getAllDataFilteredByKeywordWoAndWithDeptId(pageable, keyword, month, year, dept_id);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
		}

		public ResponseEntity<Object> getAllSortedKeywordWithConAndWithSclId(Pageable pageable, Integer month,
				Integer year, Integer school_id, String empTypeShortName) {
			System.out.println("*************** with con(no key) And with sclId ********************** " +empTypeShortName);
			Page<Object> oc_sorted_response = employeeSheetRepository.getAllSortedKeywordWithConAndWithSclId(pageable, month, year, school_id, empTypeShortName);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
		}

		public ResponseEntity<Object> getAllSortedKeywordWithConAndWithDeptId(Pageable pageable, Integer month,
				Integer year, Integer dept_id, String empTypeShortName) {
			System.out.println("*************** with con(no key) And with deptId ********************** " +empTypeShortName);
			Page<Object> oc_sorted_response = employeeSheetRepository.getAllSortedKeywordWithConAndWithDeptId(pageable, month, year, dept_id, empTypeShortName);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
		}

		public ResponseEntity<Object> getAllSortedDataWoConAndwithSclId(Pageable pageable1, Integer month, Integer year,
				Integer school_id) {
			System.out.println("**************** without con(no key) And with sclId ********************** " );
			Page<Object> oc_sorted_response = employeeSheetRepository.getAllSortedDataWoConAndwithSclId(pageable1, month, year, school_id);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
		}

		public ResponseEntity<Object> getAllSortedDataWoConAndWithDeptId(Pageable pageable1, Integer month,
				Integer year, Integer dept_id) {
			System.out.println("**************** without con(no key) And with deptId ********************** " );
			Page<Object> oc_sorted_response = employeeSheetRepository.getAllSortedDataWoConAndWithDeptId(pageable1, month, year, dept_id);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
		}

		public List<Map<String, Object>> getEmpLeaveApproverBasedOnUserId(Integer user_id) {
			return uar_repo.getleaveApprover(user_id);

		}
		
		
		public Map<String, Object> getDeptIdAndSchoolIdBasedOnUser(Integer user_id) {
			return empDetail_repo.getDeptIdAndSchoolIdBasedOnUser(user_id);
		}

		public List<Map<String, Object>> getEmpDetailsBasedOnApprover(Integer emp_id) {
			return empDetail_repo.getEmpDetailsBasedOnApprover(emp_id);
		}

		public ResponseEntity<?> getEmployeeMasterSalaryById(Integer id) {
			try {
				MasterSalaryHistoryDTO masterSalaryHistoryDTO=masterSalaryRepository.getMasterPaySlipDetails(id);
				List<InvPay> invPays =invPayRepository.getByEmpCodeAndMonthAndYear(masterSalaryHistoryDTO.getEmpCode(), masterSalaryHistoryDTO.getMonth(), masterSalaryHistoryDTO.getYear());
				List<InvPayPaySlipDTO> invPayPaySlipDTOs=new ArrayList<>();
				invPays.stream().forEach(i->{
					InvPayPaySlipDTO invPayPaySlipDTO=new InvPayPaySlipDTO();
					invPayPaySlipDTO.setInvPay(ObjectUtils.isNotEmpty(i.getInvPay())?i.getInvPay():0.0 );
					invPayPaySlipDTO.setType(ObjectUtils.isNotEmpty(i.getType())?i.getType():null);
					invPayPaySlipDTOs.add(invPayPaySlipDTO);
				});
				masterSalaryHistoryDTO.setInvPayPaySlipDTOs(invPayPaySlipDTOs);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",masterSalaryHistoryDTO);
				
			}catch(Exception e) {
				return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE",null);
					
			}
			
		}

	public Map<String, Object> getSchoolDetailsBasedOnEmpId(Integer emp_id) {
		return empDetail_repo.getSchoolDetailsBasedOnEmpId(emp_id);
	}
	
	public List<Map<String, Object>> getDeptAndDesignationBasedOnEmpIds(String emp_ids) {
		List<Integer> empIds=ResponseHandler.toConvertCommaSeperatedIdsAsList(emp_ids);
		return empDetail_repo.getDeptAndDesignationBasedOnEmpId(empIds);
	}

	public List<Map<String, Object>> getcountOfDesignationBasedOnHod(Integer leave_approver1_emp_id) {
		return empDetail_repo.getcountOfDesignationBasedOnHod(leave_approver1_emp_id);
	}

	public List<Map<String, Object>> getEmployeeDetailsDataBasedOnEmpId(Integer leave_approver1_emp_id,
			Integer designation_id) {

		List<Map<String, Object>> data1 = empDetail_repo.getEmployeeDetailsDataBasedOnEmpId(leave_approver1_emp_id,
				designation_id);

		if (data1.isEmpty()) {
			return Collections.emptyList();
		}

		List<Map<String, Object>> combinedData = new ArrayList<>();

		for (Map<String, Object> employee : data1) {
			Integer emp_id = (Integer) employee.get("emp_id");

			Map<String, Object> studentDetails = psar_repo.getCountOfStudentBasedOnEmpId(emp_id);
			Map<String, Object> courseDetails = courseAssignmentEmployeeRepository.getCountOfCourseBasedOnEmpId(emp_id);

			Map<String, Object> employeeData = new HashMap<>();

			Map<String, Object> studentDetailsMap = new HashMap<>();
			studentDetailsMap.put("studentCount",
					studentDetails != null ? studentDetails.getOrDefault("studentCount", 0) : 0);
			studentDetailsMap.put("maleStudentCount",
					studentDetails != null ? studentDetails.getOrDefault("maleStudentCount", 0) : 0);
			studentDetailsMap.put("femaleStudentCount",
					studentDetails != null ? studentDetails.getOrDefault("femaleStudentCount", 0) : 0);
			studentDetailsMap.put("employee_name",
					studentDetails != null ? studentDetails.getOrDefault("employee_name", null) : null);
			studentDetailsMap.put("emp_id",
					studentDetails != null ? studentDetails.getOrDefault("emp_id", null) : null);

			Map<String, Object> courseDetailsMap = new HashMap<>();
			courseDetailsMap.put("user_id", courseDetails != null ? courseDetails.getOrDefault("user_id", null) : null);
			courseDetailsMap.put("Coursecount",
					courseDetails != null ? courseDetails.getOrDefault("Coursecount", 0) : 0);
			courseDetailsMap.put("username",
					courseDetails != null ? courseDetails.getOrDefault("username", null) : null);

			Map<String, Object> employeeDetailsMap = new HashMap<>();
			employeeDetailsMap.put("emp_id", employee.get("emp_id"));
			employeeDetailsMap.put("empcode", employee.get("empcode"));
			employeeDetailsMap.put("designation_short_name", employee.get("designation_short_name"));
			employeeDetailsMap.put("designation_id", employee.get("designation_id"));
			employeeDetailsMap.put("employee_name", employee.get("employee_name"));
			employeeDetailsMap.put("gender", employee.get("gender"));
			employeeDetailsMap.put("designation_name", employee.get("designation_name"));

			employeeData.put("studentDetails", studentDetailsMap);
			employeeData.put("courseDetails", courseDetailsMap);
			employeeData.put("employeeDetails", employeeDetailsMap);

			combinedData.add(employeeData);
		}

		// Return the combined data list
		return combinedData;
	}

	public List<Map<String, Object>> getHodStudentCount(Integer leave_approver1_emp_id) {
		return empDetail_repo.getHodStudentCount(leave_approver1_emp_id);
	}
	

	public Map<String, Object> getCountOfEmployeeAndStrudent(Integer leave_approver1_emp_id) {
		Map<String, Object> hodStudentCount = empDetail_repo.getHodStudentCount1(leave_approver1_emp_id);
		Map<String, Object> hodEmployeeCount = empDetail_repo.getHodEmployeeCount1(leave_approver1_emp_id);

		Map<String, Object> combinedResult = new HashMap<>();
		
		combinedResult.putAll(hodStudentCount);
		combinedResult.putAll(hodEmployeeCount);

		return combinedResult;
	}
	
	
	public List<Map<String, Object>> getHoiStudentCount(Integer report_id) {
		Integer schoolId = empDetail_repo.getSchoolIdBasedOnHoi(report_id);
		return empDetail_repo.getHoiStudentCount(schoolId);
	}

	public List<Map<String, Object>> getCountOfDesignationBasedOnHoi(Integer report_id) {
		Integer schoolId = empDetail_repo.getSchoolIdBasedOnHoi(report_id);
		return empDetail_repo.getCountOfDesignationBasedOnHoi(schoolId);
	}
	
	public Map<String, Object> getCountOfEmployeeAndStrudentBasedOnHoi(Integer report_id) {
		Integer schoolId = empDetail_repo.getSchoolIdBasedOnHoi(report_id);
		Map<String, Object> hodStudentCount = empDetail_repo.getHodStudentCount1Hoi(schoolId);
		Map<String, Object> hodEmployeeCount = empDetail_repo.getHodEmployeeCount1Hoi(schoolId);

		Map<String, Object> combinedResult = new HashMap<>();
		
		combinedResult.putAll(hodStudentCount);
		combinedResult.putAll(hodEmployeeCount);

		return combinedResult;
	}

	public List<Map<String, Object>> getCountOfJobTypeBasedOnHoi(Integer report_id) {
		Integer schoolId = empDetail_repo.getSchoolIdBasedOnHoi(report_id);
		return empDetail_repo.getCountOfJobTypeBasedOnHoi(schoolId);
	}

	public List<Map<String, Object>> getCountOfGenderBasedOnHoi(Integer report_id) {
		Integer schoolId = empDetail_repo.getSchoolIdBasedOnHoi(report_id);
		return empDetail_repo.getCountOfGenderBasedOnHoi(schoolId);
	}
	

	public List<Map<String, Object>> getCountOfDeptBasedOnHoi(Integer report_id) {
		Integer schoolId = empDetail_repo.getSchoolIdBasedOnHoi(report_id);
		return empDetail_repo.getCountOfDeptBasedOnHoi(schoolId);
	}

	public List<Map<String, Object>> getCountOfAgeGroupBasedOnHoi(Integer report_id) {
		Integer schoolId = empDetail_repo.getSchoolIdBasedOnHoi(report_id);
		return empDetail_repo.getCountOfAgeGroupBasedOnHoi(schoolId);
	}

	public List<Map<String, Object>> getCountOfDateOfJoiningMonthWiseBasedOnHoi(Integer report_id) {
		Integer schoolId = empDetail_repo.getSchoolIdBasedOnHoi(report_id);
		return empDetail_repo.getCountOfDateOfJoiningMonthWiseBasedOnHoi(schoolId);
	}


	public List<Map<String, Object>> getEmployeeDetailsDataBasedOnReportId(Integer report_id,Integer designation_id) {
		Integer schoolId = empDetail_repo.getSchoolIdBasedOnHoi(report_id);

		List<Map<String, Object>> data1 = empDetail_repo.getEmployeeDetailsDataBasedOnReportId(schoolId,designation_id);

		if (data1.isEmpty()) {
			return Collections.emptyList();
		}

		List<Map<String, Object>> combinedData = new ArrayList<>();

		for (Map<String, Object> employee : data1) {
			Integer emp_id = (Integer) employee.get("emp_id");

			Map<String, Object> studentDetails = psar_repo.getCountOfStudentBasedOnEmpId(emp_id);
			Map<String, Object> courseDetails = courseAssignmentEmployeeRepository.getCountOfCourseBasedOnEmpId(emp_id);

			Map<String, Object> employeeData = new HashMap<>();

			Map<String, Object> studentDetailsMap = new HashMap<>();
			studentDetailsMap.put("studentCount",
					studentDetails != null ? studentDetails.getOrDefault("studentCount", 0) : 0);
			studentDetailsMap.put("maleStudentCount",
					studentDetails != null ? studentDetails.getOrDefault("maleStudentCount", 0) : 0);
			studentDetailsMap.put("femaleStudentCount",
					studentDetails != null ? studentDetails.getOrDefault("femaleStudentCount", 0) : 0);
			studentDetailsMap.put("employee_name",
					studentDetails != null ? studentDetails.getOrDefault("employee_name", null) : null);
			studentDetailsMap.put("emp_id",
					studentDetails != null ? studentDetails.getOrDefault("emp_id", null) : null);

			Map<String, Object> courseDetailsMap = new HashMap<>();
			courseDetailsMap.put("user_id", courseDetails != null ? courseDetails.getOrDefault("user_id", null) : null);
			courseDetailsMap.put("Coursecount",
					courseDetails != null ? courseDetails.getOrDefault("Coursecount", 0) : 0);
			courseDetailsMap.put("username",
					courseDetails != null ? courseDetails.getOrDefault("username", null) : null);

			Map<String, Object> employeeDetailsMap = new HashMap<>();
			employeeDetailsMap.put("emp_id", employee.get("emp_id"));
			employeeDetailsMap.put("empcode", employee.get("empcode"));
			employeeDetailsMap.put("designation_short_name", employee.get("designation_short_name"));
			employeeDetailsMap.put("designation_id", employee.get("designation_id"));
			employeeDetailsMap.put("employee_name", employee.get("employee_name"));
			employeeDetailsMap.put("gender", employee.get("gender"));
			employeeDetailsMap.put("designation_name", employee.get("designation_name"));

			employeeData.put("studentDetails", studentDetailsMap);
			employeeData.put("courseDetails", courseDetailsMap);
			employeeData.put("employeeDetails", employeeDetailsMap);

			combinedData.add(employeeData);
		}
		return combinedData;
	}

	
	public EmployeeDetails updateDeptByEmpId(@Valid EmployeeDeptDto dto, String jwtToken) 
	        throws JsonParseException, JsonMappingException, IOException {
	    JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    EmployeeDetails empDataDetail = empDetail_repo.employeDetails(dto.getEmp_id());

	    empDataDetail.setModified_username(jwtDetails.getUserName());
	    empDataDetail.setModified_by(jwtDetails.getUserId());
	    empDataDetail.setActive(true);
	    empDataDetail.setDept_id(dto.getDept_id());
	    empDataDetail.setSchool_id(dto.getSchool_id());
		empDataDetail.setDept_name_short(dto.getDept_name_short());
		Optional<Schools> optionalSchool = sc_repo.findById(dto.getSchool_id());
        optionalSchool.ifPresent(school -> empDataDetail.setSchool(school.getSchool_name()));
	    empDetail_repo.save(empDataDetail);

	    EmployeeDetails oldEmployeeDetails = empDetail_repo.findById(empDataDetail.getEmp_id())
	            .orElseThrow(() -> new ResourceNotFoundException("Employee Details Not Found: " + empDataDetail.getEmp_id()));

	    // Configure ModelMapper
	    ModelMapper modelMapper = new ModelMapper();
	    modelMapper.addMappings(new PropertyMap<EmployeeDetails, EmployeeDetailsHistory>() {
	        @Override
	        protected void configure() {
	            map().setEmployee_status(source.getMartial_status()); // Explicitly map the correct source property
	            // Add mappings for other fields if needed
	        }
	    });

	    // Map and save history with custom status determination
	    EmployeeDetailsHistory employeeDetailsHistory = modelMapper.map(oldEmployeeDetails, EmployeeDetailsHistory.class);
	    employeeDetailsHistory.setDept_name_short(dto.getDept_name_short());
	    employeeDetailsHistory.setSchool_name_short(dto.getSchool_name_short());
	    emp_history_repo.save(employeeDetailsHistory);
		assignNewLeaveKitty(dto.getEmp_id(), "school change"); // Assigning new leave kitty after school changed
	    return empDataDetail;
	}

	private void assignNewLeaveKitty(Integer empId, String type) {

		List<Map<Integer, Double>> leaveIdAndAccumulatedCountByEmpIdList = leave_pattern_repo.findLeaveIdPatternIdAndAccumulatedCountByEmpId(empId);
		assignKitty(empId, leaveIdAndAccumulatedCountByEmpIdList, type);
	}

	private void assignKitty(Integer empId, List<Map<Integer, Double>> leaveIdAndAccumulatedCountByEmpIdList, String type) {
		int currentYear = Year.now().getValue();
		EmployeeDetailsForLeavePattern elp = empDetail_repo.getEmployeeDetailsForLeavePatternWithEmployeeId(empId);
		List<LeavePattern> leavePattern = leave_pattern_repo.getLeavePatternByEmpTypeIdAndJobTypeIdAndSchoolId(elp.getEmpTypeId(), elp.getJobTypeId(),
				elp.getSchoolId(),currentYear);
		String empType = employeeTypeRepository.getEmployeeTypeName(elp.getEmpTypeId());
		String jobType=jobTypeRepository.getJobTypeName(elp.getJobTypeId());

		leavePattern.forEach(lp ->{

			LeaveKitty leaveKitty = leaveKittyRepository.getByEmpIdAndLeaveId(empId, lp.getLeave_pattern_id());
			LeaveType leaveType = leaveTypeRepository.fetchLeaveType(lp.getLeave_id());

			if (ObjectUtils.isEmpty(leaveKitty)) {
				leaveKitty = new LeaveKitty();
				leaveKitty.setActive(Boolean.TRUE);
			}
			leaveKitty.setEmp_id(elp.getEmpId());
			leaveKitty.setInitial_days_count(lp.getLeave_days_permit());
			leaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());

			//Casual leave for probationary
			if(LeaveKittyEnum.CAUSAL_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short()) && elp.getPermanentStatus() == 1) {
				leaveKittyEventPublisher.handleCLForProbationary(leaveKitty, leaveType, lp, elp, empType);
				checkForPreviousCount(leaveKitty, leaveIdAndAccumulatedCountByEmpIdList, leaveType.getLeave_id());
			}

			//Casual leave for permanent
			if(!type.equalsIgnoreCase("permanent")) {
				if (LeaveKittyEnum.CAUSAL_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short()) && elp.getPermanentStatus() == 2) {
					leaveKittyEventPublisher.handleCLForPermanent(leaveKitty, leaveType, lp, elp, empType);
					checkForPreviousCount(leaveKitty, leaveIdAndAccumulatedCountByEmpIdList, leaveType.getLeave_id());

				}
			}
			//Earned leave for permanent
			if(!type.equalsIgnoreCase("permanent")) {
				if (LeaveKittyEnum.EARNED_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short()) && elp.getPermanentStatus() == 2) {
					leaveKittyEventPublisher.handleELForPermanent(leaveKitty, leaveType, lp, elp, empType, jobType);
					checkForPreviousCount(leaveKitty, leaveIdAndAccumulatedCountByEmpIdList, leaveType.getLeave_id());
				}
			}
			// Wedding leave
			if(LeaveKittyEnum.WEDDING_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short()) && elp.getPermanentStatus() == 2) {
				leaveKittyForYearEventPublisher.handleWL(leaveKitty, leaveType, lp, elp, empType);
			}

			//Maternity leave
			if(LeaveKittyEnum.MATERNITY_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short())&& elp.getPermanentStatus() == 2) {
				leaveKittyForYearEventPublisher.handleML(leaveKitty, leaveType, lp, elp, empType);
			}

			//Paternity leave
			if(LeaveKittyEnum.PATERNITY_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short())&& elp.getPermanentStatus() == 2) {
				leaveKittyForYearEventPublisher.handlePL(leaveKitty, leaveType, lp, elp, empType);
			}

			//Research leave
			if(LeaveKittyEnum.RESEARCH_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short())&& elp.getPermanentStatus() == 2) {
				leaveKittyForYearEventPublisher.handleRL(leaveKitty, leaveType, lp, elp, empType, jobType);
				checkForPreviousCount(leaveKitty, leaveIdAndAccumulatedCountByEmpIdList, leaveType.getLeave_id());
			}

			//Restricted Holiday Leave
			if (LeaveKittyEnum.RESTRICTED_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short())&& elp.getPermanentStatus() == 2) {
				leaveKittyForYearEventPublisher.handleRH(leaveKitty, leaveType, lp, elp, empType);
				checkForPreviousCount(leaveKitty, leaveIdAndAccumulatedCountByEmpIdList, leaveType.getLeave_id());
			}

			//Vacation Leave
			if(LeaveKittyEnum.VACATION_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short())&& elp.getPermanentStatus() == 2){
				leaveKittyForYearEventPublisher.handleVL(leaveKitty, leaveType, lp, elp, empType, jobType);
				checkForPreviousCount(leaveKitty, leaveIdAndAccumulatedCountByEmpIdList, leaveType.getLeave_id());
			}
		});
	}

	private void checkForPreviousCount(LeaveKitty leaveKitty, List<Map<Integer, Double>> leaveIdAndAccumulatedCountByEmpIdList, Integer leaveId) {
		if(leaveIdAndAccumulatedCountByEmpIdList.isEmpty()) return;
		for (Map<Integer, Double> leaveIdAndAccumulated : leaveIdAndAccumulatedCountByEmpIdList) {
			if (leaveIdAndAccumulated.get("leaveId") != null && Integer.valueOf(String.valueOf(leaveIdAndAccumulated.get("leaveId"))).equals(leaveId)){
				Double count = leaveIdAndAccumulated.get("accumulatedCount");
				if (count != null) {
					leaveKitty.setAccumulated_count(count);
					leaveKitty.setAccumulated_date(new Date());
					leaveKittyRepository.save(leaveKitty);
					break;
				}
			}
		}
	}


	public ResponseEntity<Object> updateProctorHead(@Valid ProctorHeadDto proctorHeadDto, String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);


		 List<EmployeeDetails> employeeDetails = empDetail_repo.findAll1(proctorHeadDto.getEmpId());
		 employeeDetails.stream().forEach(ed ->{
			 ed.setChief_proctor_id(proctorHeadDto.getChiefProctorId());
			 ed.setModified_by(jwtDetails.getUserId());
			 ed.setModified_username(jwtDetails.getUserName());
			 empDetail_repo.save(ed);
			 
			 EmployeeDetails oldEmployeeDetails = empDetail_repo.findById(ed.getEmp_id())
			            .orElseThrow(() -> new ResourceNotFoundException("Employee Details Not Found: " + ed.getEmp_id()));

			    // Configure ModelMapper
			    ModelMapper modelMapper = new ModelMapper();
			    modelMapper.addMappings(new PropertyMap<EmployeeDetails, EmployeeDetailsHistory>() {
			        @Override
			        protected void configure() {
			            map().setEmployee_status(source.getMartial_status()); // Explicitly map the correct source property
			            // Add mappings for other fields if needed
			        }
			    });

			    // Map and save history with custom status determination
			    EmployeeDetailsHistory employeeDetailsHistory = modelMapper.map(oldEmployeeDetails, EmployeeDetailsHistory.class);
			    employeeDetailsHistory.setDept_name_short(ed.getDept_name_short());
			    emp_history_repo.save(employeeDetailsHistory);
		 });	
		 return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
	}
	
	public List<Map<String, Object>> getEmployeeDetailsBasedOnProctor() {
		return empDetail_repo.getEmployeeDetailsBasedOnProctor();
	}

	public ResponseEntity<Object> fetchAllEmployeeDetailsBasedOnProctor(Pageable pageable, Object keyword,
			Integer school_id, Integer dept_id, Integer emp_id) {
		Page<Map<String, Object>> oc_filtered_response = empDetail_repo.fetchAllEmployeeDetailsBasedOnProctor(pageable, keyword,school_id, dept_id, emp_id);
		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, oc_filtered_response);
	}

	public ResponseEntity<Object> fetchAllEmployeeDetailsBasedOnProctor(Pageable pageable1, Integer school_id,
			Integer dept_id, Integer emp_id) {
		Page<Map<String, Object>> oc_sorted_response = empDetail_repo.fetchAllEmployeeDetailsBasedOnProctor(pageable1, school_id, dept_id, emp_id);
		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, oc_sorted_response);
	}



	public EmployeeDetails updateContractEmpCodeOfEmployee(@Valid EmployeeCodeDto dto, String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		EmployeeDetails empDataDetail = empDetail_repo.employeDetails(dto.getEmp_id());

		empDataDetail.setModified_username(jwtDetails.getUserName());
		empDataDetail.setModified_by(jwtDetails.getUserId());
		empDataDetail.setActive(true);
		empDataDetail.setContract_empcode(dto.getContract_empcode());
		empDetail_repo.save(empDataDetail);

		EmployeeDetails oldEmployeeDetails = empDetail_repo.findById(empDataDetail.getEmp_id()).orElseThrow(
				() -> new ResourceNotFoundException("Employee Details Not Found:" + empDataDetail.getEmp_id()));

		// Configure ModelMapper
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<EmployeeDetails, EmployeeDetailsHistory>() {
			@Override
			protected void configure() {
				map().setEmployee_status(source.getMartial_status()); // Explicitly map the correct source property
				// Add mappings for other fields if needed
			}
		});

		// Map and save history
		EmployeeDetailsHistory employeeDetailsHistory = modelMapper.map(oldEmployeeDetails,
				EmployeeDetailsHistory.class);
		employeeDetailsHistory.setContract_empcode(dto.getContract_empcode());
		emp_history_repo.save(employeeDetailsHistory);

		return empDataDetail;
	}

	public List<EmployeeContractsAttachment> getEmployeeWorkExperienceFileById(Integer empId) {
		EmployeeDetails emp = empDetail_repo.findById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("employee with id not present"));
		List<EmployeeContractsAttachment> findByEmployeeId = employeeContractsAttachmentRepo
				.findByEmployeeIdAndDocumentType(emp.getEmp_id());
		return findByEmployeeId;
	}
	
	
	public void employeeWorkExperienceDeactivate(Long id) {
	EmployeeIDsAttachment co = employeeIDsAttachmentRepo.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Employee Attachment Not Found:" + id));
	employeeContractsAttachmentRepo.employeeWorkExperienceDeactivate(id);
}

	public List<Map<String, Object>> getEmployeeDetailsDetailsDataBasedOnEmpId(Integer empId) {
		return empDetail_repo.getEmployeeDetailsDetailsDataBasedOnEmpId(empId);
	}

	public PaySlipDetails paySlipOfUser(Integer user_id,String Date) throws ParseException {

		EmployeeDetails employeeDetails=getEmployeeDataByUserID(user_id);
		Integer empId = employeeDetails.getEmp_id();
		DateFormat df= new SimpleDateFormat("dd-MM-yyyy");
		Date date1=df.parse(Date);
		LocalDate paySlipDate = LocalDate.fromDateFields(date1);
		Integer month=paySlipDate.getMonthOfYear();
		Integer year=paySlipDate.getYear();
		LocalDate currentDate=LocalDate.now();
		Integer schoolId = employeeDetails.getSchool_id();
		PaySlipLockDate paySlipLockDate= Optional.ofNullable(paySlipLockDateRepository.findByMonthAndYearAndActiveAndSchoolId(month.toString(),year.toString(), schoolId)).get();
		String restrictedEmployeeId=paySlipLockDate.getEmp_id() == null?DEFAULT_RESTRICTED_ID.toString(): paySlipLockDate.getEmp_id();

		if(paySlipLockDate != null) {

			Date date2=df.parse(paySlipLockDate.getDisplay_date());
			LocalDate paylockDate = LocalDate.fromDateFields(date2);

			if(year != currentDate.getYear() && !(restrictedEmployeeId.equals(empId.toString())) && (paylockDate.isBefore(currentDate) || paylockDate.isEqual(currentDate))) {

				PaySlipDetails paySlipDetailsOfEmployee = employeePayHistoryRepository.getPaySlipDetailsOfEmployee(empId, month, year);
				checkInvPayDtos(paySlipDetailsOfEmployee);
				return paySlipDetailsOfEmployee;
			}else {
				if(month != currentDate.getMonthOfYear() && !(restrictedEmployeeId.equals(empId.toString())) && (paylockDate.isBefore(currentDate) || paylockDate.isEqual(currentDate))) {
					PaySlipDetails paySlipDetailsOfEmployee =  employeePayHistoryRepository.getPaySlipDetailsOfEmployee(empId,month,year);
					checkInvPayDtos(paySlipDetailsOfEmployee);
					return paySlipDetailsOfEmployee;
				} else if(paySlipLockDate != null && !(restrictedEmployeeId.equals(empId.toString())) && (paylockDate.isBefore(currentDate) || paylockDate.isEqual(currentDate)) ) {
					PaySlipDetails paySlipDetailsOfEmployee = employeePayHistoryRepository.getPaySlipDetailsOfEmployee(empId,month,year);
					checkInvPayDtos(paySlipDetailsOfEmployee);
					return paySlipDetailsOfEmployee;
				} else {
					throw new RuntimeException("Pay Slip is not generated, Please contact to HR !! ");
				}
			}
		} else {
			throw new RuntimeException("Pay Slip is not generated, Please contact to HR !! ");
		}

	}

	private void checkInvPayDtos(PaySlipDetails paySlipDetailsOfEmployee) {
		List<InvPay> invPays =invPayRepository.getByEmpCodeAndMonthAndYear(paySlipDetailsOfEmployee.getEmpCode(),
				paySlipDetailsOfEmployee.getMonth(), paySlipDetailsOfEmployee.getYear());
		List<InvPayPaySlipDTO> invPayPaySlipDTOs=new ArrayList<>();
		invPays.stream().forEach(i->{
			InvPayPaySlipDTO invPayPaySlipDTO=new InvPayPaySlipDTO();
			invPayPaySlipDTO.setInvPay(ObjectUtils.isNotEmpty(i.getInvPay())?i.getInvPay():0.0 );
			invPayPaySlipDTO.setType(ObjectUtils.isNotEmpty(i.getType())?i.getType():null);
			invPayPaySlipDTOs.add(invPayPaySlipDTO);
		});
		paySlipDetailsOfEmployee.setInvPayPaySlipDTOs(invPayPaySlipDTOs);
	}

	public List<Map<String, Object>> getEmployeeDetailsDataBasedOnEmpId() {
		return empDetail_repo.getEmployeeDetailsDataBasedOnEmpId();
	}

	public Map<String, Object> getEmployeeDetailsDataBasedOnEmpId11111(Integer empId) {
	   Map<String, Object> employeeDetails = empDetail_repo.getEmployeeDetailsDataBasedOnEmpId11111(empId);

		try {
			// Convert the employee details map to a JSON string (or any string format) for encryption
			String employeeDetailsJson = new ObjectMapper().writeValueAsString(employeeDetails);

			// Encrypt the employee details string
			String encryptedData = EncryptionUtil.encrypt(employeeDetailsJson);

			// Return a map containing the encrypted data
			Map<String, Object> encryptedEmployeeDetails = new HashMap<>();
			encryptedEmployeeDetails.put("encryptedEmployeeDetails", encryptedData);

			return encryptedEmployeeDetails;

		} catch (Exception e) {
			// Handle encryption error
			e.printStackTrace();
			throw new RuntimeException("Error during encryption");
		}
	}
	 public Map<String, Object> encryptedEmployeeDetails(Integer id) {
		// Fetch employee details from the repository
		Map<String, Object> data = empDetail_repo.encryptedEmployeeDetails(id);

		// Handle null values by replacing them with empty strings or other default values
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			if (entry.getValue() == null) {
				entry.setValue(""); // Replace null values with empty string or another default value
			}
		}

		try {
			// Convert the data to a JSON string before encryption
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonData = objectMapper.writeValueAsString(data);

			// Encrypt the data using the encryption utility
			String encryptedData = EncryptionUtil.encrypt(jsonData);

			// Create a new map to hold the encrypted data
			Map<String, Object> encryptedResponse = new HashMap<>();
			encryptedResponse.put("encryptedEmployeeDetails", encryptedData);

			return encryptedResponse;
		} catch (Exception e) {
			// Handle encryption failure
			e.printStackTrace();
			throw new RuntimeException("Error during encryption");
		}
	}

	public void handleCLForNewPermanentStaff(Integer empId){
		Integer currentYear = Year.now().getValue();
		EmployeeDetailsForLeavePattern elp = employeeDetailsRepository.getEmployeeDetailsForLeavePatternWithEmployeeId(empId);
		List<LeavePattern> leavePattern = leavePatternRepository.getLeavePatternByEmpTypeIdAndJobTypeIdAndSchoolId(elp.getEmpTypeId(), elp.getJobTypeId(),
				elp.getSchoolId(),currentYear);
		String empType = employeeTypeRepository.getEmployeeTypeName(elp.getEmpTypeId());
		String jobType=jobTypeRepository.getJobTypeName(elp.getJobTypeId());
		leavePattern.forEach(lp -> {
			try {
				LeaveType leaveType = leaveTypeRepository.fetchLeaveType(lp.getLeave_id());
				System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id());
				LeaveKitty leaveKitty = leaveKittyRepository.getByEmpIdAndLeaveId(elp.getEmpId(), lp.getLeave_pattern_id());
				System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id() + " " + leaveType.getLeave_type_short());

				if(ObjectUtils.isEmpty(leaveKitty)) {
					leaveKitty = new LeaveKitty();
					leaveKitty.setActive(Boolean.TRUE);
				}
				if (LeaveKittyEnum.CAUSAL_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short()))
					handleCLForPermanent(leaveKitty, leaveType, lp, elp, empType);
				if (LeaveKittyEnum.RESEARCH_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short()))
					handleRLForPermanent(leaveKitty, leaveType, lp, elp, empType);
				if (LeaveKittyEnum.RESTRICTED_LEAVE.getCode().equalsIgnoreCase(leaveType.getLeave_type_short()))
					handleRHForPermanent(leaveKitty, leaveType, lp, elp, empType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void handleRHForPermanent(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp, String empType) {
		if (StringUtils.equals(empType, "ORR")
				&& ObjectUtils.isNotEmpty(elp.getPermanentStatus())
				&& elp.getPermanentStatus() == 2) {
			assignRHToORRPermanentStaff(leaveKitty, leaveType, lp, elp);
		}
	}

	private void assignRLToORRPermanentStaff(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp) {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		String year = String.valueOf(Year.now().getValue());
		Integer leaveApplyCount = leaveApplyRepository.getCountLeaveApply(elp.getEmpId(), lp.getLeave_id(), year);
		double accumulatedCount = 0.0;
		accumulatedCount = 12 - month;
		if (leaveApplyCount <= lp.getLeave_days_permit()) {
			leaveKitty.setAccumulated_count(accumulatedCount);
		} else {
			leaveKitty.setAccumulated_count(0.0);
		}
		leaveKitty.setInitial_days_count(lp.getLeave_days_permit());
		leaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
		leaveKitty.setEmp_id(elp.getEmpId());
		leaveKitty.setAccumulated_date(new Date());

		leaveKittyRepository.save(leaveKitty);
		System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short() + " " + leaveKitty.getAccumulated_count());
	}

	private void handleRLForPermanent(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp, String empType) {
		if (StringUtils.equals(empType, "ORR")
				&& ObjectUtils.isNotEmpty(elp.getPermanentStatus())
				&& elp.getPermanentStatus() == 2) {
			assignRLToORRPermanentStaff(leaveKitty, leaveType, lp, elp);
		}
	}

	private void assignRHToORRPermanentStaff(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp) {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		String year = String.valueOf(Year.now().getValue());
		Integer leaveApplyCount = leaveApplyRepository.getCountLeaveApply(elp.getEmpId(), lp.getLeave_id(), year);
		double accumalatedCount = 0.0;
		if (month <= 6) {
			accumalatedCount = ObjectUtils.isNotEmpty(leaveApplyCount) && leaveApplyCount != 0 ? 2.0 - leaveApplyCount : 2.0;
		} else {
			accumalatedCount = ObjectUtils.isNotEmpty(leaveApplyCount) && leaveApplyCount != 0 ? 1.0 - leaveApplyCount : 1.0;
		}
		if (leaveApplyCount <= lp.getLeave_days_permit()) {
			leaveKitty.setAccumulated_count(accumalatedCount);
		} else {
			leaveKitty.setAccumulated_count(0.0);
		}
		leaveKitty.setInitial_days_count(lp.getLeave_days_permit());
		leaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
		leaveKitty.setEmp_id(elp.getEmpId());
		leaveKitty.setAccumulated_date(new Date());

		leaveKittyRepository.save(leaveKitty);
		System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short() + " " + leaveKitty.getAccumulated_count());
	}

	private void handleCLForPermanent(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp, String empType) {
		if (StringUtils.equals(empType, "ORR")
				&& ObjectUtils.isNotEmpty(elp.getPermanentStatus())
				&& elp.getPermanentStatus() == 2) {
			assignClToORRPermanentStaff(leaveKitty, leaveType, lp, elp);
		}
	}

	private void assignClToORRPermanentStaff(LeaveKitty leaveKitty, LeaveType leaveType, LeavePattern lp, EmployeeDetailsForLeavePattern elp) {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		String year = String.valueOf(Year.now().getValue());
		Integer leaveApplyCount = leaveApplyRepository.getCountLeaveApply(elp.getEmpId(), lp.getLeave_id(), year);
		double accumalatedCount = 0.0;
		if (month <= 6) {
			accumalatedCount = ObjectUtils.isNotEmpty(leaveApplyCount) && leaveApplyCount != 0 ? 6.0 - leaveApplyCount : 6.0;
		} else {
			accumalatedCount = ObjectUtils.isNotEmpty(leaveApplyCount) && leaveApplyCount != 0 ? 12.0 - leaveApplyCount : 12.0;
		}
		if (leaveApplyCount <= lp.getLeave_days_permit()) {
			leaveKitty.setAccumulated_count(accumalatedCount);
		} else {
			leaveKitty.setAccumulated_count(0.0);
		}
		leaveKitty.setInitial_days_count(lp.getLeave_days_permit());
		leaveKitty.setLeave_pattern_id(lp.getLeave_pattern_id());
		leaveKitty.setEmp_id(elp.getEmpId());
		leaveKitty.setAccumulated_date(new Date());

		leaveKittyRepository.save(leaveKitty);
		System.out.println(elp.getEmpId() + " " + lp.getLeave_pattern_id()+" "+leaveType.getLeave_type_short() + " " + leaveKitty.getAccumulated_count());

	}

	public byte[] viewFiles1(String fileName) throws NoSuchFileException {
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

	public ResponseEntity<Object> employeePayHistoryReport(Integer schoolId, Integer deptId, Integer month, Integer year) {
		List<HashMap<String,Object>> payHistory=employeePayHistoryRepository.getEmployeePayHistoryData(schoolId,deptId,month,year);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, payHistory);
	}

	public ResponseEntity<Object> payReportOfEmployeeBySchoolAndBank(Integer month, Integer year) {
		List<Map<String,Object>> payReport=employeePayHistoryRepository.payReportOfEmployeeBySchoolAndBank(month,year);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, payReport);
	}

	public ResponseEntity<Object> employeesCountFromAttendanceAndPaySheet(Integer month, Integer year) {
		List<Map<String, Object>> response = new ArrayList<>();
		List<Map<String, Object>> attendanceAndPaidCount = employeePayHistoryRepository.employeesCountFromAttendanceAndPaySheet(month, year);
		List<Map<String, Object>> maternityLeaveCount = leaveApplyRepository.maternityLeaveAppliedEmp(month, year);
		attendanceAndPaidCount.stream().forEach(ap -> {
			Integer attendanceCount = ObjectUtils.isNotEmpty(ap.get("attendanceCount")) ? (Integer) ((BigInteger) ap.get("attendanceCount")).intValue() : 0;
			Integer paidCount = ObjectUtils.isNotEmpty(ap.get("paidAttendanceCount")) ? (Integer) ((BigInteger) ap.get("paidAttendanceCount")).intValue() : 0;
			Map m = modelMapper.map(ap, Map.class);
			m.put("maternity", maternityLeaveCount.size());
			m.put("Absentees/Inactive", attendanceCount - paidCount - maternityLeaveCount.size());
			response.add(m);
		});
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> totalSalarySlipByMonthAndYear(Integer month, Integer year) {
		Map<String, Object> rawResult = employeePayHistoryRepository.totalSalarySlipByMonthAndYear(month,year);
		Map<String, Object> attendanceAndPaidCount = new HashMap<>(rawResult);
		List<Map<String, Object>> invPay=invPayRepository.totalExtraRemunerationByMonthAndYear(month,year);
		attendanceAndPaidCount.put("totalExtraRemuneration",invPay);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, attendanceAndPaidCount);
	}
}
