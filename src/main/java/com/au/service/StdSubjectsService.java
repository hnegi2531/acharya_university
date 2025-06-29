package com.au.service;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.StdSubjects;
import com.au.repository.StdSubjectsRepository;
import com.au.repository.SubjectWorkLoadTypeRepository;
import com.au.response.ResponseHandler;

@Service
public class StdSubjectsService {

	@Autowired
	private StdSubjectsRepository r_repo;

	public List<StdSubjects> listAll1() {
		return r_repo.findAll1();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> ssc_filtered_response = r_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, ssc_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> ssc_sorted_response = r_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, ssc_sorted_response);
	}
	
	public StdSubjects save_Std_subjects(@Valid StdSubjects s) {
		
		if (r_repo.existsBySubjectCode(s.getSubjectCode())) {
			
			System.out.println("Subject code is already taken!!");
			throw new RuntimeException("123");
		}
		return r_repo.save(s);
	}

	
/*
	public StdSubjects save_Std_subjects(SubjectAcademicWorkLoadDto s,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		
		if (r_repo.existsBySubjectCode(s.getSubjectCode())) {
			throw new RuntimeException("Subject code is already taken!!");
		}

		StdSubjects s2 = new StdSubjects();
		
		s2.setSubjectName(s.getSubjectName());
		s2.setSubjectNameShort(s.getSubjectNameShort());
		s2.setSubjectCode(s.getSubjectCode());
		s2.setCreatedBy(jwtDetails.getUserId());
		s2.setCreatedUsername(jwtDetails.getUserName());
		
		s2.setActive(s.getActive());
	/*	s2.setCredits(s.getCredits());
		s2.setMaxCieMarks(s.getMaxCieMarks());
		s2.setMaxSemYearMarks(s.getMaxSemYearMarks());
		s2.setMinCieMarks(s.getMinCieMarks());
		s2.setMinSemYearMarks(s.getMinSemYearMarks());
		s2.setSubjectUniversityMaxHours(s.getSubjectUniversityMaxHours());
		s2.setSubjectInstituteMaxHours(s.getSubjectInstituteMaxHours());
		s2.setSubjectTypeId(s.getSubjectTypeId());
		*/
//		StdSubjects s3 = r_repo.save(s2);
		
		//List<StdSubjects> s1 = new ArrayList<>();
	/*	
		s.getSw_hours().entrySet().stream().forEach(kv->{			
			SubjectWorkLoadType sw_type = new SubjectWorkLoadType();
			sw_type.setSubjectId(s3.getSubjectId());			
			
			sw_type.setAcademicWordLoadTypeId(kv.getKey());
			sw_type.setHours(kv.getValue());
			sw_type.setCreatedBy(jwtDetails.getUserId());
			sw_type.setCreatedUsername(jwtDetails.getUserName());
			s_repo.save(sw_type);
		});
		
		return s2;

	}
*/
	public StdSubjects get(Integer id) {
		return r_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Std_subjects Not Found:" + id));
	}

	public void delete(Integer id) {
		StdSubjects cc = r_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Std_subjects Not Found:" + id));
		r_repo.updateStd_subjects(id);
	}

	public void delete1(Integer id) {
		StdSubjects cc = r_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Std_subjects Not Found:" + id));
		r_repo.updateStd_subjects1(id);

	}

	public StdSubjects save_Std_subjects1(StdSubjects r) {
	
		if(r_repo.findBysubjectCodeNotIn(r.getSubjectCode()) != null) {
			System.out.println("subject code already present!!!");			
		}
		
		return r_repo.save(r);
		
	}


}
