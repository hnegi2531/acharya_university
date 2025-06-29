package com.au.repository;
import java.util.List;
import java.util.Map;

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
public interface EmployeeExitFormalityQuestionsRepository  extends JpaRepository<EmployeeExitFormalityQuestions, Integer>

 {

	@Query(value = "select count(*) from EmployeeExitFormalityQuestions eefq where eefq.question=?1")
	public Integer getcountOfquestion(String question);
	
	
	@Query(value = "select new map(eefq.eefqid as id,eefq.category_details_id as category_details_id,eefq.question as question,eefq.type as type,"
			+ "eefq.created_username as created_username,eefq.modified_username as modified_username,ctd.category_detail as category_detail,"
			+ "eefq.created_date as created_date,eefq.modified_date as modified_date,eefq.created_by as created_by,"
			+ "eefq.modified_by as modified_by,eefq.active as active) from EmployeeExitFormalityQuestions eefq "
			+ "left join CategoryTypeDetails ctd on ctd.category_details_id=eefq.category_details_id "
			+ "where CONCAT(IfNull(eefq.created_username,''),'',IfNull(eefq.question,''),'',IfNull(eefq.category_details_id,''),"
			+ "'',IfNull(eefq.created_by,''),'',IfNull(eefq.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	
	@Query(value = "select new map(eefq.eefqid as id,eefq.category_details_id as category_details_id,eefq.question as question,eefq.type as type,"
			+ "eefq.created_username as created_username,eefq.modified_username as modified_username,ctd.category_detail as category_detail,"
			+ "eefq.created_date as created_date,eefq.modified_date as modified_date,eefq.created_by as created_by,"
			+ "eefq.modified_by as modified_by,eefq.active as active) from EmployeeExitFormalityQuestions eefq "
			+ "left join CategoryTypeDetails ctd on ctd.category_details_id=eefq.category_details_id ")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
	@Query(value = "SELECT eefq from EmployeeExitFormalityQuestions eefq where eefq.active=true")
	public List<EmployeeExitFormalityQuestions> findAll1();
	
	@Modifying
	@Query(value = "update EmployeeExitFormalityQuestions eefq set eefq.active=false where eefq.eefqid=?1")
	public void updateDept(Integer id);
	
	@Modifying
	@Query(value = "update EmployeeExitFormalityQuestions eefq set eefq.active=true where eefq.eefqid=?1")
	public void updateDept1(Integer id);


	@Query(value = "select new map(eefq.eefqid as id,eefq.category_details_id as category_details_id,eefq.question as question,"
			+ "eefq.type as type,ctc.category_name_sort as category_name_sort,ctc.category_name as category_name,ctd.category_type_id as category_type_id,"
			+ "ctd.category_detail as category_detail,eefq.created_username as created_username,eefq.modified_username as modified_username,"
			+ "eefq.created_date as created_date,eefq.modified_date as modified_date,eefq.created_by as created_by,"
			+ "eefq.modified_by as modified_by,eefq.active as active) from EmployeeExitFormalityQuestions eefq "
			+ "inner join CategoryTypeDetails ctd on ctd.category_details_id=eefq.category_details_id "
			+ "inner join CategoryTypeCreation ctc on ctc.category_type_id=ctd.category_type_id")
	public List<Map<String,Object>>  listAllData1();
	
	
	
	
}
