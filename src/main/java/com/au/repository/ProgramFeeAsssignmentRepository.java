package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.ProgramFeeAsssignment;

@Transactional
@Repository
public interface ProgramFeeAsssignmentRepository extends JpaRepository<ProgramFeeAsssignment, Integer> {

	
	@Query(value = "SELECT pfa from ProgramFeeAsssignment pfa where pfa.active=true")
	public List<ProgramFeeAsssignment> findAll11();
	
	
	@Query(value = "select new map(pfa.program_fee_asssignment_id as id,pfa.program_specialization_id as program_specialization_id,"
			+ "pfa.program_assignment_id as program_assignment_id,pfa.program_id as program_id,"
			+ "pfa.application_fee_1st_attempt as application_fee_1st_attempt,pfa.ac_year_id as ac_year_id,"
			+ "pfa.application_fee_2nd_attempt as application_fee_2nd_attempt,p.program_name as program_name,"
			+ "p.program_short_name as program_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ay.ac_year as ac_year,"
			+ "pfa.created_by as created_by,pfa.modified_by as modified_by,"
			+ "pfa.created_Date as created_Date,pfa.modified_Date as modified_Date,pfa.active as active,"
			+ "pfa.created_username as created_username,pfa.modified_username as modified_username) from ProgramFeeAsssignment pfa "
			+ "left join Program p on p.program_id=pfa.program_id "
			+ "left join Academic_year ay on ay.ac_year_id=pfa.ac_year_id "
			+ "left join ProgramSpecilization ps on ps.program_specialization_id=pfa.program_specialization_id "
			+ "where CONCAT(IfNull(pfa.program_specialization_id,''),'',IfNull(pfa.program_id,''),'',IfNull(pfa.program_assignment_id,''),'',"
			+ "IfNull(pfa.created_by,''),'',IfNull(pfa.created_Date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	

	@Query(value = "select new map(pfa.program_fee_asssignment_id as id,pfa.program_specialization_id as program_specialization_id,"
			+ "pfa.program_assignment_id as program_assignment_id,pfa.program_id as program_id,"
			+ "pfa.application_fee_1st_attempt as application_fee_1st_attempt,pfa.ac_year_id as ac_year_id,"
			+ "pfa.application_fee_2nd_attempt as application_fee_2nd_attempt,p.program_name as program_name,"
			+ "p.program_short_name as program_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ay.ac_year as ac_year,"
			+ "pfa.created_by as created_by,pfa.modified_by as modified_by,"
			+ "pfa.created_Date as created_Date,pfa.modified_Date as modified_Date,pfa.active as active,"
			+ "pfa.created_username as created_username,pfa.modified_username as modified_username) from ProgramFeeAsssignment pfa "
			+ "left join Program p on p.program_id=pfa.program_id "
			+ "left join Academic_year ay on ay.ac_year_id=pfa.ac_year_id "
			+ "left join ProgramSpecilization ps on ps.program_specialization_id=pfa.program_specialization_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
	@Modifying
	@Query(value = "update ProgramFeeAsssignment pfa set pfa.active=false where pfa.program_fee_asssignment_id=?1")
	public void updateToDeactive(Integer id);

	@Modifying
	@Query(value = "update ProgramFeeAsssignment pfa set pfa.active=true where pfa.program_fee_asssignment_id=?1")
	public void updateToActive(Integer id);
	
	
	@Query(value = "select count(*) from program_fee_asssignment pfa where pfa.ac_year_id=?1 and pfa.program_id=?2 and pfa.active=true", nativeQuery = true)
	public Integer getCountOfProgramAndAc_year(Integer ac_year_id,Integer program_id);
	
	
	@Query(value = "select pfa.program_specialization_id as program_specialization_id,pfa.program_id as program_id,"
			+ "concat(p.program_name,'-',ps.program_specialization_name) as ProgramConcat from program_fee_asssignment pfa "
			+ "left join program p on p.program_id=pfa.program_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=pfa.program_specialization_id",nativeQuery = true )
	public List<Map<String,Object>> getProgramConcat();
	
	@Query(value = "select pfa from ProgramFeeAsssignment pfa where pfa.ac_year_id=?1 And pfa.program_id=?2 And pfa.active=true")
	public ProgramFeeAsssignment getProgramFeeAsssignment(Integer ac_year_id,Integer program_id);
	
	@Query(value = "select count(*) from program_fee_asssignment pfa where pfa.program_fee_asssignment_id !=?1 and  pfa.ac_year_id=?2 and pfa.program_id=?3 and pfa.active=true", nativeQuery = true)
	public Integer getCountOfProgramAndAc_year1(Integer program_fee_asssignment_id ,Integer ac_year_id,Integer program_id);
	
}
