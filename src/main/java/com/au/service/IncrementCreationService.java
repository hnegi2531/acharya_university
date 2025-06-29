package com.au.service;

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
import com.au.dto.*;
import com.au.model.*;
import com.au.repository.*;
import com.au.response.ResponseHandler;
import com.au.util.CsvUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;




@Service
public class IncrementCreationService {

	@Autowired
	private IncrementCreationRepository incrementCreationRepository;

	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private DesignationRepository designationRepository;

	@Autowired
	private SalaryStructureRepository salaryStructureRepository;

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private TemporaryIncrementCreationRepository temporaryIncrementCreationRepository;
	
	@Autowired
	private TemporaryBatchConfigRepository temporaryBatchConfigRepository;

	private final ModelMapper modelMapper = new ModelMapper();
	
	private Logger logger = LoggerFactory.getLogger(IncrementCreationService.class);

	

	public static final String value = "IncrementCreationBucket";

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


	

	public ResponseEntity<Object> getEmployeeListForIncrementCreation(
			EmployeeListForIncrementCreationrequestDTO employeeListForIncrementCreationRequestDTO) throws ParseException {
		try {
			List<EmployeeListForIncrementCreationResponseDTO> employeeListForIncrementCreationResponseDTOs = incrementCreationRepository
					.getEmployeeListForIncrementCreation(employeeListForIncrementCreationRequestDTO);
			employeeListForIncrementCreationResponseDTOs.stream().forEach(e -> {
				if (StringUtils.isNotEmpty(e.getDateofJoining())) {
					String totalExperience = calculateExperience(e.getDateofJoining());
					e.setExperience(totalExperience); // Assuming Employee class has setExperience method
				}
			});
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
					employeeListForIncrementCreationResponseDTOs);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}
	
