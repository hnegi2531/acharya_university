package com.au.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Student;
import com.au.repository.StudentRepository;
import com.au.response.ResponseHandler;

@Service
public class StudentService {

	@Autowired
	private StudentRepository stu_repo;

	public List<Student> listAll() {
		return stu_repo.findAll1();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> student_filtered_response = stu_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, student_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> student_sorted_response = stu_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, student_sorted_response);
	}

	public Student saveStudent(Student s) {
		return stu_repo.save(s);
	}

	public Student get(Integer id) {
		return stu_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student id Not Found:" + id));
	}

	public void delete(Integer id) {
		Student ay = stu_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student id Not Found:" + id));
		stu_repo.update(id);
	}

	public void delete1(Integer id) {
		Student ay = stu_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student id Not Found:" + id));
		stu_repo.update1(id);

	}
}
