package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.au.dto.PurchaseIndentDTO;
import com.au.model.FinancialYear;
import com.au.model.PurchaseIndent;
import com.au.model.StoreIndentRequest;
import com.au.repository.FinancialYearRepository;
import com.au.repository.PurchaseIndentRepository;
import com.au.response.ResponseHandler;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@Service
public class PurchaseIndentService {

	@Autowired
	private PurchaseIndentRepository purchaseIndentRepository;
	
	@Autowired
	private FinancialYearRepository financialYearRepository;

	public static final String value = "PurchaseIndentBucket";

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

	public ResponseEntity<Object> savePurchaseIndent(List<PurchaseIndentDTO> purchaseIndentDTO) {
		try {
			List<PurchaseIndent> purchaseIndents = new ArrayList<>();

			purchaseIndentDTO.stream().forEach(t -> {
				PurchaseIndent purchaseIndent = new PurchaseIndent();
				purchaseIndent.setApproxRate(t.getApproxRate());
				purchaseIndent.setBalanceQuantity(t.getBalanceQuantity());
				purchaseIndent.setBudgetAvaialable(t.getBudgetAvaialable());
				purchaseIndent.setEnvItemId(t.getEnvItemId());
				purchaseIndent.setItemDescription(t.getItemDescription());
				purchaseIndent.setQuantity(t.getQuantity());
				purchaseIndent.setLedgerId(t.getLedgerId());
				purchaseIndent.setVendorContactNo(t.getVendorContactNo());
				purchaseIndent.setVendorName(t.getVendorName());
				purchaseIndent.setActive(Boolean.TRUE);
				purchaseIndent.setCreatedBy(t.getCreatedBy());
				purchaseIndent.setRemark(t.getRemark());
				purchaseIndent.setTotalValue(t.getTotalValue());
				purchaseIndent.setAttachmantPathType(t.getAttachmantPathType());
				purchaseIndent.setApproverId(t.getApproverId());
				purchaseIndent.setStatus("Pending");
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDateTime now = LocalDateTime.now();
				String date = dtf.format(now);
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				LocalDate date1 = null;
				date1 = LocalDate.parse(date);
				
				FinancialYear f_year = financialYearRepository.getFinancialYearDataForIndent(date1); // financial year details
				
				String indent_ticket = null;
				String count_for_id = purchaseIndentRepository.getMaxPurchaseIndentRequestCount();
				String year1 = f_year.getFinancial_year().substring(2, 5); // 24-
				String year2 = f_year.getFinancial_year().substring(7, 9); // 25
				PurchaseIndent latest_indent = purchaseIndentRepository.getLatestPurchaseIndentRequest(); // based on latest indent_ticket , id
																						// will come

				if (count_for_id == null) {
					indent_ticket = String.format("%04d", 1);
				} else if (f_year.getFinancial_year_id() == latest_indent.getFinancialYearId()) {
					String abc = count_for_id.substring(0, 4);
					Integer count = Integer.valueOf(abc) + 1;
					indent_ticket = String.format("%04d", count);
				} else {
					indent_ticket = String.format("%04d", 1);

				}
				purchaseIndent.setIndentNo(indent_ticket + "/" + year1 + "" + year2);
				purchaseIndent.setFinancialYearId(f_year.getFinancial_year_id());
				purchaseIndents.add(purchaseIndent);

			});

			purchaseIndentRepository.saveAll(purchaseIndents);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}

	}

	public ResponseEntity<Object> uploadPurchaseIndentFile(MultipartFile multipartFile) {
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			uploadFileToS3Bucket(fileName, file);
			String attachmentPath = endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/"
					+ fileName;
			Map<String, Object> responseMap = new HashMap<String, Object>();
			responseMap.put("attachmentPath", attachmentPath);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", responseMap);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convertFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convertFile);
		fos.write(file.getBytes());
		fos.close();
		return convertFile;

	}

	@SuppressWarnings("null")
	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileToS3Bucket(String fileName, File file) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + fileName;
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));

	}

	public ResponseEntity<Object> getAllPurchaseIndent() {
		try {
			List<PurchaseIndentDTO> purchaseIndentDTOs = purchaseIndentRepository.getAllPurchaseIndent();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", purchaseIndentDTOs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> getAllPurchaseIndentById(Integer purchaseIndentId) {
		try {
			PurchaseIndentDTO purchaseIndentDTOs = purchaseIndentRepository.getAllPurchaseIndentById(purchaseIndentId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", purchaseIndentDTOs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> deactivatePurchaseIndentById(Integer purchaseIndentId) {
		try {
			PurchaseIndent purchaseIndent = purchaseIndentRepository.findById(purchaseIndentId).get();
			purchaseIndent.setActive(Boolean.FALSE);
			purchaseIndentRepository.save(purchaseIndent);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> saveAllPurchaseIndentForApproval(List<PurchaseIndentDTO> purchaseIndentDTO) {
		try {
			purchaseIndentDTO.stream().forEach(t -> {
				PurchaseIndent purchaseIndent = purchaseIndentRepository.findById(t.getPurchaseIndentId()).get();
				purchaseIndent.setStatus("Pending");
				purchaseIndent.setApproverId(t.getApproverId());
				purchaseIndentRepository.save(purchaseIndent);
			});

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> getAllPurchaseIndentForApproval(Integer approverId) {
		try {
			List<PurchaseIndentDTO> purchaseIndentDTOs = purchaseIndentRepository.getAllPurchaseIndentForApproval(approverId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", purchaseIndentDTOs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> approveOrRejectPurchaseIndent(List<PurchaseIndentDTO> purchaseIndentDTO) {
		try {
			purchaseIndentDTO.stream().forEach(t -> {
				PurchaseIndent purchaseIndent = purchaseIndentRepository.findById(t.getPurchaseIndentId()).get();
				purchaseIndent.setStatus(t.getStatus());
				purchaseIndent.setApproverId(t.getApproverId());
				purchaseIndent.setApprovedDate(new Date());
				purchaseIndentRepository.save(purchaseIndent);
			});

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> getAllPurchaseIndentbyStatus(String status) {
		try {
			List<PurchaseIndentDTO> purchaseIndentDTOs = purchaseIndentRepository.getAllPurchaseIndentbyStatus(status);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", purchaseIndentDTOs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> getAllPurchaseIndentByUserId(Integer userId) {
		try {
			List<PurchaseIndentDTO> purchaseIndentDTOs = purchaseIndentRepository.getAllPurchaseIndentByUserId(userId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", purchaseIndentDTOs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> getApproverId(Integer userId) {
		try {
			Map<String, Object> approverId = purchaseIndentRepository.getApproverId(userId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", approverId);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> getPurchaseIndentHistory() {
		try {
			List<PurchaseIndentDTO> purchaseIndentDTOs = purchaseIndentRepository.getPurchaseIndentHistory();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", purchaseIndentDTOs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}
	
	
	
	
}




