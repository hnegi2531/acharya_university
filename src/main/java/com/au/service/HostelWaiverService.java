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
import java.util.Optional;

import org.springframework.http.HttpStatus;
import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.HostelWaiver;
import com.au.model.Student_Details;
import com.au.repository.FeeReceiptRepository;
import com.au.repository.FeeTemplateSubAmountRepository;
import com.au.repository.HostelBedAssignmentRepository;
import com.au.repository.HostelDueRepository;
import com.au.repository.HostelWaiverRepository;
import com.au.repository.ScholarshipApprovalStatusRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.StudentPaymentHistoryRepository;
import com.au.response.ResponseHandler;

@Service
public class HostelWaiverService {
	
	private Logger logger = LoggerFactory.getLogger(VendorAttachmentService.class);
	
	public static final String value = "HostelWaiverBucket";
	
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
	private HostelWaiverRepository hostelWaiverRepository;
	
	@Autowired
	private StudentPaymentHistoryRepository stu_pay_hs_repo;
	
	@Autowired
	private StudentDetailsRepository stu_repo;
	
	@Autowired
	private FeeTemplateSubAmountRepository ftsa_repo;
	
	@Autowired
	private FeeReceiptRepository frc_repo;
	
	@Autowired
	private ScholarshipApprovalStatusRepository s_repo;
	
	@Autowired
	private HostelBedAssignmentRepository hostelBedAssignmentRepository;
	
	@Autowired
	private HostelDueRepository hostelDueRepository;
	
	private final ModelMapper modelMapper = new ModelMapper();
	
	
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}
	
	public HostelWaiver saveAttachments(@Valid HostelWaiver hw_attachments) throws IOException {
		
		return hostelWaiverRepository.save(hw_attachments);
	}
	
