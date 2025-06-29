package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.IvrCreation;

@Transactional
@Repository
public interface IvrCreationRepository extends JpaRepository<IvrCreation, Integer>{

	
	@Query(value ="SELECT ivr.ivr_creation_id As ivr_creation_id,ivr.student_id As student_id,ivr.proctor_id As proctor_id,ivr.customer As customer, "
			+ "sd.father_name As fatherName,sd.mother_name As motherName,ivr.status As status,"
			+ "ivr.filename As recording,ivr.summarize As summarize,sd.student_name As studentName,ed.employee_name As callFrom,ivr.created_date As created_date, "
			+ "sd.auid As auid,sd.usn As usn  "
			+ "FROM ivr_creation ivr  "
			+ "left join student_details sd on sd.student_id = ivr.student_id  "
			+ "left join employee_details ed on ed.emp_id = ivr.proctor_id  "
			+ "where ivr.created_date = ( SELECT MAX(created_date) FROM acharya_erp.ivr_creation WHERE filename = ivr.filename ) And "
			+ "ivr.student_id=?1 And ivr.active=true And sd.active=true And ivr.status = 'Call Complete' And ivr.duration >= 30 ",nativeQuery=true)
	List<Map<String, Object>> getIvrCreationData(Integer student_id);

}
