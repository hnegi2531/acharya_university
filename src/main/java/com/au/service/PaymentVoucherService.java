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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.au.dto.PaymentVoucherDto;
import com.au.model.CourseObjective;
import com.au.model.DraftPaymentVoucher;
import com.au.model.FinancialYear;
import com.au.model.PaymentVoucher;
import com.au.model.PaymentVoucherAttachment;
import com.au.repository.DraftPaymentVoucherRepository;
import com.au.repository.FinancialYearRepository;
import com.au.repository.JournalVoucherRepository;
import com.au.repository.PaymentVoucherAttachmentRepo;
import com.au.repository.PaymentVoucherRepository;
import com.au.response.ResponseHandler;


@Service
public class PaymentVoucherService {

	private Logger log = LoggerFactory.getLogger(PaymentVoucherService.class);

	public static final String value = "DraftPaymentVoucher";
	
	public static final String value2 = "VoucherPayment";
	
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
	private PaymentVoucherRepository pv_repo;
	
	@Autowired
	private PaymentVoucherAttachmentRepo pvaRepo;
	
	@Autowired
	private FinancialYearRepository financial_year_repo;
	
	@Autowired
	private DraftPaymentVoucherRepository draftRepo;
	
	@Autowired
	private JournalVoucherRepository jv_repo;
	
	Integer count = 0;

	public List<PaymentVoucher> savePaymentVoucher(@Valid List<PaymentVoucher> paymentVoucher) throws Exception {
		paymentVoucher.stream().forEach(pv -> {

//			Integer journalVoucherId = jv_repo.getJournalVoucherId(pv.getJv_joucher_number(),
//					pv.getJv_financial_year_id(), pv.getJv_school_id());
//			pv.setJournal_voucher_id(journalVoucherId);
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

			FinancialYear f_year = financial_year_repo.getFinancialYearData(date1); // financial year details of one
																					// particular id.
			PaymentVoucher id = pv_repo.getLatestDraftPaymentVoucher(f_year.getFinancial_year_id(),
					pv.getSchool_id()); // id will come
			Integer voucher_no = pv_repo.getLatestData(f_year.getFinancial_year_id(), pv.getSchool_id());
			try {
				f_year.equals(null);
			} catch (NullPointerException e) {

				throw new NullPointerException("Finanacial Year Is Not Created Yet !!!");
			}

			if (voucher_no == null) {
				pv.setVoucher_no(1);
				pv.setFinancial_year_id(f_year.getFinancial_year_id());
			} else if (f_year.getFinancial_year_id() == id.getFinancial_year_id()) {
				pv.setVoucher_no(voucher_no + 1);
				pv.setFinancial_year_id(f_year.getFinancial_year_id());
			} else {

				pv.setVoucher_no(1);
				pv.setFinancial_year_id(f_year.getFinancial_year_id());
			}
		});
		return pv_repo.saveAll(paymentVoucher);
	}
	
	public List<PaymentVoucher> listAll1() {
		return pv_repo.findAll11();
	}
	
	public PaymentVoucher get(Integer id) {
		return pv_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException(" Payment Voucher:" + id));
	}
	
	public List<PaymentVoucher> updatePaymentVoucher1(List<PaymentVoucher> cos) {
		return pv_repo.saveAll(cos);
	}
	
	public void delete(Integer id) {
		PaymentVoucher ed = pv_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Payment Voucher Not Found:" + id));
		pv_repo.updateExamDetail(id);
	}

	public void delete1(Integer id) {
		PaymentVoucher ed = pv_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Payment Voucher Not Found:" + id));
		pv_repo.updateExamDetail1(id);
	}
	
	
