package com.au.service;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.au.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.BatchAssignmentDto;
import com.au.dto.BatchAssignmentRequestDto;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.BatchAssignment;
import com.au.model.BatchProgramAssignment;
import com.au.model.FamilyStructure;
import com.au.model.Resignation;
import com.au.model.TimeIntervalTypes;
import com.au.response.ResponseHandler;

@Service
public class BatchAssignmentService {

	@Autowired
	private BatchAssignmentRepository s_repo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private StudentDetailsRepository stu_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	@Autowired
	private TimeIntervalTypesRepository tt_repo;
	
	@Autowired
	private ProgramAssigmentRepository programAssigmentRepository;
	
	@Autowired
	private BatchProgramAssignmentRepository bpar;
	
	@Autowired
	private ProgramSpecilizationService programSpecilizationService;

	@Autowired
	private ProgramSpecilizationRepository ps_repo;

	@Autowired
	private SectionAssignmentService sectionAssignmentService;

	@Autowired
	private StudentPermissionRepository studentPermissionRepository;

	private final ModelMapper modelMapper = new ModelMapper();
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Integer ac_year_id, Integer school_id, Integer program_specialization_id, Integer batch_id,Integer current_year_sem, Object keyword) {
		List<Map<String, Object>> response1 = s_repo.findAll2(pageable,ac_year_id,school_id,program_specialization_id,batch_id,current_year_sem, keyword);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable, Integer ac_year_id, Integer school_id, Integer program_specialization_id, Integer batch_id,Integer current_year_sem) {
		List<Map<String, Object>> response = s_repo.findAll3(pageable,ac_year_id,school_id,program_specialization_id,batch_id,current_year_sem);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}
	
	
	public ResponseEntity<Object> fetchAllBatchAssignmentDetailsBasedOnSchoolAndCreatedBylistAll1(Pageable pageable,
			Object keyword, Integer schoolId, Integer createdBy, Integer dept_id) {
		List<Map<String, Object>> response1 = s_repo.fetchAllBatchAssignmentDetailsBasedOnSchoolAndCreatedBylistAll1(pageable, keyword,schoolId,createdBy,dept_id );
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> fetchAllBatchAssignmentDetailsBasedOnSchoolAndCreatedBylistAll2(Pageable pageable1,
			Integer schoolId, Integer createdBy, Integer dept_id) {
		List<Map<String, Object>> response = s_repo.fetchAllBatchAssignmentDetailsBasedOnSchoolAndCreatedBylistAll2(pageable1,schoolId,createdBy,dept_id);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}
	
	

	public List<BatchAssignment> saveBatchAssignment(BatchAssignmentDto s,String jwtToken) throws Exception {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		List<BatchAssignment> batchAssignList=new ArrayList<BatchAssignment>();
		String studentIds = s.getStudent_ids();
			String[] idsArray = studentIds.split(",");
			for(String id : idsArray) {
				Integer studentId = Integer.parseInt(id);
				if(s.getCurrent_sem() != null &&s_repo.getCountOfBatchAssignmentOnSem(s.getSchool_id(),s.getProgram_specialization_id(),s.getCurrent_sem(),s.getAc_year_id(), studentId) >= 1) {
					throw new RuntimeException("Batch assignment contains duplicate student id !");
				}
				if(s.getCurrent_year() != null && s_repo.getCountOfBatchAssignmentOnYear(s.getSchool_id(),s.getProgram_specialization_id(),s.getCurrent_year(),s.getAc_year_id(), studentId) >= 1) {
					throw new RuntimeException("Batch assignment contains duplicate student id !");
				}
			}

			// Integer count = s.getCurrent_sem() == null ? s_repo.getCountOfBatchAssignmentOnYear(s.getSchool_id(),s.getProgram_specialization_id(),s.getCurrent_year(),s.getAc_year_id(),s.getBatch_id()):
				            // s_repo.getCountOfBatchAssignmentOnSem(s.getSchool_id(),s.getProgram_specialization_id(),s.getCurrent_sem(),s.getAc_year_id(),s.getBatch_id());
			// if(count >=1) {

			// 	throw new RuntimeException("Batch Assignment On Combination Of School, Program Specialization, Academic Year And Current Year OR Sem Already Exist");
			// } else {
				//if(s_repo.checkRemarksValidation(s.getRemarks()) >=1 ) {
				if(s_repo.checkRemarksValidationWithBatchId(s.getBatch_id(),s.getRemarks()) >=1 ) {
					throw new RuntimeException("Batch name and Remarks are duplicate !");
				} else {
					BatchAssignment ba=new BatchAssignment();
					ba.setBatch_id(s.getBatch_id());
					ba.setSchool_id(s.getSchool_id());
					ba.setAc_year_id(s.getAc_year_id());
					ba.setCurrent_year(s.getCurrent_year());
					ba.setCurrent_sem(s.getCurrent_sem());
					ba.setStudent_ids(s.getStudent_ids());
					ba.setBatch_type(s.getBatch_type());
					ba.setBatch_master_id(s_repo.getMaxBatchMasterId(s.getAc_year_id())+1);
					ba.setRemarks(s.getRemarks());
					ba.setCreated_by(jwtDetails.getUserId());
					ba.setCreated_username(jwtDetails.getUserName());
					ba.setActive(s.getActive());
					ba.setGuest_uesr_ids(s.getGuest_uesr_ids());
					ba.setInterval_type_id(s.getInterval_type_id());

					
					s_repo.save(ba);
					batchAssignList.add(ba);
				}
			// }
		return batchAssignList;

	}
	
	public void updateBatchProgramAssignment(BatchAssignmentDto r, List<BatchAssignment> batch_assignment) {
		
		r.getProgram_specialization_id().stream().forEach(pg -> {
		 batch_assignment.stream().forEach(batch -> {
			BatchProgramAssignment ba = new BatchProgramAssignment();
			
			r.getProgram_assignment_id().stream().forEach(pa  -> {
				ba.setProgram_assignment_id(pa);
			   });
				  
			r.getProgram_id().stream().forEach(p -> {
				ba.setProgram_id(p);
			  });
			ba.setBatch_assignment_id(batch.getBatch_assignment_id());
			ba.setCreated_by(batch.getCreated_by());
			ba.setCreated_date(batch.getCreated_date());
			ba.setCreated_username(batch.getCreated_username());
			ba.setModified_by(batch.getModified_by());
			ba.setModified_date(batch.getModified_date());
			ba.setModified_username(batch.getModified_username());
			ba.setActive(batch.getActive());
			ba.setAc_year_id(batch.getAc_year_id());
			ba.setBatch_id(batch.getBatch_id());
			ba.setCurrent_sem(batch.getCurrent_sem());
			ba.setCurrent_year(batch.getCurrent_year());
			ba.setSchool_id(batch.getSchool_id());

		
			ba.setProgram_specialization_id(pg);
		
			bpar.save(ba);
		
	
		}); 
	    });	  
		  
	}
	
	
	
	public BatchAssignment get(Integer id) {
		return s_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("BatchAssignment Not Found:" + id));
	}

	public void delete(Integer id) {
		BatchAssignment cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("BatchAssignment Not Found:" + id));
		s_repo.update(id);
	}

	public void delete1(Integer id) {
		BatchAssignment cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("BatchAssignment Not Found:" + id));
		s_repo.update1(id);
	}
	
	public List<Map<String, Object>> fetchStudentDetailForBatchAssignmentOnSem(Integer school_id,List<Integer> program_specialization_id, Integer current_sem) {
		return stu_repo.fetchStudentDetailForBatchAssignmentOnSem(school_id, program_specialization_id,current_sem);
	}
	
	public List<Map<String, Object>> fetchStudentDetailForBatchAssignmentOnYear(Integer school_id,List<Integer> program_specialization_id,Integer current_year) {
		return stu_repo.fetchStudentDetailForBatchAssignmentOnYear( school_id, program_specialization_id,current_year);
	}
	
	
	public List<BatchAssignment> fetchBatchAssignmentDetailForStudentFromIndexOnSem(Integer school_id,List<Integer> program_id, Integer current_sem,Integer ac_year_id,Integer batch_id) {
		return s_repo.fetchStudentDetailForBatchAssignmentOnSem(school_id, program_id,current_sem,ac_year_id,batch_id);
	}
	
	public List<BatchAssignment> fetchBatchAssignmentDetailForStudentFromIndexOnYear(Integer school_id,List<Integer> program_id,Integer current_year,Integer ac_year_id,Integer batch_id) {
		return s_repo.fetchStudentDetailForBatchAssignmentOnYear( school_id, program_id,current_year,ac_year_id,batch_id);
	}
	
	public List<Object> fetchStudentDetailForBatchAssignmentFromIndexOnSem(Integer school_id,List<Integer> program_specialization_id, Integer current_sem,Integer ac_year_id,List<Integer> student_ids,List<Integer> unassigned_school_id) {
		List<Object> return_student_data=new ArrayList<Object>();
		Map<Object,Object> all_student_details=new HashMap<>();
		List<Map<String, Object>> assigned_student= stu_repo.fetchAssignedStudentDetails(student_ids);
		List<Map<String, Object>> unassigned_student= stu_repo.fetchUnAssignedStudentDetailForBatchAssignmentFromIndexOnSem(program_specialization_id,current_sem,ac_year_id,student_ids,unassigned_school_id);
		all_student_details.put("batch_assigned_student_details", assigned_student);
		all_student_details.put("batch_unassigned_student_details", unassigned_student);
		return_student_data.add(all_student_details);
		return return_student_data;
	}
	
	public List<Object> fetchStudentDetailForBatchAssignmentFromIndexOnYear(Integer school_id,List<Integer> program_specialization_id,Integer current_year,Integer ac_year_id,List<Integer> student_ids,List<Integer> unassigned_school_id) {
		List<Object> return_student_data=new ArrayList<Object>();
		Map<Object,Object> all_student_details=new HashMap<>();
		List<Map<String, Object>> assigned_student= stu_repo.fetchAssignedStudentDetails(student_ids);
		List<Map<String, Object>> unassigned_student= stu_repo.fetchUnAssignedStudentDetailForBatchAssignmentFromIndexOnYear(program_specialization_id,current_year,ac_year_id,student_ids,unassigned_school_id);
		all_student_details.put("batch_assigned_student_details", assigned_student);
		all_student_details.put("batch_unassigned_student_details", unassigned_student);
		return_student_data.add(all_student_details);
		return return_student_data;
	}
	
	public List<BatchAssignment> batchAssignmentUpdateFromIndexPage(List<BatchAssignment> s){
		return s_repo.saveAll(s);
	}
	
	public List<Object> fetchGuestStudentDetailsOnSem(Integer school_id,Integer program_id, Integer current_sem,Integer ac_year_id,Integer batch_id) {
		String guest_ids=s_repo.getGuestUserIDsOnSem(school_id,program_id,current_sem,ac_year_id,batch_id);
		List<Object> return_guest_student_data=new ArrayList<Object>();
		if(guest_ids == null) {
			List<Map<String, Object>> guest_student= uar_repo.fetchGuestStudentDetails();
			Map<Object,Object> all_guest_student_details=new HashMap<>();
			all_guest_student_details.put("batch_unassigned_guest_student_details", guest_student);
			return_guest_student_data.add(all_guest_student_details);
			return return_guest_student_data;
		}else {
			List<Integer> guest_user_ids=ResponseHandler.toConvertCommaSeperatedIdsAsList(guest_ids);
			Map<Object,Object> all_guest_student_details=new HashMap<>();
			List<Map<String, Object>> assigned_student= uar_repo.fetchAssignedGuestStudentDetails(guest_user_ids);
			List<Map<String, Object>> unassigned_student= uar_repo.fetchUnAssignedGuestStudentDetails(guest_user_ids);
			all_guest_student_details.put("batch_assigned_guest_student_details", assigned_student);
			all_guest_student_details.put("batch_unassigned_guest_student_details", unassigned_student);
			return_guest_student_data.add(all_guest_student_details);
			return return_guest_student_data;
		}
	}
	
	public List<Object> fetchGuestStudentDetailsOnYear(Integer school_id,Integer program_id,Integer current_year,Integer ac_year_id,Integer batch_id) {
		String guest_ids=s_repo.getGuestUserIDsOnYear(school_id,program_id,current_year,ac_year_id,batch_id);
		List<Object> return_guest_student_data=new ArrayList<Object>();
		if(guest_ids == null) {
			List<Map<String, Object>> guest_student= uar_repo.fetchGuestStudentDetails();
			Map<Object,Object> all_guest_student_details=new HashMap<>();
			all_guest_student_details.put("batch_unassigned_guest_student_details", guest_student);
			return_guest_student_data.add(all_guest_student_details);
			return return_guest_student_data;
		}else {
			List<Integer> guest_user_ids=ResponseHandler.toConvertCommaSeperatedIdsAsList(guest_ids);
			Map<Object,Object> all_guest_student_details=new HashMap<>();
			List<Map<String, Object>> assigned_student= uar_repo.fetchAssignedGuestStudentDetails(guest_user_ids);
			List<Map<String, Object>> unassigned_student= uar_repo.fetchUnAssignedGuestStudentDetails(guest_user_ids);
			all_guest_student_details.put("batch_assigned_guest_student_details", assigned_student);
			all_guest_student_details.put("batch_unassigned_guest_student_details", unassigned_student);
			return_guest_student_data.add(all_guest_student_details);
			return return_guest_student_data;
		}
	}
	public List<Map<String, Object>> fetchUnAssignedStudentDetailsOfSchool(Integer ac_year_id, Integer school_id,
	        List<Integer> student_ids, List<Integer> program_specialization_id, Integer program_id, Integer current_year_sem,
	        Integer program_assignment_id) {
	    
	    // Ensure that if school_id is null, the query condition is handled
	    if (school_id != null) {
	       
	    // Determine which query to use based on the program assignment type
	    if (programAssigmentRepository.getProgramTypeByProgramAssignmentId(program_assignment_id).equalsIgnoreCase("Semester")) {
	        return stu_repo.fetchUnAssignedStudentDetailsOfSchoolOnSem(ac_year_id, school_id, student_ids,
	                program_specialization_id, current_year_sem);
	    } else {
	        return stu_repo.fetchUnAssignedStudentDetailsOfSchoolOnYear(ac_year_id, school_id, student_ids,
	                program_specialization_id, current_year_sem);
	    }
	}else {
		 if (programAssigmentRepository.getProgramTypeByProgramAssignmentId(program_assignment_id).equalsIgnoreCase("Semester")) {
		        return stu_repo.fetchUnAssignedStudentDetailsOfSchoolOnSem1(ac_year_id, student_ids,
		                program_specialization_id, current_year_sem);
		    } else {
		        return stu_repo.fetchUnAssignedStudentDetailsOfSchoolOnYear1(ac_year_id, student_ids,
		                program_specialization_id, current_year_sem);
		    }
	}
	}

	public List<Map<String, Object>> fetchUnAssignedStudentDetailsOfSchoolWOStudent(Integer ac_year_id, Integer school_id,
	        List<Integer> program_specialization_id, Integer program_id, Integer current_year_sem, Integer program_assignment_id) {
	    
	    // Handle school_id as null if not provided
	    if (school_id != null) {
	       
	    if (programAssigmentRepository.getProgramTypeByProgramAssignmentId(program_assignment_id).equalsIgnoreCase("Semester")) {
	        return stu_repo.fetchUnAssignedStudentDetailsOfSchoolWOStudentOnSem(ac_year_id, school_id,
	                program_specialization_id, current_year_sem);
	    } else {
	        return stu_repo.fetchUnAssignedStudentDetailsOfSchoolWOStudentOnYear(ac_year_id, school_id,
	                program_specialization_id, current_year_sem);
	    }
	}else {
	    
	    if (programAssigmentRepository.getProgramTypeByProgramAssignmentId(program_assignment_id).equalsIgnoreCase("Semester")) {
	        return stu_repo.fetchUnAssignedStudentDetailsOfSchoolWOStudentOnSem1(ac_year_id,
	                program_specialization_id, current_year_sem);
	    } else {
	        return stu_repo.fetchUnAssignedStudentDetailsOfSchoolWOStudentOnYear1(ac_year_id,
	                program_specialization_id, current_year_sem);
	    }
	}
	    
	}

	
	public List<Map<String, Object>> fetchStudentDetailsForUpdate(List<Integer> student_ids) {
		List<Map<String, Object>> assigned_student= stu_repo.fetchAssignedStudentDetails(student_ids);
		return assigned_student;
	}	
	
	Integer count=0;
	Integer count2;
	public Integer getCountOfStudentsAssignedToBatch(Integer batch_id) {
			List<BatchAssignment> list = s_repo.getCountOfStudentsAssignedToBatch(batch_id);
			list.stream().forEach(l -> {
				String std_ids = l.getStudent_ids();
				if(std_ids != null) {
					String[] num = std_ids.split(",");
					for(int i=0;i<num.length;i++) {
						count++;
					}
				}
			});
			count2=count;
			count=0;
			return count2;
	}

	public List<HashMap<String,Object>> assignedStudentDetailsByBatchAssignmentId(Integer batch_assignment_id) {
		BatchAssignment batchAssignment = get(batch_assignment_id);
		List<Integer> studentIds = ResponseHandler.toConvertCommaSeperatedIdsAsList(batchAssignment.getStudent_ids())
				.parallelStream().distinct().collect(Collectors.toList());
		List<Map<String, Object>> assignedStudents = stu_repo.getAllReportedStudents(studentIds);
		List<HashMap<String, Object>> responseWithDueStatus = new ArrayList<>();
		Optional.ofNullable(assignedStudents)
				.orElse(new ArrayList<>()).forEach(st -> {
					HashMap<String, Object> map = modelMapper.map(st, HashMap.class);
					Boolean dueStatus = sectionAssignmentService.studentTillYearOrSemWithExemptDays((Integer) st.get("student_id"));

					String permissionDate = studentPermissionRepository.getPermissionDate((String) st.get("auid"), (Integer) st.get("current_sem"), (Integer) st.get("current_year"));
					if(dueStatus && StringUtils.isNotEmpty(permissionDate)){
						map.put("due_status",false);
						map.put("attendance_permitted_date",permissionDate);
					}else{
						map.put("due_status",dueStatus);
						map.put("attendance_permitted_date",null);
					}
					responseWithDueStatus.add(map);
				});
		return responseWithDueStatus;
	}
	
	public List<Map<String, Object>> studentDetailsWithBatchName(Integer batch_assignment_id) {
		BatchAssignment batchAssignment=s_repo.activeBatchAssignmentDetail(batch_assignment_id);
		if(batchAssignment != null && StringUtils.isNotBlank(batchAssignment.getStudent_ids())) {
		List<Integer> studentIds = ResponseHandler.toConvertCommaSeperatedIdsAsList(batchAssignment.getStudent_ids())
				.parallelStream().distinct().collect(Collectors.toList());
		return stu_repo.studentDetailsWithBatchName(studentIds, batch_assignment_id);
		
		}
		return new ArrayList<Map<String,Object>>();
	}
	
	public List<Map<String, Object>> batchByAcademicYearAndSpecializationId(Integer acYearId,Integer programSpecializationId,Integer currentYearSem) {
		String programTypeForCurrentYearOrSem=programAssigmentRepository.getProgramTypeByProgramAssignmentId(programSpecilizationService.get(programSpecializationId).getProgram_assignment_id());
		if(programTypeForCurrentYearOrSem.equalsIgnoreCase("Semester")) {
			return s_repo.batchByAcademicYearAndSpecializationIdBySem(acYearId,programSpecializationId,currentYearSem);
		}else {
			return s_repo.batchByAcademicYearAndSpecializationIdByYear(acYearId,programSpecializationId,currentYearSem);
		}
	}
	
	public List<Map<String, Object>> batchByAcademicYear(Integer acYearId,Integer currentYearSem) {
			return s_repo.batchByAcademicYear(acYearId,currentYearSem);
	
	}

	public List<BatchProgramAssignment> getBatchProgramAssignment(Integer batch_assignment_id) {
		return s_repo.getBatchProgramAssignment(batch_assignment_id);
	}

//	public void updateBatchAssignment(@Valid List<BatchProgramAssignment> batch) {
//		// TODO Auto-generated method stub
//		
//	}
	
	public BatchAssignment updateBatchAssignment(BatchAssignment res) {
		return s_repo.save(res);
	}

	public List<Map<String, Object>> getBatchAssignmentUserId(String guest_uesr_ids) {
		List<Integer> guestUserIds = new ArrayList<Integer>();
		List<Map<String, Object>> batchAssignment = new ArrayList<Map<String, Object>>();
		String s = ",";
		if(guest_uesr_ids.contains(s) ) {
				List<Integer> new_list = ResponseHandler.toConvertCommaSeperatedIdsAsList(guest_uesr_ids);
				guestUserIds.addAll(new_list);
			} else {
				guestUserIds.add(Integer.parseInt(guest_uesr_ids));
				
			}
		
		guestUserIds.stream().forEach(userId ->{
			Map<String, Object> data = uar_repo.getUserData(userId);
			batchAssignment.add(data);
			 
		});
	
		return batchAssignment;
	}


	
}
