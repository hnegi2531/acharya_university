package com.au.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.au.dto.CSVHelperDto;
import com.au.dto.JwtDetails;
import com.au.model.CandidateResults;
import com.au.repository.CandidateResultsRepository;
import com.au.response.ResponseHandler;

@Service
public class CandidateResultsService {

	@Autowired
	private CandidateResultsRepository csr_repo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private CSVHelperDto cvshelper;

	
	public void getDataFromFile(MultipartFile file, CandidateResults cr, String jwtToken) 
			throws Exception {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		
	//	LessonPlanAssignment lessonplanAssignment = new LessonPlanAssignment();
	//	lessonplanAssignment.setLesson_id(lessonplan.getLesson_id());
		if (CSVHelperDto.hasCSVFormat(file)) {
				saveFile(file,jwtDetails.getUserId(),jwtDetails.getUserName());
		}
	
	}
	
	public void saveFile(MultipartFile file, Integer user_id, String username) throws Exception {
		
		List<CandidateResults> candidate_result = cvshelper.csvToTutorialsForCandidateResults(file.getInputStream(),user_id);
//		candidate_result.stream().forEach(cr->{
//			cr.setCreated_by(user_id);
//			cr.setCreated_username(username);
//			System.out.println("(((((((((((((((()))))))))))))))) :  "+candidate_result.size());
//		});
//		System.out.println("++_+_+_+_+_+_+_+_+_+_+_+_+_+_+_ : "+candidate_result);
		csr_repo.saveAll(candidate_result);

}
	
	public CandidateResults saveCandidateResults(CandidateResults s) {
		return csr_repo.save(s);
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> exam_details_filtered_response = csr_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> exam_details_response = csr_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_details_response);
	}

	public HashMap<String, Object> getCountOfResult() {
		
		
		HashMap<String, Object> hm1 = new HashMap<String, Object>();
		
		List<Integer> Exam_id = csr_repo.getExamDate();
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +Exam_id);
		Exam_id.stream().forEach(id ->{
		
			HashMap<String, Object> hm = new HashMap<String, Object>();
			
			Integer Pass_count = csr_repo.getCountOfPassedResult(id);
			System.out.println("###############################################" +Pass_count);
			Integer Fail_count = csr_repo.getCountOfFailedResult(id);
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" +Fail_count);
			Integer Absent_count = csr_repo.getCountOfAbsentResult(id);
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" +Absent_count);
			hm.put("Pass",Pass_count);
			hm.put("Fail",Fail_count);
			hm.put("Absent",Absent_count);
			System.out.println("*********************************************************"+hm);
			hm1.put(id.toString(), hm);
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+hm1);
		});
		return hm1;
		
	
	}

	public List<HashMap<String, Object>> getAllDataOfCandidateResult() {
		return  csr_repo.getAllDataOfCandidateResult();
	}
	
}