//	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
//		Page<Object> exam_details_filtered_response = pv_repo.getAllDataFilteredByKeyword(pageable, keyword);
//		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_filtered_response);
//	}
//
//	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
//		Page<Object> exam_details_response = pv_repo.getAllSortedData(pageable);
//		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_response);
//	}
	
	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable, LocalDate start, LocalDate end,
			LocalDate minDate) {
		Date startDate = (start != null) ? java.sql.Date.valueOf(start) : null;
		Date endDate = (end != null) ? java.sql.Date.valueOf(end) : null;
		Date minStartDate = (minDate != null) ? java.sql.Date.valueOf(minDate) : null;

		Page<Object> response = pv_repo.getAllSortedData(pageable, startDate, endDate, minStartDate);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, LocalDate start,
			LocalDate end, LocalDate minDate) {
		Date startDate = (start != null) ? java.sql.Date.valueOf(start) : null;
		Date endDate = (end != null) ? java.sql.Date.valueOf(end) : null;
		Date minStartDate = (minDate != null) ? java.sql.Date.valueOf(minDate) : null;

		Page<Object> response = pv_repo.getAllDataFilteredByKeyword(pageable, keyword, startDate, endDate,
				minStartDate);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public LocalDate getMinimumPaymentVoucherDate() {
	    Optional<Date> minCreatedDate = pv_repo.findMinCreatedDate();
	    return minCreatedDate.map((Date date) -> date.toInstant()
	                                                 .atZone(ZoneId.systemDefault())
	                                                 .toLocalDate())
	                         .orElse(LocalDate.of(2024, 1, 1)); // fallback
	}
	
	public List<Map<String, Object>> getPaymentVoucherData(Integer voucher_no,Integer school_id,Integer financial_year_id){
		return pv_repo.getPaymentVoucherData(voucher_no,school_id,financial_year_id);
	}
	
	public Map<String, Object> getPaymentVoucherDataById(Integer payment_voucher_id){
		return pv_repo.getPaymentVoucherDataById(payment_voucher_id);
	}
	
	public void uploadFile(MultipartFile multipartFile,Integer payment_voucher_no) {

		PaymentVoucherAttachment attachment1= new PaymentVoucherAttachment();
//		List<PaymentVoucher> list = pv_repo.findByVoucher_no(payment_voucher_no);
		;
		try {
			log.debug("Message For Payment voucher Attachment --------------");
			
			
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
		
			log.debug("Payment voucher Attachment", multipartFile);
			attachment1.setPaymentVoucherNo(payment_voucher_no);
			attachment1.setFileName(fileName);
			attachment1.setFilePath(LocalDate.now() + "/" + payment_voucher_no + "/" + fileName);
			attachment1.setFileType(endpointUrl + "/" + bucketName + "/" + value2 + "/" + LocalDate.now() + "/"
					+ payment_voucher_no + "/" + fileName);
			uploadFileTos3bucket(fileName, file, payment_voucher_no);
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
		pvaRepo.save(attachment1);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer payment_voucher_id) {
		final String uniqueFileName = value2 + "/" + LocalDate.now() + "/" + payment_voucher_id + "/" + fileName; // file.getName()
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));
	}


	public List<PaymentVoucherAttachment> getPaymentVoucherByVoucherNo(Integer voucherNo) {
		List<PaymentVoucherAttachment> findByPaymentVoucherNo = pvaRepo.findByPaymentVoucherNo(voucherNo);
		return findByPaymentVoucherNo;
	}

	public byte[] paymentVoucherFileDownload(final String keyName) throws NoSuchFileException {
		try {
			byte[] content;
			final S3Object s3Object = s3client.getObject(bucketName, value2 + "/" + keyName);
			final S3ObjectInputStream stream = s3Object.getObjectContent();
			content = IOUtils.toByteArray(stream);
			System.out.println(content);
			s3Object.close();
			return content;

		} catch (AmazonS3Exception e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
				throw new NoSuchFileException("File Not Found");
			}
			throw new AmazonClientException("", e);
		} catch (IOException | AmazonClientException ex) {
			throw new AmazonClientException("", ex);
		}
	}

	public DraftPaymentVoucher getVoucherFileByVoucherId(Integer voucherId) {
	DraftPaymentVoucher draftFile=	draftRepo.findByVoucherId(voucherId);
		return draftFile;
	}

	public List<Map<String, Object>> getVoucherHeadBankDetails(Integer bankId) {
		return pv_repo.getVoucherHeadBankDetails(bankId);
	}
}
