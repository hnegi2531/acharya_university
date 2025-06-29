package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.Date;
import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
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
import com.au.dto.ChangeOfCourseProgramAttachmentRequest;
import com.au.dto.ChangeOfCourseProgramDto;
import com.au.dto.JwtDetails;
import com.au.dto.ReportingStudentsDto;
import com.au.dto.StudentDetailsDto;
import com.au.event.StudentDueEvent;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Academic_year;
import com.au.model.ChangeOfCourseProgramAttachment;
import com.au.model.ReportingStudents;
import com.au.model.Roles;
import com.au.model.Student_Details;
import com.au.model.UserAuthentication;
import com.au.model.UserRole;
import com.au.repository.Academic_year_repository;
import com.au.repository.ChangeOfCourseProgramRepository;
import com.au.repository.ProgramSpecilizationRepository;
import com.au.repository.ReportingStudentsRepository;
import com.au.repository.RolesRepository;
import com.au.repository.School_Repository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.StudentTranscriptSubmissionRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.repository.UserRoleRepository;
import com.au.response.ResponseHandler;
import com.au.service.ChangeOfCourseProgramService;

@Service
public class ChangeOfCourseProgramService {

	
Logger logger = LoggerFactory.getLogger(ChangeOfCourseProgramService.class);
	
	@Autowired
	private JwtTokenService jwtService;
	
	@Autowired
	private ChangeOfCourseProgramRepository changeOfCourseProgramRepository;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	
	@Autowired
	private Academic_year_repository academicYearRepository;

	@Autowired
	private ProgramSpecilizationRepository programSpecilizationRepository;
	
	
	@Autowired
	private ReportingStudentsRepository reportingStudentsRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private RolesRepository rolesRepository;
	
	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;
	
	@Autowired
	private StudentTranscriptSubmissionRepository studentTranscriptSubmissionRepository;
	
	@Autowired
	private School_Repository schoolRepository;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
   public static final String value = "ChangeOfCourseProgramAttachmentBucket";
   
   public static final Integer INTIATED_COURSE_APPROVER_STATUS=2;
   
   public static final Integer NEW_STUDENT_COURSE_APPROVER_STATUS=0;
   
   public static final String USER_DEFAULT_PASSWORD="acharya12345";
   
