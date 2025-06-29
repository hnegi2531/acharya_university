package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import com.au.repository.*;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.au.dto.HostelBedAssignmentDto;
import com.au.dto.HostelIdCardDto;
import com.au.dto.JwtDetails;
import com.au.dto.StudentHostelBedAssignmentDetails;
import com.au.model.Academic_year;
import com.au.model.HostelBedAssignment;
import com.au.model.HostelBlocks;
import com.au.model.HostelDue;
import com.au.model.ReportingStudents;
import com.au.model.Student_Details;
import com.au.response.ResponseHandler;

@Service
public class HostelBedAssignmentService {

	Logger log = LoggerFactory.getLogger(HostelBedAssignmentService.class);

	@Autowired
	private HostelBedAssignmentRepository hostelBedAssignmentRepository;

	@Autowired
	private HostelBlocksRepository hostelBlocksRepository;

	@Autowired
	private HostelFloorRepository hostelFloorRepository;

	@Autowired
	private HostelRoomsRepository hostelRoomsRepository;

	@Autowired
	private Academic_year_repository academicYearRepository;

	@Autowired
	private HostelBedsRepository hostelBedsRepository;

	@Autowired
	private HostelFeeTemplateRepository hostelFeeTemplateRepository;

	@Autowired
	private StudentDetailsRepository studentDetailsRepository;

	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;

	@Autowired
	private StudentDetailsService studentDetailsService;

	@Autowired
	private AcademicYearService academicYearService;

	@Autowired
	private JwtTokenService jwtService;

	@Autowired
	private ReportingStudentsRepository reportingStudentsRepository;

	@Autowired
	private HostelDueRepository hostelDueRepository;
	
	@Autowired
	private StudentPaymentHistoryRepository studentPaymentHistoryRepository;
	
	@Autowired
	private HostelWaiverRepository hostelWaiverRepository;

    @Autowired
    private HostelFeeReceiptVoucherHeadWiseRepository hstlFeeReciptRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public static final String value = "CancelHostelBedAssignmentBucket";

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

