package com.au.service;

import com.au.controller.TriggerRepository;
import com.au.dto.TransactionSettlementDTO;
import com.au.event.*;
import com.au.model.*;
import com.au.repository.BankImportTransactionRepository;
import com.au.repository.RazorPaySecretKeyRepository;
import com.au.repository.SchedulerLockRepository;
import com.au.repository.School_Repository;
import com.au.scheduler.BankImportScheduler;
import com.au.scheduler.GenerateFeeReceiptScheduler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TriggerService {

	public static final String INACTIVE = "INACTIVE";

	@Autowired
	private TriggerRepository triggerRepository;
  
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private RazorPaySecretKeyRepository razorPaySecretKeyRepository;

	@Autowired
	GenerateFeeReceiptEventPublisher generateFeeReceiptEventPublisher;

	@Autowired
	GenerateFeeReceiptScheduler generateFeeReceiptScheduler;

	@Autowired
	BankImportTransactionRepository bankImportTransactionRepository;

	@Autowired
	private BankImportScheduler bankImportScheduler;

	@Autowired
	private SchedulerLockRepository schedulerLockRepository;

	@Autowired
	private School_Repository school_Repository;

	public void biometricAttendanceTrigger(Integer month, Integer year, Integer empId) {
		BioMetricAttendanceEvent bioMetricAttendanceEvent = new BioMetricAttendanceEvent(month, year, empId);
		applicationEventPublisher.publishEvent(bioMetricAttendanceEvent);
	}

	public void attendanceSheetTrigger(Integer month, Integer year, Integer empId, Integer schoolId) {
		AttendanceSheetEvent attendanceSheetEvent = new AttendanceSheetEvent(month, year, empId, schoolId);
		applicationEventPublisher.publishEvent(attendanceSheetEvent);

	}

	public void employeeSalaryTrigger(Integer month, Integer year, Integer empId) {
		PayDaysSalaryEvent payDaysSalaryEvent = new PayDaysSalaryEvent(month, year, empId);
		applicationEventPublisher.publishEvent(payDaysSalaryEvent);
	}

	public void createEmployeeLeaveKitty(Integer empId) {
		LeaveKittyEvent leaveKittyEvent = new LeaveKittyEvent("leaveKitty", empId);
		applicationEventPublisher.publishEvent(leaveKittyEvent);

	}

	public void bioTransTrigger(String date, Integer empId) {
		BioTransactionEvent bioTransactionEvent = new BioTransactionEvent(date, empId);
		applicationEventPublisher.publishEvent(bioTransactionEvent);

	}

	public void bioTransMonthWiseTrigger(Integer month, Integer year, Integer empId) {
		BioMetricTransactionEventMonthWise bioMetricTransactionEventMonthWise = new BioMetricTransactionEventMonthWise(
				month, year, empId);
		applicationEventPublisher.publishEvent(bioMetricTransactionEventMonthWise);
	}

	public void calculateMasterSalaryTrigger(Integer month, Integer year, Integer empId) {
		MasterSalaryEvent payDaysSalaryEvent = new MasterSalaryEvent(month, year, empId);
		applicationEventPublisher.publishEvent(payDaysSalaryEvent);

	}

	public void leaveApplyTrigger(String date) {
		LeaveApplyEventDTO leaveApplyEventDTO = new LeaveApplyEventDTO(date);
		applicationEventPublisher.publishEvent(leaveApplyEventDTO);

	}

	public void studentReportingTrigger(String auid) {
		StudentReportingEvent studentReportingEvent = new StudentReportingEvent();
		if (ObjectUtils.isNotEmpty(auid)) {
			studentReportingEvent.setAuidOrAuidWithoutIncrement(auid);
		}
		studentReportingEvent.setEventName("studentReporting");
		applicationEventPublisher.publishEvent(studentReportingEvent);
	}

	public void bankImportSettlement(TransactionSettlementDTO transactionSettlementDTO) {
		applicationEventPublisher.publishEvent(transactionSettlementDTO);

	}

	public void bankImportSettlementTrigger(Integer year, Integer month, Integer day, Integer schoolId) {
		try {
			String url = null;
			RazorPaySecretKeys razorPaySecretKeys = razorPaySecretKeyRepository
					.getRazorPaySecretKeysBySchoolId(schoolId);
			if (ObjectUtils.isEmpty(day) || day == 0) {
				url = "https://api.razorpay.com/v1/settlements/recon/combined?year=" + year + "&month=" + month;
			}else {
				url = "https://api.razorpay.com/v1/settlements/recon/combined?year=" + year + "&month=" + month+"&day="+day;		
			}
			String username = razorPaySecretKeys.getRazorPayKey();
			String password = razorPaySecretKeys.getSecretKey();
			RestTemplate restTemplate = new RestTemplate();

			// Prepare headers with Basic Authentication
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", createBasicAuthHeader(username, password));

			// Create HttpEntity with headers
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ObjectMapper objectMapper = new ObjectMapper();
			// Make GET request
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

			TransactionSettlementDTO transactionSettlementDTO = objectMapper.readValue(response.getBody(),
					TransactionSettlementDTO.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				applicationEventPublisher.publishEvent(transactionSettlementDTO);
			} else {
				throw new RuntimeException("HTTP error: " + response.getStatusCode());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	  private static String createBasicAuthHeader(String keyId, String keySecret) {
	        String auth = keyId + ":" + keySecret;
	        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
	        return "Basic " + new String(encodedAuth);
	    }

	public String getStatusOfTheTrigger(String triggerName) {
		Optional<Triggers> optionalTrigger = triggerRepository.findByName(triggerName);
		if (optionalTrigger.isPresent()){
			return optionalTrigger.get().getStatus();
		}
		Triggers trigger = new Triggers();
		trigger.setName(triggerName);
		trigger.setStatus(INACTIVE);
		triggerRepository.save(trigger);
		return INACTIVE;
	}
	
	public void leaveKittyForYearTrigger(Integer empId) {
		LeaveKittyForYearEvent leaveKittyForYearEvent = new LeaveKittyForYearEvent("LeaveKittyForYearEvent", empId);
		applicationEventPublisher.publishEvent(leaveKittyForYearEvent);

	}

	public void registrationFeeReceiptGenerationForPendingRequest() {
		RegistrationFeeTransaction registrationFeeTransaction = new RegistrationFeeTransaction();
		generateFeeReceiptEventPublisher.registrationFee(registrationFeeTransaction);
	}

	public void generateFeeReceiptTrigger(String orderId) {

		BankImportTransaction bankImportTransaction = bankImportTransactionRepository.getByOrderId(orderId);
		//generateFeeReceiptScheduler.generateFeeReceiptScheduler();
		GenerateFeeReceiptEvent generateFeeReceiptEvent = new GenerateFeeReceiptEvent();
		ArrayList<BankImportTransaction> bankImportTransactionList = new ArrayList<>();
		bankImportTransactionList.add(bankImportTransaction);
		generateFeeReceiptEvent.setBankImportTransactionList(bankImportTransactionList);
		generateFeeReceiptEventPublisher.generateFeeReceiptEvent(generateFeeReceiptEvent);
	}

	public void generateAllFeeReceipts() {
		generateFeeReceiptScheduler.generateFeeReceiptScheduler();
	}

	public void bankImportSettlementTriggerForAll(Integer year,Integer month,Integer day) {

		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
		String formattedDateTime = now.format(formatter);
		String uniqueJobName = "bankImportJob_" + formattedDateTime;

		SchedulerLock newLock = new SchedulerLock();
		newLock.setJobName(uniqueJobName); // Ensures a unique name
		newLock.setLockedBy("Instance-" + UUID.randomUUID());
		newLock.setLockedAt(LocalDateTime.now());
		newLock.setStatus("LOCKED");

		try {

			schedulerLockRepository.save(newLock);
			System.out.println("Processing bank import for " + uniqueJobName);

			List<Schools> schools = school_Repository.findAll();

			for (Schools s : schools) {
				RazorPaySecretKeys razorPaySecretKeys = razorPaySecretKeyRepository
						.getRazorPaySecretKeysBySchoolId(s.getSchool_id());

				if (ObjectUtils.isNotEmpty(razorPaySecretKeys)) {

					String url = "https://api.razorpay.com/v1/settlements/recon/combined?year=" + year + "&month="
							+ month + "&day=" + day;

					String username = razorPaySecretKeys.getRazorPayKey();
					String password = razorPaySecretKeys.getSecretKey();
					RestTemplate restTemplate = new RestTemplate();

					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					headers.set("Authorization", createBasicAuthHeader(username, password));

					HttpEntity<String> entity = new HttpEntity<>(headers);
					ObjectMapper objectMapper = new ObjectMapper();
					ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

					TransactionSettlementDTO transactionSettlementDTO = objectMapper.readValue(response.getBody(),
							TransactionSettlementDTO.class);

					if (response.getStatusCode() == HttpStatus.OK) {
						applicationEventPublisher.publishEvent(transactionSettlementDTO);
					} else {
						throw new RuntimeException("HTTP error: " + response.getStatusCode());
					}

				}

			}
			newLock.setStatus("UNLOCKED");
			schedulerLockRepository.save(newLock);

		}

		catch (Exception e) {
			if (e instanceof DataIntegrityViolationException || e instanceof ConstraintViolationException) {
				System.out.println("Scheduler is already locked by another instance!"+e.getMessage());
			} else {
				System.out.println("Unexpected error in bank import scheduler: " + e.getMessage());
				newLock.setStatus("UNLOCKED");
				schedulerLockRepository.save(newLock);
			}
		}
	}



}
