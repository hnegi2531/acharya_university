
package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.ProgramType;

@Transactional
@Repository
public interface ProgramTypeRepository extends JpaRepository<ProgramType, Integer> {

	@Query(value = "SELECT * FROM program_type where active=true", nativeQuery = true)
	public List<ProgramType> findAll1();

	@Modifying
	@Query(value = "update ProgramType pt set pt.active=false where pt.program_type_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update ProgramType pt set pt.active=true where pt.program_type_id=?1")
	public void update1(Integer id);
	
	@Query(value ="Select new map(pt.program_type_id as id,pt.program_type_name as program_type_name,"
			+ "pt.program_type_code as program_type_code,pt.created_date as created_date,pt.modified_date as modified_date,"
			+ "pt.created_by as created_by,pt.modified_by as modified_by,pt.active as active,"
			+ "pt.created_username as created_username,pt.modified_username as modified_username) From ProgramType pt "
			+ "Where CONCAT(IfNull(pt.program_type_id,''),'',IfNull(pt.program_type_name,''),'',IfNull(pt.program_type_code,''),'',IfNull(pt.created_date,''),'',IfNull(pt.created_by,''),'',IfNull(pt.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(pt.program_type_id as id,pt.program_type_name as program_type_name,"
			+ "pt.program_type_code as program_type_code,pt.created_date as created_date,pt.modified_date as modified_date,"
			+ "pt.created_by as created_by,pt.modified_by as modified_by,pt.active as active,"
			+ "pt.created_username as created_username,pt.modified_username as modified_username) From ProgramType pt")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM ProgramType pt where pt.program_type_name=?1 and pt.active=true")
	public Integer countOfProgramTypeName(String program_type_name);
	
	@Query(value = "SELECT count(*) FROM ProgramType pt where pt.program_type_code=?1 and pt.active=true")
	public Integer countOfProgramShortName(String program_type_code);
	
	@Query(value = "Select pt.program_type_name FROM ProgramType pt where pt.program_type_id=?1 and pt.active=true")
	public String fetchProgramType(Integer program_type_id);

}