public HostelWaiver uploadFile(MultipartFile multipartFile, Integer hostel_waiver_id) throws IOException{
		
		Optional<HostelWaiver> hostelWaiver = hostelWaiverRepository.findById(hostel_waiver_id);
		HostelWaiver hw_attachment=hostelWaiver.get();
		try
		{
			logger.debug("Message For HostelWaiver--------------");
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			hw_attachment.setHostel_waiver_id(hostel_waiver_id);
			logger.debug("Message For HostelWaiver", file);
			System.out.println(hostel_waiver_id);
			hw_attachment.setHw_attachment_file_name(fileName);
			hw_attachment.setHw_attachment_path(LocalDate.now() + "/" + hostel_waiver_id + "/" + fileName);
			hw_attachment.setHw_attachement_type(
					endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/" + hostel_waiver_id + "/" + fileName);
			uploadFileToS3Bucket(fileName, file, hostel_waiver_id);
			logger.debug("Message For Attachment", file);
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
		return hostelWaiverRepository.save(hw_attachment);
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
	
	private void uploadFileToS3Bucket(String fileName, File file, Integer hostel_waiver_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + hostel_waiver_id + "/" + fileName; 
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
			if (e.getStatusCode() == org.apache.http.HttpStatus.SC_NOT_FOUND) {
				throw new NoSuchFileException("File Not Found");
			}
			throw new AmazonClientException("", e);
		} catch (IOException | AmazonClientException ex) {
			throw new AmazonClientException("", ex);
		}
	}
	
	public HostelWaiver get1(Integer hostel_waiver_id) {
		if(hostel_waiver_id.equals(0)) {
			throw new RuntimeException("Oops Exception raised....");
		}
		return hostelWaiverRepository.findById1(hostel_waiver_id);
	}
	
	public HostelWaiver saveHostelWaiver(HostelWaiver Waiver) {
		HostelWaiver hostelWaiver = hostelWaiverRepository
				.hostelWaiverByAcademicYearAndStudentId(Waiver.getAc_year_id(), Waiver.getStudent_id(),Waiver.getType());
		if (ObjectUtils.isNotEmpty(hostelWaiver)) {
			
			hostelWaiver.setModified_by(Waiver.getCreated_by());
			hostelWaiver.setModified_username(Waiver.getCreated_username());
			hostelWaiver.setPaid_amount(Waiver.getPaid_amount());
			hostelWaiver.setTotal_amount(Waiver.getTotal_amount());
			hostelWaiver.setRemarks(Waiver.getRemarks());
	        hostelWaiver.setApprove_status(true);

			hostelWaiver= hostelWaiverRepository.save(hostelWaiver);
			if(ObjectUtils.isNotEmpty(hostelWaiver.getStudent_id()) && ObjectUtils.isNotEmpty(hostelWaiver.getAc_year_id())) {
				hostelDueRepository.hostelDueProcedureByStudentIdAndAcYearId(hostelWaiver.getStudent_id(), hostelWaiver.getAc_year_id());
			}else {
				throw new RuntimeException("Academic Year And Student Id is not Present");
			}
			return hostelWaiver;
		} else {
			Integer bedAssignmentId = hostelBedAssignmentRepository
					.getHostelBedAssignmentDetails(Waiver.getAc_year_id(), Waiver.getStudent_id());
			Integer bed_id = hostelBedAssignmentRepository.getHostelBedAssignmentData(Waiver.getAc_year_id(),
					Waiver.getStudent_id());
			Waiver.setHostel_bed_assignment_id(bedAssignmentId);
			Waiver.setHostel_bed_id(bed_id);
			Waiver.setApprove_status(true);
			hostelWaiver=hostelWaiverRepository.save(Waiver);
			hostelDueRepository.hostelDueProcedureByStudentIdAndAcYearId(Waiver.getStudent_id(),Waiver.getAc_year_id());
			return hostelWaiver;
		}
	}
	
	public List<HostelWaiver> findAll1() {
		return hostelWaiverRepository.findAll1();
	}
	
	public List<HostelWaiver> findAll() {
		return hostelWaiverRepository.findAll();
	}
	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> roles_filtered_response = hostelWaiverRepository.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> roles_sorted_response = hostelWaiverRepository.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}
	
	
	public HostelWaiver get(Integer hostel_waiver_id) {
		return hostelWaiverRepository.findById(hostel_waiver_id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelWaiver Not Found:" +hostel_waiver_id));
	}
	
	public HostelWaiver saveUpdateHostelWaiver(HostelWaiver waiver) {
		HostelWaiver hostelWaiver= hostelWaiverRepository.save(waiver);
		if(ObjectUtils.isNotEmpty(hostelWaiver.getStudent_id()) && ObjectUtils.isNotEmpty(hostelWaiver.getAc_year_id())) {
			hostelDueRepository.hostelDueProcedureByStudentIdAndAcYearId(hostelWaiver.getStudent_id(), hostelWaiver.getAc_year_id());
		}else {
			throw new RuntimeException("Academic Year And Student Id is not Present");
		}
		return waiver;
	}
	
	public void delete(Integer hostel_waiver_id) {
		hostelWaiverRepository.findById(hostel_waiver_id)
		.orElseThrow(() -> new ResourceNotFoundException("Hostel Waiver Not Found:" +hostel_waiver_id));
		hostelWaiverRepository.update(hostel_waiver_id);
	}
	
	public void delete1(Integer hostel_waiver_id) {
		hostelWaiverRepository.findById(hostel_waiver_id)
		.orElseThrow(() -> new ResourceNotFoundException("Hostel Waiver Not Found:" +hostel_waiver_id));
		hostelWaiverRepository.update1(hostel_waiver_id);
	}
	
	public HashMap<String, Object> getAllDataOfFeeReceiptForFormating1(Integer student_id, Integer ac_year_id){
		
		HashMap<String, Object> formatedRersponse=new HashMap<>();
		HashMap<String, Object> dueAmount=new HashMap<>();
		Student_Details st=stu_repo.findById(student_id).orElseThrow(()-> new ResourceNotFoundException("student_Details Not Found:" + student_id));
		List<HashMap<String, Object>> stu1=stu_repo.studentDetailsForFeeReceipt1(student_id,ac_year_id);
		List<HashMap<String, Object>> fts1=ftsa_repo.feeTemplateSubAmountForFeeReceipt(st.getFee_template_id());
		
		List<Map<String, Object>> fts2=ftsa_repo.feeTemplateSubAmountForFeeReceiptOnPaidAtBoard(st.getFee_template_id());
		System.out.println("(((((((((((((((((((((((((( :- " + fts2.get(0).get("year1_amt"));
		List<HashMap<String, Object>> fee_rec1=frc_repo.getAllDataOfFeeReceiptForFormating(student_id);
		List<HashMap<String, Object>> sas1=s_repo.scholarshipApprovalStatusForFeeReceipt(student_id);
		formatedRersponse.put("Student_info", stu1);
		formatedRersponse.put("fee_template_sub_amount_info", fts1);
		formatedRersponse.put("fee_receipt_student_pay_his", fee_rec1);
		formatedRersponse.put("scholarship_approval_amount", sas1);
		//Float year1_amount=(Float) sas1.get(0).get("year1_amount");
		//Double paid_amount=(Double) fee_rec1.get(0).get("paid_amount");
		//Integer year1_amt=(Integer) fts1.get(0).get("year1_amt");
		//formatedRersponse.put("due_Amt", year1_amt-year1_amount- paid_amount);
		Integer y=(Integer)stu1.get(0).get("number_of_years");
		System.out.println("------------------" + y);
		if(((String)stu1.get(0).get("program_type_name")).equalsIgnoreCase("YEARLY")) {
			for(int i=1;i<=y;i++) {
				
				if(((boolean)fts1.get(0).get("is_paid_at_board"))==true) {
						if(sas1.size()==0) {
							 System.out.println("***************************"  + i ); 
							Integer paidamount=0;//stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i);
							  System.out.println("***************************" + paidamount + i ); 
							Integer x=((Number)fts2.get(0).get("year"+i+"_amt")).intValue() -0 /*stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i)*/ ;
							dueAmount.put("year"+i+"_due_Amt",(((Number)fts2.get(0).get("year"+i+"_amt")).intValue() - 0/*stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i)*/  ));
							System.out.println("IF paid at booard true and scholarship zero Statement program Type Yearly if =================================================" + x);
							System.out.println("IF paid at booard true and scholarship zero Statement program Type Yearly if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{" + paidamount);
							} else {
								 Integer paidamount=0;//stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i);
								   
								Float x=((Number)fts2.get(0).get("year"+i+"_amt")).intValue() - 0/*stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i)*/ - (Float) sas1.get(0).get("year"+i+"_amount");
								dueAmount.put("year"+i+"_due_Amt",(((Number)fts2.get(0).get("year"+i+"_amt")).intValue() - 0/*stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i)*/ - (Float) sas1.get(0).get("year"+i+"_amount") ));
								System.out.println("IF paid at booard true schoolarship size not zero else  program Type Yearly if =================================================" + x);
								System.out.println("IF paid at booard true schoolarship size not zero schoolarship else  Statement program Type Yearly if{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{" + paidamount);
								}
					
					}else {
						if(sas1.size()==0) {
							Integer paidamount=0;//stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i);
							System.out.println("Else Statement program Type Yearly{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{" + paidamount +   i);
							Integer x=(Integer)fts1.get(0).get("year"+i+"_amt") - 0/*stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i)*/;
							dueAmount.put("year"+i+"_due_Amt",((Integer) fts1.get(0).get("year"+i+"_amt") -0/* stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i)*/));
							System.out.println("IF paid at booard false and scholarship zero if Statement program Type Yearly=================================================" + x);
							System.out.println("IF paid at booard false and scholarship zero if Statement program Type Yearly{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{" + paidamount);
						}else {
							Integer paidamount=0;//stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i);
							System.out.println("2nd Else Statement program Type Yearly{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{" + paidamount +   i);
							Float x=(Integer)fts1.get(0).get("year"+i+"_amt") - 0/*stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i)*/ - (Float) sas1.get(0).get("year"+i+"_amount");
							dueAmount.put("year"+i+"_due_Amt",((Integer) fts1.get(0).get("year"+i+"_amt") - 0/*stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i)*/ - (Float) sas1.get(0).get("year"+i+"_amount") ));
							System.out.println("IF paid at booard false and scholarship non zero if Statement program Type Yearly if =================================================" + x);
							System.out.println("IF paid at booard false and scholarship non zero if Statement program Type Yearly if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{" + paidamount);
						}
					}
			}
		}else if(((String)stu1.get(0).get("program_type_name")).equalsIgnoreCase("SEMESTER")){
			for(int i=1;i<=(Integer)stu1.get(0).get("number_of_semester");i++) {
				//int z=i-1;
				if(((boolean)fts1.get(0).get("is_paid_at_board"))==true) {
					if(sas1.size()==0) {
						Integer paidamount=0;//stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i);
						dueAmount.put("year"+i+"_due_Amt",(((Number)fts2.get(0).get("year"+i+"_amt")).intValue()- 0/*stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i)*/ ));
						System.out.println("IF paid at booard true and scholarship zero Statement program Type semster if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{" + paidamount);
						
					}else {
						Integer paidamount=0;//stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i);
						dueAmount.put("year"+i+"_due_Amt",(((Number)fts2.get(0).get("year"+i+"_amt")).intValue()-0/* stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i)*/ - (Float) sas1.get(0).get("year"+i+"_amount")));
						System.out.println("IF paid at booard true schoolarship size not zero else  program Type semster if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{" + paidamount);
					}
				}else {
					if(sas1.size()==0) {
						Integer paidamount=0;//stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i);
						dueAmount.put("year"+i+"_due_Amt",((Integer) fts1.get(0).get("year"+i+"_amt") -0/* stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i)*/ ));
						System.out.println("IF paid at booard false schoolarship size  zero else  program Type semster if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{" + paidamount);
					} else {
						Integer paidamount=0;//stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i);
						dueAmount.put("year"+i+"_due_Amt",((Integer) fts1.get(0).get("year"+i+"_amt") -0/* stu_pay_hs_repo.sumOfPaidAmount(student_id,(Integer)fee_rec1.get(0).get("fee_receipt"),i)*/ - (Float) sas1.get(0).get("year"+i+"_amount")));
						System.out.println("IF paid at booard false and scholarship non zero if Statement program Type semster {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{" + paidamount);
					}
				}
			}
		}

		formatedRersponse.put("dueAmount", dueAmount);
		return formatedRersponse;
	}
	
	
	public Map<String, Object> checkAuidWithTypeIsAlreadyPresentOrNot(Integer student_id, Integer academic_year_id, String type) {
		Map<String, Object> status=new HashMap<>();
		if (ObjectUtils.isEmpty(hostelBedAssignmentRepository.getAssignedHostelBedId(student_id,academic_year_id))) {
			status.put("Status",false);
			status.put("message","Hostel is not assigned to student");
		} else if (hostelWaiverRepository.checkAuidWithTypeIsAlreadyPresentOrNot(student_id,academic_year_id,type) >= 1) {
			status.put("Status",false);
			status.put("message","Student is already paid for given academic year");
		}else {
			status.put("Status",true);
			status.put("message",null);
		}

		return status;
	}
	
	
	

	
	
}