   private final ModelMapper modelMapper = new ModelMapper();
	
	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;
	

	
	@SuppressWarnings("deprecation")
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}
	
	
	public ResponseEntity<Object> changeOfCourseProgram(ChangeOfCourseProgramDto  changeOfCourseProgramDto, @RequestHeader("Authorization") String jwtToken) {
		try {
			Integer courseApproverStatusOfStudent=studentDetailsRepository.getPreviousCourseApproverStatus(changeOfCourseProgramDto.getOldStudentId());
			if(ObjectUtils.isNotEmpty(courseApproverStatusOfStudent) && studentDetailsRepository.getPreviousCourseApproverStatus(changeOfCourseProgramDto.getOldStudentId()).equals(INTIATED_COURSE_APPROVER_STATUS) ) {
				return ResponseHandler.generateResponse(true, HttpStatus.ALREADY_REPORTED,"Change Of Course Already Initiated For the Student");
			}else {
				
				JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
				Student_Details OldStudent=studentDetailsRepository.getStudentByStudentIdAndActive(changeOfCourseProgramDto.getOldStudentId());
				modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
				StudentDetailsDto studentDetailsDto=modelMapper.map(OldStudent, StudentDetailsDto.class);
				Student_Details newStudentDetails=modelMapper.map(studentDetailsDto, Student_Details.class);
				
				newStudentDetails.setCreated_by(jwtDetails.getUserId());
				newStudentDetails.setCreated_username(jwtDetails.getUserName());
				newStudentDetails.setActive(false);	
				newStudentDetails.setAuid(null);
				newStudentDetails.setAcharya_email(null);
				newStudentDetails.setCourse_approver_status(NEW_STUDENT_COURSE_APPROVER_STATUS);
				newStudentDetails.setOld_student_id(changeOfCourseProgramDto.getOldStudentId());
				newStudentDetails.setAc_year_id(changeOfCourseProgramDto.getAcYearId());
				newStudentDetails.setSchool_id(changeOfCourseProgramDto.getSchoolId());
				newStudentDetails.setProgram_assignment_id(changeOfCourseProgramDto.getProgramAssignmentId());
				newStudentDetails.setProgram_id(changeOfCourseProgramDto.getProgramId());
				newStudentDetails.setProgram_specialization_id(changeOfCourseProgramDto.getProgramSpecializationId());
				newStudentDetails.setFee_admission_category_id(changeOfCourseProgramDto.getFeeAdmissionCategoryId());
				newStudentDetails.setNationality(changeOfCourseProgramDto.getNationalityId().toString());
				newStudentDetails= studentDetailsRepository.save(newStudentDetails);
				studentDetailsRepository.updateChangeOfCourseApproverStatus(changeOfCourseProgramDto.getOldStudentId());
				reportingStudentCreation(changeOfCourseProgramDto.getOldStudentId(),newStudentDetails);

				return ResponseHandler.generateResponse(true, HttpStatus.OK,newStudentDetails);
			}
		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
		}
	}
	
	private void reportingStudentCreation(Integer oldStudentId, Student_Details newStudentDetails) {
		ReportingStudents oldReportingStudent = reportingStudentsRepository.getDetailsOfReportingStudentsByStudentId(oldStudentId);
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ReportingStudentsDto reportingStudentDto=modelMapper.map(oldReportingStudent, ReportingStudentsDto.class);
		ReportingStudents newReportingStudent =modelMapper.map(reportingStudentDto, ReportingStudents.class);
		newReportingStudent.setReporting_id(null);
		newReportingStudent.setStudent_id(newStudentDetails.getStudent_id());
		newReportingStudent.setCreated_by(newStudentDetails.getCreated_by());
		newReportingStudent.setCreated_username(newStudentDetails.getCreated_username());
		newReportingStudent.setActive(false);
		reportingStudentsRepository.save(newReportingStudent);
		
	}


	public ChangeOfCourseProgramAttachment uploadFile(ChangeOfCourseProgramAttachmentRequest attachmentfilerequest,MultipartFile multipartFile, Integer newStudentId,String jwtToken) throws IOException{
		
		ChangeOfCourseProgramAttachment changeOfCourseProgramAttachment = ObjectUtils
				.isNotEmpty(changeOfCourseProgramRepository
						.findByNewStudentIdAndActiveTrue(attachmentfilerequest.getNewStudentId()))
								? changeOfCourseProgramRepository
										.findByNewStudentIdAndActiveTrue(attachmentfilerequest.getNewStudentId())
								: new ChangeOfCourseProgramAttachment();
		JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
		try
		{
			logger.debug("Message For Change Of Course Program Attachment --------------");
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			changeOfCourseProgramAttachment.setNewStudentId(newStudentId);
			changeOfCourseProgramAttachment.setCreated_by(jwtDetails.getUserId());
			changeOfCourseProgramAttachment.setCreated_username(jwtDetails.getUserName());
			changeOfCourseProgramAttachment.setActive(true);
			changeOfCourseProgramAttachment.setRemarks(attachmentfilerequest.getRemarks());
			changeOfCourseProgramAttachment.setAmount(attachmentfilerequest.getAmount());
			logger.debug("Message For Change Of Course Program Attachment", file);
			changeOfCourseProgramAttachment.setChangeOfCourseProgramAttachmentFileName(fileName);
			changeOfCourseProgramAttachment.setChangeOfCourseProgramAttachmentPath(LocalDate.now() + "/" + newStudentId + "/" + fileName);
			changeOfCourseProgramAttachment.setChangeOfCourseProgramAttachmentType(
					endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + newStudentId + "/" + fileName);
			uploadFileToS3Bucket(fileName, file, newStudentId);
			logger.debug("Message For Change Of Course Program Attachment", file);
			file.delete();
		}catch (AmazonServiceException ase) {

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
		return changeOfCourseProgramRepository.save(changeOfCourseProgramAttachment);
	}
	
	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convertFile = new File(file.getOriginalFilename());
		try(FileOutputStream fos = new FileOutputStream(convertFile)){
			fos.write(file.getBytes());
			fos.close();
		}
		return convertFile;

	}
	
	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}
	
	private void uploadFileToS3Bucket(String fileName, File file, Integer newStudentId) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + newStudentId + "/" + fileName; 
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));
		
	}
	
	public byte[] downloadFile(final String keyName) throws NoSuchFileException {
		try {
			byte[] content;
			final S3Object s3Object = s3client.getObject(bucketName, value + "/" + keyName);
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
	
	public  ResponseEntity<Object> changeOfCourseProgramAttachmentDetail(Integer studentId) {
		ChangeOfCourseProgramAttachment details= changeOfCourseProgramRepository.changeOfCourseProgramAttachmentDetail(studentId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, details);
	}

	public ResponseEntity<Object> initiatedChangeOfCourseProgramStudentDetailsSerach(Pageable pageable, Object keyword) {

		Page<Object> response1 = changeOfCourseProgramRepository.initiatedChangeOfCourseProgramStudentDetailsSerach(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> initiatedChangeOfCourseProgramStudentDetailsSorting(Pageable pageable) {

		Page<Object> response = changeOfCourseProgramRepository.initiatedChangeOfCourseProgramStudentDetailsSorting(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public ResponseEntity<Object> approveChangeOfCourseProgramRequest(ChangeOfCourseProgramDto  changeOfCourseProgramDto,String jwtToken) {
		Student_Details newStudentData = studentDetailsRepository.findById(changeOfCourseProgramDto.getNewStudentId())
				.orElseThrow(() -> new ResourceNotFoundException("Student_Details Not Found:" + changeOfCourseProgramDto.getNewStudentId()));

		Student_Details oldDataOFStudent = studentDetailsRepository.findById(newStudentData.getOld_student_id())
				.orElseThrow(() -> new ResourceNotFoundException("Student_Details With Previous Student Id Not Found:" + newStudentData.getOld_student_id()));
		try {
			creatingAuidAndEmailForNewStudent(newStudentData,oldDataOFStudent,changeOfCourseProgramDto,jwtToken);
			reportingStudentsRepository.activateReporitngOfNewStudentTable(newStudentData.getStudent_id());
			newStudentUserCreation(newStudentData,oldDataOFStudent);
			studentTranscriptSubmissionRepository.updateOldStudentIdToNewStudentId(newStudentData.getStudent_id(),newStudentData.getOld_student_id());
			studentDetailsRepository.updateApplicantDetails(newStudentData.getStudent_id(),newStudentData.getOld_student_id());
			studentDetailsRepository.updatePGApplicable(newStudentData.getStudent_id(),newStudentData.getOld_student_id());
			studentDetailsRepository.updateStdEntranceExam(newStudentData.getStudent_id(),newStudentData.getOld_student_id());
			changeOfCourseProgramRepository.updateApprovalStatusByNewStudentId(newStudentData.getStudent_id());
			StudentDueEvent studDue=new StudentDueEvent(null,null,newStudentData.getStudent_id(),null);
			applicationEventPublisher.publishEvent(studDue);
				
			
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "Course Of Change Has Approved !!");
		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	private void creatingAuidAndEmailForNewStudent(Student_Details newStudentData, Student_Details oldDataOFStudent, ChangeOfCourseProgramDto changeOfCourseProgramDto, String jwtToken) {
		try {
			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
			newAuidAndEmailCreation(newStudentData,changeOfCourseProgramDto,jwtToken);
			newStudentData.setCourse_approver_status(null);
			newStudentData.setOld_student_id(changeOfCourseProgramDto.getOldStudentId());
			newStudentData.setAc_year_id(changeOfCourseProgramDto.getAcYearId());
			newStudentData.setSchool_id(changeOfCourseProgramDto.getSchoolId());
			newStudentData.setProgram_assignment_id(changeOfCourseProgramDto.getProgramAssignmentId());
			newStudentData.setProgram_id(changeOfCourseProgramDto.getProgramId());
			newStudentData.setProgram_specialization_id(changeOfCourseProgramDto.getProgramSpecializationId());
			newStudentData.setFee_admission_category_id(changeOfCourseProgramDto.getFeeAdmissionCategoryId());
			newStudentData.setFee_template_id(changeOfCourseProgramDto.getFeeTempalateId());
			newStudentData.setModified_by(jwtDetails.getUserId());
			newStudentData.setModified_username(jwtDetails.getUserName());
			newStudentData.setActive(true);
			newStudentData=studentDetailsRepository.save(newStudentData);
			deactivationOfOldStudentData(oldDataOFStudent,jwtDetails.getUserId(),jwtDetails.getUserName());
			

			
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Check Program Code Or Specialization Auid Format IS NULL Or Role Name Student Not Found Or"
							+ e.getMessage());
		}
	}
	
	
	public static final Integer STARTING_OR_INCREMENT_COUNT=1;
	private void newAuidAndEmailCreation(Student_Details newStudentData,
			ChangeOfCourseProgramDto changeOfCourseProgramDto, String jwtToken) {
		
		Academic_year academicYearsDetails=academicYearRepository.findByAcademicId(changeOfCourseProgramDto.getAcYearId());
		if (academicYearsDetails == null) {
			throw new IllegalArgumentException("Academic Year IS NULL");
		}
		String acYear = academicYearsDetails.getCurrent_year().toString().substring(2, 4);
		String specializationAuidFormat = programSpecilizationRepository.getProgramAuid(changeOfCourseProgramDto.getProgramSpecializationId());
		if (specializationAuidFormat == null) {
			throw new IllegalArgumentException("Specialization Auid Format IS NULL");
		}
		String schoolShortName = schoolRepository.getSchoolShortName(changeOfCourseProgramDto.getSchoolId());
		String acharyaEmail = newStudentData.getEmail_preferred_name().toLowerCase() + acYear + specializationAuidFormat.toLowerCase() + "@acharya.ac.in";
		
		Integer lastAuidCount=studentDetailsRepository.getLastStudentDetailsAuidCount(changeOfCourseProgramDto.getAcYearId(), changeOfCourseProgramDto.getProgramId(),changeOfCourseProgramDto.getProgramSpecializationId());
		Integer countForAuid=ObjectUtils.isNotEmpty(lastAuidCount) ? lastAuidCount + STARTING_OR_INCREMENT_COUNT : STARTING_OR_INCREMENT_COUNT;
		
		String formattedCount = String.format("%03d", countForAuid);
		String newAuid = schoolShortName + acYear + specializationAuidFormat + formattedCount;
		newStudentData.setAuid(newAuid);
		newStudentData.setAcharya_email(acharyaEmail);
		
	}
	
	
//	private void deactivationOfOldStudentData(Student_Details oldStudent, Integer userId, String userName) {
//
//		if(userAuthenticationRepository.getUserAuthenticationDetails(oldStudent.getAcharya_email()) != null) {
//			userRoleRepository.deactivateRoleOfUser(userAuthenticationRepository.getUserAuthenticationDetails(oldStudent.getAcharya_email()).getId());
//			userAuthenticationRepository.deactivateUserByEmail(oldStudent.getAcharya_email());
//		}
//
//		oldStudent.setActive(false);
//		oldStudent.setCourse_approver_status(1);
//		oldStudent.setModified_by(userId);
//		oldStudent.setModified_username(userName);
//		studentDetailsRepository.save(oldStudent);
//		reportingStudentsRepository.deactivateReporitngStudentTable(oldStudent.getStudent_id());
//
//	}
	
	
	private void deactivationOfOldStudentData(Student_Details oldStudent, Integer userId, String userName) {
	    // Get the UserAuthentication details based on the student's email
	    UserAuthentication userAuth = userAuthenticationRepository.getUserAuthenticationDetails(oldStudent.getAcharya_email());

	    if (userAuth != null) {
	        // Deactivate the user role
	        userRoleRepository.deactivateRoleOfUser(userAuth.getId());
	        // Deactivate the user by email
	        userAuthenticationRepository.deactivateUserByEmail(oldStudent.getAcharya_email());
	    } else {
	        // Log or handle the case where the user does not exist
	        System.out.println("User not found for email: " + oldStudent.getAcharya_email());
	    }

	    // Deactivate the old student record
	    oldStudent.setActive(false);
	    oldStudent.setCourse_approver_status(1);
	    oldStudent.setModified_by(userId);
	    oldStudent.setModified_username(userName);
	    studentDetailsRepository.save(oldStudent);

	    // Deactivate the corresponding reporting student record
	    reportingStudentsRepository.deactivateReporitngStudentTable(oldStudent.getStudent_id());
	}



//	private void newStudentUserCreation(Student_Details newStudentData, Student_Details oldDataOFStudent) {
//		
//		UserAuthentication oldUser=userAuthenticationRepository.getUserAuthenticationDetails(oldDataOFStudent.getAcharya_email());
//		UserAuthentication newUser=new UserAuthentication();
//		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		String passwordEncoder1 = passwordEncoder.encode(USER_DEFAULT_PASSWORD);
//
//		newUser.setUsername(newStudentData.getAuid());
//		newUser.setPassword(passwordEncoder1);
//		newUser.setEmail(newStudentData.getAcharya_email());
//		newUser.setUsertype("Student");
//		newUser.setCreated_by(newStudentData.getModified_by());
//		newUser.setCreated_username(newStudentData.getModified_username());
//		newUser.setActive(true);
//		newUser.setUsercode(oldUser.getUsercode());
//		newUser.setGuest_type(oldUser.getGuest_type());
//		newUser.setIs_lms_auditor(oldUser.getIs_lms_auditor());
//		newUser=userAuthenticationRepository.save(newUser);
//		newStudentUserRoleCreation(newUser);
//		
//	}
	
	private void newStudentUserCreation(Student_Details newStudentData, Student_Details oldDataOFStudent) {
	    // Get the old user details based on the student's email
	    UserAuthentication oldUser = userAuthenticationRepository.getUserAuthenticationDetails(oldDataOFStudent.getAcharya_email());
	    
	    // Create a new UserAuthentication object
	    UserAuthentication newUser = new UserAuthentication();
	    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    
	    // Encode the default password
	    String passwordEncoder1 = passwordEncoder.encode(USER_DEFAULT_PASSWORD);

	    // Set basic details for the new user
	    newUser.setUsername(newStudentData.getAuid());
	    newUser.setPassword(passwordEncoder1);
	    newUser.setEmail(newStudentData.getAcharya_email());
	    newUser.setUsertype("Student");
	    newUser.setCreated_by(newStudentData.getModified_by());
	    newUser.setCreated_username(newStudentData.getModified_username());
	    newUser.setActive(true);

	    // Only set the values from oldUser if it is not null
	    if (oldUser != null) {
	        newUser.setUsercode(oldUser.getUsercode());
	        newUser.setGuest_type(oldUser.getGuest_type());
	        newUser.setIs_lms_auditor(oldUser.getIs_lms_auditor());
	    } else {
	        // Optionally, log a message or handle the case where oldUser is null
	        System.out.println("Old user not found for email: " + oldDataOFStudent.getAcharya_email());
	    }

	    // Save the new user to the repository
	    newUser = userAuthenticationRepository.save(newUser);

	    // Create the role for the new user
	    newStudentUserRoleCreation(newUser);
	}



	private void newStudentUserRoleCreation(UserAuthentication newUser) {
		Roles roles = rolesRepository.findRoleByRoleName("Student");
		if (roles != null) {
			UserRole userRole = new UserRole();
			userRole.setId(newUser.getId());
			userRole.setRole_id(roles.getRole_id());
			userRole.setCreated_by(newUser.getCreated_by());
			userRole.setCreated_username(newUser.getCreated_username());
			userRole.setActive(true);
			userRoleRepository.save(userRole);
		} else {
			userAuthenticationRepository.deleteById(newUser.getId());
			throw new IllegalArgumentException("Role Name Student Not Found");
		}
		
	}

}
