package com.au.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.au.dto.BatchAssignmentRequestDto;
import com.au.model.BatchAssignment;
import com.au.model.BatchProgramAssignment;


public interface BatchProgramAssignmentRepository extends JpaRepository<BatchProgramAssignment, Integer>{
	
	@Query(value = "select bpa.program_id from BatchProgramAssignment bpa where bpa.batch_assignment_id=?1 and bpa.active=true")
	public List<Integer> getProgramIdFromBatchProgramAssignment(Integer batch_assignment_id);
	
	@Query(value = "Select distinct bpa.program_specialization_id from BatchProgramAssignment bpa where bpa.batch_assignment_id=?1 and bpa.active=true")
	public List<Integer> assignedProgramSpecilizationByBatchAssignmentId(Integer batch_assignment_id);
	
	
	@Query(value = "Select distinct bpa.program_assignment_id from BatchProgramAssignment bpa where bpa.batch_assignment_id=?1 and bpa.active=true")
	public List<Integer> assignedProgramAssignmentByBatchAssignmentId(Integer batch_assignment_id);

	@Query(value = "Select distinct bpa.program_id from BatchProgramAssignment bpa where bpa.batch_assignment_id=?1 and bpa.active=true")
	public List<Integer> assignedProgramByBatchAssignmentId(Integer batch_assignment_id);

	@Query(value = "Select GROUP_CONCAT(ps.program_specialization_short_name) as specializationShortNames,GROUP_CONCAT(pr.program_short_name) as programShortNames,"
			+ "GROUP_CONCAT(pr.program_name) as programNames,GROUP_CONCAT(ps.program_specialization_name) as specializationNames,"
			+ "GROUP_CONCAT(bpa.program_id) as programIds,GROUP_CONCAT(bpa.program_specialization_id) as specializationIds from batch_program_assignment bpa "
			+ "Inner join batch_assignment ba on ba.batch_assignment_id=bpa.batch_assignment_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=bpa.program_specialization_id "
			+ "Left join program pr on pr.program_id=bpa.program_id where bpa.batch_assignment_id=?1 and bpa.active=true And ba.active=true ",nativeQuery=true)
	public Map<String,String> specializationAndProgramNames(Integer batch_assignment_id);

	@Query(value="Select pt.program_type_id from batch_program_assignment bpa "
			+ "Inner join batch_assignment ba on ba.batch_assignment_id=bpa.batch_assignment_id "
			+ "Inner join program_assignment pa on pa.program_assignment_id=bpa.program_assignment_id "
			+ "Inner join program_type pt on pt.program_type_id=pa.program_type_id where bpa.batch_assignment_id=?1 and bpa.active=true And ba.active=true group by bpa.batch_assignment_id ",nativeQuery=true)
	String getProgramTypeByBatchAssignmentId(Integer batchAssignmentId);
}
