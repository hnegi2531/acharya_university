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
import com.au.model.Publications;
import com.au.repository.IncentiveApproverRepository;
import com.au.repository.PublicationsRepository;
import com.au.response.ResponseHandler;

@Service
public class PublicationsService {

	@Autowired
	private PublicationsRepository publication_repo;

	@Autowired
	private IncentiveApproverRepository ApproverRepository;

	private Logger log = LoggerFactory.getLogger(PublicationsService.class);

	public static final String value = "Publication";

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

	public List<Publications> savePublication(@Valid List<Publications> ademail) throws Exception {
		ademail.stream().forEach(publication -> {
			if (publication_repo.getCountOfIssn(publication.getIssn(),publication.getEmp_id()) >= 1) {
				throw new RuntimeException("Issn already created " + publication.getIssn() + "!!!");
			}

			if (publication_repo.getCountOfIssue_number(publication.getIssue_number(),publication.getEmp_id()) >= 1) {
				throw new RuntimeException("Issue Number already created " + publication.getIssue_number() + "!!!");
			}

		});

		return publication_repo.saveAll(ademail);
	}

	public List<Publications> getAllActivePublication() {
		return publication_repo.getAllActivePublication();
	}

	public Publications get(Integer id) {
		return publication_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Publication Not Found:" + id));
	}

	public Publications updatePublication(Publications ademail) {
		return publication_repo.save(ademail);
	}

	public void deactivate(Integer id) {
		Publications ademail = publication_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Objective Not Found:" + id));
		publication_repo.deactivate(id);
	}

	public void activate(Integer id) {
		Publications ademail = publication_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Objective Not Found:" + id));
		publication_repo.activate(id);
	}

//	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer percentageFilter) {
//		Page<Map<String, Object>> oc_filtered_response = publication_repo.getAllDataFilteredByKeyword(pageable,
//				keyword,percentageFilter);
//		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, oc_filtered_response);
//	}
//
//	public ResponseEntity<Object> getAllSortedData(Pageable pageable, Integer percentageFilter) {
//		Page<Map<String, Object>> oc_sorted_response = publication_repo.getAllSortedData(pageable,percentageFilter);
//		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, oc_sorted_response);
//	}

	public void uploadFile(MultipartFile multipartFile, Integer publications_id) {

		Publications entity = publication_repo.findById(publications_id)
				.orElseThrow(() -> new ResourceNotFoundException("Publications not found"));
		try {
			log.debug("Message For Cancel admission Attachment --------------");

			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);

			log.debug("Cancel admission Attachment", multipartFile);

			entity.setAttachment_name(fileName);
			entity.setAttachment_path(LocalDate.now() + "/" + publications_id + "/" + fileName);
			uploadFileTos3bucket(fileName, file, publications_id);
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
		publication_repo.save(entity);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer publications_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + publications_id + "/" + fileName; // file.getName()
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

	public List<Map<String, Object>> publicationDetailsBasedOnEmpId(List<Integer> emp_id, Integer percentageFilter) {

		List<Map<String, Object>> publicationDetails = publication_repo.publicationDetailsBasedOnEmpId(emp_id,percentageFilter);

		List<Map<String, Object>> modifiedPublicationDetails = new ArrayList<>();

		for (Map<String, Object> detail : publicationDetails) {

			Integer incentiveApproverId = (Integer) detail.get("incentive_approver_id");
			System.out.println("Incentive Approver ID: " + incentiveApproverId);

			IncentiveApprover allActiveIncentiveApprover = ApproverRepository
					.getAllActiveIncentiveApprover(incentiveApproverId);

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
				approvedStatus = "All Approved"; // Set to "All Approved" if all statuses are true
			} else if (hasFalseStatus) {
				approverStatus = false;
				approvedStatus = null; // Set to null if any status is false
			} else if (hasTrueStatus) {
				approverStatus = true;
				approvedStatus = null; // Set to null if any status is true
			} else if (allStatusesNull) {
				approverStatus = false;
				approvedStatus = null; // Set to null if all statuses are null

			} else {
				approverStatus = true;
				approvedStatus = "All Approved";
			}

			boolean allNullStatusesNull = hoiStatus == null && hodStatus == null && asstDirStatus == null
					&& hrStatus == null && financeStatus == null && qaStatus == null;

			if (allNullStatusesNull) {

				approverStatus = null;
				approvedStatus = null;
			}

			modifiedDetail.put("approver_status", approverStatus);
			modifiedDetail.put("approved_status", approvedStatus);

			allActiveIncentiveApprover.setApproved_status(approvedStatus != null ? approvedStatus
					: (modifiedDetail.get("approved_status") != null ? modifiedDetail.get("approved_status").toString()
							: null));
			allActiveIncentiveApprover.setApprover_status(approverStatus);

			IncentiveApprover updatedIncentiveApprover = ApproverRepository.save(allActiveIncentiveApprover);

			modifiedDetail.put("approver_status", updatedIncentiveApprover.getApprover_status());
			modifiedDetail.put("approved_status", updatedIncentiveApprover.getApproved_status());

			modifiedPublicationDetails.add(modifiedDetail);
		}

		return modifiedPublicationDetails;
	}

	public List<Map<String, Object>> fetchAllPublication(Integer percentageFilter) {
		List<Map<String, Object>> publicationDetails = publication_repo.getAllSortedData(percentageFilter);

		List<Map<String, Object>> modifiedPublicationDetails = new ArrayList<>();

		for (Map<String, Object> detail : publicationDetails) {

			Integer incentiveApproverId = (Integer) detail.get("incentive_approver_id");
			System.out.println("Incentive Approver ID: " + incentiveApproverId);

			IncentiveApprover allActiveIncentiveApprover = ApproverRepository
					.getAllActiveIncentiveApprover(incentiveApproverId);

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
				approvedStatus = "All Approved"; // Set to "All Approved" if all statuses are true
			} else if (hasFalseStatus) {
				approverStatus = false;
				approvedStatus = null; // Set to null if any status is false
			} else if (hasTrueStatus) {
				approverStatus = true;
				approvedStatus = null; // Set to null if any status is true
			} else if (allStatusesNull) {
				approverStatus = false;
				approvedStatus = null; // Set to null if all statuses are null

			} else {
				approverStatus = true;
				approvedStatus = "All Approved";
			}

			boolean allNullStatusesNull = hoiStatus == null && hodStatus == null && asstDirStatus == null
					&& hrStatus == null && financeStatus == null && qaStatus == null;

			if (allNullStatusesNull) {

				approverStatus = null;
				approvedStatus = null;
			}

			modifiedDetail.put("approver_status", approverStatus);
			modifiedDetail.put("approved_status", approvedStatus);

			allActiveIncentiveApprover.setApproved_status(approvedStatus != null ? approvedStatus
					: (modifiedDetail.get("approved_status") != null ? modifiedDetail.get("approved_status").toString()
							: null));
			allActiveIncentiveApprover.setApprover_status(approverStatus);

			IncentiveApprover updatedIncentiveApprover = ApproverRepository.save(allActiveIncentiveApprover);

			modifiedDetail.put("approver_status", updatedIncentiveApprover.getApprover_status());
			modifiedDetail.put("approved_status", updatedIncentiveApprover.getApproved_status());

			modifiedPublicationDetails.add(modifiedDetail);
		}

		return modifiedPublicationDetails;
	}

	public List<Map<String, Object>> publicationBasedOnEmpId(Integer emp_id) {
		return publication_repo.publicationBasedOnEmpId(emp_id);
	}

}
