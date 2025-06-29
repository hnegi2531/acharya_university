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
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
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
import com.au.model.IncentiveApprover;
import com.au.model.Patent;
import com.au.repository.IncentiveApproverRepository;
import com.au.repository.PatentRepository;
import com.au.response.ResponseHandler;

@Service
public class PatentService {

	private Logger log = LoggerFactory.getLogger(PatentService.class);

	public static final String value = "Patent";

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
	private PatentRepository patent_repository;

	@Autowired
	private IncentiveApproverRepository ApproverRepository;

	public List<Patent> savePatent(@Valid List<Patent> patent) {
		patent.stream().forEach(pate -> {
			if (patent_repository.getCountOfPatentTitle(pate.getPatent_title(),pate.getEmp_id()) >= 1) {
				throw new RuntimeException("PatentTitle already created " + pate.getPatent_title() + "!!!");
			}

		});

		return patent_repository.saveAll(patent);
	}

	public List<Patent> getAllActivePatent() {
		return patent_repository.getAllActivePatent();
	}

	public Patent get(Integer id) {
		return patent_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Patent Not Found:" + id));
	}

	public Patent updatePatent(Patent patent) {
		return patent_repository.save(patent);
	}

	public void deactivate(Integer id) {
		Patent ademail = patent_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Patent Not Found:" + id));
		patent_repository.deactivate(id);
	}

	public void activate(Integer id) {
		Patent ademail = patent_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Patent Not Found:" + id));
		patent_repository.activate(id);
	}

//	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer percentageFilter) {
//		Page<Map<String, Object>> oc_filtered_response = patent_repository.getAllDataFilteredByKeyword(pageable,
//				keyword,percentageFilter);
//		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, oc_filtered_response);
//	}
//
//	public ResponseEntity<Object> getAllSortedData(Pageable pageable, Integer percentageFilter) {
//		Page<Map<String, Object>> oc_sorted_response = patent_repository.getAllSortedData(pageable,percentageFilter);
//		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, oc_sorted_response);
//	}

