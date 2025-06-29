package com.au.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.au.exception.ResourceNotFoundException;
import com.au.model.Attachments;
import com.au.model.HigherEducationAttachments;
import com.au.model.JobProfile;
import com.au.model.UserAuthentication;
import com.au.repository.ApplicantDetailsRepository;
import com.au.repository.AttachmentsRepository;
import com.au.repository.EducationDetailsRepository;
import com.au.repository.ExperienceDetailsRepository;
import com.au.repository.HigherEducationAttachmentsRepository;
import com.au.repository.JobProfileRepository;
import com.au.response.ResponseHandler;

@Service
public class JobProfileService {

	@Autowired
	private JobProfileRepository jpr_repo;
	
	@Autowired
	private EducationDetailsRepository edr_repo;
	
	@Autowired
	private ExperienceDetailsRepository exr_repo;
	
	@Autowired
	private AttachmentsRepository ar_repo;
	
	@Autowired
	private HigherEducationAttachmentsRepository hea_repo;
	
	@Autowired
	private ApplicantDetailsRepository a_repo;
	
	@Autowired
	private UserAuthenticationService uservice;

	public List<JobProfile> listAll() {
		return jpr_repo.findAll();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword, LocalDate start, LocalDate end) {
	    List<Map<String, Object>> response1 = jpr_repo.findAll1(pageable, keyword, start, end);
	    return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> listAll2(Pageable pageable, LocalDate start, LocalDate end) {
	    List<Map<String, Object>> response = jpr_repo.findAll2(pageable, start, end);
	    return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}

	

	public JobProfile saveJobProfile(JobProfile jobprofile) throws Exception {
		if (jpr_repo.getCountNameAndDob(jobprofile.getFirstname(),jobprofile.getDateofbirth()) >= 1)
			throw new Exception("Name and DOB is already present. Please contact with HR.");
		
		else if (jpr_repo.getCountEmail(jobprofile.getEmail()) >=1)
		              throw new Exception("Email is already present. Please login with another Email id ");
		else if(jpr_repo.countMobileNumber(jobprofile.getMobile()) >= 1){
			throw new Exception("Whatsapp Number is already present. Please provide another number");
		}else {
			jobprofile.setFirstname(ResponseHandler.nameCapital(jobprofile.getFirstname()));
			jobprofile.setCurrent_location(jobprofile.getStreet()+" "+jobprofile.getLocality()+" - "+jobprofile.getPincode());
				jpr_repo.save(jobprofile);
			}
		return jobprofile;
	}
	
	private Integer getCountAll1(String firstname,Date dateofbirth) {
		return jpr_repo.getCountNameAndDob(firstname,dateofbirth);
	}

	private Integer getCountAll11(String email) {
		return jpr_repo.getCountEmail(email);
	}
	
	public JobProfile saveJobProfiles(JobProfile jobprofile) {
		return jpr_repo.save(jobprofile);	
	}

	public JobProfile get(Integer id) {
		if (id.equals(0)) {
			throw new RuntimeException("Opps Exception raised....");
		}
		return jpr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("JobProfile id Not Found:" + id));
	}

	public void delete(Integer id) {
		JobProfile ay = jpr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("JobProfile id Not Found:" + id));
		jpr_repo.delete(ay);
	}

	public List<HashMap<String, Object>> getJobDetails(Integer id) {
		return jpr_repo.getAllJobDetails(id);
	}

	public HashMap<String,Object> getNameAndEmailByJobId(Integer job_id) {
		return jpr_repo.getNameAndEmailByJobId(job_id);
	}
	
	public String getReferenceNo(Integer id) {
		return jpr_repo.fetchReferenceNo(id);
	}
	
	
	public HashMap<String,Boolean> checkEmail(String email) {
		HashMap<String,Boolean> hs = new HashMap<String,Boolean>();
		if(jpr_repo.checkEmailIsPresent(email)==true) {
			hs.put("Email Present",true);
			return hs;
		}
			else {
				hs.put("Email Present",false);
				return hs;
			}
		}
	
	public HashMap<String, Object> getAllApplicantDetails(@PathVariable Integer job_id) {
		
		HashMap<String, Object> formatedRersponse = new HashMap<>();
		
		JobProfile st=jpr_repo.findById(job_id).orElseThrow(()-> new ResourceNotFoundException("JobProfile Not Found:" + job_id));
		HashMap<String, Object> jobprofile_details = jpr_repo.getJobProfile(job_id);
		List<HashMap<String, Object>> edu_details = edr_repo.getEducationDetails(job_id);
		List<HashMap<String, Object>> exp_details = exr_repo.getExperienceDetails(job_id);
		Attachments attachment = ar_repo.getFileName(job_id);
		HigherEducationAttachments he_details = hea_repo.getHigherEducationAttachment(job_id);
		
		jobprofile_details.put("Educational_Details",edu_details);
		jobprofile_details.put("Experience_Details",exp_details);
		jobprofile_details.put("Resume_Attachment",attachment);
		jobprofile_details.put("Higher_Education_Attachment",he_details);
		formatedRersponse.put("Job_Profile",jobprofile_details);
		return formatedRersponse;
		
	}
	
	public List<Map<String, Object>> jobProfileDetailsOnUserId(Integer id) {
		UserAuthentication user_detail = uservice.get(id);
		return jpr_repo.jobProfileDetailsOnUserId(user_detail.getEmail());
	}
	
	public  Object updateJobProfileHrStatus(String hr_status, String hr_remark , Integer job_id) {
		
		return	jpr_repo.updateJobProfileHrStatus(hr_status,hr_remark,job_id);
	
	}

	public JobProfile getJobProfileById(Integer id) {
		return jpr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Job ID Not Found:" + id));
	}

	public List<Object> jobProfileDetailsOnDate(String created_date) {
	    // Retrieve job profiles based on the created_date
	    List<Map<String, Object>> jobProfile = jpr_repo.jobProfileDetailsOnDate(created_date);
	    
	    // Initialize the result map
//	    Map<Integer, Map<String, Object>> result = new HashMap<>();
	    
	    List<Object> result = new ArrayList<>();

	    for (Map<String, Object> profile : jobProfile) {
	        // Get job_id from the profile
	        Integer jobId = (Integer) profile.get("job_id");
	        
	        // Create a map to hold the job details
	        Map<String, Object> jobDetails = new HashMap<>();
	        jobDetails.put("jobDetails", profile);  // Add job profile data
	        
	       Map<String, Object> jobProfileData = jpr_repo.jobProfileDetailsDataOnDate(jobId);
	        
	        // Retrieve education details for the current job_id
	        List<Map<String, Object>> educationDetails = edr_repo.educationDetailsOnDate(jobId);
	        
	        // Retrieve experience details for the current job_id
	        List<Map<String, Object>> experienceDetails = exr_repo.experienceDetailsOnDate(jobId);
	        
	        // Construct the details map for the current job_id
	        Map<String, Object> jobInfo = new HashMap<>();
	        
	        jobInfo.put("jobProfile", jobProfileData);
	        jobInfo.put("education", educationDetails); // Include education details
	        jobInfo.put("experience", experienceDetails); // Include experience details
	        
	        // Put jobInfo into the result map using job_id as the key
	        result.add( jobInfo);
	    }
	    
	    // Return the constructed result map
	    return result;
	}

	
}
