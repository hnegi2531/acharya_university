package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
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
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.au.dto.JwtDetails;
import com.au.event.StudentDueEvent;
import com.au.model.Academic_year;
import com.au.model.CancelAddmissions;
import com.au.model.CmaFeeReceipt;
import com.au.model.FeePaymentWindow;
import com.au.model.ProgramAssigment;
import com.au.model.ReportingStudents;
import com.au.model.Schools;
import com.au.model.StudentDues;
import com.au.model.StudentPermission;
import com.au.model.Student_Details;
import com.au.model.UserAuthentication;
import com.au.model.VoucherHeadNew;
import com.au.repository.Academic_year_repository;
import com.au.repository.FeePaymentWindowRepository;
import com.au.repository.ProgramAssigmentRepository;
import com.au.repository.ProgramRepository;
import com.au.repository.ReportingStudentsRepository;
import com.au.repository.School_Repository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.StudentDueRepository;
import com.au.repository.StudentPermissionRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.repository.VoucherHeadNewRepository;
import com.au.response.ResponseHandler;

@Service
public class FeePaymentWindowService {
	
	
private Logger log = LoggerFactory.getLogger(FeePaymentWindowService.class);

	
	public static final String value = "FeePaymentWindow";
	
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
	
	@Autowired
	private FeePaymentWindowRepository feePaymentWindowRepository;
	
	@Autowired
	private VoucherHeadNewRepository voucherHeadNewRepository;
	
	@Autowired
	private JwtTokenService jwtService;
	
	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private School_Repository schoolRepository;
	
	@Autowired
	private StudentDueRepository studentDueRepository;
	
	@Autowired
	private StudentPermissionRepository studentPermissionRepository;
	
	@Autowired
	private ReportingStudentsRepository reportingStudentsRepository;
	
	@Autowired
	private ProgramAssigmentRepository programAssigmentRepository;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private ProgramRepository programRepository;
	
	@Autowired
	private Academic_year_repository academicYearRepository;
	
	private Integer count=0;
	
	public FeePaymentWindow createFeePayment(@Valid FeePaymentWindow feePayment) throws Exception {
		
		if(feePayment.getWindow_type().equalsIgnoreCase("EXAM")) {
			List<Integer> programIds=ResponseHandler.toConvertCommaSeperatedIdsAsList(feePayment.getProgram_id());
			programIds.stream().forEach(id -> {
				Integer countById= feePaymentWindowRepository.getcount(feePayment.getSchool_id(),id,feePayment.getFrom_date(),feePayment.getTo_date());
				if(countById >= 1) {
					count++;
				}
			});
			if (count >=1) {
				throw new Exception("Combination of School,Programs,FromDate And ToDate Already exist");
			}	
		}
		if(feePayment.getWindow_type().equalsIgnoreCase("BULK")) {
			feePayment.setVoucher_head(voucherHeadNewRepository.getCommaSeperartedVoucher_head(ResponseHandler.toConvertCommaSeperatedIdsAsList(feePayment.getVoucher_head_new_id())));
			feePayment.setUserName(userAuthenticationRepository.getCommaSeperartedUserName(ResponseHandler.toConvertCommaSeperatedIdsAsList(feePayment.getUser_id())));
		}else if(feePayment.getWindow_type().equalsIgnoreCase("EXAM")) {
			feePayment.setVoucher_head(voucherHeadNewRepository.getCommaSeperartedVoucher_head(ResponseHandler.toConvertCommaSeperatedIdsAsList(feePayment.getVoucher_head_new_id())));
			feePayment.setProgram(programRepository.getCommaSeperartedProgramShortName(ResponseHandler.toConvertCommaSeperatedIdsAsList(feePayment.getProgram_id())));
		}
		
		count=0;
		return	feePaymentWindowRepository.save(feePayment);

	}

