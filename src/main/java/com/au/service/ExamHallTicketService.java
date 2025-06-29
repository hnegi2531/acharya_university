package com.au.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.ExamHallTicket;
import com.au.repository.ExamHallTicketRepository;
import com.au.response.ResponseHandler;

@Service
public class ExamHallTicketService {

	
	@Autowired
	private ExamHallTicketRepository exam_ht_repo;
	
	
	public ExamHallTicket saveExamHallTicket(ExamHallTicket eht) throws Exception {
		if (exam_ht_repo.getcountOfHallTicket(eht.getCandidate_id()) < 1) {
			exam_ht_repo.save(eht);
	             
		}else{
			if(exam_ht_repo.checkCandidateExamResult(eht.getCandidate_id()).equalsIgnoreCase("pass")) {
				 throw new Exception("Exam Result Already Passed");
			}
			
			else
			{
				exam_ht_repo.save(eht);
			}
		}
		return	eht;
		}
		
				
	public List<ExamHallTicket> listAll1() {
		return exam_ht_repo.findAll11();
	}

	
	public ExamHallTicket get(Integer id) {
		return exam_ht_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Exam Hall Ticket Not Found:" + id));
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> exam_ht_filtered_response = exam_ht_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_ht_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> exam_ht_response = exam_ht_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, exam_ht_response);
	}
	
	public ExamHallTicket updateExamHallTicket(ExamHallTicket eht) {
		return exam_ht_repo.save(eht);
	}

	
	public void delete(Integer id) {
		ExamHallTicket eht = exam_ht_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Exam Hall Ticket Not Found:" + id));
		exam_ht_repo.updateExamHallTickets(id);
	}

	public void delete1(Integer id) {
		ExamHallTicket eht = exam_ht_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Exam Hall Ticket Not Found:" + id));
		exam_ht_repo.updateExamHallTickets1(id);
	}
	
	public List<Map<String,Object>> getCandidateNameConcatWithApplicationNumber() {	
		return exam_ht_repo.getCandidateNameConcatWithApplicationNumber();
	}
	
	public List<Map<String,Object>> getAdmitCardDetail(String application_number) {	
		return exam_ht_repo.getAdmitCardDetail(application_number);
	}
	
}