	public ResponseEntity<Object> hostelBedAssignment(@Valid HostelBedAssignmentDto hostelBedAssignmentDto,
			String jwtToken) {
		try {

			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
			HostelBedAssignment hostelBedAssignment = modelMapper.map(hostelBedAssignmentDto,
					HostelBedAssignment.class);
			hostelBedAssignment.setCreatedBy(jwtDetails.getUserId());
			hostelBedAssignment.setCreatedUsername(jwtDetails.getUserName());
			hostelBedAssignment
					.setHostelBlock(hostelBlocksRepository.findById(hostelBedAssignmentDto.getHostelBlockId()).get());
			hostelBedAssignment
					.setHostelFloor(hostelFloorRepository.findById(hostelBedAssignmentDto.getHostelFloorId()).get());
			hostelBedAssignment
					.setHostelRoom(hostelRoomsRepository.findById(hostelBedAssignmentDto.getHostelRoomId()).get());
			hostelBedAssignment
					.setHostelBed(hostelBedsRepository.findById(hostelBedAssignmentDto.getHostelBedId()).get());
			hostelBedAssignment.setAcYear(academicYearRepository.findById(hostelBedAssignmentDto.getAcYearId()).get());
			hostelBedAssignment.setHostelFeeTemplate(
					hostelFeeTemplateRepository.findById(hostelBedAssignmentDto.getHostelFeeTemplateId()).get());
			hostelBedAssignment
					.setStudent(studentDetailsRepository.findById(hostelBedAssignmentDto.getStudentId()).get());
			if (ObjectUtils.isNotEmpty(hostelBedAssignmentDto.getVacateBy())) {
				hostelBedAssignment
						.setVacateBy(userAuthenticationRepository.findById(hostelBedAssignmentDto.getVacateBy()).get());
			}
			hostelBedAssignmentRepository.save(hostelBedAssignment);
			hostelBedsRepository.updateBedStatusByBedId(hostelBedAssignmentDto.getBedStatus(),
					hostelBedAssignmentDto.getHostelBedId());
			if (ObjectUtils.isNotEmpty(hostelBedAssignmentDto.getStudentId())
					&& ObjectUtils.isNotEmpty(hostelBedAssignmentDto.getAcYearId())) {
				hostelDueRepository.hostelDueProcedureByStudentIdAndAcYearId(hostelBedAssignmentDto.getStudentId(),
						hostelBedAssignmentDto.getAcYearId());

			}
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "Created Successfully");
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}

	}

	public ResponseEntity<Object> filteredAndSortedResponses(Pageable pageable, Object keyword, Boolean active,
															 String cancelledStatus, Integer schoolId, Integer acYearId, Integer blockId) {
		Page<Object> filterAndSortedResponse = hostelBedAssignmentRepository.filteredAndSortedResponses(pageable,
				keyword, active, cancelledStatus,schoolId,acYearId,blockId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, filterAndSortedResponse);
	}

	public ResponseEntity<Object> sortedResponses(Pageable pageable1, Boolean active, String cancelledStatus, Integer schoolId, Integer acYearId, Integer blockId) {
		Page<Object> sortedResponse = hostelBedAssignmentRepository.sortedResponses(pageable1, active, cancelledStatus,schoolId,acYearId,blockId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, sortedResponse);
	}

	public ResponseEntity<Object> updateHostelBedAssignment(@Valid HostelBedAssignmentDto hostelBedAssignmentDto,
			Integer hostelBedAssignmentId, String jwtToken) {
		try {
			Optional<HostelBedAssignment> hostelBedAssignment1 = hostelBedAssignmentRepository
					.findById(hostelBedAssignmentId);
			HostelBedAssignment hostelBedAssignment = hostelBedAssignment1.get();
			JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
			if (hostelBedAssignment != null) {
				HostelBedAssignment dbd = modelMapper.map(hostelBedAssignmentDto, HostelBedAssignment.class);
				dbd.setHostelBedAssignmentId(hostelBedAssignmentId);
				modelMapper.map(dbd, hostelBedAssignment);
				hostelBedAssignment.setModifiedBy(jwtDetails.getUserId());
				hostelBedAssignment.setModifiedUsername(jwtDetails.getUserName());
				hostelBedAssignment.setHostelBlock(
						hostelBlocksRepository.findById(hostelBedAssignmentDto.getHostelBlockId()).get());
				hostelBedAssignment.setHostelFloor(
						hostelFloorRepository.findById(hostelBedAssignmentDto.getHostelFloorId()).get());
				hostelBedAssignment
						.setHostelRoom(hostelRoomsRepository.findById(hostelBedAssignmentDto.getHostelRoomId()).get());
				hostelBedAssignment
						.setHostelBed(hostelBedsRepository.findById(hostelBedAssignmentDto.getHostelBedId()).get());
				hostelBedAssignment
						.setAcYear(academicYearRepository.findById(hostelBedAssignmentDto.getAcYearId()).get());
				hostelBedAssignment.setHostelFeeTemplate(
						hostelFeeTemplateRepository.findById(hostelBedAssignmentDto.getHostelFeeTemplateId()).get());
				hostelBedAssignment
						.setStudent(studentDetailsRepository.findById(hostelBedAssignmentDto.getStudentId()).get());
				hostelBedAssignment.setFoodStatus(hostelBedAssignmentDto.getFoodStatus());
				if (ObjectUtils.isNotEmpty(hostelBedAssignmentDto.getVacateBy())) {
					hostelBedAssignment.setVacateBy(
							userAuthenticationRepository.findById(hostelBedAssignmentDto.getVacateBy()).get());
				}
				hostelBedsRepository.updateBedStatusByBedId(hostelBedAssignmentDto.getBedStatus(),
						hostelBedAssignmentDto.getHostelBedId());
				hostelBedAssignmentRepository.save(hostelBedAssignment);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, "Updated Successfully");
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR,
						"Not Updated Successfully");
			}

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}

	}

	public ResponseEntity<Object> hostelBedAssignment(Integer hostelBedAssignmentId) {
		Optional<HostelBedAssignment> hostelBedAssignment = hostelBedAssignmentRepository
				.findById(hostelBedAssignmentId);
		if (hostelBedAssignment.isPresent()) {
			return ResponseHandler.generateResponse(true, HttpStatus.OK, hostelBedAssignment.get());
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR,
					"Hostel Bed Assignment Not Found");
		}
	}

	public ResponseEntity<Object> deactiveHostelBedAssignment(Integer hostelBedAssignmentId) {
		HostelBedAssignment hostelBedAssignment = hostelBedAssignmentRepository.findById(hostelBedAssignmentId).get();
		hostelBedAssignmentRepository.deactiveHostelBedAssignment(hostelBedAssignmentId);
		hostelBedsRepository.updateBedStatusByBedId(null, hostelBedAssignment.getHostelBed().getHostelBedId());
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "Deactivated Successfully");
	}

	public ResponseEntity<Object> activateHostelBedAssignment(Integer hostelBedAssignmentId) {
		hostelBedAssignmentRepository.activateHostelBedAssignment(hostelBedAssignmentId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "Activated Successfully");
	}

	public ResponseEntity<Object> uploadFile(MultipartFile multipartFile, Integer hostelBedAssignmentId) {
		try {
			HostelBedAssignment hosetBedAssignmentDetail = (HostelBedAssignment) ((LinkedHashMap<String, Object>) hostelBedAssignment(
					hostelBedAssignmentId).getBody()).get("data");
			log.debug("Message For Cancel Bed Assignmnet Attachment");
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			log.debug("Message For Cancel Bed Assignmnet Attachment ", file);
			hosetBedAssignmentDetail.setHostelCancelledAttachmentFileName(fileName);
			hosetBedAssignmentDetail
					.setHostelCancelledAttachmentPath(LocalDate.now() + "/" + hostelBedAssignmentId + "/" + fileName);
			uploadFileToS3Bucket(fileName, file, hostelBedAssignmentId);
			log.debug("Message For Attachment", file);
			file.delete();
			hostelBedAssignmentRepository.save(hosetBedAssignmentDetail);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "Attachment Uploaded Succesfully");
		} catch (AmazonServiceException ase) {

			log.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
			log.info("Error Message:    " + ase.getMessage());
			log.info("HTTP Status Code: " + ase.getStatusCode());
			log.info("AWS Error Code:   " + ase.getErrorCode());
			log.info("Error Type:       " + ase.getErrorType());
			log.info("Request ID:       " + ase.getRequestId());
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, ase.getErrorMessage());

		} catch (AmazonClientException ace) {
			log.info("Caught an AmazonClientException: ");
			log.info("Error Message: " + ace.getMessage());
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, ace.getMessage());
		} catch (IOException ioe) {
			log.info("IOE Error Message: " + ioe.getMessage());
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, ioe.getMessage());
		}

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

	private void uploadFileToS3Bucket(String fileName, File file, Integer hostelBedAssignmentId) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + hostelBedAssignmentId + "/" + fileName;
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

	public ResponseEntity<Object> hostelBedAssignmentForBedChangeHistory(Integer academicYearId, Integer studentId) {
		try {
			Student_Details studentDetails = studentDetailsService.get(studentId);
			Academic_year academicYear = academicYearService.get(academicYearId);
			List<HostelBedAssignment> bedChangeDetails = hostelBedAssignmentRepository
					.findByAcYearAndStudentAndActiveFalse(academicYear, studentDetails);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, bedChangeDetails);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	public ResponseEntity<Object> cancelhostelBedAssignmentDetails() {

		List<HashMap<String, Object>> cancelHostelBedDetails = hostelBedAssignmentRepository
				.cancelhostelBedAssignmentDetails();
		return ResponseHandler.generateResponse(true, HttpStatus.OK, cancelHostelBedDetails);
	}

	public List<HashMap<String, Object>> getHostelDueReportByAcademicYearGroupedByBlock(
			@RequestParam(value = "acYearId", required = false) Integer acYearId) {

		Academic_year academicYear;
		if (ObjectUtils.isNotEmpty(acYearId)) {
			academicYear = academicYearService.get(acYearId);

		} else {
			academicYear = null;
		}
		return hostelBedAssignmentRepository.getHostelDueReportByAcademicYearGroupedByBlock(academicYear);
	}

	public ResponseEntity<Object> hostelBedAssignmentByAcYearIdAndStudentId(Integer academicYearId, Integer studentId) {
		try {
			Student_Details studentDetails = studentDetailsService.get(studentId);
			Academic_year academicYear = academicYearService.get(academicYearId);
			HostelBedAssignment hostelBedAssignment = hostelBedAssignmentRepository
					.findByAcYearAndStudentAndCancelledRemarksIsNullAndActiveTrue(academicYear, studentDetails);
			if (ObjectUtils.isNotEmpty(hostelBedAssignment)) {
				List<HashMap<String, Object>> feeTemplateDeatilsByTemplateName = hostelFeeTemplateRepository
						.feeTemplateDeatilsByTemplateName(
								hostelBedAssignment.getHostelFeeTemplate().getTemplate_name());
				return ResponseHandler.generateResponse(true, HttpStatus.OK, feeTemplateDeatilsByTemplateName);
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR,
						"Hostel is not assigned to student for given academic year");
			}
		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	public ResponseEntity<Object> hostelBedAssignmentByAcYearAndhostelBlock(Integer hostelBlockId) {
//		Academic_year academicYear = academicYearService.get(academicYearId);
		HostelBlocks hostelBlocks = hostelBlocksRepository.findById(hostelBlockId).get();
		List<HostelBedAssignment> hostelBedAssignments = hostelBedAssignmentRepository
				.findByHostelBlockAndCancelledRemarksIsNullAndActiveTrue(hostelBlocks);
		List<StudentHostelBedAssignmentDetails> details = new ArrayList<StudentHostelBedAssignmentDetails>();
		hostelBedAssignments.stream().forEach(hba -> {
			ReportingStudents reporting = Optional.ofNullable(reportingStudentsRepository
					.getDetailsOfReportingStudentsByStudentId(hba.getStudent().getStudent_id())).orElse(new ReportingStudents());
			HostelDue dueDetails = hostelDueRepository.hostelDueByAcademicYearIdAndStudentId(
					hba.getAcYear().getAc_year_id(), hba.getStudent().getStudent_id());
			StudentHostelBedAssignmentDetails student = StudentHostelBedAssignmentDetails.builder()
					.studentId(hba.getStudent().getStudent_id()).studentName(hba.getStudent().getStudent_name())
					.auid(hba.getStudent().getAuid()).usn(hba.getStudent().getUsn())
					.acYear(hba.getAcYear().getAc_year()).occipiedDate(hba.getFromDate())
					.year(Optional.ofNullable(reporting.getCurrent_year()).orElse(null)).sem(Optional.ofNullable(reporting.getCurrent_sem()).orElse(null))
					.bedName(hba.getHostelBed().getBedName()).fixed(dueDetails.getTotal_amount())
					.paid(dueDetails.getPaid()).due(dueDetails.getDue()).waiver(dueDetails.getWaiver()).build();
			details.add(student);
		});
		return ResponseHandler.generateResponse(true, HttpStatus.OK, details);
	}

	public ResponseEntity<Object> isHostelBedAssignedByAcademicYearAndStudentId(Integer academicYearId,
			Integer studentId) {

		Academic_year academicYear = academicYearService.get(academicYearId);
		Student_Details studentDetails = studentDetailsService.get(studentId);
		Boolean response = hostelBedAssignmentRepository
				.existsByAcYearAndStudentAndCancelledRemarksIsNullAndActiveTrue(academicYear, studentDetails);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}

	public List<Map<String, Object>> getStudentDetailDataBasedOnBlock(Integer acYearId, Integer hostelBlockId) {
		return hostelBedAssignmentRepository.getStudentDetailDataBasedOnBlock(acYearId, hostelBlockId);
	}
	
	
	
	public ResponseEntity<Object> updateHostelIdCard(@Valid List<HostelIdCardDto> hostelIdCardDtos, List<Integer> hostelBedAssignmentId, String jwtToken) {
	    try {
	        // Initialize a list to store failed updates (optional, in case you want to return specific errors later)
	        List<String> failedUpdates = new ArrayList<>();

	        // Iterate over each request in the list
	        for (HostelIdCardDto hostelIdCardDto : hostelIdCardDtos) {
	            // Retrieve the existing HostelBedAssignment entity
	            Optional<HostelBedAssignment> hostelBedAssignmentOpt = hostelBedAssignmentRepository
	                    .findById(hostelIdCardDto.getHostelBedAssignmentId());

	            // If the hostelBedAssignment is present
	            if (hostelBedAssignmentOpt.isPresent()) {
	                HostelBedAssignment hostelBedAssignment = hostelBedAssignmentOpt.get();

	                // Extract JWT details for tracking who modified the record
	                JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);

	                // Set the modified fields from the DTO to the existing entity
	                hostelBedAssignment.setFromDate(hostelIdCardDto.getFromDate());
	                hostelBedAssignment.setStudent(studentDetailsRepository.findById(hostelIdCardDto.getStudentId()).get());
	                hostelBedAssignment.setModifiedBy(jwtDetails.getUserId());
	                hostelBedAssignment.setModifiedUsername(jwtDetails.getUserName());
	                hostelBedAssignment.setIdCardAcStatus(1); // Mark ID card activation status

	                // Save the updated entity
	                hostelBedAssignmentRepository.save(hostelBedAssignment);
	            } else {
	                // Collect failed updates (optional)
	                failedUpdates.add("HostelBedAssignment with ID " + hostelIdCardDto.getHostelBedAssignmentId() + " not found.");
	            }
	        }

	        // Check if there were any failed updates
	        if (!failedUpdates.isEmpty()) {
	            return ResponseHandler.generateResponse(false, HttpStatus.NOT_FOUND, String.join(", ", failedUpdates));
	        }

	        // Return success response if all updates were successful
	        return ResponseHandler.generateResponse(true, HttpStatus.OK, "Updated Successfully");

	    } catch (Exception e) {
	        // Handle exception and return error response
	        return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	    }
	}
	
	
	public List<Map<String, Object>> getHostelDetailsForLedger(Integer studentId) {
		
		List<Map<String, Object>> hostelDataForLedger = new ArrayList<Map<String, Object>>();
		
		Student_Details studentDetails = studentDetailsRepository.getStudentByStudentIdAndActive(studentId);
//		List<HostelBedAssignment> hostelBedAssignment = hostelBedAssignmentRepository.findByStudentAndActiveTrueAndConfirmJoinTrue(studentDetails);
		List<HostelBedAssignment> hostelBedAssignment = hostelBedAssignmentRepository.getStudentAndActiveTrue(studentId);
		System.out.println(hostelBedAssignment.size());
		hostelBedAssignment.stream().forEach(hba -> {
			Map<String, Object> hostelData = new HashMap<String, Object>();

			Double paidAmount = studentPaymentHistoryRepository.hostelPaidAmount(hba.getAcYear().getAc_year_id(),hba.getHostelFeeTemplate().getHostel_fee_template_id(), hba.getHostelFeeTemplate().getFee_head_id(), studentId);
			Integer hostelWaiverAmount = hostelWaiverRepository.getDataForLedger(hba.getAcYear().getAc_year_id(), studentId);
//			Integer hostelWaiverPaidAmount = hostelWaiverRepository.getFeePaidDataForLedger(hba.getAcYear().getAc_year_id(), studentId);
			Double dueAmount = hba.getHostelFeeTemplate().getTotal_amount() - (hostelWaiverAmount + paidAmount);
			
			hostelData.put("bedName", hba.getHostelBed().getBedName());
			hostelData.put("blockName", hba.getHostelBlock().getBlockName());
			hostelData.put("blockShortName", hba.getHostelBlock().getBlockShortName());
			hostelData.put("hostelTemplateName", hba.getHostelFeeTemplate().getTemplate_name());
			hostelData.put("acYear", hba.getAcYear().getAc_year());
			hostelData.put("totalAmount", hba.getHostelFeeTemplate().getTotal_amount());
			hostelData.put("waiverAmount", hostelWaiverAmount);
//			hostelData.put("hostelWaiverPaidAmount", hostelWaiverPaidAmount);
			hostelData.put("paidAmount", paidAmount);
			hostelData.put("dueAmount", dueAmount);
			hostelData.put("fromDate", hba.getFromDate());
			hostelData.put("confirmJoin", hba.getConfirmJoin());
			hostelData.put("foodStatus", hba.getFoodStatus()==null?null:hba.getFoodStatus().toString());
			
			hostelDataForLedger.add(hostelData);
			
		});
		return hostelDataForLedger;
	}

	public ResponseEntity<Object> hostelbedDetailsByBlockAndAcademic(Integer academicYearId) {
		List<Map<String, Object>> response=new ArrayList<>();
		List<Map<String, Object>> details=hostelBedsRepository.getCountOfFloorsRoomsBedsByAcademicyear(academicYearId);
		details.stream().forEach(m -> {
			Map<String, Object> mapper=modelMapper.map(m,Map.class);
			int totalcount=m.get("countOfBeds") != null ? ((BigInteger)m.get("countOfBeds")).intValue():0;
			int totalOccupied=m.get("occupiedCountBeds") != null ? ((BigInteger)m.get("occupiedCountBeds")).intValue() :0;
			mapper.put("vacantBeds",totalcount-totalOccupied);
			response.add(mapper);
		});
		return ResponseHandler.generateResponse(true, HttpStatus.OK,details);
	}
}
