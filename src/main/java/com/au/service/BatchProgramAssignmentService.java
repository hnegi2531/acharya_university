package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.au.repository.BatchProgramAssignmentRepository;
import com.au.repository.ProgramSpecilizationRepository;

@Service
public class BatchProgramAssignmentService {

	@Autowired
	private BatchProgramAssignmentRepository batchProgramAssignmentRepository;
	
	public List<Integer> assignedProgramSpecilizationByBatchAssignmentId(Integer batch_assignment_id){
		return batchProgramAssignmentRepository.assignedProgramSpecilizationByBatchAssignmentId(batch_assignment_id);
	}
	
	public List<Integer> assignedProgramAssignmentByBatchAssignmentId(Integer batch_assignment_id){
		return batchProgramAssignmentRepository.assignedProgramAssignmentByBatchAssignmentId(batch_assignment_id);
	}

	public List<Integer> assignedProgramByBatchAssignmentId(Integer batch_assignment_id) {
		return batchProgramAssignmentRepository.assignedProgramByBatchAssignmentId(batch_assignment_id);
	}

}
