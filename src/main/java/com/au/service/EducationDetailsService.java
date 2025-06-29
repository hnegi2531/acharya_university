package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;
import com.au.dto.EducationDetailsAttachmentsDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.EducationDetails;
import com.au.model.EducationDetailsAttachment;
import com.au.model.EmployeeDetails;
import com.au.model.EmployeeIDsAttachment;
import com.au.model.Graduation;
import com.au.repository.EducationDetailsAttachmentrepo;
import com.au.repository.EducationDetailsRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.Graduation_Repository;
import com.au.repository.JobProfileRepository;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

@Service
public class EducationDetailsService {

public static final String value = "EducationDetailsBucket";
	
	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private EducationDetailsAttachmentrepo eddaRepo;
	
	@Autowired
	private EducationDetailsRepository edr_repo;

	@Autowired
	private Graduation_Repository gr;
	
	@Autowired
	private JobProfileRepository jpr_repo;
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}
	
	public List<EducationDetailsAttachment> getEducationDocsByEmpId(Integer empId) {
		List<EducationDetailsAttachment> findByEmployeeId = eddaRepo.getEducationDocsByEmpId(empId);
		return findByEmployeeId;
	}
	
	public void deactivate(Integer id) {
		EducationDetailsAttachment co = eddaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee Attachment Not Found:" + id));
		eddaRepo.updateEducationDetailsAttachment(id);
	}
	
	
	public EducationDetailsAttachment uploadFile(EducationDetailsAttachmentsDto cwad) {
		EmployeeDetails employee = empDetail_repo.findById(cwad.getEmpId())
				.orElseThrow(() -> new ResourceNotFoundException("employee with id not present"));

			Graduation findById = gr.findById(cwad.getGraduationId())
					.orElseThrow(() -> new ResourceNotFoundException("Graduation with id not present"));
			EducationDetailsAttachment educationDetailsAttachment1 = new EducationDetailsAttachment();
		try
			{
		File file = convertMultiPartToFile(cwad.getDocument());
		String fileName = generateFileName(cwad.getDocument());
		educationDetailsAttachment1.setEmpId(employee.getEmp_id());
		educationDetailsAttachment1.setActive(true);
		educationDetailsAttachment1.setGraduationId(findById.getGraduation_id());
		educationDetailsAttachment1.setGraduationName(findById.getGraduation_name());
		educationDetailsAttachment1.setAttachment_file_name(fileName);
		educationDetailsAttachment1.setAttachment_path(LocalDate.now() + "/" + cwad.getEmpId()+ "/" + fileName);
		educationDetailsAttachment1.setAttachment_type(
				endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + cwad.getEmpId() + "/" + fileName);
		uploadFileToS3Bucket(fileName, file, cwad.getEmpId());	
		file.delete();
		} catch (AmazonServiceException e) {
		    
		    e.printStackTrace();
		} catch (SdkClientException e) {
		    // Handle the AWS SDK client exception
		    e.printStackTrace();
		}
		catch (IOException ioe) {  
			ioe.printStackTrace();
			}
	   eddaRepo.save(educationDetailsAttachment1);
		return educationDetailsAttachment1;
	}
	
	public List<EducationDetails> listAll() {
		return edr_repo.findAll();
	}

	public List<EducationDetails> saveEducationDetails(List<EducationDetails> educationdetails) {
		educationdetails.stream().forEach(edu -> {
			edu.setJob_id(jpr_repo.getLatestJobId());
			System.out.println(edu.getJob_id());
		});
		return edr_repo.saveAll(educationdetails);
	}

	public EducationDetails get(Integer id) {
		if (id.equals(0)) {
			throw new RuntimeException("Opps Exception raised....");
		}
		return edr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("EducationDetails id Not Found:" + id));
	}

	public void delete(Integer id) {
		EducationDetails ay = edr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("EducationDetails id Not Found:" + id));
		edr_repo.delete(ay);
	}

	public List<HashMap<String, Object>> fetchEducationDetails(Integer job_id) {
		 return edr_repo.getEducationDetails(job_id);
	}
	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convertFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convertFile);
		fos.write(file.getBytes());
		fos.close();
		return convertFile;
		
	}
	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "");
	}
	
	private void uploadFileToS3Bucket(String fileName, File file, Integer candidate_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + candidate_id + "/" + fileName; 
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
			if (e.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				throw new NoSuchFileException("File Not Found");
			}
			throw new AmazonClientException("", e);
		} catch (IOException | AmazonClientException ex) {
			throw new AmazonClientException("", ex);
		}
	}

	public List<EducationDetails> createEducationDetails(@Valid List<EducationDetails> ed) {
		 return	edr_repo.saveAll(ed);
	}

	public List<EducationDetails> newsaveEducationDetails(@Valid List<EducationDetails> educationdetails) {
		// TODO Auto-generated method stub
		return edr_repo.saveAll(educationdetails);
	}


}
