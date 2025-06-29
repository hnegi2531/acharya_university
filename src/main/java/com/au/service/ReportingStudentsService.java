package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.au.exception.ResourceNotFoundException;
import com.au.model.ReasonFeeExcemption;
import com.au.model.ReportingStudents;
import com.au.repository.ReportingStudentsRepository;
import com.au.repository.StudentDetailsRepository;

@Service
public class ReportingStudentsService {

	@Autowired
	private ReportingStudentsRepository reportingStudentsRepository;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private TriggerService triggerService;
	
	public List<ReportingStudents> listAll(){
		return reportingStudentsRepository.findAll();
	}
	
	public ReportingStudents saveReportingStudents(ReportingStudents r) throws Exception {
		if(reportingStudentsRepository.getStudentId(r.getStudent_id()) >= 1) {
			throw new Exception("StudentId already exist !!!");
		}
		return reportingStudentsRepository.save(r);
	}
	
	public List<ReportingStudents> saveReportingStudents(List<ReportingStudents> r) {
		return reportingStudentsRepository.saveAll(r);
	}
	
	public ReportingStudents get(Integer id) {
        return reportingStudentsRepository.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("ReportingStudents Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	ReportingStudents cc = reportingStudentsRepository.findById(id)
    	.orElseThrow(()-> new ResourceNotFoundException("ReportingStudents Not Found:"+id));    	
    	reportingStudentsRepository.delete(cc);
    }
    
    public List<Map<String, Object>> fetchAllStudentDetailsToReportOnSem(Integer school_id,Integer program_id,Integer ac_year_id,Integer program_specialization_id, Integer current_sem) {
		return studentDetailsRepository.fetchAllStudentDetailsToReportOnSem(school_id, program_id,ac_year_id,program_specialization_id,current_sem);
	}
	
	public List<Map<String, Object>> fetchAllStudentDetailsToReportOnYear(Integer school_id, Integer program_id,Integer ac_year_id,Integer program_specialization_id, Integer current_year) {
		return studentDetailsRepository.fetchAllStudentDetailsToReportOnYear( school_id, program_id,ac_year_id,program_specialization_id,current_year);
	}
	
	public List<Map<String, Object>> fetchAllStudentsWithNoStatusAndNotEligibleOnSem(Integer school_id,Integer program_id, Integer current_sem) {
		return studentDetailsRepository.fetchAllStudentsWithNoStatusAndNotEligibleOnSem(school_id, program_id,current_sem);
	}
	
	public List<Map<String, Object>> fetchAllStudentsWithNoStatusAndNotEligibleOnYear(Integer school_id, Integer program_id, Integer current_year) {
		return studentDetailsRepository.fetchAllStudentsWithNoStatusAndNotEligibleOnYear( school_id, program_id,current_year);
	}

	public List<Map<String, Object>> fetchAllStudentDetailssWithEligibleStatusOnSem(Integer school_id,Integer program_id,Integer program_specialization_id, Integer current_sem,Integer eligible_reported_status) {
		return studentDetailsRepository.fetchAllStudentDetailssWithEligibleStatusThreeOnSem(school_id, program_id,program_specialization_id,current_sem,eligible_reported_status);
	}
	
	public List<Map<String, Object>> fetchAllStudentDetailssWithEligibleStatusOnYear(Integer school_id, Integer program_id, Integer program_specialization_id, Integer current_year,Integer eligible_reported_status) {
		return studentDetailsRepository.fetchAllStudentDetailssWithEligibleStatusThreeOnYear( school_id, program_id,program_specialization_id,current_year,eligible_reported_status);
	}

	public void updationOfStudentReportingYearAndUsn(String auid) {
		triggerService.studentReportingTrigger(auid);

	}
	
	public ReportingStudents reportingStudentByStudentId(Integer studentId) {
        return reportingStudentsRepository.getDetailsOfReportingStudentsByStudentId(studentId);
    }
	
	public List<Map<String, Object>> allNotReportedStudentDetailsBySem(Integer school_id,Integer program_id, Integer program_specialization_id, Integer current_sem) {
		return studentDetailsRepository.allNotReportedStudentDetailsBySem(school_id, program_id,program_specialization_id,current_sem);
	}
	
	public List<Map<String, Object>> allNotReportedStudentDetailsByYear(Integer school_id, Integer program_id, Integer program_specialization_id, Integer current_year) {
		return studentDetailsRepository.allNotReportedStudentDetailsByYear( school_id, program_id,program_specialization_id,current_year);
	}


}
