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
import com.au.model.Membership;
import com.au.repository.IncentiveApproverRepository;
import com.au.repository.MembershipRepository;
import com.au.response.ResponseHandler;

@Service
public class MembershipService {

	private Logger log = LoggerFactory.getLogger(MembershipService.class);

	public static final String value = "Membership";

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
	private MembershipRepository membership_repo;

	@Autowired
	private IncentiveApproverRepository ApproverRepository;

	public List<Membership> saveMembership(@Valid List<Membership> mem) {
		mem.stream().forEach(membership -> {
			if (membership_repo.getCountOfMemberId(membership.getMember_id(),membership.getEmp_id()) >= 1) {
				throw new RuntimeException("MemberId already created " + membership.getMember_id() + "!!!");
			}

		});

		return membership_repo.saveAll(mem);
	}

	public List<Membership> getAllActiveMembership() {
		return membership_repo.getAllActiveMembership();
	}

	public Membership get(Integer id) {
		return membership_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Membership Not Found:" + id));
	}

	public Membership updateMembership(Membership book) {
		return membership_repo.save(book);
	}

	public void deactivate(Integer id) {
		Membership ademail = membership_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Membership Not Found:" + id));
		membership_repo.deactivate(id);
	}

	public void activate(Integer id) {
		Membership ademail = membership_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Membership Not Found:" + id));
		membership_repo.activate(id);
	}

//	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer percentageFilter) {
//		Page<Map<String, Object>> oc_filtered_response = membership_repo.getAllDataFilteredByKeyword(pageable, keyword,percentageFilter);
//		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, oc_filtered_response);
//	}
//
//	public ResponseEntity<Object> getAllSortedData(Pageable pageable, Integer percentageFilter) {
//		Page<Map<String, Object>> oc_sorted_response = membership_repo.getAllSortedData(pageable,percentageFilter);
//		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, oc_sorted_response);
//	}

	public void uploadFile(MultipartFile multipartFile, Integer membership_id) {

		Membership entity = membership_repo.findById(membership_id)
				.orElseThrow(() -> new ResourceNotFoundException("Membership not found"));
		try {
			log.debug("Message For Cancel admission Attachment --------------");

			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);

			log.debug("Cancel admission Attachment", multipartFile);

			entity.setAttachment_name(fileName);
			entity.setAttachment_path(LocalDate.now() + "/" + membership_id + "/" + fileName);
			uploadFileTos3bucket(fileName, file, membership_id);
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
		membership_repo.save(entity);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer membership_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + membership_id + "/" + fileName; // file.getName()
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

	public List<Map<String, Object>> membershipDetailsBasedOnEmpId(List<Integer> emp_id, Integer percentageFilter) {
		List<Map<String, Object>> membershipDetails = membership_repo.membershipDetailsBasedOnEmpId(emp_id,percentageFilter);


		List<Map<String, Object>> modifiedPublicationDetails = new ArrayList<>();

		for (Map<String, Object> detail : membershipDetails) {


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

	        Boolean approverStatus = null;
	        String approvedStatus = null;

	        boolean allApproversTrue = Boolean.TRUE.equals(hoiStatus) && Boolean.TRUE.equals(hodStatus)
	                && Boolean.TRUE.equals(asstDirStatus) && Boolean.TRUE.equals(hrStatus)
	                && Boolean.TRUE.equals(qaStatus) && Boolean.TRUE.equals(financeStatus);

	        boolean hasFalseStatus = Boolean.FALSE.equals(hoiStatus) || Boolean.FALSE.equals(hodStatus)
	                || Boolean.FALSE.equals(asstDirStatus) || Boolean.FALSE.equals(hrStatus)
	                || Boolean.FALSE.equals(qaStatus) || Boolean.FALSE.equals(financeStatus);

	        boolean hasTrueStatus = Boolean.TRUE.equals(hoiStatus) || Boolean.TRUE.equals(hodStatus)
	                || Boolean.TRUE.equals(asstDirStatus) || Boolean.TRUE.equals(hrStatus)
	                || Boolean.TRUE.equals(qaStatus) || Boolean.TRUE.equals(financeStatus);

	        boolean allStatusesNull = hoiStatus == null || hodStatus == null || qaStatus == null
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
	        
	        boolean allNullStatusesNull = hoiStatus == null && hodStatus == null && asstDirStatus == null && 
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

	public List<Map<String, Object>> fetchAllMembership(Integer percentageFilter) {
		List<Map<String, Object>> membershipDetails = membership_repo.getAllSortedData(percentageFilter);


		List<Map<String, Object>> modifiedPublicationDetails = new ArrayList<>();

		for (Map<String, Object> detail : membershipDetails) {


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

	        Boolean approverStatus = null;
	        String approvedStatus = null;

	        boolean allApproversTrue = Boolean.TRUE.equals(hoiStatus) && Boolean.TRUE.equals(hodStatus)
	                && Boolean.TRUE.equals(asstDirStatus) && Boolean.TRUE.equals(hrStatus)
	                && Boolean.TRUE.equals(qaStatus) && Boolean.TRUE.equals(financeStatus);

	        boolean hasFalseStatus = Boolean.FALSE.equals(hoiStatus) || Boolean.FALSE.equals(hodStatus)
	                || Boolean.FALSE.equals(asstDirStatus) || Boolean.FALSE.equals(hrStatus)
	                || Boolean.FALSE.equals(qaStatus) || Boolean.FALSE.equals(financeStatus);

	        boolean hasTrueStatus = Boolean.TRUE.equals(hoiStatus) || Boolean.TRUE.equals(hodStatus)
	                || Boolean.TRUE.equals(asstDirStatus) || Boolean.TRUE.equals(hrStatus)
	                || Boolean.TRUE.equals(qaStatus) || Boolean.TRUE.equals(financeStatus);

	        boolean allStatusesNull = hoiStatus == null || hodStatus == null || qaStatus == null
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
	        
	        boolean allNullStatusesNull = hoiStatus == null && hodStatus == null && asstDirStatus == null && 
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

	public List<Map<String, Object>> membershipBasedOnEmpId(Integer emp_id) {
		return membership_repo.membershipBasedOnEmpId(emp_id);
	}

}
