package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
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
import com.au.dto.CopyFeeTemplateDto;
import com.au.dto.FeeTemplateHistoryRequest;
import com.au.dto.FeeTemplateRequest;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.FeeTemplate;
import com.au.model.FeeTemplateHistory;
import com.au.model.FeeTemplateSubAmount;
import com.au.repository.Academic_year_repository;
import com.au.repository.FeeAdmissionSubCategoryRepository;
import com.au.repository.FeeTemplateHistoryRepository;
import com.au.repository.FeeTemplateRepository;
import com.au.repository.FeeTemplateSubAmountRepository;
import com.au.repository.OtherFeeDetailsRepository;
import com.au.repository.ProgramSpecilizationRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class FeeTemplateService {

	private Logger logger = LoggerFactory.getLogger(FeeTemplateService.class);

	public static final String value = "FeeBucket";
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
	private FeeTemplateRepository ft_repo;

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private FeeTemplateHistoryRepository fth_repo;
	
	@Autowired
	private FeeAdmissionSubCategoryRepository fee_adm_sub_repo;
	
	@Autowired
	private Academic_year_repository ac_repo;
	
	@Autowired
	private ProgramSpecilizationRepository ps_repo;
	
	@Autowired
	private StudentDetailsRepository stu_repo;
	
	@Autowired
	private FeeTemplateSubAmountRepository ftsa_repo;
	
	@Autowired
	private OtherFeeDetailsRepository otherFeeDetailsRepository;

	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

	public List<FeeTemplate> listAll() {
		return ft_repo.findAll1();
	}

	public FeeTemplate saveFeeTemplate(FeeTemplate fee) {
		return ft_repo.save(fee);
	}

	public FeeTemplate get(Integer id) {
		return ft_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("FeeTemplate Not Found:" + id));
	}

	public void delete(Integer fee_template_id) {
		FeeTemplate ay = ft_repo.findById(fee_template_id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeTemplate Not Found:" + fee_template_id));
		ft_repo.updateFeetemplate(fee_template_id);
	}

	public void delete1(Integer fee_template_id) {
		FeeTemplate ay = ft_repo.findById(fee_template_id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeTemplate Not Found:" + fee_template_id));
		ft_repo.update(fee_template_id);
	}

	public void delete2(Integer fee_template_id) {
		FeeTemplate ay = ft_repo.findById(fee_template_id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeTemplate Not Found:" + fee_template_id));
		ft_repo.update1(fee_template_id);
	}
	
	 /* FOR fee_template_name CREATION
	  * 24BEM1MGTR  
	 * 2 letter academic year
	 * program short name (3 letter)
	 * number 
	 * fee_admission_category_short_name short name (3 letter)
	 * currency type 1st letter
		*/

	public List<FeeTemplate> getTemplateName(FeeTemplateRequest feetemplaterequest,String jwtToken) throws JsonParseException, JsonMappingException, IOException{

		ResponseHandler.toConvertCommaSeperatedIdsAsList(feetemplaterequest.getProgram_specialization_id()).stream()
				.forEach(ps -> {

					if (ft_repo.countFeeTemplate(feetemplaterequest.getAc_year_id(),
							feetemplaterequest.getFee_admission_category_id(),
							feetemplaterequest.getFee_admission_sub_category_id(), feetemplaterequest.getProgram_id(),
							ps.toString()) >= 1) {
						throw new RuntimeException(
								"Fee Template With Combination Of Academic Year, Admission Category,Admission Subcategory, Program And Specialization Already Exist");
					}
				});

		List<FeeTemplate> lt = new ArrayList<FeeTemplate>();
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		feetemplaterequest.setAc_year(ac_repo.getAcademicYear(feetemplaterequest.getAc_year_id()));
		feetemplaterequest.setProgram_specialization(ps_repo.getCommaSeperartedProgramSpecializationShortName(ResponseHandler.toConvertCommaSeperatedIdsAsList(feetemplaterequest.getProgram_specialization_id())));
		//feetemplaterequest.getFee_admission_sub_category_id().stream().forEach(a -> {
		FeeTemplate ft = new FeeTemplate();
		String feeTemp = feetemplaterequest.getAc_year().substring(2, 4);
		feeTemp += feetemplaterequest.getProgram_sht().length() > 3
				? feetemplaterequest.getProgram_sht().substring(0, 3)
						: feetemplaterequest.getProgram_sht();
		feeTemp += feetemplaterequest.getFee_admission_category().length() > 1
				? feetemplaterequest.getFee_admission_category().substring(0, 1)
						: feetemplaterequest.getFee_admission_category();
		feeTemp += (ft_repo.findById(feetemplaterequest.getAc_year_id(),
				feetemplaterequest.getFee_admission_category_id(), feetemplaterequest.getProgram_id()) + 1);
		feeTemp +=fee_adm_sub_repo.getFeeAdmissionSubCategoryShortName(feetemplaterequest.getFee_admission_sub_category_id());
		feeTemp += feetemplaterequest.getCurrency_short().length() > 1
				? feetemplaterequest.getCurrency_short().substring(2)
						: feetemplaterequest.getCurrency_short();

		ft.setFee_template_name(feeTemp);
		ft.setAc_year_id(feetemplaterequest.getAc_year_id());
		ft.setSchool_id(feetemplaterequest.getSchool_id());
		ft.setProgram_id(feetemplaterequest.getProgram_id());
		ft.setCurrency_type_id(feetemplaterequest.getCurrency_type_id());
		ft.setFee_admission_category_id(feetemplaterequest.getFee_admission_category_id());
		ft.setFee_admission_sub_category_id(feetemplaterequest.getFee_admission_sub_category_id());
		ft.setNationality(feetemplaterequest.getNationality());
		ft.setIs_saarc(feetemplaterequest.getIs_saarc());
		// ft.setProgram_specialization(feetemplaterequest.getProgram_specialization());
		ft.setProgram_specialization_id(feetemplaterequest.getProgram_specialization_id());
		ft.setProgram_type_id(feetemplaterequest.getProgram_type_id());
		ft.setIs_nri(feetemplaterequest.getIs_nri());
		ft.setIs_paid_at_board(feetemplaterequest.getIs_paid_at_board());
		ft.setApproved_by(feetemplaterequest.getApproved_by());
		ft.setCreated_by(jwtDetails.getUserId());
		//ft.setModified_by(feetemplaterequest.getModified_by());
		ft.setCreated_username(jwtDetails.getUserName());
		ft.setProgram_specialization(feetemplaterequest.getProgram_specialization());
		ft.setRemarks(feetemplaterequest.getRemarks());
		ft.setActive(feetemplaterequest.getActive());
		ft.setLat_year_sem(feetemplaterequest.getLat_year_sem());
		ft.setUniform_status(feetemplaterequest.getUniform_status());
		ft.setLaptop_status(feetemplaterequest.getLaptop_status());
		saveFeeTemplate(ft);
		lt.add(ft);
		//});
		return lt;

	}

	public Integer countRecords(Integer id1, Integer id2, Integer id3) {
		return ft_repo.findById(id1, id2, id3);
	}

	public FeeTemplate saveFeeTemplate1(FeeTemplate academic) {
		return ft_repo.save(academic);
	}

	/*
	 * public List<HashMap<String, Object>> findById(List<Integer> fee_template_id)
	 * { return ft_repo.fetchFeeTemplateDetails(fee_template_id); }
	 * 
	 */
	public List<HashMap<String, Object>> findByFeeAdmissionSubCategory(Integer fee_admission_sub_category_id) {
		return ft_repo.fetchFeeTemplateDetail(fee_admission_sub_category_id);
	}

	public List<HashMap<String, Object>> listAll1() {
		return ft_repo.edittemplate();
	}

	public List<HashMap<String, Object>> findByFeeTemplate1(Integer fee_template_id) {
		return ft_repo.editfeetemplate(fee_template_id);
	}

	public List<Map<String, Object>> findByFeeTemplate3(Integer ac_year_id, Integer school_id, Integer program_id,
			String program_specialization_id, Integer fee_admission_category_id,
			Integer fee_admission_sub_category_id, Boolean is_nri) {
		
		List<Map<String, Object>> feeTemplateNameResponse = new ArrayList<Map<String, Object>>();
				
		 String pid = "%" + program_specialization_id + "%";
		 List<Map<String, Object>> getfeeTemplateName = ft_repo.getfeeTemplateName(ac_year_id, school_id, program_id, pid,
				fee_admission_category_id, fee_admission_sub_category_id, is_nri);
		 getfeeTemplateName.stream().forEach(fee -> {
			 System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL "+fee.get("program_specialization_id").toString());
			 
			 String programSpecializationIdStr = "" + program_specialization_id;
			 String[] specializationIds = fee.get("program_specialization_id").toString().split(",");

			 if (Arrays.asList(specializationIds).contains(programSpecializationIdStr)) {
			     feeTemplateNameResponse.add(fee);
			 }
			 
//			 String programSpecializationIdStr = "" + program_specialization_id;
//			 String programSpecializationIdRegex = "(^" + programSpecializationIdStr + "$)|(^" + programSpecializationIdStr + ",)|(" + programSpecializationIdStr + "$)|(" + programSpecializationIdStr + ",)";
//
//			 // Compile the regex pattern
//			 Pattern pattern = Pattern.compile(programSpecializationIdRegex);
//
//			 if (pattern.matcher(fee.get("program_specialization_id").toString()).find()) {
//			     feeTemplateNameResponse.add(fee);
//			 }
//			 if(
//					 fee.get("program_specialization_id").toString().contains(""+program_specialization_id+" ") ||
//					 fee.get("program_specialization_id").toString().contains(""+program_specialization_id+"") ||
//					 fee.get("program_specialization_id").toString().contains(","+program_specialization_id+",")|| 
//					 fee.get("program_specialization_id").toString().contains(","+program_specialization_id+"") ||
//					 fee.get("program_specialization_id").toString().contains(""+program_specialization_id+",") )
//				 feeTemplateNameResponse.add(fee);
	 });
		 return feeTemplateNameResponse;
	}

//	public List<HashMap<String, Object>> listAll2() {
//		return ft_repo.fetchFeeTemplateDetail();
//	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable, Object keyword) {
		//return ft_repo.fetchFeeTemplateDetail();
		Page<Object> response1 = ft_repo.fetchFeeTemplateDetail1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK , response1);
	}
	
	public ResponseEntity<Object> listAll3(Pageable pageable) {
		//return ft_repo.fetchFeeTemplateDetail2();
		Page<Object> response = ft_repo.fetchFeeTemplateDetail2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public ResponseEntity<Object> listAllAcYearId1(Pageable pageable, Object keyword, Integer ac_year_id, Integer school_id, Integer fee_admission_category_id) {
		//return ft_repo.fetchFeeTemplateDetail();
		Page<Object> response1 = ft_repo.listAllAcYearId1(pageable, keyword, ac_year_id, school_id, fee_admission_category_id );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK , response1);
	}
	
	public ResponseEntity<Object> listAllAcYearId2(Pageable pageable, Integer ac_year_id, Integer school_id, Integer fee_admission_category_id) {
		//return ft_repo.fetchFeeTemplateDetail2();
		Page<Object> response = ft_repo.listAllAcYearId2(pageable, ac_year_id, school_id, fee_admission_category_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}


	public List<HashMap<String, Object>> findByFeeTemplate4(Integer ac_year_id, Integer fee_admission_category_id,
			Integer fee_admission_sub_category_id, String nationality, Integer program_type_id,
			Integer currency_type_id, Integer school_id, Integer program_id) {
		return ft_repo.getFeeTemplateId(ac_year_id, fee_admission_category_id, fee_admission_sub_category_id,
				nationality, program_type_id, currency_type_id, school_id, program_id);
	}
	/*
	 * public FeeTemplate saveFeeTemplates(FeeTemplateHistoryRequest
	 * feetemplate,@RequestHeader("Authorization") String jwtToken) throws
	 * JsonParseException, JsonMappingException, IOException { FeeTemplate
	 * feetemplate1 =
	 * ft_repo.findByfee_template_id(feetemplate.getFtt().getFee_template_id());
	 * feetemplate1.setFee_template_name(feetemplate.getFtt().getFee_template_name()
	 * ); feetemplate1.setAc_year_id(feetemplate.getFtt().getAc_year_id());
	 * feetemplate1.setFee_admission_category_id(feetemplate.getFtt().
	 * getFee_admission_category_id());
	 * feetemplate1.setFee_admission_sub_category_id(feetemplate.getFtt().
	 * getFee_admission_sub_category_id());
	 * feetemplate1.setNationality(feetemplate.getFtt().getNationality());
	 * feetemplate1.setIs_nri(feetemplate.getFtt().getIs_nri());
	 * feetemplate1.setProgram_type_id(feetemplate.getFtt().getProgram_type_id());
	 * feetemplate1.setCurrency_type_id(feetemplate.getFtt().getCurrency_type_id());
	 * feetemplate1.setRemarks(feetemplate.getFtt().getRemarks());
	 * feetemplate1.setSchool_id(feetemplate.getFtt().getSchool_id());
	 * feetemplate1.setProgram_id(feetemplate.getFtt().getProgram_id());
	 * feetemplate1.setProgram_specialization_id(feetemplate.getFtt().
	 * getProgram_specialization_id());
	 * feetemplate1.setProgram_specialization(feetemplate.getFtt().
	 * getProgram_specialization());
	 * feetemplate1.setActive(feetemplate.getFtt().getActive()); return
	 * saveFeeTemplate(feetemplate.getFtt()); }
	 */

	public FeeTemplate saveFeeTemplate(FeeTemplateHistoryRequest feetemplate,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		FeeTemplateHistory feeTemplateHistory = new FeeTemplateHistory();
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		FeeTemplate feetemplate1 = ft_repo.findByfee_template_id(feetemplate.getFtt().getFee_template_id());
		feetemplate1.setFee_template_name(feetemplate.getFtt().getFee_template_name());
		feetemplate1.setAc_year_id(feetemplate.getFtt().getAc_year_id());
		feetemplate1.setFee_admission_category_id(feetemplate.getFtt().getFee_admission_category_id());
		feetemplate1.setFee_admission_sub_category_id(feetemplate.getFtt().getFee_admission_sub_category_id());
		feetemplate1.setNationality(feetemplate.getFtt().getNationality());
		feetemplate1.setIs_nri(feetemplate.getFtt().getIs_nri());
		feetemplate1.setProgram_type_id(feetemplate.getFtt().getProgram_type_id());
		feetemplate1.setCurrency_type_id(feetemplate.getFtt().getCurrency_type_id());
		feetemplate1.setRemarks(feetemplate.getFtt().getRemarks());
		feetemplate1.setSchool_id(feetemplate.getFtt().getSchool_id());
		feetemplate1.setProgram_id(feetemplate.getFtt().getProgram_id());
		feetemplate1.setProgram_specialization_id(feetemplate.getFtt().getProgram_specialization_id());
		feetemplate1.setProgram_specialization(ps_repo.getCommaSeperartedProgramSpecializationShortName(ResponseHandler.toConvertCommaSeperatedIdsAsList(feetemplate.getFtt().getProgram_specialization_id())));
		feetemplate1.setActive(feetemplate.getFtt().getActive());
		feetemplate1.setIs_saarc(feetemplate.getFtt().getIs_saarc());
		feeTemplateHistory.setFee_template_id(feetemplate.getFtt().getFee_template_id());
		feeTemplateHistory.setFee_template_name(feetemplate.getFth().getFee_template_name());
		feeTemplateHistory.setAc_year_id(feetemplate.getFth().getAc_year_id());
		feeTemplateHistory.setFee_admission_category_id(feetemplate.getFth().getFee_admission_category_id());
		feeTemplateHistory.setFee_admission_sub_category_id(feetemplate.getFth().getFee_admission_sub_category_id());
		feeTemplateHistory.setNationality(feetemplate.getFth().getNationality());
		feeTemplateHistory.setIs_nri(feetemplate.getFth().getIs_nri());
		feeTemplateHistory.setProgram_type_id(feetemplate.getFth().getProgram_type_id());
		feeTemplateHistory.setCurrency_type_id(feetemplate.getFth().getCurrency_type_id());
		feeTemplateHistory.setRemarks(feetemplate.getFth().getRemarks());
		feeTemplateHistory.setSchool_id(feetemplate.getFth().getSchool_id());
		feeTemplateHistory.setProgram_id(feetemplate.getFth().getProgram_id());
		feeTemplateHistory.setProgram_specialization_id(feetemplate.getFth().getProgram_specialization_id());
		feeTemplateHistory.setProgram_specialization(ps_repo.getCommaSeperartedProgramSpecializationShortName(ResponseHandler.toConvertCommaSeperatedIdsAsList(feetemplate.getFth().getProgram_specialization_id())));
		feeTemplateHistory.setCreated_by(jwtDetails.getUserId());
		feeTemplateHistory.setActive(feetemplate.getFth().getActive());
		feeTemplateHistory.setIs_saarc(feetemplate.getFth().getIs_saarc());
		feeTemplateHistory.setCreated_username(jwtDetails.getUserName());
		fth_repo.save(feeTemplateHistory);
		return saveFeeTemplate(feetemplate1);
	}

	public FeeTemplate saveFeeTemplates(@Valid FeeTemplate feetemplate, String jwtToken) {
		FeeTemplate ft = ft_repo.save(feetemplate);
		return ft;
	}

	public void uploadFile(MultipartFile multipartFile, Integer fee_template_id) {
		FeeTemplate feeTemplate = new FeeTemplate();
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String t1 = feeTemplate.setFee_template_path(LocalDate.now() + "/" + fee_template_id + "/" + fileName);
			uploadFileTos3bucket(fileName, file, fee_template_id);
			file.delete();
			ft_repo.updatePath(fee_template_id, t1);
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

	private void uploadFileTos3bucket(String fileName, File file, Integer job) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + job + "/" + fileName; // file.getName()
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));
		// .withCannedAcl(CannedAccessControlList.PublicRead);
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
	
	public List<HashMap<String, Object>> findStudentDetailsByFeeTemplateId(Integer fee_template_id) {
		return stu_repo.findStudentDetailsByFeeTemplateId(fee_template_id);
	}
	
	public List<Map<String, Object>> fetchCountOfStudentDetailsByFeeTemplateId() {
		return stu_repo.fetchCountOfStudentDetailsByFeeTemplateId();
	}
	List<FeeTemplateSubAmount> fee_template_sub_amount;
	Integer fee_template_ids;
	public List<FeeTemplate>  copyFeeTemplates(CopyFeeTemplateDto cftd,String jwtToken) throws JsonParseException, JsonMappingException, IOException{
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		List<String> feeTemplateName = ft_repo.getFeeTemplateName(cftd.getOld_ac_year_id());
		System.out.println("00000000000000000000000000000000000000000000 " +feeTemplateName);
		System.out.println("0000000000000000001111111111110000000000000000000000 " +ft_repo.countFeeTemplate(cftd.getNew_ac_year_id(),feeTemplateName) );
//		if(ft_repo.countFeeTemplate(cftd.getOld_ac_year_id(),feeTemplateName) >=1)	{
//			throw new RuntimeException("Fee Template With Combination Of Academic Year And Template Name Already Exist");
//	} else {
		
//		List<Integer> fee_template_ids;
		List<FeeTemplate> fee_template;
//		List<FeeTemplateSubAmount> fee_template_sub_amount;
		if(cftd.getFee_template_id()==null) {
			System.out.println("lllllllllllllllllllllll" +cftd.getFee_template_id());
//			fee_template_ids=ft_repo.fetchFeeTemplateIdsonAcademicYear(cftd.getOld_ac_year_id());
			System.out.println("111111111111111111111!!!!!!!!!!!!!!!!!!!!!!!!!!! " +cftd.getFee_template_id());
			System.out.println("111111111111111111111!!!!!!!!!!!!!!!!!!!!!!!!!!! " +cftd.getFee_template_id());
			fee_template=ft_repo.fetchFeeTemplateDetails(cftd.getFee_template_id());
			fee_template_sub_amount=ftsa_repo.fetchFeeTemplateSubAmountDetails(cftd.getFee_template_id());
		}else {
//			fee_template_ids.add(cftd.getFee_template_id());
			System.out.println("111111111111111111111!!!!!!!!!!!!!!!!!!!!!!!!!!! " +cftd.getFee_template_id());
			fee_template=ft_repo.fetchFeeTemplateDetails(cftd.getFee_template_id());
			fee_template_sub_amount=ftsa_repo.fetchFeeTemplateSubAmountDetails(cftd.getFee_template_id());
		}
		
		fee_template.stream().forEach(ft -> {
			FeeTemplate ft1=new FeeTemplate();
//			List<FeeTemplateSubAmount> fee_template_sub_amount_on_id=fee_template_sub_amount.stream().filter(ftsa -> ftsa.getFee_template_id() == ft.getFee_template_id()).collect(Collectors.toList());
			ft1.setAc_year_id(cftd.getNew_ac_year_id());
			ft1.setFee_template_name(cftd.getCurrent_year().toString().substring(2,4) + ft.getFee_template_name().substring(2));
			ft1.setCreated_by(jwtDetails.getUserId());
			ft1.setCreated_username(jwtDetails.getUserName());
			ft1.setSchool_id(ft.getSchool_id());
			ft1.setProgram_id(ft.getProgram_id());
			ft1.setProgram_specialization_id(ft.getProgram_specialization_id());
			ft1.setCurrency_type_id(ft.getCurrency_type_id());
			ft1.setFee_admission_category_id(ft.getFee_admission_category_id());
			ft1.setFee_admission_sub_category_id(ft.getFee_admission_sub_category_id());
			ft1.setIs_paid_at_board(ft.getIs_paid_at_board());
			ft1.setNationality(ft.getNationality());
			ft1.setIs_nri(ft.getIs_nri());
			ft1.setProgram_type_id(ft.getProgram_type_id());
			ft1.setApproved_by(ft.getApproved_by());
			ft1.setApproved_status(ft.getApproved_status());
			ft1.setApproved_date(ft.getApproved_date());
			ft1.setFee_year1_amt(ft.getFee_year1_amt());
			ft1.setFee_year2_amt(ft.getFee_year2_amt());
			ft1.setFee_year3_amt(ft.getFee_year3_amt());
			ft1.setFee_year4_amt(ft.getFee_year4_amt());
			ft1.setFee_year5_amt(ft.getFee_year5_amt());
			ft1.setFee_year6_amt(ft.getFee_year6_amt());
			ft1.setFee_year7_amt(ft.getFee_year7_amt());
			ft1.setFee_year8_amt(ft.getFee_year8_amt());
			ft1.setFee_year9_amt(ft.getFee_year9_amt());
			ft1.setFee_year10_amt(ft.getFee_year10_amt());
			ft1.setFee_year11_amt(ft.getFee_year11_amt());
			ft1.setFee_year12_amt(ft.getFee_year12_amt());
			ft1.setFee_year_total_amount(ft.getFee_year_total_amount());
			ft1.setActive(ft.getActive());
			ft1.setRemarks(ft.getRemarks());
			ft1.setProgram_specialization(ft.getProgram_specialization());
			ft1.setFee_template_path(ft.getFee_template_path());
			ft1.setIs_saarc(ft.getIs_saarc());
			
			FeeTemplate new_fee_teplate=ft_repo.save(ft1);
			List<FeeTemplateSubAmount> fee_template_sub_amount_on_id= ftsa_repo.getFeeTemplateIds(cftd.getFee_template_id());
			
			fee_template_sub_amount_on_id.stream().forEach(new_ftsa -> {
				FeeTemplateSubAmount ftsa1=new FeeTemplateSubAmount();
				ftsa1.setFee_template_id(new_fee_teplate.getFee_template_id());
				ftsa1.setCreated_by(jwtDetails.getUserId());
				ftsa1.setCreated_username(jwtDetails.getUserName());
				ftsa1.setVoucher_head_id(new_ftsa.getvoucher_head_new_id());
				ftsa1.setBoard_unique_id(new_ftsa.getBoard_unique_id());
				ftsa1.setAlias_id(new_ftsa.getAlias_id());
				ftsa1.setYear1_amt(new_ftsa.getYear1_amt());
				ftsa1.setYear2_amt(new_ftsa.getYear2_amt());
				ftsa1.setYear3_amt(new_ftsa.getYear3_amt());
				ftsa1.setYear4_amt(new_ftsa.getYear4_amt());
				ftsa1.setYear5_amt(new_ftsa.getYear5_amt());
				ftsa1.setYear6_amt(new_ftsa.getYear6_amt());
				ftsa1.setYear7_amt(new_ftsa.getYear7_amt());
				ftsa1.setYear8_amt(new_ftsa.getYear8_amt());
				ftsa1.setYear9_amt(new_ftsa.getYear9_amt());
				ftsa1.setYear10_amt(new_ftsa.getYear10_amt());
				ftsa1.setYear11_amt(new_ftsa.getYear11_amt());
				ftsa1.setYear12_amt(new_ftsa.getYear12_amt());
				ftsa1.setTotal_amt(new_ftsa.getTotal_amt());
				ftsa1.setReceive_for_all_year(new_ftsa.getReceive_for_all_year());
				ftsa1.setActive(new_ftsa.getActive());
				ftsa1.setRemarks(new_ftsa.getRemarks());
				ftsa_repo.save(ftsa1);
			});
			
		});
		fee_template_sub_amount.clear();
//		fee_template_ids.clear();
		return fee_template;
	}
// }

	public Map<String, Object> getFeeTemplateDetailsData(Integer fee_template_id) {
		return ft_repo.getFeeTemplateDetailsData(fee_template_id);
	}


	public Map<Integer, List<Map<String, Object>>> FetchFeeTemplateDetailsByFeeTemplateId(List<Integer> fee_template_ids) {
	    // Initialize a result map to store details for each fee_template_id
	    Map<Integer, List<Map<String, Object>>> result = new HashMap<>();

	    // Loop through each fee_template_id in the provided list
	    for (Integer fee_template_id : fee_template_ids) {
	        // Fetch fee template details for the current fee_template_id
	        Map<String, Object> feeDetailsData = ft_repo.FetchFeeTemplateDetailsByFeeTemplateId(fee_template_id);

	        // Fetch associated sub amounts and add-on details
	        List<Map<String, Object>> feeTemplateSubAmount = ftsa_repo.fetchFeeTempalteSubAmount(fee_template_id);
	        List<Map<String, Object>> fetchAddOnDetails = otherFeeDetailsRepository.fetchAddOnDetails(fee_template_id);

	        // Retrieve program specialization IDs, academic year, and school ID
	        List<Integer> programSpecializationIds = ResponseHandler.toConvertCommaSeperatedIdsAsList((String) feeDetailsData.get("program_specialization_id"));
	        Integer academicYear = (Integer) feeDetailsData.get("ac_year_id");
	        Integer schoolId = (Integer) feeDetailsData.get("school_id");

	        // Create a list to store the fee template details for the current fee_template_id
	        List<Map<String, Object>> feeTemplateDetailsList = new ArrayList<>();

	        // Create a map to store details for the current fee_template_id
	        Map<String, Object> feeTemplateDetails = new HashMap<>();
	        feeTemplateDetails.put("FeeTemplate", feeDetailsData);
	        feeTemplateDetails.put("FeeTemplateSubAmount", feeTemplateSubAmount);
	        feeTemplateDetails.put("AddOn", fetchAddOnDetails);

	        // New format for uniform details
	        Map<String, List<Map<String, Object>>> uniformDetailsMap = new HashMap<>();

	        // Fetch uniform details for each program specialization ID
	        programSpecializationIds.forEach(psi -> {
	            // Fetch uniform details
	            List<Map<String, Object>> fetchUniformDetails = otherFeeDetailsRepository.fetchUniformDetails(psi, academicYear, schoolId);
	            
	            // Check if the list is not empty
	            if (!fetchUniformDetails.isEmpty()) {
	                // Get program specialization short name from the first uniform detail
	                String programSpecializationShortName = (String) fetchUniformDetails.get(0).get("program_specialization_short_name");
	                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + programSpecializationShortName);
	                
	                // Initialize the list for this program specialization short name if not already present
	                uniformDetailsMap.putIfAbsent(programSpecializationShortName, new ArrayList<>());

	                // Add all fetched uniform details to the corresponding list in the map
	                uniformDetailsMap.get(programSpecializationShortName).addAll(fetchUniformDetails);
	            } else {
	                // Log when no uniform details are found for the current specialization ID
	                System.out.println("No uniform details found for program specialization ID: " + psi);
	            }
	        });

	        // Add the uniform details map to the main feeTemplateDetails map
	        feeTemplateDetails.put("Uniform", uniformDetailsMap);

	        // Add the populated feeTemplateDetails to the list for the current fee_template_id
	        feeTemplateDetailsList.add(feeTemplateDetails);

	        // Add the feeTemplateDetailsList to the result map under the current fee_template_id
	        result.put(fee_template_id, feeTemplateDetailsList);
	    }

	    return result; // Return the compiled result map
	}


	public ResponseEntity<Object> getAllFeeTemplate() {

		List<String> feeTemplates = ft_repo.getAllActiveFeeTemplate();
		return ResponseHandler.generateResponse(true, HttpStatus.OK, feeTemplates);
	}

	public List<HashMap<String, Object>> findStudentDetailsByFeeTemplateIds(List<Integer> fee_template_ids) {
		return stu_repo.findStudentDetailsByFeeTemplateIds(fee_template_ids);
	}
}