package com.au.scheduler;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import com.au.model.SchedulerLock;
import com.au.repository.SchedulerLockRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.au.dto.TransactionSettlementDTO;
import com.au.model.RazorPaySecretKeys;
import com.au.model.Schools;
import com.au.repository.RazorPaySecretKeyRepository;
import com.au.repository.School_Repository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BankImportScheduler {

	@Autowired
	private School_Repository school_Repository;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private RazorPaySecretKeyRepository razorPaySecretKeyRepository;

	@Autowired
	SchedulerLockRepository schedulerLockRepository;

	@Scheduled(cron = "0 5 * * * ?", zone = "Asia/Kolkata")
	public void handleBankImportTransaction() {
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
					LocalDate currentDate = LocalDate.now();
					int year = currentDate.getYear();
					int month = currentDate.getMonthValue();
					int day = currentDate.getDayOfMonth();
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
	
	private static String createBasicAuthHeader(String keyId, String keySecret) {
		String auth = keyId + ":" + keySecret;
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
		return "Basic " + new String(encodedAuth);
	}
}
