package com.au.service;

import java.util.HashMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

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
import com.au.dto.ProfileResearchDto;
import com.au.model.Notifications;
import com.au.model.ProfileResearch;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.ProfileResearchRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class ProfileResearchService {

	@Autowired
	private ProfileResearchRepository pr_repo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
private Logger logger = LoggerFactory.getLogger(FeeTemplateService.class);
	
	public static final String value = "ProfileResearchBucket";
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
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

	public List<ProfileResearch> createProfileResearch(ProfileResearchDto pr) throws Exception {
		
		List<ProfileResearch> data = new ArrayList<ProfileResearch>();
		
		if(pr.getPhdHolderPursuing().equalsIgnoreCase("PHDHolder") || pr.getPhdHolderPursuing().equalsIgnoreCase("PHDPursuing")) {
		
		if(pr_repo.getCountOfTitleOfThesis(pr.getTitleOfThesis(), pr.getEmpId()) >= 1) {
				throw new RuntimeException("Data already present for the given title of thesis!!!");
			}
		
		}
		
		if(pr.getPhdHolderPursuing().equalsIgnoreCase("PHDHolder")) {
			
			ProfileResearch da = new ProfileResearch();
			
			da.setCreatedBy(pr.getCreatedBy());
			da.setCreatedDate(pr.getCreatedDate());
			da.setCreatedUsername(pr.getCreatedUsername());
			da.setEmpId(pr.getEmpId());
			da.setOtherCitationDatabase(pr.getOtherCitationDatabase());
			da.setGoogleScholar(pr.getGoogleScholar());
			da.setLinkedInLink(pr.getLinkedInLink());
			da.setNoOfConferences(pr.getNoOfConferences());
			da.setPartOfResearchProject(pr.getPartOfResearchProject());
			da.setPeerViewed(pr.getPeerViewed());
			da.setPhdCompletedDate(pr.getPhdCompletedDate());
			da.setPhdRegisterDate(pr.getPhdRegisterDate());
			da.setPhdCount(pr.getPhdCount());
			da.setPhdHolderPursuing(pr.getPhdHolderPursuing());
			da.setProfessionalOrganisation(pr.getProfessionalOrganisation());
			da.setResearchAttachment(pr.getResearchAttachment());
			da.setTenureStatus(pr.getTenureStatus());
			da.setTitleOfThesis(pr.getTitleOfThesis());
			da.setUniversityRegisterNumber(pr.getUniversityRegisterNumber());
			da.setUniversityName(pr.getUniversityName());
			da.setYesNumberOfProjects(pr.getYesNumberOfProjects());
			da.setActive(pr.getActive());
			da.setResearchForCollaboration(pr.getResearchForCollaboration());
			
			data.add(da);
			
		} else if(pr.getPhdHolderPursuing().equalsIgnoreCase("PHDPursuing")) {
			
				ProfileResearch da = new ProfileResearch();
				
				da.setCreatedBy(pr.getCreatedBy());
				da.setCreatedDate(pr.getCreatedDate());
				da.setCreatedUsername(pr.getCreatedUsername());
				da.setActive(pr.getActive());
				da.setEmpId(pr.getEmpId());
				da.setPhdHolderPursuing(pr.getPhdHolderPursuing());
				da.setTenureStatus(pr.getTenureStatus());
				
				da.setTitleOfThesis(pr.getTitleOfThesis());
				da.setUniversityRegisterNumber(pr.getUniversityRegisterNumber());
				da.setUniversityName(pr.getUniversityName());
				da.setPhdRegisterDate(pr.getPhdRegisterDate());


				data.add(da);
				


		} else {
			pr.getResearchInterests().stream().forEach(profile -> {	
				
				ProfileResearch da = new ProfileResearch();
				
				da.setCreatedBy(pr.getCreatedBy());
				da.setCreatedDate(pr.getCreatedDate());
				da.setCreatedUsername(pr.getCreatedUsername());
				da.setActive(pr.getActive());
				da.setEmpId(pr.getEmpId());
				da.setPhdHolderPursuing(pr.getPhdHolderPursuing());
				
				da.setAreasOfExpertise(profile.getAreasOfExpertise());
				da.setKeywordsResearch(profile.getKeywordsResearch());
				da.setTechniquesExpert(profile.getTechniquesExpert());
				da.setCurrentProfessional(profile.getCurrentProfessional());
				
				data.add(da);
			});
		}
		return pr_repo.saveAll(data);
	}

	public List<ProfileResearch> listAll() {
		return pr_repo.findAll1();
	}

	public ProfileResearch get(Integer profileResearchId) {
		return pr_repo.findById(profileResearchId)
				.orElseThrow(() -> new ResourceNotFoundException("ProfileResearch Not Found:" + profileResearchId));
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> profile_research_filtered_response = pr_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> profile_research_response = pr_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_response);
	}

	public ProfileResearch updateProfileResearch(ProfileResearch pr) {
		return pr_repo.save(pr);
	}

	public void delete(Integer profileResearchId) {
		ProfileResearch sir = pr_repo.findById(profileResearchId)
				.orElseThrow(() -> new ResourceNotFoundException("ProfileResearch Not Found:" + profileResearchId));
		pr_repo.updateProfileResearch(profileResearchId);
	}

	public void delete1(Integer profileResearchId) {
		ProfileResearch sir = pr_repo.findById(profileResearchId)
				.orElseThrow(() -> new ResourceNotFoundException("ProfileResearch Not Found:" + profileResearchId));
		pr_repo.updateProfileResearch1(profileResearchId);
	}

	public List<HashMap<String, Object>> fetchAllProfileResearchForEmployee(Integer userId) {
		Integer empId = empDetail_repo.getEmployeeIdOnUserMail(uar_repo.getEmail(userId));
		
		List<HashMap<String, Object>> profileData = pr_repo.fetchAllProfileResearchForEmployee(empId);
		return profileData;
	}

	public void uploadFile(MultipartFile multipartFile, List<Integer> profileResearchId) {
		Notifications noti = new Notifications();
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String t2 = noti.setNotification_attach_path(LocalDate.now() + "/" + profileResearchId.get(0) + "/" + fileName);
			System.out.println("[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]] "+t2);
			uploadFileTos3bucket(fileName, file, profileResearchId.get(0));
			file.delete();
			pr_repo.updatePath(profileResearchId, t2);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer profileResearchId) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + profileResearchId + "/" + fileName; // file.getName()
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
			
}