	public List<VoucherHeadNew> getvoucherHeadDetailsBasedOnSchoolId(Integer school_id, String from_date,String to_date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String formattedDate = sdf.format(currentDate);
        System.out.println("Current Date: " + formattedDate);
        System.out.println("From Date: " + from_date);
        System.out.println("To Date: " + to_date);

        // Get comma-separated ID strings from the repository
        List<String> vhn = feePaymentWindowRepository.getVoucherHeadId(school_id, formattedDate);
        System.out.println("Voucher Head IDs: " + vhn);

        // Convert the comma-separated ID strings to a List of Integers using Streams
        List<Integer> numberIntegers = vhn.stream()
                .flatMap(ids -> Stream.of(ids.split(",")))  // Split each string by comma
                .map(String::trim)  // Trim whitespace
                .map(id -> {
                    try {
                        return Integer.parseInt(id);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid ID format: " + id);
                        return null;
                    }
                })
                .filter(id -> id != null)  // Filter out null values
                .collect(Collectors.toList());

        System.out.println("Number Integers: " + numberIntegers);

        // Get VoucherHeadNew details based on the integer IDs
        List<VoucherHeadNew> voucherHeadNewList = voucherHeadNewRepository.voucherHeadDetails(numberIntegers);
        return voucherHeadNewList;
    }
	
	
	public ResponseEntity<Object> feePaymentDetailsForPayment(String paymentType, String jwtToken) {
		try {

			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
			UserAuthentication studentUserDetails = userAuthenticationRepository.findById(jwtDetails.getUserId()).get();
			Student_Details studentDetails = studentDetailsRepository.findByAcharyaEmail(studentUserDetails.getEmail());
			ReportingStudents reportingStudent = reportingStudentsRepository
					.getDetailsOfReportingStudentsByStudentId(studentDetails.getStudent_id());
			List<HashMap<String, Object>> programAssignment = programAssigmentRepository
					.findDetailsById(studentDetails.getProgram_assignment_id());
			Schools school = schoolRepository.findById(studentDetails.getSchool_id()).get();
			Date currentDate = new Date();

			if (paymentType.equalsIgnoreCase("EXAM")) {
				List<HashMap<String, Object>> feePaymentDetails = feePaymentWindowRepository.feePaymentWindowsDetails(
						studentDetails.getSchool_id(), currentDate, studentDetails.getProgram_id().toString());
				Integer year=1;
				if (ObjectUtils.isNotEmpty(feePaymentDetails)) {
//					StudentDueEvent studDue=new StudentDueEvent(null,null,studentDetails.getStudent_id(),"studentDue");
//					applicationEventPublisher.publishEvent(studDue);
					StudentDues studentDues = studentDueRepository.getBySId(studentDetails.getStudent_id());
					String voucherHeadNewIds = feePaymentDetails.stream()
							.filter(e -> paymentType.equalsIgnoreCase(e.get("window_type").toString()))
							.map(e -> e.get("voucher_head_new_id").toString()).collect(Collectors.joining(","));
					List<Map<String, Object>> finalResponseOfExam=new ArrayList<>();
					for(int i=1 ; i<=reportingStudent.getCurrent_sem();i++ ) {
					Double dueDetails=getYearlyDue(studentDues,i);
					StudentPermission studentPermission = studentPermissionRepository
							.findByAuidAndCurrentYearAndCurrentSemAndPermissionTypeAndActiveTrue(
									studentDetails.getAuid(), i,
									reportingStudent.getCurrent_sem(), "Examination");
					

						
						
					 if (dueDetails <= 0.0 || ObjectUtils.isNotEmpty(studentPermission)) {
						 year=i % 2 != 0 ? year : year+1;
						Map<String, Object> responseofExamPayment = responseofExamPayment(studentDetails,
								reportingStudent, programAssignment, school, voucherHeadNewIds, paymentType,
								studentPermission,i,year);
						finalResponseOfExam.add(responseofExamPayment);
						
					 }else {
						 break;
					 }
					}
					if (ObjectUtils.isNotEmpty(finalResponseOfExam)) {
					 return ResponseHandler.generateResponse(true, HttpStatus.OK, finalResponseOfExam);

					} else {
					    return ResponseHandler.generateResponse(true, HttpStatus.OK, "College Fees to be paid");
					}
				} else {
					return ResponseHandler.generateResponse(true, HttpStatus.OK, "Exam window not opened");
				}

			} else {
				List<HashMap<String, Object>> feePaymentDetails = feePaymentWindowRepository
						.feePaymentWindowsDetailsForBulk(studentDetails.getSchool_id(), currentDate);
				if (ObjectUtils.isNotEmpty(feePaymentDetails)) {
					List<HashMap<String, Object>> bulkFeePayments = feePaymentDetails.stream()
							.filter(e -> paymentType.equalsIgnoreCase(e.get("window_type").toString()) && (Boolean)e.get("external_status") == false)
							.collect(Collectors.toList());
					Map<String, Object> responseofBulkPayment = responseofBulkPayment(studentDetails, reportingStudent,
							programAssignment, school, bulkFeePayments, paymentType);
					return ResponseHandler.generateResponse(true, HttpStatus.OK, responseofBulkPayment);
				} else {
					return ResponseHandler.generateResponse(true, HttpStatus.OK, "Bulk Fee Paid Date Expired");
				}
			}

		} catch (IOException e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	private double getYearlyDue(StudentDues studentDues, int year) {
		switch (year) {
		case 1:
			return studentDues.getS1due();
		case 2:
			return studentDues.getS2due();
		case 3:
			return studentDues.getS3due();
		case 4:
			return studentDues.getS4due();
		case 5:
			return studentDues.getS5due();
		case 6:
			return studentDues.getS6due();
		case 7:
			return studentDues.getS7due();
		case 8:
			return studentDues.getS8due();
		default:
			return 0.0;
		}
	}
	
	
	
	private Map<String, Object> responseofBulkPayment(Student_Details studentDetails,
			ReportingStudents reportingStudent, List<HashMap<String, Object>> programAssignment, Schools school,
			List<HashMap<String, Object>> bulkFeePayments, String paymentType) {
		Academic_year academicYear=academicYearRepository.findById(studentDetails.getAc_year_id()).get();
		Map<String,Object> response=new HashMap<String, Object>();
		response.put("school_id", school.getSchool_id());
		response.put("school_name", school.getSchool_name());
		response.put("window_type", paymentType);
		response.put("current_year", reportingStudent.getCurrent_year());
		response.put("current_sem", reportingStudent.getCurrent_sem());
		response.put("program_type_name", programAssignment.get(0).get("program_type_name"));
		response.put("auid", studentDetails.getAuid());
		response.put("student_id", studentDetails.getStudent_id());
		response.put("student_name", studentDetails.getStudent_name());
		response.put("acharya_email", studentDetails.getAcharya_email());
		response.put("ac_year", academicYear.getAc_year());
		response.put("ac_year_id", academicYear.getAc_year_id());
		response.put("mobile_number", studentDetails.getMobile());
		List<Map<String,Object>> voucherHeadResponse=new ArrayList<>();
		bulkFeePayments.stream().forEach(bp -> {
			VoucherHeadNew vocherHead=voucherHeadNewRepository.findById(Integer.valueOf(bp.get("voucher_head_new_id").toString())).get();
			Map<String,Object> voucherHeadKey=new HashMap<String, Object>();
			voucherHeadKey.put("voucher_head", vocherHead.getVoucher_head());
			voucherHeadKey.put("voucher_head_new_id", vocherHead.getVoucher_head_new_id());
			voucherHeadKey.put("fixed", bp.get("fixed"));
			voucherHeadKey.put("amount", bp.get("amount"));
			voucherHeadKey.put("fee_payment_window_id", bp.get("fee_payment_window_id"));
			voucherHeadResponse.add(voucherHeadKey);
			
		});
		response.put("vocherHead", voucherHeadResponse);
		return response;
	}

	private Map<String, Object> responseofExamPayment(Student_Details studentDetails,
			ReportingStudents reportingStudent, List<HashMap<String, Object>> programAssignment, Schools school,
			String voucherHeadNewIds, String paymentType, StudentPermission studentPermission, Integer sem,Integer year) {
		Academic_year academicYear=academicYearRepository.findById(studentDetails.getAc_year_id()).get();
		Map<String,Object> response=new HashMap<String, Object>();
		response.put("school_id", school.getSchool_id());
		response.put("school_name", school.getSchool_name());
		response.put("window_type", paymentType);
		response.put("current_year", year );
		response.put("current_sem", sem);
		response.put("program_type_name", programAssignment.get(0).get("program_type_name"));
		response.put("auid", studentDetails.getAuid());
		response.put("student_id", studentDetails.getStudent_id());
		response.put("student_name", studentDetails.getStudent_name());
		response.put("acharya_email", studentDetails.getAcharya_email());
		response.put("ac_year", academicYear.getAc_year());
		response.put("ac_year_id", academicYear.getAc_year_id());
		response.put("mobile_number", studentDetails.getMobile());
		List<VoucherHeadNew> vocherHead=voucherHeadNewRepository.voucherHeadDetailsByVoucherHeadNewIds(ResponseHandler.toConvertCommaSeperatedIdsAsList(voucherHeadNewIds));
		List<Map<String,Object>> voucherHeadResponse=new ArrayList<>();
		vocherHead.stream().forEach( vhn -> {
			Map<String,Object> voucherHeadKey=new HashMap<String, Object>();
			voucherHeadKey.put("voucher_head", vhn.getVoucher_head());
			voucherHeadKey.put("voucher_head_new_id", vhn.getVoucher_head_new_id());
			voucherHeadResponse.add(voucherHeadKey);
		});
		response.put("vocherHead", voucherHeadResponse);
		if(ObjectUtils.isNotEmpty(studentPermission)){
			response.put("pay_till", studentPermission.getAllowSem());
		}else {
			if(programAssignment.get(0).get("program_type_name").toString().equalsIgnoreCase("Semester")) {
				response.put("pay_till", reportingStudent.getCurrent_sem());
			}else {
				response.put("pay_till", reportingStudent.getCurrent_year());
			}
		}
		return response;
	}

	public void uploadFile(MultipartFile multipartFile,Integer fee_payment_window_id) {


		FeePaymentWindow entity = feePaymentWindowRepository.findById(fee_payment_window_id)
		.orElseThrow(()-> new ResourceNotFoundException("Cancel admissiond not found"));
		try {
			log.debug("Message For Cancel admission Attachment --------------");
			
			
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
		
			log.debug("Cancel admission Attachment", multipartFile);
			
			entity.setAttachment_file(fileName);
			entity.setAttachment_path(LocalDate.now() + "/" + fee_payment_window_id + "/" + fileName);
			uploadFileTos3bucket(fileName, file, fee_payment_window_id);
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
		feePaymentWindowRepository.save(entity);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer fee_payment_window_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + fee_payment_window_id + "/" + fileName; // file.getName()
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
	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable,String user_id, Object keyword) {
		Page<Object> response1 = feePaymentWindowRepository.getAllDataFilteredByKeyword(pageable,user_id, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable,String user_id) {
		Page<Object> response = feePaymentWindowRepository.getAllSortedData(pageable,user_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public FeePaymentWindow get(Integer id) {
		return feePaymentWindowRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("FeePaymentWindow Not Found:" + id));
	}
	
	public FeePaymentWindow updateFeePaymentWindow(FeePaymentWindow feePayment) {
		if(feePayment.getWindow_type().equalsIgnoreCase("BULK")) {
			feePayment.setVoucher_head(voucherHeadNewRepository.getCommaSeperartedVoucher_head(ResponseHandler.toConvertCommaSeperatedIdsAsList(feePayment.getVoucher_head_new_id())));
			feePayment.setUserName(userAuthenticationRepository.getCommaSeperartedUserName(ResponseHandler.toConvertCommaSeperatedIdsAsList(feePayment.getUser_id())));
		}else if(feePayment.getWindow_type().equalsIgnoreCase("EXAM")) {
			feePayment.setVoucher_head(voucherHeadNewRepository.getCommaSeperartedVoucher_head(ResponseHandler.toConvertCommaSeperatedIdsAsList(feePayment.getVoucher_head_new_id())));
			feePayment.setProgram(programRepository.getCommaSeperartedProgramShortName(ResponseHandler.toConvertCommaSeperatedIdsAsList(feePayment.getProgram_id())));
		}
		return feePaymentWindowRepository.save(feePayment);
	}
	
	
	
	private static final String PAYMENT_TYPE="Exam";
	public ResponseEntity<Object> feePaymentDetailsForExamFeeByStudetId(Integer studentId, String jwtToken) {
		try {

			
			Student_Details studentDetails = studentDetailsRepository.findById(studentId).get();
			ReportingStudents reportingStudent = reportingStudentsRepository
					.getDetailsOfReportingStudentsByStudentId(studentDetails.getStudent_id());
			List<HashMap<String, Object>> programAssignment = programAssigmentRepository
					.findDetailsById(studentDetails.getProgram_assignment_id());
			Schools school = schoolRepository.findById(studentDetails.getSchool_id()).get();
			Date currentDate = new Date();

				List<HashMap<String, Object>> feePaymentDetails = feePaymentWindowRepository.feePaymentWindowsDetailsForExam(
						studentDetails.getSchool_id(), currentDate, studentDetails.getProgram_id().toString());
				Integer year=1;
				if (ObjectUtils.isNotEmpty(feePaymentDetails)) {
					StudentDues studentDues = studentDueRepository.getBySId(studentDetails.getStudent_id());
					if(ObjectUtils.isEmpty(studentDues)) {
						throw new ResourceNotFoundException("Student Dues Is not Present for this student !! ");
					}
					
					String voucherHeadNewIds = feePaymentDetails.stream()
							.filter(e -> PAYMENT_TYPE.equalsIgnoreCase(e.get("window_type").toString()))
							.map(e -> e.get("voucher_head_new_id").toString()).collect(Collectors.joining(","));
					List<Map<String, Object>> finalResponseOfExam=new ArrayList<>();
					for(int i=1 ; i<=reportingStudent.getCurrent_sem();i++ ) {
					Double dueDetails=getYearlyDue(studentDues,i);
					StudentPermission studentPermission = studentPermissionRepository
							.findByAuidAndCurrentYearAndCurrentSemAndPermissionTypeAndActiveTrue(
									studentDetails.getAuid(), i,
									reportingStudent.getCurrent_sem(), "Examination");
					

						
						
					 if (dueDetails <= 0.0 || ObjectUtils.isNotEmpty(studentPermission)) {
						 year=i % 2 != 0 ? year : year+1;
						Map<String, Object> responseofExamPayment = responseofExamPayment(studentDetails,
								reportingStudent, programAssignment, school, voucherHeadNewIds, PAYMENT_TYPE,
								studentPermission,i,year);
						finalResponseOfExam.add(responseofExamPayment);
						
					 }else {
						 break;
					 }
					}
					if (ObjectUtils.isNotEmpty(finalResponseOfExam)) {
					 return ResponseHandler.generateResponse(true, HttpStatus.OK, finalResponseOfExam);

					} else {
					    return ResponseHandler.generateResponse(true, HttpStatus.OK, "College Fees to be paid");
					}
				} else {
					return ResponseHandler.generateResponse(true, HttpStatus.OK, "NO Heads Found");
				}
			} catch (Exception e) {
				return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			}
	}

	public List<Map<String, Object>> getFeePaymentWindowBasedOnUserId(Integer user_id) {	
		return feePaymentWindowRepository.getFeePaymentWindowBasedOnUserId(user_id);
	}

	public List<Map<String, Object>> getBulkPayTransaction(Integer fee_payment_window_id) {
		return feePaymentWindowRepository.getBulkPayTransaction(fee_payment_window_id);
	}


}