	public ResponseEntity<Object> getEmployeeOrrListForIncrementCreation(
			EmployeeListForIncrementCreationrequestDTO employeeListForIncrementCreationRequestDTO) throws ParseException {
		try {
			List<EmployeeListForIncrementCreationResponseDTO> employeeListForIncrementCreationResponseDTOs = incrementCreationRepository
					.getEmployeeOrrListForIncrementCreation(employeeListForIncrementCreationRequestDTO);
			employeeListForIncrementCreationResponseDTOs.stream().forEach(e -> {
				if (StringUtils.isNotEmpty(e.getDateofJoining())) {
					String totalExperience = calculateExperience(e.getDateofJoining());
					e.setExperience(totalExperience); // Assuming Employee class has setExperience method
				}
			});
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
					employeeListForIncrementCreationResponseDTOs);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> getEmployeeDetailForIncrementCreation(Integer empId) {

		try {
			EmployeeDetails employee = employeeDetailsRepository.findById(empId).get();
			EmployeeDetailForIncrementCreationDTO employeeDetailForIncrementCreationDTO = new EmployeeDetailForIncrementCreationDTO();
			employeeDetailForIncrementCreationDTO.setPreviousDepartmentId(
					ObjectUtils.isNotEmpty(employee) && ObjectUtils.isNotEmpty(employee.getDept_id())
							? employee.getDept_id()
							: null);
			employeeDetailForIncrementCreationDTO.setPreviousDesignationId(
					ObjectUtils.isNotEmpty(employee) && ObjectUtils.isNotEmpty(employee.getDesignation_id())
							? employee.getDesignation_id()
							: null);
			employeeDetailForIncrementCreationDTO.setEmpId(empId);
			employeeDetailForIncrementCreationDTO.setPreviousSalaryStructureId(
					ObjectUtils.isNotEmpty(employee) && ObjectUtils.isNotEmpty(employee.getSalary_structure_id())
							? employee.getSalary_structure_id()
							: null);
			employeeDetailForIncrementCreationDTO
					.setPreviousSplPay(ObjectUtils.isNotEmpty(employee) && ObjectUtils.isNotEmpty(employee.getSpl_pay())
							? employee.getSpl_pay()
							: null);
			employeeDetailForIncrementCreationDTO.setPreviousCtc(
					ObjectUtils.isNotEmpty(employee) && ObjectUtils.isNotEmpty(employee.getCtc())
							? employee.getCtc()
							: null);
			employeeDetailForIncrementCreationDTO.setPreviousGrosspay(
					ObjectUtils.isNotEmpty(employee) && ObjectUtils.isNotEmpty(employee.getGrosspay_ctc())
							? employee.getGrosspay_ctc()
							: null);
			employeeDetailForIncrementCreationDTO.setPreviousTa(
					ObjectUtils.isNotEmpty(employee) && ObjectUtils.isNotEmpty(employee.getTa())
							? employee.getTa()
							: null);
			employeeDetailForIncrementCreationDTO.setPreviousDepartment(
					ObjectUtils.isNotEmpty(employee) && ObjectUtils.isNotEmpty(employee.getDept_id())
							? getDepartmentName(employee.getDept_id())
							: null);
			employeeDetailForIncrementCreationDTO.setPreviousDesignation(
					ObjectUtils.isNotEmpty(employee) && ObjectUtils.isNotEmpty(employee.getDesignation_id())
							? getDesignationName(employee.getDesignation_id())
							: null);
			employeeDetailForIncrementCreationDTO.setPreviousBasic(
					ObjectUtils.isNotEmpty(employee) && ObjectUtils.isNotEmpty(employee.getSalary_structure_id())
							? employee.getAnnual_salary()
							: null);
			employeeDetailForIncrementCreationDTO.setPreviousSalaryStructure(
					ObjectUtils.isNotEmpty(employee) && ObjectUtils.isNotEmpty(employee.getSalary_structure_id())
							? getSalaryStructureName(employee.getSalary_structure_id())
							: null);
			employeeDetailForIncrementCreationDTO
					.setEmpCode(ObjectUtils.isNotEmpty(employee) && ObjectUtils.isNotEmpty(employee.getEmpcode())
							? employee.getEmpcode()
							: null);
		
			employeeDetailForIncrementCreationDTO.setPreviousSplPay(
					ObjectUtils.isNotEmpty(employee) && ObjectUtils.isNotEmpty(employee.getSpl_1())
							? employee.getSpl_1()
							: null);
			
			employeeDetailForIncrementCreationDTO
			.setDate_of_joining(ObjectUtils.isNotEmpty(employee) && ObjectUtils.isNotEmpty(employee.getDate_of_joining())
					? employee.getDate_of_joining()
					: null);
			
			String dateOfJoining = ObjectUtils.isNotEmpty(employee)
					&& ObjectUtils.isNotEmpty(employee.getDate_of_joining()) ? employee.getDate_of_joining() : null;

			   if (dateOfJoining != null && !dateOfJoining.isEmpty()) {
				   String totalExperience = calculateExperience(dateOfJoining);
				   employeeDetailForIncrementCreationDTO.setExperience(totalExperience); // Assuming Employee class has setExperience method

			}

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
					employeeDetailForIncrementCreationDTO);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}

	private String getSalaryStructureName(Integer salary_structure_id) {

		return salaryStructureRepository.getSalaryStructureName(salary_structure_id);
	}

	private String getDesignationName(Integer designation_id) {

		return designationRepository.getDesignation(designation_id);
	}

	private String getDepartmentName(Integer dept_id) {
		return departmentRepository.getDepartment(dept_id);

	}

	public ResponseEntity<Object> saveIncrementCreationDetails(IncrementCreationDTO incrementCreationDTO) {
		try {
			
			Integer count = incrementCreationRepository.count(incrementCreationDTO.getEmpId(), incrementCreationDTO.getMonth());
			
			if(count >= 1) {
				throw new RuntimeException("Data already created for the selected employee and month!!!");
			}
			
			IncrementCreation incrementCreation = new IncrementCreation();
			incrementCreation.setEmpId(incrementCreationDTO.getEmpId());
			incrementCreation.setPreviousDesignation(incrementCreationDTO.getPreviousDesignation());
			incrementCreation.setProposedDesignation(incrementCreationDTO.getProposedDesignation());
			incrementCreation.setPreviousDesignationId(incrementCreationDTO.getPreviousDesignationId());
			incrementCreation.setPreviousDepartment(incrementCreationDTO.getPreviousDepartment());
			incrementCreation.setProposedDepartment(incrementCreationDTO.getProposedDepartment());
			incrementCreation.setPreviousDepartmentId(incrementCreationDTO.getPreviousDepartmentId());
			incrementCreation.setProposedDepartmentId(incrementCreationDTO.getProposedDepartmentId());
			incrementCreation.setProposedDesignationId(incrementCreationDTO.getProposedDesignationId());
			incrementCreation.setCreatedBy(incrementCreationDTO.getCreatedBy());
			incrementCreation.setCtcDifference(incrementCreationDTO.getCtcDifference());
			incrementCreation.setGrossDifference(incrementCreationDTO.getGrossDifference());
			incrementCreation.setMonth(incrementCreationDTO.getMonth());
			incrementCreation.setPreviousBasic(incrementCreationDTO.getPreviousBasic());
			incrementCreation.setProposedBasic(incrementCreationDTO.getProposedBasic());
			incrementCreation.setPreviousCtc(incrementCreationDTO.getPreviousCtc());
			incrementCreation.setProposedCtc(incrementCreationDTO.getProposedCtc());
			incrementCreation.setPreviousTa(incrementCreationDTO.getPreviousTa());
			incrementCreation.setPreviousTa(incrementCreationDTO.getProposedTa());
			incrementCreation.setPreviousGrosspay(incrementCreationDTO.getPreviousGrosspay());
			incrementCreation.setProposedGrosspay(incrementCreationDTO.getProposedGrosspay());
			incrementCreation.setPreviousSalaryStructure(incrementCreationDTO.getPreviousSalaryStructure());
			incrementCreation.setProposedSalaryStructure(incrementCreationDTO.getProposedSalaryStructure());
			incrementCreation.setPreviousSalaryStructureId(incrementCreationDTO.getPreviousSalaryStructureId());
			incrementCreation.setProposedSalaryStructureId(incrementCreationDTO.getProposedSalaryStructureId());
			incrementCreation.setYear(incrementCreationDTO.getYear());
			incrementCreation.setPreviousSplPay(incrementCreationDTO.getPreviousSplPay());
			incrementCreation.setProposedSplPay(incrementCreationDTO.getProposedSplPay());
			incrementCreation.setPreviousMedicalReimburesment(incrementCreationDTO.getPreviousMedicalReimburesment());
			incrementCreation.setProposedMedicalReimburesment(incrementCreationDTO.getProposedMedicalReimburesment());
			incrementCreation.setRemarks(incrementCreationDTO.getRemarks());
			incrementCreation.setEmpCode(incrementCreationDTO.getEmpCode());
			incrementCreation.setActive(incrementCreationDTO.getActive());
			incrementCreationRepository.save(incrementCreation);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}

	}

	public ResponseEntity<Object> getDepartments() {
		try {
			List<DepartmentDTO> departmentDTOs = departmentRepository.getDepartments();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", departmentDTOs);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> getDesignations() {
		try {
			List<DesignationDTO> designationDTOs = designationRepository.getDesignations();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", designationDTOs);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> getsalaryStructures() {
		try {
			List<SalaryStructureDTO> designationDTOs = salaryStructureRepository.getsalaryStructures();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", designationDTOs);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> getIncrementCreationList(Integer school_id, Integer dept_id, Integer month) {
	
			List<IncrementCreationResponseDTO> incrementCreationResponseDTOs = incrementCreationRepository
					.getIncrementCreationList(school_id,dept_id,month);
			incrementCreationResponseDTOs.stream().forEach(e -> {
				 if (e.getDateofJoining() != null && !e.getDateofJoining().isEmpty()) {
					 String totalExperience = calculateExperience(e.getDateofJoining());
					e.setExperience(totalExperience); // Assuming Employee class has setExperience method
				 }
					
				
			});

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", incrementCreationResponseDTOs);
		
	}

	public ResponseEntity<Object> uploadIncrementCreationFile(MultipartFile file, Integer month, Integer year,
			String jwtToken) {
		try {
			if (!CsvUtil.hasCSVFormat(file)) {
				return ResponseHandler.generateResponse(true, HttpStatus.OK, "File is not in CSV Format", null);
			}
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			List<TemporaryIncrementCreation> incrementCreations = CsvUtil
					.getDataForIncrementCreation(file.getInputStream(), month, year, jwtDetails);
			String batchId=setBatchId();
			incrementCreations.stream().forEach(t->t.setBatchId(batchId));
			IncrementCreationEvent incrementCreationEvent = new IncrementCreationEvent(incrementCreations);
			applicationEventPublisher.publishEvent(incrementCreationEvent);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "File Started Uploading", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}

	}

	private String setBatchId() {
		Integer count = 1;
		String formattedNumber = "";
		TemporaryBatchConfig temporaryBatchConfig=temporaryBatchConfigRepository.getLatestBatch();
		if(ObjectUtils.isEmpty(temporaryBatchConfig)) {
			formattedNumber = String.format("%04d", count);
			temporaryBatchConfig=new TemporaryBatchConfig();
			temporaryBatchConfig.setBatchId("BATCH"+"-"+formattedNumber);
			temporaryBatchConfig.setBatchNo(count);
			temporaryBatchConfigRepository.save(temporaryBatchConfig);
		}else {
			count=temporaryBatchConfig.getBatchNo();
			count++;
			formattedNumber = String.format("%04d", count);
			temporaryBatchConfig.setBatchId("BATCH"+"-"+formattedNumber);
			temporaryBatchConfig.setBatchNo(count);
			temporaryBatchConfigRepository.save(temporaryBatchConfig);
		}
		
		return temporaryBatchConfig.getBatchId();
		
	}

	public ResponseEntity<Object> getTemporaryIncrementCreationList() {
		try {
			List<IncrementCreationResponseDTO> incrementCreationResponseDTOs = temporaryIncrementCreationRepository
					.getTemporaryIncrementCreationList();
			incrementCreationResponseDTOs.stream().forEach(e -> {
					if (StringUtils.isNotEmpty(e.getDateofJoining())) {
						
						String totalExperience = calculateExperience(e.getDateofJoining());
						e.setExperience(totalExperience); // Assuming Employee class has setExperience method
					}
				
			});

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", incrementCreationResponseDTOs);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}

	}

	public ResponseEntity<Object> saveIncrementCreationDetailsList(List<IncrementCreationDTO> incrementCreationList) {
		try {
			List<IncrementCreation> incrementCreations = new ArrayList<>();
			incrementCreationList.stream().forEach(incrementCreationDTO -> {
				IncrementCreation incrementCreation = new IncrementCreation();
				incrementCreation.setEmpId(incrementCreationDTO.getEmpId());
				incrementCreation.setPreviousDesignation(incrementCreationDTO.getPreviousDesignation());
				incrementCreation.setProposedDesignation(incrementCreationDTO.getProposedDesignation());
				incrementCreation.setPreviousDesignationId(incrementCreationDTO.getPreviousDesignationId());
				incrementCreation.setPreviousDepartment(incrementCreationDTO.getPreviousDepartment());
				incrementCreation.setProposedDepartment(incrementCreationDTO.getProposedDepartment());
				incrementCreation.setPreviousDepartmentId(incrementCreationDTO.getPreviousDepartmentId());
				incrementCreation.setProposedDepartmentId(incrementCreationDTO.getProposedDepartmentId());
				incrementCreation.setProposedDesignationId(incrementCreationDTO.getProposedDesignationId());
				incrementCreation.setCreatedBy(incrementCreationDTO.getCreatedBy());
				incrementCreation.setCtcDifference(incrementCreationDTO.getCtcDifference());
				incrementCreation.setGrossDifference(incrementCreation.getGrossDifference());
				incrementCreation.setMonth(incrementCreationDTO.getMonth());
				incrementCreation.setPreviousBasic(incrementCreationDTO.getPreviousBasic());
				incrementCreation.setProposedBasic(incrementCreationDTO.getProposedBasic());
				incrementCreation.setPreviousCtc(incrementCreationDTO.getPreviousCtc());
				incrementCreation.setProposedCtc(incrementCreationDTO.getProposedCtc());
				incrementCreation.setPreviousTa(incrementCreationDTO.getPreviousTa());
				incrementCreation.setPreviousTa(incrementCreationDTO.getProposedTa());
				incrementCreation.setPreviousGrosspay(incrementCreationDTO.getPreviousGrosspay());
				incrementCreation.setProposedGrosspay(incrementCreationDTO.getProposedGrosspay());
				incrementCreation.setPreviousSalaryStructure(incrementCreationDTO.getPreviousSalaryStructure());
				incrementCreation.setProposedSalaryStructure(incrementCreationDTO.getProposedSalaryStructure());
				incrementCreation.setPreviousSalaryStructureId(incrementCreationDTO.getPreviousSalaryStructureId());
				incrementCreation.setProposedSalaryStructureId(incrementCreationDTO.getProposedSalaryStructureId());
				incrementCreation.setYear(incrementCreationDTO.getYear());
				incrementCreation.setRemarks(incrementCreationDTO.getRemarks());
				incrementCreation.setPreviousSplPay(incrementCreationDTO.getPreviousSplPay());
				incrementCreation.setProposedSplPay(incrementCreationDTO.getProposedSplPay());
				incrementCreation
						.setPreviousMedicalReimburesment(incrementCreationDTO.getPreviousMedicalReimburesment());
				incrementCreation
						.setProposedMedicalReimburesment(incrementCreationDTO.getProposedMedicalReimburesment());
				incrementCreation.setEmpCode(incrementCreationDTO.getEmpCode());
				incrementCreations.add(incrementCreation);

			});
			incrementCreationRepository.saveAll(incrementCreations);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}

	}

	public ResponseEntity<Object> incrementIsFinalize(List<Long> listOfIds) {
		try {

//			incrementCreationDTO.stream().filter(t -> Boolean.TRUE.equals(t.getIsChecked())).forEach(t -> {
//				IncrementCreation incrementCreation = incrementCreationRepository
//						.findByIncrementCreationId(t.getIncrementCreationId());
//				incrementCreation.setIsFinalize(Boolean.TRUE);
//				incrementCreationRepository.save(incrementCreation);
//		});
			
			listOfIds.stream().forEach(id -> {
				IncrementCreation incrementCreation = incrementCreationRepository
				.findByIncrementCreationId(id);
		incrementCreation.setIsFinalize(Boolean.TRUE);
		incrementCreationRepository.save(incrementCreation);
			});
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> incrementIsApproved(List<Long> listOfIds) {
		try {

//			incrementCreationDTO.stream().forEach(t -> {
//				IncrementCreation incrementCreation = incrementCreationRepository
//						.findByIncrementCreationId(t.getIncrementCreationId());
//				incrementCreation.setIsApproved(Boolean.TRUE);
//				incrementCreationRepository.save(incrementCreation);
//			});
			
			listOfIds.stream().forEach(id -> {
				IncrementCreation incrementCreation = incrementCreationRepository
				.findByIncrementCreationId(id);
		incrementCreation.setIsApproved(Boolean.TRUE);
		incrementCreationRepository.save(incrementCreation);
			});
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> incrementIsRejected(List<Long> listOfIds) {
		try {

//			incrementCreationDTO.stream().forEach(t -> {
//				IncrementCreation incrementCreation = incrementCreationRepository
//						.findByIncrementCreationId(t.getIncrementCreationId());
//				incrementCreation.setIsRejected(Boolean.TRUE);
//				incrementCreationRepository.save(incrementCreation);
//			});
			
			listOfIds.stream().forEach(id -> {
				IncrementCreation incrementCreation = incrementCreationRepository
				.findByIncrementCreationId(id);
		incrementCreation.setIsRejected(Boolean.TRUE);
		incrementCreationRepository.save(incrementCreation);
			});
			
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> getIncrementFinalizeList(Integer school_id, Integer dept_id, Integer month) {
		try {
			List<IncrementCreationResponseDTO> incrementCreationResponseDTOs = incrementCreationRepository
					.getIncrementFinalizeList(school_id,dept_id,month);
			incrementCreationResponseDTOs.stream().forEach(e -> {
				
				if (StringUtils.isNotEmpty(e.getDateofJoining())) {
					String totalExperience = calculateExperience(e.getDateofJoining());
					e.setExperience(totalExperience); // Assuming Employee class has setExperience method

				}
			});

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", incrementCreationResponseDTOs);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}

	}
	
	public ResponseEntity<Object> getIncrementApprovedList(Integer school_id, Integer dept_id, Integer month) {
		try {
			List<IncrementCreationResponseDTO> incrementCreationResponseDTOs = incrementCreationRepository
					.getIncrementApprovedList(school_id,dept_id,month);
			incrementCreationResponseDTOs.stream().forEach(e -> {
				
				if (StringUtils.isNotEmpty(e.getDateofJoining())) {
					
					String totalExperience = calculateExperience(e.getDateofJoining());
					e.setExperience(totalExperience); // Assuming Employee class has setExperience method
				}
			});

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", incrementCreationResponseDTOs);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}

	}
	
	public String calculateExperience(String dateOfJoining) {
		String experience = null;
		try {
			 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy"); // Adjust format for dd-MM-yyyy
	           Date doj = simpleDateFormat.parse(dateOfJoining);
	
	           Calendar start = Calendar.getInstance();
	           start.setTime(doj);
	           Calendar end = Calendar.getInstance();
	           end.setTime(new Date()); // Current date
	           
	           
	           int years = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
	           int months = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
	           int days = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);
	
			// Adjusting the values in case of partial months or years
	           if (days < 0) {
	               months--;
	               Calendar lastDayOfPrevMonth = (Calendar) end.clone();
	               lastDayOfPrevMonth.add(Calendar.MONTH, -1);
	               lastDayOfPrevMonth.set(Calendar.DAY_OF_MONTH,
	                       lastDayOfPrevMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
	               days = lastDayOfPrevMonth.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH)
	                       + end.get(Calendar.DAY_OF_MONTH);
	           }
	           if (months < 0) {
	               years--;
	               months += 12; // Adding 12 months of the previous year
	           }
	
	           // Format experience as "YY Years, MM Months, DD Days"
	           experience = String.format("%02dY %02dM %02dD", years, months, days);
//			e.setExperience(experience); // Assuming Employee class has setExperience method
	
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return experience;
	}

	public ResponseEntity<Object> getIncrementByIncrementId(Long getIncrementByIncrementId) {
		try {
			IncrementCreation incrementCreation= incrementCreationRepository.findByIncrementCreationId(getIncrementByIncrementId);	
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", incrementCreation);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> uploadIncrementCreationFile(MultipartFile multipartFile, String request) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
	        UploadIncrementCreationDTO uploadDTO = objectMapper.readValue(request, UploadIncrementCreationDTO.class);

	  	List<Integer>	incrementIds =uploadDTO.getIncrementIds();
        List<Long> idsList=new ArrayList<>();
        incrementIds.stream().forEach(t->{
        	Long id=t.longValue();
        	idsList.add(id);
        });
		List<IncrementCreation> incrementCreations=incrementCreationRepository.getAllInIncrementIds(idsList);
			if (ObjectUtils.isNotEmpty(incrementCreations)) {
				File file = convertMultiPartToFile(multipartFile);
				String fileName = generateFileName(multipartFile);
				incrementCreations.stream().forEach(t -> {
					t.setAttachmentPath(LocalDate.now() +  "/" + fileName);
					t.setAttachmentPathType(endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now()  + "/" + fileName);

				});
				uploadFileToS3Bucket(fileName, file);
				logger.debug("Message For Attachment", file);
				incrementCreationRepository.saveAll(incrementCreations);
				file.delete();
			}
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
		} catch (AmazonServiceException ase) {

			logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		} catch (AmazonClientException ace) {
			logger.info("Caught an AmazonClientException: ");
			logger.info("Error Message: " + ace.getMessage());
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		} catch (IOException ioe) {
			logger.info("IOE Error Message: " + ioe.getMessage());
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}
	
	private void uploadFileToS3Bucket(String fileName, File file) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + fileName;
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));

	}

	public byte[] downloadIncrementCreationFile(String pathName) throws NoSuchFileException {
		try {
			byte[] content;
			final S3Object s3Object = s3client.getObject(bucketName, value + "/" + pathName);
			final S3ObjectInputStream stream = s3Object.getObjectContent();
			content = IOUtils.toByteArray(stream);
			System.out.println(content);
			s3Object.close();
			return content;

		} catch (AmazonS3Exception e) {
			if (e.getStatusCode() == org.apache.http.HttpStatus.SC_NOT_FOUND) {
				throw new NoSuchFileException("File Not Found");
			}
			throw new AmazonClientException("", e);
		} catch (IOException | AmazonClientException ex) {
			throw new AmazonClientException("", ex);
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




	public ResponseEntity<Object> updateIncrementCreationDetails(List<IncrementCreation> incrementCreation) {
		
		try {
			incrementCreation.stream().forEach(ic -> {
				ic.setModified_date(null);
			});
			List<IncrementCreation> incrementCreationData= incrementCreationRepository.saveAll(incrementCreation);	
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", incrementCreationData);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> getIncrementCreation(Long incrementCreationId) {
		try {
			IncrementCreation byId = incrementCreationRepository.getDataByid(incrementCreationId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", byId);
			
		}catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);

		}
	}


	public void deactivate(Long incrementCreationId) {
		IncrementCreation co = incrementCreationRepository.getDataByid1(incrementCreationId);
		incrementCreationRepository.deactivate(incrementCreationId);
	}

	public void activate(Long incrementCreationId) {
		IncrementCreation co = incrementCreationRepository.getDataByid1(incrementCreationId);
		incrementCreationRepository.activate(incrementCreationId);
	}

	public ResponseEntity<Object> getAllIncrementsOfMonthYear(Integer month, Integer year) {
		List<IncrementCreation> allIncrements = incrementCreationRepository.getAllIncrementsOfMonthYear(month, year);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", allIncrements);

	}
}

