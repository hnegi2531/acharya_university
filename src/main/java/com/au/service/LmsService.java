package com.au.service;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.au.config.JwtTokenUtil;
import com.au.dto.AcademicYearRequestDTO;
import com.au.dto.AttendanceResponseDTO;
import com.au.dto.BatchAndSectionResponse;
import com.au.dto.BatchByAcademicYear;
import com.au.dto.BatchDetailsByReportingStudent;
import com.au.dto.BatchDetailsResponse;
import com.au.dto.CurrentYearBatchAndSectionofStudent;
import com.au.dto.JwtAccessTokenDetails;
import com.au.dto.JwtDetails;
import com.au.dto.LmsLoginRequest;
import com.au.dto.LmsLoginResponse;
import com.au.dto.SectionByAcademicYear;
import com.au.dto.SectionDetailsResponse;
import com.au.dto.StudentAndBatchResponse;
import com.au.dto.StudentDetailsResponse;
import com.au.dto.UserDetailsDto;
import com.au.dto.programAndTypeDetailsOfStudentDto;
import com.au.model.Batch;
import com.au.model.BatchAssignment;
import com.au.model.Course;
import com.au.model.CourseAssignment;
import com.au.model.ProgramSpecilization;
import com.au.model.ReportingStudents;
import com.au.model.Section;
import com.au.model.SectionAssignment;
import com.au.model.StudentAttendance;
import com.au.model.TimeTable;
import com.au.model.TokenDetails;
import com.au.model.UserAuthentication;
import com.au.repository.Academic_year_repository;
import com.au.repository.BatchAssignmentRepository;
import com.au.repository.BatchProgramAssignmentRepository;
import com.au.repository.BatchRepository;
import com.au.repository.CourseAssignmentRepository;
import com.au.repository.CourseRepository;
import com.au.repository.CourseStudentAssignmentRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.ProctorStudentAssignmentRepository;
import com.au.repository.ProgramAssigmentRepository;
import com.au.repository.ProgramSpecilizationRepository;
import com.au.repository.ReportingStudentsRepository;
import com.au.repository.School_Repository;
import com.au.repository.SectionAssignmentRepository;
import com.au.repository.SectionRepository;
import com.au.repository.StudentAttendanceRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.TimeTableEmployeeRepository;
import com.au.repository.TimeTableRepository;
import com.au.repository.TokenDetailsRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;



@Service
public class LmsService {

	@Autowired
	private JwtTokenService jwtService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private TokenDetailsRepository tokenDetailsRepository;

	@Autowired
	private TimeTableRepository timeTableRepository;

	@Autowired
	private CourseAssignmentRepository courseAssignmentRepository;

	@Autowired
	private TimeTableEmployeeRepository timeTableEmployeeRespository;

	@Autowired
	private StudentDetailsRepository studentDetailsRepository;

	@Autowired
	private ReportingStudentsRepository reportingStudentsRepository;

	@Autowired
	private EncryptionDecriptionService encryptionDecriptionService;

	@Autowired
	private SectionAssignmentRepository sectionAssignmentRepository;
	
	@Autowired
	private BatchAssignmentRepository batchAssignmentRepository;
	
	@Autowired
	private Academic_year_repository academicYearRepository;
	
	@Autowired
	private ProctorStudentAssignmentRepository proctorStudentAssignmentRepository;
	
	@Autowired
	private School_Repository schoolRepository;
	
	@Autowired
	private CourseStudentAssignmentRepository courseStudentAssignmentRepository;
	
	@Autowired
	private ProgramSpecilizationRepository programSpecilizationRepository;
	
	@Autowired
	private BatchProgramAssignmentRepository batchProgramAssignmentRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private BatchRepository batchRepository;
	
	@Autowired
	private SectionRepository sectionRepository;
	
	@Autowired
	private ProgramAssigmentRepository programAssigmentRepository;
	
	@Autowired
	private StudentAttendanceService studentAttendanceService;
	
	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;
	
	@Autowired
	private UserAuthenticationService userAuthenticationService;
	
	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;
	
	private final ModelMapper modelMapper=new ModelMapper();
	
	
	
	

	private final String jwtSecretKey;

	private AmazonS3 s3client;

	@Value("${amazonProperties.bucketName}")
	private String bucketName;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	public static final String employee = "LMSBucket";
	public static final String student = "StudentImageBucket";
	@Autowired
	public LmsService(@Value("${jwt.secret}") String jwtSecretKey) {
		this.jwtSecretKey = jwtSecretKey;
	}

