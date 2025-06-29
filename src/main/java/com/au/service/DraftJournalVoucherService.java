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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.au.exception.ResourceNotFoundException;
import com.au.model.DraftJournalVoucher;
import com.au.model.DraftPaymentVoucher;
import com.au.model.FinancialYear;
import com.au.repository.DraftJournalVoucherRepository;
import com.au.repository.FinancialYearRepository;
import com.au.response.ResponseHandler;

@Service
public class DraftJournalVoucherService {

	private Logger logger = LoggerFactory.getLogger(DraftJournalVoucherService.class);
	public static final String value = "DraftJournalVoucher";

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
	private DraftJournalVoucherRepository djv_repo;

	@Autowired
	private FinancialYearRepository financial_year_repo;

	Integer count = 0;

	public List<DraftJournalVoucher> saveDraftJournalVoucher(@Valid List<DraftJournalVoucher> djv) throws Exception {

		djv.stream().forEach(d -> {
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
			DraftJournalVoucher id = djv_repo.getLatestDraftPaymentVoucher(f_year.getFinancial_year_id(),
					d.getSchool_id()); // id will come
			Integer journal_voucher_number = djv_repo.getLatestData(f_year.getFinancial_year_id(), d.getSchool_id());
			try {
				f_year.equals(null);
			} catch (NullPointerException e) {

				throw new NullPointerException("Finanacial Year Is Not Created Yet !!!");
			}

			if (journal_voucher_number == null) {
				d.setJournal_voucher_number(1);
				d.setFinancial_year_id(f_year.getFinancial_year_id());
			} else if (f_year.getFinancial_year_id() == id.getFinancial_year_id()) {
				d.setJournal_voucher_number(journal_voucher_number + 1);
				d.setFinancial_year_id(f_year.getFinancial_year_id());
			} else {

				d.setJournal_voucher_number(1);
				d.setFinancial_year_id(f_year.getFinancial_year_id());
			}
		});
		return djv_repo.saveAll(djv);
	}

	public List<DraftJournalVoucher> listAll1() {
		return djv_repo.findAll11();
	}

	public DraftJournalVoucher get(Integer id) {
		return djv_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException(" Draft Journal Voucher:" + id));
	}

	public List<DraftJournalVoucher> updateDraftJournalVoucher(@Valid List<DraftJournalVoucher> djv) {
		return djv_repo.saveAll(djv);
	}

	public void delete(Integer id) {
		DraftJournalVoucher ed = djv_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Draft Payment Voucher Not Found:" + id));
		djv_repo.updateDraftJournalVoucher(id);
	}

	public void delete1(Integer id) {
		DraftJournalVoucher ed = djv_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Draft Payment Voucher Not Found:" + id));
		djv_repo.updateDraftJournalVoucher1(id);
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> DraftPaymentVoucher_filtered_response = djv_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, DraftPaymentVoucher_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> DraftPaymentVoucher_response = djv_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, DraftPaymentVoucher_response);
	}

	public List<Map<String, Object>> getDraftJournalVoucherData(Integer journal_voucher_number, Integer school_id,
			Integer financial_year_id) {
		return djv_repo.getDraftJournalVoucherData(journal_voucher_number, school_id, financial_year_id);
	}

	public void uploadFile(MultipartFile multipartFile, Integer journal_voucher_number) {
		DraftPaymentVoucher s = new DraftPaymentVoucher();
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String t2 = s.setAttachment_path(LocalDate.now() + "/" + journal_voucher_number + "/" + fileName);
			System.out.println("[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]] " + t2);
			uploadFileTos3bucket(fileName, file, journal_voucher_number);
			file.delete();
			List<Integer> ids = djv_repo.getDraftJournal_ids(journal_voucher_number);

			djv_repo.updatePath(ids, t2);
		} catch (AmazonServiceException ase) {

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

	private void uploadFileTos3bucket(String fileName, File file, Integer journal_voucher_number) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + journal_voucher_number + "/" + fileName; // file.getName()
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

	public void deleteDraftJournalVoucher(Integer journal_voucher_number) {
		djv_repo.deleteDraftJournalVoucher(journal_voucher_number);
	}

	public List<HashMap<String, Object>> getdraftJournalVoucherAttachmentByVoucherNo(Integer journal_voucher_number,
			Integer financial_year_id) {
		List<HashMap<String, Object>> findByPaymentVoucherNo = djv_repo.findByPaymentVoucherNo(journal_voucher_number,
				financial_year_id);
		return findByPaymentVoucherNo;
	}

}
