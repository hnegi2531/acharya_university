package com.au.repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.au.model.EmployeeExitFormalityAnswers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.EmployeeExitFormalityQuestions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.EmployeeExitFormalityQuestions;



@Repository
@Transactional
public interface EmployeeExitFormalityAnswersRepository extends JpaRepository<EmployeeExitFormalityAnswers,Integer> 
{

	@Query(value = "SELECT count(*) FROM EmployeeExitFormalityAnswers eefa where eefa.eefqid=?1 and eefa.emp_id=?2 and eefa.active=true")
	public Integer countOfcombination(Integer eefqid,Integer emp_id);
	
	
	@Query(value = "select eefa from EmployeeExitFormalityAnswers eefa where eefa.active=true")
	public List<EmployeeExitFormalityAnswers> findAll11();
	
	
	@Query(value = "Select new map(eefa.eefaid as id,sc.emp_id as emp_id,"
			+ "eefa.created_date as created_date,eefa.eefqid as eefqid,eefa.answers as answers,"
			+ "eefa.modified_date as modified_date,eefa.created_by as created_by,eefa.modified_by as modified_by,eefa.active as active,"
			+ "eefa.created_username as created_username,eefa.modified_username as modified_username)"
			+ " from EmployeeExitFormalityAnswers eefa "
			+ "left join EmployeeDetails sc on sc.emp_id=eefa.emp_id "
			+ "Where CONCAT(IfNull(emp_id,''),'',IfNull(eefa.created_by,''),'',IfNull(eefa.created_date,''),'',IfNull(eefa.created_username,'')) LIKE %?1% ")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	
	@Query(value = "Select new map(eefa.eefaid as id,sc.emp_id as emp_id,"
			+ "eefa.created_date as created_date,eefa.eefqid as eefqid,eefa.answers as answers,"
			+ "eefa.modified_date as modified_date,eefa.created_by as created_by,eefa.modified_by as modified_by,eefa.active as active,"
			+ "eefa.created_username as created_username,eefa.modified_username as modified_username)"
			+ " from EmployeeExitFormalityAnswers eefa "
			+ "left join EmployeeDetails sc on sc.emp_id=eefa.emp_id ")
	public Page<Object> findAll2(Pageable pageable);
	
	
	@Modifying
	@Query(value = "update EmployeeExitFormalityAnswers eefa set eefa.active=false where eefa.eefaid=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update EmployeeExitFormalityAnswers eefa set eefa.active=true where eefa.eefaid=?1")
	public void update1(Integer id);
	
	
	@Query(value = "select new map(eefa.eefaid as id,eefa.emp_id as emp_id,eefa.answers as answers,"
			+ "eefa.eefqid as eefqid,ctc.category_name_sort as category_name_sort,ctc.category_name as category_name,ctd.category_type_id as category_type_id,"
			+ "ctd.category_detail as category_detail,eefa.created_username as created_username,eefa.modified_username as modified_username,"
			+ "eefa.created_date as created_date,eefa.modified_date as modified_date,eefa.created_by as created_by,"
			+ "eefa.modified_by as modified_by,eefa.active as active) from EmployeeExitFormalityAnswers eefa "
			+ "left join EmployeeExitFormalityQuestions eefq on eefq.eefqid=eefa.eefqid "
			+ "left join CategoryTypeDetails ctd on ctd.category_details_id=eefq.category_details_id "
			+ "left join CategoryTypeCreation ctc on ctc.category_type_id=ctd.category_type_id where eefa.emp_id=?1 and eefa.active=true")
	public List<HashMap<String, Object>>  listAllAnswers1(Integer emp_id);
	
	
	
}