	@SuppressWarnings("deprecation")
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}	
	
	
	public ResponseEntity<Object> lmsLogin(LmsLoginRequest lmsLoginRequest) {
		try {
			JwtDetails jwtDetails = jwtService.callJwtToken(lmsLoginRequest.getToken());

			TokenDetails tokenDetails = tokenDetailsRepository.findByUserId(jwtDetails.getUserId());
			if (ObjectUtils.isNotEmpty(tokenDetails) && isTokenExpired(lmsLoginRequest.getToken())) {
				tokenDetails.setAccessToken(null);
				tokenDetails.setToken(null);
				tokenDetailsRepository.save(tokenDetails);
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}
			if (ObjectUtils.isEmpty(tokenDetails)) {
				tokenDetails = new TokenDetails();
				tokenDetails.setToken(lmsLoginRequest.getToken());
				tokenDetails.setUserId(jwtDetails.getUserId());
				Date expDate = new Date(jwtDetails.getExp());
				tokenDetails.setExpirationTime(expDate);

			}
			LmsLoginResponse lmsLoginResponse = jwtTokenUtil.generateLmsToken(jwtDetails, lmsLoginRequest.getToken(),
					tokenDetails);
			if (ObjectUtils.isEmpty(lmsLoginResponse)) {
				return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,
						"Employee Details not present in the system.", lmsLoginResponse);
			}
			if (ObjectUtils.isEmpty(lmsLoginResponse.getLms_role())) {
				return ResponseHandler.generateResponse(true, HttpStatus.OK, "LMS role not assign to the user",
						lmsLoginResponse);

			}

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "Access Token created successfully",
					lmsLoginResponse);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}

	}
	
	
	public boolean isTokenExpired(String jwtToken) {
		try {
			Jws<Claims> jws = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(jwtToken);

			Claims claims = jws.getBody();

			long expirationTimeMillis = claims.getExpiration().getTime();
			long currentTimeMillis = System.currentTimeMillis();
			return expirationTimeMillis < currentTimeMillis;
			
		} catch (SignatureException | MalformedJwtException e) {
			System.out.print(e.getMessage());
			return true;
		} catch (ExpiredJwtException e) {
			System.out.print(e.getMessage());
			return true;
		}
	}
	
	
	
	public ResponseEntity<Object> fetchTimeTableDetailsBySelectDateForEmployees(String accessToken, String startingdate, String endingdate) {
		try {
			// String token = encryptionDecriptionService.decryptionForLms(accessToken);
			System.out.print("token " + accessToken);
			TokenDetails tokenDetails = tokenDetailsRepository.findByAccessToken(accessToken);
			System.out.print("TOkenDetails " + tokenDetails);
			if (ObjectUtils.isEmpty(tokenDetails)) {
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}
			if (isTokenExpired(tokenDetails.getToken())) {
				System.out.print("Token expired");
				tokenDetails.setAccessToken(null);
				tokenDetails.setToken(null);
				tokenDetailsRepository.save(tokenDetails);
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}
			JwtAccessTokenDetails jwtAccessTokenDetails = jwtService.getDetailsFromAccessToken(accessToken);

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			List<Map<String, Object>> list = new ArrayList<>();
			if (ObjectUtils.isEmpty(startingdate) && ObjectUtils.isEmpty(endingdate)) {
				String currentDate = dateFormat.format(new Date());
				list = timeTableRepository.fetchTimeTableDetailsBySelectDateForEmployees(dateFormat.parse(currentDate),
						jwtAccessTokenDetails.getEmp_id());
			} else {
				
				list = timeTableRepository.fetchTimeTableDetailsBetweenBySelectDateForEmployees(dateFormat.parse(startingdate),dateFormat.parse(endingdate),
						jwtAccessTokenDetails.getEmp_id());
			}

			List<Map<String, Object>> result = new ArrayList<>();

			list.stream().forEach(batchAss -> {
				if(ObjectUtils.isNotEmpty(batchAss.get("batch_assignment_id"))) {
					@SuppressWarnings("unchecked")
					Map<String, Object> batch=modelMapper.map(batchAss, Map.class);
					Map<String,String> programAndSpecialtionNames=batchProgramAssignmentRepository.specializationAndProgramNames((Integer)batchAss.get("batch_assignment_id"));
					batch.put("course_branch_short_name", programAndSpecialtionNames.get("specializationShortNames"));
					batch.put("course_short_name", programAndSpecialtionNames.get("programShortNames"));
					batch.put("course_assignment_id", programAndSpecialtionNames.get("programIds"));
					batch.put("course_branch_assignment_id", programAndSpecialtionNames.get("specializationIds"));
					result.add(batch);
				}else {
					result.add(batchAss);
				}
			});
			
			
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", result);
		} catch (Exception e) {
			System.out.print(e.getMessage());
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}
	}	
	
	
	public ResponseEntity<Object> fetchTimeTableDetailsBySelectDateForStudents(String accessToken, String startingdate, String endingdate) {
		try {
			// String token = encryptionDecriptionService.decryptionForLms(accessToken);
			TokenDetails tokenDetails = tokenDetailsRepository.findByAccessToken(accessToken);
			if (ObjectUtils.isEmpty(tokenDetails)) {
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}
			if (isTokenExpired(tokenDetails.getToken())) {
				tokenDetails.setAccessToken(null);
				tokenDetails.setToken(null);
				tokenDetailsRepository.save(tokenDetails);
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}
			JwtAccessTokenDetails jwtAccessTokenDetails = jwtService.getDetailsFromAccessToken(accessToken);

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			List<Map<String, Object>> list = new ArrayList<>();
			String currentYearOrSem = timeTableRepository.getCurrentYearOrSem(jwtAccessTokenDetails.getStudent_id());
			Integer academicYearId=academicYearRepository.getCurrentAcademicYearId();
			ReportingStudents reportingStudent=reportingStudentsRepository.getDetailsOfReportingStudentsByStudentId(jwtAccessTokenDetails.getStudent_id());
			List<Integer> sectionAssignmentId;
			List<Integer> batchAssignmentId;
			if(currentYearOrSem.equalsIgnoreCase("Semester")) {
//				sectionAssignmentId =sectionAssignmentRepository.getSectionAssignmentId(jwtAccessTokenDetails.getStudent_id(),reportingStudent.getCurrent_sem(),academicYearId);
				sectionAssignmentId =sectionAssignmentRepository.getSectionAssignmentId(jwtAccessTokenDetails.getStudent_id(),reportingStudent.getCurrent_sem());

				batchAssignmentId =batchAssignmentRepository.getBatchAssignmentIdOnSem(jwtAccessTokenDetails.getStudent_id(),reportingStudent.getCurrent_sem());
			}else {
//				sectionAssignmentId =sectionAssignmentRepository.getSectionAssignmentId(jwtAccessTokenDetails.getStudent_id(),reportingStudent.getCurrent_year(),academicYearId);
				sectionAssignmentId =sectionAssignmentRepository.getSectionAssignmentId(jwtAccessTokenDetails.getStudent_id(),reportingStudent.getCurrent_year());
				batchAssignmentId =batchAssignmentRepository.getBatchAssignmentIdOnYear(jwtAccessTokenDetails.getStudent_id(),reportingStudent.getCurrent_year());


			}
			
			if (ObjectUtils.isEmpty(startingdate) && ObjectUtils.isEmpty(endingdate)) {
				String currentDate = dateFormat.format(new Date());
					list = timeTableRepository.timeTableDetailsOfStudentForLMS(sectionAssignmentId,batchAssignmentId,dateFormat.parse(currentDate),dateFormat.parse(currentDate),jwtAccessTokenDetails.getStudent_id());
			} else {
				list = timeTableRepository.timeTableDetailsOfStudentForLMS(sectionAssignmentId,batchAssignmentId,dateFormat.parse(startingdate),dateFormat.parse(endingdate),jwtAccessTokenDetails.getStudent_id());
			}
			
			List<Map<String, Object>> result = new ArrayList<>();
			
			list.stream().forEach(batchAss -> {
				if(ObjectUtils.isNotEmpty(batchAss.get("batch_assignment_id"))) {
					@SuppressWarnings("unchecked")
					Map<String, Object> batch=modelMapper.map(batchAss, Map.class);
					Map<String,String> programAndSpecialtionNames=batchProgramAssignmentRepository.specializationAndProgramNames((Integer)batchAss.get("batch_assignment_id"));
					batch.put("course_branch_short_name", programAndSpecialtionNames.get("specializationShortNames"));
					batch.put("course_short_name", programAndSpecialtionNames.get("programShortNames"));
					batch.put("course_assignment_id", programAndSpecialtionNames.get("programIds"));
					batch.put("course_branch_assignment_id", programAndSpecialtionNames.get("specializationIds"));
					result.add(batch);
				}else{
					result.add(batchAss);
				}
			});
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", result);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}

	}
	
	

	public ResponseEntity<Object> getListofCoursesByEmployeeId(String accessToken) {
		try {
			TokenDetails tokenDetails = tokenDetailsRepository.findByAccessToken(accessToken);
			if (ObjectUtils.isEmpty(tokenDetails)) {
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}
			if (isTokenExpired(tokenDetails.getToken())) {
				tokenDetails.setAccessToken(null);
				tokenDetails.setToken(null);
				tokenDetailsRepository.save(tokenDetails);
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}

			JwtAccessTokenDetails jwtAccessTokenDetails = jwtService.getDetailsFromAccessToken(accessToken);
			List<HashMap<String, Object>> courseList = courseAssignmentRepository
					.getCoursesByEmployeeId(jwtAccessTokenDetails.getUser_id());
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", courseList);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}
	}
	
	
	public ResponseEntity<Object> getAcademicYearsByEmployeeId(String accessToken) {
		try {
			// String token = encryptionDecriptionService.decryptionForLms(accessToken);
			TokenDetails tokenDetails = tokenDetailsRepository.findByAccessToken(accessToken);
			if (ObjectUtils.isEmpty(tokenDetails)) {
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}
			if (isTokenExpired(tokenDetails.getToken())) {
				tokenDetails.setAccessToken(null);
				tokenDetails.setToken(null);
				tokenDetailsRepository.save(tokenDetails);
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}

			JwtAccessTokenDetails jwtAccessTokenDetails = jwtService.getDetailsFromAccessToken(accessToken);

			List<HashMap<String, Object>> academicYears = timeTableEmployeeRespository
					.getAcademicYearsByEmployeeId(jwtAccessTokenDetails.getEmp_id());
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", academicYears);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}
	}	
	
	
	public ResponseEntity<Object> getBatchAndSectionbyAcademicYear(String accessToken,
			AcademicYearRequestDTO academicYearRequestDto) {
		try {
			// String token = encryptionDecriptionService.decryptionForLms(accessToken);
			TokenDetails tokenDetails = tokenDetailsRepository.findByAccessToken(accessToken);
			if (ObjectUtils.isEmpty(tokenDetails)) {
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}
			if (isTokenExpired(tokenDetails.getToken())) {
				tokenDetails.setAccessToken(null);
				tokenDetails.setToken(null);
				tokenDetailsRepository.save(tokenDetails);
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}

			JwtAccessTokenDetails jwtAccessTokenDetails = jwtService.getDetailsFromAccessToken(accessToken);

			List<BatchByAcademicYear> batchByAcademicyear = timeTableRepository.getBatchByAcademicYearAndEmployeeId(
					academicYearRequestDto.getAcademic_year_id(), jwtAccessTokenDetails.getEmp_id());
			
			batchByAcademicyear.stream().forEach(ba ->   {
				Map<String,String> spcializationAndProgramnames=batchProgramAssignmentRepository.specializationAndProgramNames(ba.getBatch_assignment_id());
				ba.setCourse_short_name(spcializationAndProgramnames.get("programShortNames"));
			});
			
			List<SectionByAcademicYear> sectionByAcademicYear = timeTableRepository
					.getSectionByAcademicYearAndEmployeeId(academicYearRequestDto.getAcademic_year_id(),
							jwtAccessTokenDetails.getEmp_id());
			BatchAndSectionResponse batchAndSectionResponse = new BatchAndSectionResponse();
			batchAndSectionResponse.setBatches(batchByAcademicyear);
			batchAndSectionResponse.setSections(sectionByAcademicYear);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, batchAndSectionResponse);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

		}
	}
	
	
	public ResponseEntity<Object> getStudentList(String accessToken, Integer timetableId) {
		try {
			// String token = encryptionDecriptionService.decryptionForLms(accessToken);
			TokenDetails tokenDetails = tokenDetailsRepository.findByAccessToken(accessToken);
			if (ObjectUtils.isEmpty(tokenDetails)) {
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}
			if (isTokenExpired(tokenDetails.getToken())) {
				tokenDetails.setAccessToken(null);
				tokenDetails.setToken(null);
				tokenDetailsRepository.save(tokenDetails);
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}

			HashMap<String, Object> batchdetails = timeTableRepository.getBatchAssigmentNameandId(timetableId);   
			StudentAndBatchResponse studentAndBatchResponse = new StudentAndBatchResponse();
			if (ObjectUtils.isNotEmpty(batchdetails)) {
				String name = (String) batchdetails.get("name");
				Integer assignmentId = (Integer) batchdetails.get("assignment_id");
				String studentIds = (String) batchdetails.get("student_ids");

				BatchDetailsResponse batchDetailsResponse = new BatchDetailsResponse();
				batchDetailsResponse.setAssignment_id(assignmentId);
				batchDetailsResponse.setName(name);
				studentAndBatchResponse.setBatchdetails(batchDetailsResponse);
				List<Integer> studentId = new ArrayList<>();

				if (StringUtils.isNotEmpty(studentIds)) {
					String[] splitsId = studentIds.split(",");
					for (String s : splitsId) {
						Integer id = Integer.parseInt(s);
						studentId.add(id);
					}
				}

				List<StudentDetailsResponse> studentDetails = studentDetailsRepository
						.getStudentDetailsFromBatchAssignment(studentId);
				studentAndBatchResponse.setStudentDetails(studentDetails);
			}
			return ResponseHandler.generateResponse(true, HttpStatus.OK, studentAndBatchResponse);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	
	public ResponseEntity<Object> getCurrentYearSectionAndBatchOfStudent(String accessToken) {
		try {
			// String token = encryptionDecriptionService.decryptionForLms(accessToken);
			TokenDetails tokenDetails = tokenDetailsRepository.findByAccessToken(accessToken);
			if (ObjectUtils.isEmpty(tokenDetails)) {
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}
			if (ObjectUtils.isNotEmpty(tokenDetails)  && isTokenExpired(tokenDetails.getToken())) {
				tokenDetails.setAccessToken(null);
				tokenDetails.setToken(null);
				tokenDetailsRepository.save(tokenDetails);
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}
			JwtAccessTokenDetails jwtAccessTokenDetails = jwtService.getDetailsFromAccessToken(accessToken);

			String currentYearOrSem = timeTableRepository.getCurrentYearOrSem(jwtAccessTokenDetails.getStudent_id());
			Integer sectionId = sectionAssignmentRepository
					.getSectionIdByStudentId(jwtAccessTokenDetails.getStudent_id());
			SectionDetailsResponse sectionDetailsResponse = null;
			List<BatchDetailsByReportingStudent> batchDetailsByReportingStudents = null;
			if (StringUtils.equals(currentYearOrSem, "Yearly")) {
				sectionDetailsResponse = reportingStudentsRepository
						.getCurrentYearSectionDetails(jwtAccessTokenDetails.getCurrent_year(), sectionId,jwtAccessTokenDetails.getStudent_id());
				batchDetailsByReportingStudents = reportingStudentsRepository.getCurrentYearBatchDetails(
						jwtAccessTokenDetails.getCurrent_year(), jwtAccessTokenDetails.getStudent_id());

			} else {

				sectionDetailsResponse = reportingStudentsRepository
						.getCurrentSemSectionDetails(jwtAccessTokenDetails.getCurrent_sem(), sectionId,jwtAccessTokenDetails.getStudent_id());

				batchDetailsByReportingStudents = reportingStudentsRepository.getCurrentSemBatchDetails(
						jwtAccessTokenDetails.getCurrent_sem(), jwtAccessTokenDetails.getStudent_id());

			}
			CurrentYearBatchAndSectionofStudent currentYearBatchAndSectionofStudent = new CurrentYearBatchAndSectionofStudent();
			currentYearBatchAndSectionofStudent.setSection(sectionDetailsResponse);
			currentYearBatchAndSectionofStudent.setBatch(batchDetailsByReportingStudents);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, currentYearBatchAndSectionofStudent);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}	
	
	
	public byte[] downloadFile(String accessToken, String pathName) throws NoSuchFileException {
		try {

			JwtAccessTokenDetails jwtAccessTokenDetails = jwtService.getDetailsFromAccessToken(accessToken);
			JwtDetails jwtDetails=jwtService.callJwtToken(accessToken); 
			 UserDetailsDto userDetailsDto=userAuthenticationService.getUserDetailsById(jwtDetails.getUserId());
			if (StringUtils.equalsIgnoreCase(jwtAccessTokenDetails.getLms_role(), "Student")) {
				byte[] content;
				final S3Object s3Object = s3client.getObject(bucketName, StudentDetailsService.value + "/" + studentDetailsRepository.findById(userDetailsDto.getEmpOrStdId()).get().getStudent_image_path());
				final S3ObjectInputStream stream = s3Object.getObjectContent();
				content = IOUtils.toByteArray(stream);
				s3Object.close();
				return content;
			} else {
				byte[] content;
				final S3Object s3Object = s3client.getObject(bucketName, EmployeeDetailsService.value + "/" + employeeDetailsRepository.findById(userDetailsDto.getEmpOrStdId()).get().getEmp_image_attachment_path());
				final S3ObjectInputStream stream = s3Object.getObjectContent();
				content = IOUtils.toByteArray(stream);
				System.out.println(content);
				s3Object.close();
				return content;
			}

		} catch (AmazonS3Exception e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
				throw new NoSuchFileException("File Not Found");
			}
			throw new AmazonClientException("", e);
		} catch (IOException | AmazonClientException ex) {
			throw new AmazonClientException("", ex);
		}
	}

	public ResponseEntity<Object> getStudentDetailsByAuidForLms(String auid) {
		HashMap<String, Object> studentDetails=studentDetailsRepository.getStudentDetailsByAuidForLms(auid);
		return ResponseHandler.generateResponse(true, HttpStatus.OK,studentDetails);
	}

	public ResponseEntity<Object> assignedStudentDetailsToProctor(String accessToken) {
		try {
			try {
				TokenDetails tokenDetails = tokenDetailsRepository.findByAccessToken(accessToken);
				if (ObjectUtils.isEmpty(tokenDetails)) {
					return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
							"Token is Expired. Please login again!!", null);
				}
				if (isTokenExpired(tokenDetails.getToken())) {
					tokenDetails.setAccessToken(null);
					tokenDetails.setToken(null);
					tokenDetailsRepository.save(tokenDetails);
					return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
							"Token is Expired. Please login again!!", null);
				} 
			}catch (Exception e) {
					return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			}
			JwtAccessTokenDetails jwtAccessTokenDetails = jwtService.getDetailsFromAccessToken(accessToken);
			List<HashMap<String,Object>> studentDetails=proctorStudentAssignmentRepository.studentDetailsAssignedToProctor(jwtAccessTokenDetails.getEmp_id());
			return ResponseHandler.generateResponse(true, HttpStatus.OK,studentDetails);
			
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		} 

	}

	public ResponseEntity<Object> schoolDetailsForLms() {
		try {
		List<HashMap<String,Object>> schoolDetails=schoolRepository.schoolDetailsForLms();
		return ResponseHandler.generateResponse(true, HttpStatus.OK,schoolDetails);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	public ResponseEntity<Object> studentListBySchoolSpecializationAcYearAndTokenFlag(Integer schoolId,
			Integer specializationId, Integer acYearId, Integer tokenFlag, String accessToken) {
		if(ObjectUtils.isNotEmpty(accessToken) && tokenFlag == 0) {
			try {
				TokenDetails tokenDetails = tokenDetailsRepository.findByAccessToken(accessToken);
				if (ObjectUtils.isEmpty(tokenDetails)) {
					return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
							"Token is Expired. Please login again!!", null);
				}
				if (isTokenExpired(tokenDetails.getToken())) {
					tokenDetails.setAccessToken(null);
					tokenDetails.setToken(null);
					tokenDetailsRepository.save(tokenDetails);
					return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
							"Token is Expired. Please login again!!", null);
				}
			List<HashMap<String, Object>> studentList= studentDetailsRepository.studentDetailsBySchoolSpecializationAcYear(schoolId,specializationId,acYearId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,studentList);
			} catch (Exception e) {
				return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			}
		}else {
			List<HashMap<String, Object>> studentList= studentDetailsRepository.studentDetailsBySchoolSpecializationAcYear(schoolId,specializationId,acYearId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,studentList);
		}
	}

	public ResponseEntity<Object> studentSubjects(String accessToken) {
		try {
			TokenDetails tokenDetails = tokenDetailsRepository.findByAccessToken(accessToken);
			if (ObjectUtils.isEmpty(tokenDetails)) {
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}
			if (isTokenExpired(tokenDetails.getToken())) {
				tokenDetails.setAccessToken(null);
				tokenDetails.setToken(null);
				tokenDetailsRepository.save(tokenDetails);
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}
			JwtAccessTokenDetails jwtAccessTokenDetails = jwtService.getDetailsFromAccessToken(accessToken);
			programAndTypeDetailsOfStudentDto programDetails = studentDetailsRepository.programAndTypeDetailsOfStudentDto(jwtAccessTokenDetails.getStudent_id());
			if (StringUtils.equals(programDetails.getProgramType(), "Yearly")) {
				Map<String,Object> response=new TreeMap<>();
				for(int i= 1 ; i <= jwtAccessTokenDetails.getCurrent_year(); i++) {
					List<TreeMap<String, Object>> allSubjects=new ArrayList<>();
//					List<HashMap<String, Object>> courseAssignmentCourse=courseAssignmentRepository.courseDetailBySpecializationAndYearSem(programDetails.getProgramSpecializationId(),i);
					List<Map<String, Object>> subjectAssignedToStudent=courseStudentAssignmentRepository.subjectAssignedToStudent(jwtAccessTokenDetails.getStudent_id(),i);
					
//					for (HashMap<String, Object> course : courseAssignmentCourse) {
//					    TreeMap<String, Object> subjectMap = new TreeMap<>(course);
//					    allSubjects.add(subjectMap);
//					}
					
					for (Map<String, Object> studentSubject : subjectAssignedToStudent) {
					    TreeMap<String, Object> subjectMap = new TreeMap<>(studentSubject);  
					    allSubjects.add(subjectMap);
					}
					response.put(String.valueOf(i), allSubjects);	
				}
				return ResponseHandler.generateResponse(true, HttpStatus.OK,response);
			} else {
				Map<String, Object> response = new TreeMap<>(); 
				int year = 1;  
				for (int i = 1; i <= jwtAccessTokenDetails.getCurrent_sem(); i++) {
				    List<TreeMap<String, Object>> allSubjects = new ArrayList<>();

				    
//				    List<HashMap<String, Object>> courseAssignmentCourse =
//				        courseAssignmentRepository.courseDetailBySpecializationAndYearSem(programDetails.getProgramSpecializationId(), i);
				    
				    
				    List<Map<String, Object>> subjectAssignedToStudent = 
				        courseStudentAssignmentRepository.subjectAssignedToStudent(jwtAccessTokenDetails.getStudent_id(), i);

				    
				    

				    
//				    for (HashMap<String, Object> course : courseAssignmentCourse) {
//				        course.put("current_year", String.valueOf(year));
//				        course.put("current_sem", String.valueOf(i));
//
//				        TreeMap<String, Object> subjectMap = new TreeMap<>(course);
//				        allSubjects.add(subjectMap);
//				    }

				    
				    for (Map<String, Object> studentSubject : subjectAssignedToStudent) {
						TreeMap<String, Object> subjectMap = new TreeMap<>(studentSubject);
						subjectMap.put("current_year", String.valueOf(year));
						subjectMap.put("current_sem", String.valueOf(i));

				       

				        allSubjects.add(subjectMap);
				    }

				    
				    Map<String, Object> yearSubjects = (Map<String, Object>) response.get(String.valueOf(year));

				    
				    if (yearSubjects == null) {
				        yearSubjects = new TreeMap<>();
				    }

				    
				    yearSubjects.put(String.valueOf(i), allSubjects);
				    response.put(String.valueOf(year), yearSubjects);
				    year = i % 2 == 0 ? year + 1 : year;
				}
				return ResponseHandler.generateResponse(true, HttpStatus.OK,response);
			}
			
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	public ResponseEntity<Object> acYearDetails(Integer tokenFlag) {
		try {
//			TokenDetails tokenDetails = tokenDetailsRepository.findByAccessToken();
//			if (isTokenExpired(tokenDetails.getToken())) {
//				tokenDetails.setAccessToken(null);
//				tokenDetails.setToken(null);
//				tokenDetailsRepository.save(tokenDetails);
//				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
//						"Token is Expired. Please login again!!", null);
//			}
			if(ObjectUtils.isNotEmpty(tokenFlag) && tokenFlag== 1) {
				List<Map<String, Object>> acYearDetails=academicYearRepository.activeAcademicYear();
				return ResponseHandler.generateResponse(true, HttpStatus.OK,acYearDetails);
			} else {
				List<Map<String, Object>> acYearDetails=academicYearRepository.latestAcademicYear();
				return ResponseHandler.generateResponse(true, HttpStatus.OK,acYearDetails);
			}
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	public ResponseEntity<Object> specializationDetails() {
		List<Map<String,Object>> specializationDetails=programSpecilizationRepository.specializationDetails();
		return ResponseHandler.generateResponse(true, HttpStatus.OK,specializationDetails);
	}

	public ResponseEntity<Object> batchOrSectionAssignedStudentDetails(Integer section_assignment_id,
			Integer batch_assignment_id,String accessToken) {
		try {
			TokenDetails tokenDetails = tokenDetailsRepository.findByAccessToken(accessToken);
			if (ObjectUtils.isEmpty(tokenDetails)) {
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}
			if (isTokenExpired(tokenDetails.getToken())) {
				tokenDetails.setAccessToken(null);
				tokenDetails.setToken(null);
				tokenDetailsRepository.save(tokenDetails);
				return ResponseHandler.generateResponse(true, HttpStatus.UNAUTHORIZED,
						"Token is Expired. Please login again!!", null);
			}
			SectionAssignment sectionAssignment=null;
			BatchAssignment batchAssignment=null;
			if(ObjectUtils.isNotEmpty(section_assignment_id)) {
				sectionAssignment=sectionAssignmentRepository.activeSectionAssignmentDetail(section_assignment_id);
			}
			if(ObjectUtils.isNotEmpty(batch_assignment_id)) {
				batchAssignment=batchAssignmentRepository.activeBatchAssignmentDetail(batch_assignment_id);
			}
			String sectionAssignedStudentIds=ObjectUtils.isNotEmpty(sectionAssignment) && ObjectUtils.isNotEmpty(sectionAssignment.getStudent_ids()) ? sectionAssignment.getStudent_ids():"";
			String batchAssignedStudentIds=ObjectUtils.isNotEmpty(batchAssignment) && ObjectUtils.isNotEmpty(batchAssignment.getStudent_ids()) ? batchAssignment.getStudent_ids():"";		
			List<Integer> assignedStudentIds=ResponseHandler.toConvertCommaSeperatedIdsAsList(sectionAssignedStudentIds+","+batchAssignedStudentIds);
			List<HashMap<String,Object>> assignedStudentDetails=studentDetailsRepository.assignedBatchOrSectionStudentDetails(assignedStudentIds);

			return ResponseHandler.generateResponse(true, HttpStatus.OK,assignedStudentDetails);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	static Section section;
	static Batch batch;
	public ResponseEntity<Object> studentAttendanceByFaculty(AttendanceResponseDTO attendance, String accessToken) {
		try {
			
			UserAuthentication userDetails=userAuthenticationRepository.userDetailsByEmployeeCode(attendance.getData().getClassDetails().getEmpID());
			Integer employeeId=employeeDetailsRepository.getEmpIdByEmpCode(attendance.getData().getClassDetails().getEmpID());
			List<StudentAttendance> studentAttendances=new ArrayList<>();
			TimeTable timeTime=timeTableRepository.findById(Integer.valueOf(attendance.getData().getClassDetails().getTimeTableId())).get();
			ProgramSpecilization specialization=programSpecilizationRepository.findById(Integer.valueOf(attendance.getData().getClassDetails().getCourseBranchAssignmentId())).get();
			String programType=programAssigmentRepository.getProgramTypeOnProgramAssignmentId(specialization.getProgram_assignment_id());
			if(ObjectUtils.isNotEmpty(attendance.getData().getClassDetails().getSectionShortName())) 
					section=sectionRepository.findBySectionName(attendance.getData().getClassDetails().getSectionShortName());
			
			if(ObjectUtils.isNotEmpty(attendance.getData().getClassDetails().getBatchShortName()))
						batch=batchRepository.findByBatchShortName(attendance.getData().getClassDetails().getBatchShortName());
			
			attendance.getData().getStudentAttendance().stream().forEach(st -> {
				Integer studentId=studentDetailsRepository.getStudentIdByAuid(st);
				StudentAttendance sa=new StudentAttendance();
				sa.setSchool_id(Integer.valueOf(attendance.getData().getClassDetails().getInstituteId()));
				sa.setAc_year_id(timeTime.getAc_year_id());
				Course cr=courseRepository.findCourseByNameAndShortName(attendance.getData().getClassDetails().getSubjectNameShort());
				sa.setCourse_id(cr.getCourse_id());
				sa.setRemarks("Lms Attendance");
				sa.setDescription(attendance.getMessage());
				sa.setStudent_id(studentId);
				sa.setPresent_status(true);
				sa.setOffline_status(timeTime.getIs_online());
				
				if(ObjectUtils.isNotEmpty(batch)) 
					sa.setBatch_id(batch.getBatch_id());
				else 
					sa.setBatch_id(null);
				
				
				if(ObjectUtils.isNotEmpty(section)) 
					sa.setSection_id(section.getSection_id());
				else 
					sa.setSection_id(null);
				
				sa.setDate_of_class(timeTime.getSelected_date());
				sa.setTime_slots_id(timeTime.getTime_slots_id());
				sa.setTime_table_id(timeTime.getTime_table_id());
				Integer currentOrSem=programType.equalsIgnoreCase("Semester") ? timeTime.getCurrent_sem(): timeTime.getCurrent_year();
				sa.setYear_or_sem(currentOrSem);
				sa.setActive(true);
				sa.setCreated_username(userDetails.getUsername());
				sa.setCreated_by(userDetails.getId());
				sa.setCourse_assignment_id(Integer.valueOf(attendance.getData().getClassDetails().getInstituteId()));
				sa.setEmp_id(employeeId);
				studentAttendances.add(sa);
				
			});
			if(! attendance.getData().getClassDetails().getBatchAssignmentId().equals("0")) {
				studentAttendanceService.saveStudentAttendanceBatchForLMS(studentAttendances,Integer.valueOf(attendance.getData().getClassDetails().getBatchAssignmentId()));
			}else {
				studentAttendanceService.saveStudentAttendanceOnSectionForLMS(studentAttendances,Integer.valueOf(attendance.getData().getClassDetails().getSectionAssignmentId()));
			}
			return ResponseHandler.generateResponse(true, HttpStatus.OK,"Attendance is taken !!");
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}	
}
