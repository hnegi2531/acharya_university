package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
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
import com.au.dto.FeeHeadAmountRestrictionResponse;
import com.au.dto.JwtDetails;
import com.au.model.ClickPayment;
import com.au.model.FeeHeadAmountRestriction;
import com.au.model.PaymeTransactions;
import com.au.model.Schools;
import com.au.model.VoucherHead;
import com.au.repository.ClickPaymentRepository;
import com.au.repository.FeeHeadAmountRestrictionRepository;
import com.au.repository.PaymePaymentGatewayRepository;
import com.au.repository.School_Repository;
import com.au.repository.UserAuthenticationRepository;
import com.au.repository.VoucherHeadNewRepository;
import com.au.repository.VoucherHeadRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class FeeHeadAmountRestrictionService {
	
	private Logger logger = LoggerFactory.getLogger(FeeHeadAmountRestrictionService.class);

	private final ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private JwtTokenService jwtService;

	@Autowired
	private FeeHeadAmountRestrictionRepository feeHeadAmountRestrictionRepository;

	@Autowired
	private School_Repository schoolRepository;

	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;

	@Autowired
	private VoucherHeadRepository voucherHeadRepository;

	@Autowired
	private VoucherHeadNewRepository voucherHeadNewRepository;
	
	@Autowired
	private ClickPaymentRepository clickPaymentRepository;
	
	@Autowired
	private PaymePaymentGatewayRepository paymePaymentGatewayRepository;
	
	public static final String value = "FeeHeadAmountRestrictionBucket";
	
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

	public List<FeeHeadAmountRestrictionResponse> saveFeeHeadAmountRestriction(
			List<FeeHeadAmountRestriction> feeHeadAmountRestriction, String jwtToken)
					throws JsonParseException, JsonMappingException, IOException {

		List<FeeHeadAmountRestrictionResponse> feeHeadAmountRestrictionResponseList=new ArrayList<>();
		JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);

		feeHeadAmountRestriction.parallelStream().forEachOrdered(fhar -> {
			Schools school=schoolRepository.findById(fhar.getSchoolId()).orElseThrow(() -> new ResourceNotFoundException("School Not Found :"+fhar.getSchoolId()));
			VoucherHead voucherHead=voucherHeadRepository.findById(fhar.getVoucherHeadId()).orElseThrow(() -> new ResourceNotFoundException("Voucher Head Not Found :"+fhar.getSchoolId()));
//			UserAuthentication userDetails=userAuthenticationRepository.findById(fhar.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User Not Found :"+fhar.getSchoolId()));

			fhar.setCreatedBy(jwtDetails.getUserId());
			fhar.setCreatedUsername(jwtDetails.getUserName());
			feeHeadAmountRestrictionRepository.save(fhar);

			FeeHeadAmountRestrictionResponse feeHeadAmountRestrictionResponse=modelMapper.map(feeHeadAmountRestriction,FeeHeadAmountRestrictionResponse.class);
			feeHeadAmountRestrictionResponse.setId(fhar.getFeeHeadAmountRestrictionId());
			feeHeadAmountRestrictionResponse.setSchoolId(school.getSchool_id());
			feeHeadAmountRestrictionResponse.setVoucherHeadId(voucherHead.getVoucher_head_id());
//			feeHeadAmountRestrictionResponse.setUserId(userDetails.getId());
			feeHeadAmountRestrictionResponseList.add(feeHeadAmountRestrictionResponse);
		});
		return feeHeadAmountRestrictionResponseList;

	}


	public List<FeeHeadAmountRestriction> getActiveFeeHeadAmountRestriction() {
		return feeHeadAmountRestrictionRepository.getActiveFeeHeadAmountRestriction();

	}
	
	
	public ResponseEntity<Object> fetchAllFeeHeadAmountRestrictionDetailsByKeywordAndUserId(Pageable pageable, Object keyword,Integer userId) {
		String userIdCase1 = userId + ",%";
		String userIdCase2 = "%," + userId + ",%";
		String userIdCase3 = "%," + userId;
		String userIdCase4 = userId.toString();
		Page<Object> response1 = feeHeadAmountRestrictionRepository.fetchAllFeeHeadAmountRestrictionDetailsByKeywordAndUserId(pageable, keyword , userIdCase1, userIdCase2,userIdCase3,userIdCase4);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllFeeHeadAmountRestrictionDetailsByKeyword(Pageable pageable, Object keyword) {

		Page<Object> response1 = feeHeadAmountRestrictionRepository.fetchAllFeeHeadAmountRestrictionDetailsByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllSortedFeeHeadAmountRestrictionDetailsAndUserId(Pageable pageable,Integer userId) {
		String userIdCase1 = userId + ",%";
		String userIdCase2 = "%," + userId + ",%";
		String userIdCase3 = "%," + userId;
		String userIdCase4 = userId.toString();
		Page<Object> response = feeHeadAmountRestrictionRepository.fetchAllSortedFeeHeadAmountRestrictionDetailsAndUserId(pageable,userIdCase1, userIdCase2,userIdCase3,userIdCase4);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public ResponseEntity<Object> fetchAllSortedFeeHeadAmountRestrictionDetails(Pageable pageable) {

		Page<Object> response = feeHeadAmountRestrictionRepository.fetchAllSortedFeeHeadAmountRestrictionDetails(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public FeeHeadAmountRestriction FeeHeadAmountRestrictionDetailByfeeHeadAmountRestrictionId(Integer feeHeadAmountRestrictionId) {
		FeeHeadAmountRestriction  feeHeadAmountRestriction = feeHeadAmountRestrictionRepository.findById(feeHeadAmountRestrictionId).orElseThrow(() -> new ResourceNotFoundException("Fee Head Amount Restriction  Not Found:" + feeHeadAmountRestrictionId));
		return feeHeadAmountRestriction;
		
	}
	
	public void updateFeeHeadAmountRestrictionDetailByfeeHeadAmountRestrictionId(FeeHeadAmountRestriction updateRequest, @PathVariable Integer feeHeadAmountRestrictionId,@RequestHeader("Authorization")  String jwtToken) throws Exception {
		FeeHeadAmountRestriction feeHeadAmountRestriction= feeHeadAmountRestrictionRepository.findById(feeHeadAmountRestrictionId).orElseThrow(() -> new ResourceNotFoundException("Fee Head Amount Restriction  Not Found:" + feeHeadAmountRestrictionId));
		JwtDetails jwtDetails = jwtService.callJwtToken(jwtToken);
		updateRequest.setModifiedBy(jwtDetails.getUserId());
		updateRequest.setModifiedUsername(jwtDetails.getUserName());
		feeHeadAmountRestrictionRepository.save(updateRequest);

	}
	
	public void deactivateFeeHeadAmountRestrictionDetail(Integer feeHeadAmountRestrictionId) {
		feeHeadAmountRestrictionRepository.findById(feeHeadAmountRestrictionId)
				.orElseThrow(() -> new ResourceNotFoundException("Fee Head Amount Restriction  Not Found:" + feeHeadAmountRestrictionId));
		feeHeadAmountRestrictionRepository.deactivateFeeHeadAmountRestrictionDetail(feeHeadAmountRestrictionId);
	}

	public void activateFeeHeadAmountRestrictionDetail(Integer feeHeadAmountRestrictionId) {
		feeHeadAmountRestrictionRepository.findById(feeHeadAmountRestrictionId)
				.orElseThrow(() -> new ResourceNotFoundException("Fee Head Amount Restriction  Not Found:" + feeHeadAmountRestrictionId));
		feeHeadAmountRestrictionRepository.activateFeeHeadAmountRestrictionDetail(feeHeadAmountRestrictionId);

	}
	
	public List<FeeHeadAmountRestriction> uploadFile(MultipartFile multipartFile, List<Integer> feeHeadAmountRestrictionIds) throws IOException{
		List<FeeHeadAmountRestriction> feeHeadAmountRestrictionList=new ArrayList<>();
		feeHeadAmountRestrictionIds.parallelStream().forEachOrdered(id -> {
			FeeHeadAmountRestriction feeHeadAmountRestrictionAttachment = feeHeadAmountRestrictionRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Fee Head Amount Restriction  Not Found:" + id));
			try
			{
				logger.debug("Message For FeeHead Amount Restriction Attachment--------------");
				File file = convertMultiPartToFile(multipartFile);
				String fileName = generateFileName(multipartFile);
				logger.debug("Message For FeeHead Amount Restriction Attachment: {}", file);
				feeHeadAmountRestrictionAttachment.setAttachmentFileName(fileName);
				feeHeadAmountRestrictionAttachment.setAttachmentPath(LocalDate.now() + "/" + id + "/" + fileName);
				feeHeadAmountRestrictionAttachment.setAttachmentType(
						endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + id + "/" + fileName);
				uploadFileToS3Bucket(fileName, file, id);
				logger.debug("Message For Attachment : {} ", file);
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
			feeHeadAmountRestrictionRepository.save(feeHeadAmountRestrictionAttachment);
			feeHeadAmountRestrictionList.add(feeHeadAmountRestrictionAttachment);
		});
		return feeHeadAmountRestrictionList;
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
	
	private void uploadFileToS3Bucket(String fileName, File file, Integer feeHeadAmountRestrictionId) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + feeHeadAmountRestrictionId + "/" + fileName; 
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
	
	public List<HashMap<String,Object>> feeHeadAmountRestrictionDetailsForPayment(Integer feeHeadAmountRestrictionId) {
		return feeHeadAmountRestrictionRepository.feeHeadAmountRestrictionDetailsForPayment(feeHeadAmountRestrictionId);
		
	}
	
	public Map<String,Object> feeHeadAmountRestrictionPaidDetails(Integer feeHeadAmountRestrictionId) {
		Map<String,Object> finalResponse=new HashMap<>();
		List<HashMap<String,Object>> feeHeadAmountRestrictionDetail=feeHeadAmountRestrictionRepository.feeHeadAmountRestrictionDetailsForPayment(feeHeadAmountRestrictionId);
		List<ClickPayment> clickPaymentDetails=clickPaymentRepository.findClickPaymentByFeeHeadAmountRestrictionId(feeHeadAmountRestrictionId);
		List<PaymeTransactions> paymeTransactionDetails=paymePaymentGatewayRepository.findPaymeTransactionsByFeeHeadAmountRestrictionId(feeHeadAmountRestrictionId);
		
		finalResponse.put("feeHeadAmountRestrictionDetail", feeHeadAmountRestrictionDetail);
		finalResponse.put("clickPaymentDetails", clickPaymentDetails);
		finalResponse.put("paymeTransactionDetails", paymeTransactionDetails);
		return finalResponse;
	}
}

