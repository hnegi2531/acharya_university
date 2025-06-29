package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.au.dto.FrroDTO;
import com.au.dto.FrroResponseDTO;
import com.au.dto.JwtDetails;
import com.au.model.FRRO;
import com.au.model.FrroHistory;
import com.au.repository.FrroHistoryRepository;
import com.au.repository.FrroRepository;
import com.au.response.ResponseHandler;

@Service
public class FrroService {

	@Autowired
	private FrroRepository frroRepository;

	@Autowired
	private FrroHistoryRepository frroHistoryRepository;

	@Autowired
	private JwtTokenService jwtTokenService;

	public static final String value = "FrroBucket";

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

	public ResponseEntity<Object> saveFrro(FrroDTO frroDTO, String jwtToken) {
		try {

			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);

			FRRO isExists=frroRepository.getByStudentId(frroDTO.getStudentId());
			
			if(ObjectUtils.isNotEmpty(isExists)) {
				return ResponseHandler.generateResponse(false, HttpStatus.OK, "SUCCESS", "Data Already exists");
			}
			
			FRRO frro = new FRRO();
			frro.setAluEquivalenceDocument(frroDTO.getAluEquivalenceDocument());
			frro.setBirthPlace(frroDTO.getBirthPlace());
			frro.setFsis(frroDTO.getFsis());
			frro.setImmigrationDate(frroDTO.getImmigrationDate());
			frro.setIsReportedToIndia(frroDTO.getIsReportedToIndia());
			frro.setIssueBy(frroDTO.getIssueBy());
			frro.setNameAsPerPassport(frroDTO.getNameAsPerPassport());
			frro.setPassportCopyDocument(frroDTO.getPassportCopyDocument());
			frro.setPassportExpiryDate(frroDTO.getPassportExpiryDate());
			frro.setPassportIssuePlace(frroDTO.getPassportIssuePlace());
			frro.setPassportIssueDate(frroDTO.getPassportIssueDate());
			frro.setPassportNo(frroDTO.getPassportNo());
			frro.setPlaceOfVisaIssue(frroDTO.getPlaceOfVisaIssue());
			frro.setPortOfArrival(frroDTO.getPortOfArrival());
			frro.setPortOfDeparture(frroDTO.getPortOfDeparture());
			frro.setRemarks(frroDTO.getRemarks());
			frro.setReportedOn(frroDTO.getReportedOn());
			frro.setResidentialPermitCopyDocument(frroDTO.getResidentialPermitCopyDocument());
			frro.setRpExpiryDate(frroDTO.getRpExpiryDate());
			frro.setRpIssueDate(frroDTO.getRpIssueDate());
			frro.setRpNo(frroDTO.getRpNo());
			frro.setStudentId(frroDTO.getStudentId());
			frro.setTypeOfEntry(frroDTO.getTypeOfEntry());
			frro.setVisaCopyDocument(frroDTO.getVisaCopyDocument());
			frro.setVisaExpiryDate(frroDTO.getVisaExpiryDate());
			frro.setVisaIssueDate(frroDTO.getVisaIssueDate());
			frro.setVisaNo(frroDTO.getVisaNo());
			frro.setVisaType(frroDTO.getVisaType());
			frro.setCreated_by(jwtDetails.getUserName());
			frro.setVisaIssued(frroDTO.getVisaIssued());
			frro.setAffiliataion(frroDTO.getAffiliataion());
			frro.setRecognition(frroDTO.getRecognition());
			frroRepository.save(frro);

			FrroHistory frroHistory = new FrroHistory();
			frroHistory.setAluEquivalenceDocument(frroDTO.getAluEquivalenceDocument());
			frroHistory.setBirthPlace(frroDTO.getBirthPlace());
			frroHistory.setFsis(frroDTO.getFsis());
			frroHistory.setImmigrationDate(frroDTO.getImmigrationDate());
			frroHistory.setIsReportedToIndia(frroDTO.getIsReportedToIndia());
			frroHistory.setIssueBy(frroDTO.getIssueBy());
			frroHistory.setNameAsPerPassport(frroDTO.getNameAsPerPassport());
			frroHistory.setPassportCopyDocument(frroDTO.getPassportCopyDocument());
			frroHistory.setPassportExpiryDate(frroDTO.getPassportExpiryDate());
			frroHistory.setPassportIssuePlace(frroDTO.getPassportIssuePlace());
			frroHistory.setPassportIssueDate(frroDTO.getPassportIssueDate());
			frroHistory.setPassportNo(frroDTO.getPassportNo());
			frroHistory.setPlaceOfVisaIssue(frroDTO.getPlaceOfVisaIssue());
			frroHistory.setPortOfArrival(frroDTO.getPortOfArrival());
			frroHistory.setPortOfDeparture(frroDTO.getPortOfDeparture());
			frroHistory.setRemarks(frroDTO.getRemarks());
			frroHistory.setReportedOn(frroDTO.getReportedOn());
			frroHistory.setResidentialPermitCopyDocument(frroDTO.getResidentialPermitCopyDocument());
			frroHistory.setRpExpiryDate(frroDTO.getRpExpiryDate());
			frroHistory.setRpIssueDate(frroDTO.getRpIssueDate());
			frroHistory.setRpNo(frroDTO.getRpNo());
			frroHistory.setStudentId(frroDTO.getStudentId());
			frroHistory.setTypeOfEntry(frroDTO.getTypeOfEntry());
			frroHistory.setVisaCopyDocument(frroDTO.getVisaCopyDocument());
			frroHistory.setVisaExpiryDate(frroDTO.getVisaExpiryDate());
			frroHistory.setVisaIssueDate(frroDTO.getVisaIssueDate());
			frroHistory.setVisaNo(frroDTO.getVisaNo());
			frroHistory.setVisaType(frroDTO.getVisaType());
			frroHistory.setCreated_by(jwtDetails.getUserName());
			frroHistoryRepository.save(frroHistory);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> getStudentDetailsForFrro(String auid) {
		try {

			Map<String, Object> studentDetails = frroRepository.getFrroByStudentId(auid);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", studentDetails);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> getStudentDetailsListForFrro(String auid) {
		try {
			List<Map<String, Object>> studentDetails =null;
            
			if(StringUtils.isEmpty(auid)) {
				studentDetails = frroRepository.getStudentDetailsListForFrro();	
			}else {
				studentDetails = frroRepository.searchStudentDetailsListForFrro(auid);			
			}
			
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", studentDetails);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> uploadFrroFile(MultipartFile multipartFile, String fileType, String studentId) {
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			uploadFileToS3Bucket(fileName, file, studentId, fileType);
			String attachmentPath = LocalDate.now() + "/" 
					+ studentId + "/" + fileType + "/" + fileName;
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

	private void uploadFileToS3Bucket(String fileName, File file, String studentId, String fileType) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + studentId + "/" + fileType + "/" + fileName;
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));

	}

	public ResponseEntity<Object> checkFrroIsPresentOrNot(Integer studentId) {
		try {

			List<Map<String, Object>> studentDetails = frroRepository.getStudentDetailsListForFrro();

			if (ObjectUtils.isNotEmpty(studentDetails)) {
				return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", true);

			}

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", false);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> getFrroList() {
		try {

			List<Map<String, Object>> frroList = frroRepository.getFrroDetails();

			if (ObjectUtils.isNotEmpty(frroList)) {
				return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", frroList);

			}

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> updateFrro(Integer studentId, FrroDTO frroDTO, String jwtToken) {
		try {

			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);

			FRRO frro = frroRepository.getByStudentId(studentId);

			if (ObjectUtils.isNotEmpty(frro)) {
				
				FrroHistory frroHistory = new FrroHistory();
				frroHistory.setIsChangealuEquivalenceDocument(StringUtils.isNotEmpty(frro.getAluEquivalenceDocument())
						&& !StringUtils.equals(frro.getAluEquivalenceDocument(), frroDTO.getAluEquivalenceDocument())
								? Boolean.TRUE
								: Boolean.FALSE);
				
				frroHistory.setIsChangebirthPlace(StringUtils.isNotEmpty(frro.getBirthPlace())
						&& !StringUtils.equals(frro.getBirthPlace(), frroDTO.getBirthPlace())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangefsis(StringUtils.isNotEmpty(frro.getFsis())
						&& !StringUtils.equals(frro.getFsis(), frroDTO.getFsis())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangeimmigrationDate(StringUtils.isNotEmpty(frro.getImmigrationDate())
						&& !StringUtils.equals(frro.getImmigrationDate(), frroDTO.getImmigrationDate())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangeisReportedToIndia(ObjectUtils.isNotEmpty(frro.getIsReportedToIndia())
						&& frro.getIsReportedToIndia()!=frroDTO.getIsReportedToIndia()
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangeissueBy(StringUtils.isNotEmpty(frro.getIssueBy())
						&& !StringUtils.equals(frro.getIssueBy(), frroDTO.getIssueBy())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangenameAsPerPassport(StringUtils.isNotEmpty(frro.getNameAsPerPassport())
						&& !StringUtils.equals(frro.getNameAsPerPassport(), frroDTO.getNameAsPerPassport())
								? Boolean.TRUE
								: Boolean.FALSE);
				
				frroHistory.setIsChangepassportCopyDocument(StringUtils.isNotEmpty(frro.getPassportCopyDocument())
						&& !StringUtils.equals(frro.getPassportCopyDocument(), frroDTO.getPassportCopyDocument())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangepassportExpiryDate(StringUtils.isNotEmpty(frro.getPassportExpiryDate())
						&& !StringUtils.equals(frro.getPassportExpiryDate(), frroDTO.getPassportExpiryDate())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangepassportIssuePlace(StringUtils.isNotEmpty(frro.getPassportIssuePlace())
						&& !StringUtils.equals(frro.getPassportIssuePlace(), frroDTO.getPassportIssuePlace())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangepassportIssueDate(StringUtils.isNotEmpty(frro.getPassportIssueDate())
						&& !StringUtils.equals(frro.getPassportIssueDate(), frroDTO.getPassportIssueDate())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangepassportNo(StringUtils.isNotEmpty(frro.getPassportNo())
						&& !StringUtils.equals(frro.getPassportNo(), frroDTO.getPassportNo())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangeplaceOfVisaIssue(StringUtils.isNotEmpty(frro.getPlaceOfVisaIssue())
						&& !StringUtils.equals(frro.getPlaceOfVisaIssue(), frroDTO.getPlaceOfVisaIssue())
								? Boolean.TRUE
								: Boolean.FALSE);
				
				frroHistory.setIsChangeportOfArrival(StringUtils.isNotEmpty(frro.getPortOfArrival())
						&& !StringUtils.equals(frro.getPortOfArrival(), frroDTO.getPortOfArrival())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangeportOfDeparture(StringUtils.isNotEmpty(frro.getPortOfDeparture())
						&& !StringUtils.equals(frro.getPortOfDeparture(), frroDTO.getPortOfDeparture())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangeremarks(StringUtils.isNotEmpty(frro.getRemarks())
						&& !StringUtils.equals(frro.getRemarks(), frroDTO.getRemarks())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangereportedOn(StringUtils.isNotEmpty(frro.getReportedOn())
						&& !StringUtils.equals(frro.getReportedOn(), frroDTO.getReportedOn())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangeresidentialPermitCopyDocument(StringUtils.isNotEmpty(frro.getResidentialPermitCopyDocument())
						&& !StringUtils.equals(frro.getResidentialPermitCopyDocument(), frroDTO.getResidentialPermitCopyDocument())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangerpExpiryDate(StringUtils.isNotEmpty(frro.getRpExpiryDate())
						&& !StringUtils.equals(frro.getRpExpiryDate(), frroDTO.getRpExpiryDate())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangerpIssueDate(StringUtils.isNotEmpty(frro.getRpIssueDate())
						&& !StringUtils.equals(frro.getRpIssueDate(), frroDTO.getRpIssueDate())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangerpNo(StringUtils.isNotEmpty(frro.getRpNo())
						&& !StringUtils.equals(frro.getRpNo(), frroDTO.getRpNo())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangerpNo(StringUtils.isNotEmpty(frro.getRpNo())
						&& !StringUtils.equals(frro.getRpNo(), frroDTO.getRpNo())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangerpNo(StringUtils.isNotEmpty(frro.getRpNo())
						&& !StringUtils.equals(frro.getRpNo(), frroDTO.getRpNo())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangetypeOfEntry(StringUtils.isNotEmpty(frro.getTypeOfEntry())
						&& !StringUtils.equals(frro.getTypeOfEntry(), frroDTO.getTypeOfEntry())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangevisaCopyDocument(StringUtils.isNotEmpty(frro.getVisaCopyDocument())
						&& !StringUtils.equals(frro.getVisaCopyDocument(), frroDTO.getVisaCopyDocument())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangevisaExpiryDate(StringUtils.isNotEmpty(frro.getVisaExpiryDate())
						&& !StringUtils.equals(frro.getVisaExpiryDate(), frroDTO.getVisaExpiryDate())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangevisaNo(StringUtils.isNotEmpty(frro.getVisaNo())
						&& !StringUtils.equals(frro.getVisaNo(), frroDTO.getVisaNo())
								? Boolean.TRUE
								: Boolean.FALSE);
	
				frroHistory.setIsChangevisaIssued(StringUtils.isNotEmpty(frro.getVisaIssued())
						&& !StringUtils.equals(frro.getVisaIssued(), frroDTO.getVisaIssued())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangeRecogination(StringUtils.isNotEmpty(frro.getRecognition())
						&& !StringUtils.equals(frro.getRecognition(), frroDTO.getRecognition())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setIsChangeAffiliation(StringUtils.isNotEmpty(frro.getAffiliataion())
						&& !StringUtils.equals(frro.getAffiliataion(), frroDTO.getAffiliataion())
								? Boolean.TRUE
								: Boolean.FALSE);
				frroHistory.setAluEquivalenceDocument(frroDTO.getAluEquivalenceDocument());
				frroHistory.setBirthPlace(frroDTO.getBirthPlace());
				frroHistory.setFsis(frroDTO.getFsis());
				frroHistory.setImmigrationDate(frroDTO.getImmigrationDate());
				frroHistory.setIsReportedToIndia(frroDTO.getIsReportedToIndia());
				frroHistory.setIssueBy(frroDTO.getIssueBy());
				frroHistory.setNameAsPerPassport(frroDTO.getNameAsPerPassport());
				frroHistory.setPassportCopyDocument(frroDTO.getPassportCopyDocument());
				frroHistory.setPassportExpiryDate(frroDTO.getPassportExpiryDate());
				frroHistory.setPassportIssuePlace(frroDTO.getPassportIssuePlace());
				frroHistory.setPassportIssueDate(frroDTO.getPassportIssueDate());
				frroHistory.setPassportNo(frroDTO.getPassportNo());
				frroHistory.setPlaceOfVisaIssue(frroDTO.getPlaceOfVisaIssue());
				frroHistory.setPortOfArrival(frroDTO.getPortOfArrival());
				frroHistory.setPortOfDeparture(frroDTO.getPortOfDeparture());
				frroHistory.setRemarks(frroDTO.getRemarks());
				frroHistory.setReportedOn(frroDTO.getReportedOn());
				frroHistory.setResidentialPermitCopyDocument(frroDTO.getResidentialPermitCopyDocument());
				frroHistory.setRpExpiryDate(frroDTO.getRpExpiryDate());
				frroHistory.setRpIssueDate(frroDTO.getRpIssueDate());
				frroHistory.setRpNo(frroDTO.getRpNo());
				frroHistory.setStudentId(frroDTO.getStudentId());
				frroHistory.setTypeOfEntry(frroDTO.getTypeOfEntry());
				frroHistory.setVisaCopyDocument(frroDTO.getVisaCopyDocument());
				frroHistory.setVisaExpiryDate(frroDTO.getVisaExpiryDate());
				frroHistory.setVisaIssueDate(frroDTO.getVisaIssueDate());
				frroHistory.setVisaNo(frroDTO.getVisaNo());
				frroHistory.setVisaType(frroDTO.getVisaType());
				frroHistory.setCreated_by(frro.getCreated_by());
				frroHistory.setModified_by(jwtDetails.getUserName());
				frroHistoryRepository.save(frroHistory);

				
				frro.setAluEquivalenceDocument(frroDTO.getAluEquivalenceDocument());
				frro.setBirthPlace(frroDTO.getBirthPlace());
				frro.setFsis(frroDTO.getFsis());
				frro.setImmigrationDate(frroDTO.getImmigrationDate());
				frro.setIsReportedToIndia(frroDTO.getIsReportedToIndia());
				frro.setIssueBy(frroDTO.getIssueBy());
				frro.setNameAsPerPassport(frroDTO.getNameAsPerPassport());
				frro.setPassportCopyDocument(frroDTO.getPassportCopyDocument());
				frro.setPassportExpiryDate(frroDTO.getPassportExpiryDate());
				frro.setPassportIssuePlace(frroDTO.getPassportIssuePlace());
				frro.setPassportIssueDate(frroDTO.getPassportIssueDate());
				frro.setPassportNo(frroDTO.getPassportNo());
				frro.setPlaceOfVisaIssue(frroDTO.getPlaceOfVisaIssue());
				frro.setPortOfArrival(frroDTO.getPortOfArrival());
				frro.setPortOfDeparture(frroDTO.getPortOfDeparture());
				frro.setRemarks(frroDTO.getRemarks());
				frro.setReportedOn(frroDTO.getReportedOn());
				frro.setResidentialPermitCopyDocument(frroDTO.getResidentialPermitCopyDocument());
				frro.setRpExpiryDate(frroDTO.getRpExpiryDate());
				frro.setRpIssueDate(frroDTO.getRpIssueDate());
				frro.setRpNo(frroDTO.getRpNo());
				frro.setTypeOfEntry(frroDTO.getTypeOfEntry());
				frro.setVisaCopyDocument(frroDTO.getVisaCopyDocument());
				frro.setVisaExpiryDate(frroDTO.getVisaExpiryDate());
				frro.setVisaIssueDate(frroDTO.getVisaIssueDate());
				frro.setVisaNo(frroDTO.getVisaNo());
				frro.setVisaType(frroDTO.getVisaType());
				frro.setModified_by(jwtDetails.getUserName());
				frro.setVisaIssued(frroDTO.getVisaIssued());
				frro.setAffiliataion(frroDTO.getAffiliataion());
				frro.setRecognition(frroDTO.getRecognition());
				frroRepository.save(frro);

			
			}

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> getFrroHistory(Integer studentId) {
		try {

			List<FrroHistory> frroHistory = frroHistoryRepository.getFrroHistory(studentId);

			if (ObjectUtils.isNotEmpty(frroHistory)) {
				return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", frroHistory);

			}

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
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
			if (e.getStatusCode() == org.apache.http.HttpStatus.SC_NOT_FOUND) {
				throw new NoSuchFileException("File Not Found");
			}
			throw new AmazonClientException("", e);
		} catch (IOException | AmazonClientException ex) {
			throw new AmazonClientException("", ex);
		}
	}

	public ResponseEntity<Object> getFrroStudentList(String auid) {
		try {
			List<Map<String, Object>> studentDetails =null;
            
			if(StringUtils.isEmpty(auid)) {
				studentDetails = frroRepository.getFrroStudentList();	
			}else {
				studentDetails = frroRepository.getFrroStudentList(auid);			
			}
			
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", studentDetails);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		
	}
}
}
