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
import com.au.model.DraftPaymentVoucher;
import com.au.model.FinancialYear;
import com.au.repository.DraftPaymentVoucherRepository;
import com.au.repository.FinancialYearRepository;
import com.au.response.ResponseHandler;


@Service
public class DraftPaymentVoucherService {
	
	private Logger logger = LoggerFactory.getLogger(DraftPaymentVoucherService.class);

	public static final String value = "DraftPaymentVoucher";

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
	private DraftPaymentVoucherRepository pvd_repo;
	
	@Autowired
	private FinancialYearRepository financial_year_repo;
	
	Integer count = 0;
	public List<DraftPaymentVoucher> saveDraftPaymentVoucher(List<DraftPaymentVoucher> dpv) throws Exception {
		
		dpv.stream().forEach(d -> {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			   LocalDateTime now = LocalDateTime.now();
			   String date  = dtf.format(now);
				DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
				Date date1=null;
				try {
					date1 = df.parse(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
	
		FinancialYear f_year = financial_year_repo.getFinancialYearData(date1); //financial year details of one particular id.
		DraftPaymentVoucher id = pvd_repo.getLatestDraftPaymentVoucher(f_year.getFinancial_year_id(), d.getSchool_id()); // id will come
		Integer voucher_no = pvd_repo.getLatestData(f_year.getFinancial_year_id(), d.getSchool_id());
			try{
				f_year.equals(null);
			}catch(NullPointerException e) {
				
				throw new NullPointerException("Finanacial Year Is Not Created Yet !!!");
			}
			
			if(voucher_no==null) {
				d.setVoucher_no(1);
				d.setFinancial_year_id(f_year.getFinancial_year_id());
			}else if(f_year.getFinancial_year_id() == id.getFinancial_year_id()) {   
				d.setVoucher_no(voucher_no+1);
				d.setFinancial_year_id(f_year.getFinancial_year_id());
			}else {
				
				d.setVoucher_no(1);
				d.setFinancial_year_id(f_year.getFinancial_year_id());
			}
		});
		return pvd_repo.saveAll(dpv);
	}
	
	public List<DraftPaymentVoucher> listAll1() {
		return pvd_repo.findAll11();
	}
	
	public DraftPaymentVoucher get(Integer id) {
		return pvd_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException(" Payment Voucher:" + id));
	}
	
	public List<DraftPaymentVoucher> updateDraftPaymentVoucher(@Valid List<DraftPaymentVoucher> pv) {
		return pvd_repo.saveAll(pv);
	}
	
	public void delete(Integer id) {
		DraftPaymentVoucher ed = pvd_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Payment Voucher Not Found:" + id));
		pvd_repo.updateExamDetail(id);
	}

	public void delete1(Integer id) {
		DraftPaymentVoucher ed = pvd_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Payment Voucher Not Found:" + id));
		pvd_repo.updateExamDetail1(id);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> exam_details_filtered_response = pvd_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> exam_details_response = pvd_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_response);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeywordStatus(Pageable pageable, Integer verifier_id,Integer verified_status, Object keyword) {
		Page<Object> exam_details_filtered_response = pvd_repo.getAllDataFilteredByKeywordStatus(pageable,verifier_id,verified_status, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedDataStatus(Pageable pageable,Integer verifier_id, Integer verified_status) {
		Page<Object> exam_details_response = pvd_repo.getAllSortedDataStatus(pageable,verifier_id,verified_status);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_response);
	}
	

	public List<HashMap<String, Object>> getDraftPaymentVoucherData(Integer voucher_no,Integer school_id,Integer financial_year_id){
		return pvd_repo.getDraftPaymentVoucherData(voucher_no,school_id,financial_year_id);
	}
	

	public void uploadFile(MultipartFile multipartFile,Integer voucher_no) {
		DraftPaymentVoucher s = new DraftPaymentVoucher();
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String t2 = s.setAttachment_path(LocalDate.now() + "/" + voucher_no + "/" + fileName);
			System.out.println("[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]] "+t2);
			uploadFileTos3bucket(fileName, file, voucher_no);
			file.delete();
			List<Integer> ids = pvd_repo.getDraftPaymentVoucher_ids(voucher_no);

			
			pvd_repo.updatePath(ids, t2);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer voucher_no) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + voucher_no + "/" + fileName; // file.getName()
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
	
	
	public void deleteDraftPaymentVoucher(Integer voucher_no) {
		
		pvd_repo.deleteDraftPaymentVoucher(voucher_no);
	}
	
	
	public void deactiveDraftPaymentVoucher(Integer voucher_no,Integer financial_year_id) {
		pvd_repo.deactiveDraftPaymentVoucher(voucher_no,financial_year_id);
		
	}

}
