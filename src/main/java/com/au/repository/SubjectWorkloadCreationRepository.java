package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.SubjectWorkloadCreation;

@Repository
@Transactional
public interface SubjectWorkloadCreationRepository extends JpaRepository<SubjectWorkloadCreation, Integer>{

	public Boolean existsBysubWorkloadName(String subWorkloadName);
	
	@Query(value = "select h from SubjectWorkloadCreation h where h.active=true")
	public List<SubjectWorkloadCreation> findAll1();
	
	@Modifying
	@Query(value = "update SubjectWorkloadCreation h set h.active=false where h.subWorkloadId=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update SubjectWorkloadCreation h set h.active=true where h.subWorkloadId=?1")
	public void update1(Integer id);
	
	@Query(value = "select new map(swlc.subWorkloadId as id,swlc.subWorkloadName as subWorkloadName,"
			+ "swlc.subjectType as subjectType,swlc.workLoadType as workLoadType,swlc.createdBy as createdBy,swlc.modifiedBy as modifiedBy,"
			+ "swlc.createdDate as createdDate,swlc.modifiedDate as modifiedDate,swlc.active as active,"
			+ "swlc.createdUsername as createdUsername,swlc.modifiedUsername as modifiedUsername) "
			+ "from SubjectWorkloadCreation swlc "
			+ "where CONCAT(IfNull(swlc.subWorkloadId,''),'',IfNull(swlc.subWorkloadName,''),'',IfNull(swlc.subjectType,''),"
			+ "'',IfNull(swlc.workLoadType,''),'',IfNull(swlc.createdBy,''),'',IfNull(swlc.createdDate,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(swlc.subWorkloadId as id,swlc.subWorkloadName as subWorkloadName,"
			+ "swlc.subjectType as subjectType,swlc.workLoadType as workLoadType,swlc.createdBy as createdBy,swlc.modifiedBy as modifiedBy,"
			+ "swlc.createdDate as createdDate,swlc.modifiedDate as modifiedDate,swlc.active as active,"
			+ "swlc.createdUsername as createdUsername,swlc.modifiedUsername as modifiedUsername) "
			+ "from SubjectWorkloadCreation swlc")
	public Page<Object> getAllSortedData(Pageable pageable);
}