	public void uploadFile(MultipartFile multipartFile, Integer patent_id) {

		Patent entity = patent_repository.findById(patent_id)
				.orElseThrow(() -> new ResourceNotFoundException("Patent not found"));
		try {
			log.debug("Message For Patent Attachment --------------");

			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);

			log.debug("Patent Attachment", multipartFile);

			entity.setAttachment_name(fileName);
			entity.setAttachment_path(LocalDate.now() + "/" + patent_id + "/" + fileName);
			uploadFileTos3bucket(fileName, file, patent_id);
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
		patent_repository.save(entity);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer patent_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + patent_id + "/" + fileName; // file.getName()
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

	public List<Map<String, Object>> patentDetailsBasedOnEmpId(List<Integer> emp_id, Integer percentageFilter) {
		List<Map<String, Object>> patentDetails = patent_repository.patentDetailsBasedOnEmpId(emp_id,percentageFilter);

		List<Map<String, Object>> modifiedPublicationDetails = new ArrayList<>();

		for (Map<String, Object> detail : patentDetails) {

		 Integer incentiveApproverId = (Integer) detail.get("incentive_approver_id");
	        System.out.println("Incentive Approver ID: " + incentiveApproverId);
	        
	        IncentiveApprover allActiveIncentiveApprover = ApproverRepository.getAllActiveIncentiveApprover(incentiveApproverId);
	        
	        if (allActiveIncentiveApprover == null || ObjectUtils.isEmpty(allActiveIncentiveApprover)) {
	            modifiedPublicationDetails.add(detail); 
	            continue; 
	        }

	        Map<String, Object> modifiedDetail = new HashMap<>(detail); 
	        Boolean hoiStatus = (Boolean) detail.get("hoi_status");
	        Boolean hodStatus = (Boolean) detail.get("hod_status");
	        Boolean asstDirStatus = (Boolean) detail.get("asst_dir_status");
	        Boolean hrStatus = (Boolean) detail.get("hr_status");
	        Boolean financeStatus = (Boolean) detail.get("finance_status");
	        Boolean qaStatus = (Boolean) detail.get("qa_status");
	        Boolean iprStatus = (Boolean) detail.get("ipr_status");

	        Boolean approverStatus = null;
	        String approvedStatus = null;


	        boolean allApproversTrue = Boolean.TRUE.equals(hoiStatus) && Boolean.TRUE.equals(hodStatus) && Boolean.TRUE.equals(iprStatus)
	                && Boolean.TRUE.equals(asstDirStatus) && Boolean.TRUE.equals(hrStatus)
	                && Boolean.TRUE.equals(qaStatus) && Boolean.TRUE.equals(financeStatus);

	        boolean hasFalseStatus = Boolean.FALSE.equals(hoiStatus) || Boolean.FALSE.equals(hodStatus) || Boolean.FALSE.equals(iprStatus)
	                || Boolean.FALSE.equals(asstDirStatus) || Boolean.FALSE.equals(hrStatus)
	                || Boolean.FALSE.equals(qaStatus) || Boolean.FALSE.equals(financeStatus);

	        boolean hasTrueStatus = Boolean.TRUE.equals(hoiStatus) || Boolean.TRUE.equals(hodStatus) || Boolean.TRUE.equals(iprStatus)
	                || Boolean.TRUE.equals(asstDirStatus) || Boolean.TRUE.equals(hrStatus)
	                || Boolean.TRUE.equals(qaStatus) || Boolean.TRUE.equals(financeStatus);

	        boolean allStatusesNull = hoiStatus == null || hodStatus == null || qaStatus == null || iprStatus == null
	                || asstDirStatus == null || hrStatus == null || financeStatus == null;

	        if (allApproversTrue) {
	            approverStatus = true;
	            approvedStatus = "All Approved"; 
	        } else if (hasFalseStatus) {
	            approverStatus = false;
	            approvedStatus = null; 
	        } else if (hasTrueStatus) {
	            approverStatus = true;
	            approvedStatus = null; 
	        } else if (allStatusesNull) {
	            approverStatus = false;
	            approvedStatus = null; 
	        } else {
	            approverStatus = true;
	            approvedStatus = "All Approved"; 
	        }
	        
	        boolean allNullStatusesNull = hoiStatus == null && hodStatus == null && asstDirStatus == null && iprStatus == null &&
                    hrStatus == null && financeStatus == null && qaStatus == null;

				if (allNullStatusesNull) {
					System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ " + allNullStatusesNull);
				approverStatus = null;
				approvedStatus = null;
				}

	        modifiedDetail.put("approver_status", approverStatus);
	        modifiedDetail.put("approved_status", approvedStatus);

	        allActiveIncentiveApprover.setApproved_status(approvedStatus != null ? approvedStatus
	                : (modifiedDetail.get("approved_status") != null ? modifiedDetail.get("approved_status").toString() : null));
	        allActiveIncentiveApprover.setApprover_status(approverStatus);

	        IncentiveApprover updatedIncentiveApprover = ApproverRepository.save(allActiveIncentiveApprover);
	        
	        System.out.println("Updated Incentive Approver: " + updatedIncentiveApprover);

	        modifiedDetail.put("approver_status", updatedIncentiveApprover.getApprover_status());
	        modifiedDetail.put("approved_status", updatedIncentiveApprover.getApproved_status());

	        modifiedPublicationDetails.add(modifiedDetail);
	    }

	    return modifiedPublicationDetails; 
	}

	public List<Map<String, Object>> fetchAllPatent(Integer percentageFilter) {
		List<Map<String, Object>> patentDetails = patent_repository.getAllSortedData(percentageFilter);

		List<Map<String, Object>> modifiedPublicationDetails = new ArrayList<>();

		for (Map<String, Object> detail : patentDetails) {

		 Integer incentiveApproverId = (Integer) detail.get("incentive_approver_id");
	        System.out.println("Incentive Approver ID: " + incentiveApproverId);
	        
	        IncentiveApprover allActiveIncentiveApprover = ApproverRepository.getAllActiveIncentiveApprover(incentiveApproverId);
	        
	        if (allActiveIncentiveApprover == null || ObjectUtils.isEmpty(allActiveIncentiveApprover)) {
	            modifiedPublicationDetails.add(detail); 
	            continue; 
	        }

	        Map<String, Object> modifiedDetail = new HashMap<>(detail); 
	        Boolean hoiStatus = (Boolean) detail.get("hoi_status");
	        Boolean hodStatus = (Boolean) detail.get("hod_status");
	        Boolean asstDirStatus = (Boolean) detail.get("asst_dir_status");
	        Boolean hrStatus = (Boolean) detail.get("hr_status");
	        Boolean financeStatus = (Boolean) detail.get("finance_status");
	        Boolean qaStatus = (Boolean) detail.get("qa_status");
	        Boolean iprStatus = (Boolean) detail.get("ipr_status");

	        Boolean approverStatus = null;
	        String approvedStatus = null;


	        boolean allApproversTrue = Boolean.TRUE.equals(hoiStatus) && Boolean.TRUE.equals(hodStatus) && Boolean.TRUE.equals(iprStatus)
	                && Boolean.TRUE.equals(asstDirStatus) && Boolean.TRUE.equals(hrStatus)
	                && Boolean.TRUE.equals(qaStatus) && Boolean.TRUE.equals(financeStatus);

	        boolean hasFalseStatus = Boolean.FALSE.equals(hoiStatus) || Boolean.FALSE.equals(hodStatus) || Boolean.FALSE.equals(iprStatus)
	                || Boolean.FALSE.equals(asstDirStatus) || Boolean.FALSE.equals(hrStatus)
	                || Boolean.FALSE.equals(qaStatus) || Boolean.FALSE.equals(financeStatus);

	        boolean hasTrueStatus = Boolean.TRUE.equals(hoiStatus) || Boolean.TRUE.equals(hodStatus) || Boolean.TRUE.equals(iprStatus)
	                || Boolean.TRUE.equals(asstDirStatus) || Boolean.TRUE.equals(hrStatus)
	                || Boolean.TRUE.equals(qaStatus) || Boolean.TRUE.equals(financeStatus);

	        boolean allStatusesNull = hoiStatus == null || hodStatus == null || qaStatus == null || iprStatus == null
	                || asstDirStatus == null || hrStatus == null || financeStatus == null;

	        if (allApproversTrue) {
	            approverStatus = true;
	            approvedStatus = "All Approved"; 
	        } else if (hasFalseStatus) {
	            approverStatus = false;
	            approvedStatus = null; 
	        } else if (hasTrueStatus) {
	            approverStatus = true;
	            approvedStatus = null; 
	        } else if (allStatusesNull) {
	            approverStatus = false;
	            approvedStatus = null; 
	        } else {
	            approverStatus = true;
	            approvedStatus = "All Approved"; 
	        }
	        
	        boolean allNullStatusesNull = hoiStatus == null && hodStatus == null && asstDirStatus == null && iprStatus == null &&
                    hrStatus == null && financeStatus == null && qaStatus == null;

				if (allNullStatusesNull) {
					System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ " + allNullStatusesNull);
				approverStatus = null;
				approvedStatus = null;
				}

	        modifiedDetail.put("approver_status", approverStatus);
	        modifiedDetail.put("approved_status", approvedStatus);

	        allActiveIncentiveApprover.setApproved_status(approvedStatus != null ? approvedStatus
	                : (modifiedDetail.get("approved_status") != null ? modifiedDetail.get("approved_status").toString() : null));
	        allActiveIncentiveApprover.setApprover_status(approverStatus);

	        IncentiveApprover updatedIncentiveApprover = ApproverRepository.save(allActiveIncentiveApprover);
	        
	        System.out.println("Updated Incentive Approver: " + updatedIncentiveApprover);

	        modifiedDetail.put("approver_status", updatedIncentiveApprover.getApprover_status());
	        modifiedDetail.put("approved_status", updatedIncentiveApprover.getApproved_status());

	        modifiedPublicationDetails.add(modifiedDetail);
	    }

	    return modifiedPublicationDetails; 
	}

	public List<Map<String, Object>> patentBasedOnEmpId(Integer emp_id) {
		return patent_repository.patentBasedOnEmpId(emp_id);
	}

}
